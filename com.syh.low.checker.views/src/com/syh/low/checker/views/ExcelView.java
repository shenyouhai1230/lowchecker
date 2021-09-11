package com.syh.low.checker.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.jcraft.jsch.ChannelSftp;
import com.syh.low.checker.utils.FileInfo;


public class ExcelView extends ViewPart {
	public ExcelView() {
	}
	public static final String ID = "com.syh.low.checker.excelView";
	List<String> remoteFiles = new ArrayList<String>();
	List<FileInfo> lfiles = new ArrayList<FileInfo>();
	Map<String,String> mfiles = new HashMap<String,String>();
	StringWriter sw = new StringWriter();
	ChannelSftp sftp;
	private Text text;
	private Text tlog;
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		
		Group group = new Group(composite, SWT.NONE);
		group.setBounds(10, 10, 574, 87);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(20, 36, 52, 20);
		label.setText("\u6587\u4EF6\u5939");
		
		text = new Text(group, SWT.BORDER);
		text.setBounds(88, 36, 338, 26);
		
		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.setBounds(432, 36, 52, 26);
		btnNewButton.setText("\u6D4F\u89C8");
		
		Button combineBt = new Button(group, SWT.NONE);
		combineBt.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("null")
			@Override
			public void widgetSelected(SelectionEvent e) {
				String dir = text.getText().trim();
				File dirF = new File(dir);
				tlog.setText("开始合并文件"+"\n");
				List<Map<String,Object>> listAll = new ArrayList<Map<String,Object>>();
				for(File excelF : dirF.listFiles()){
					try {
						List<Map<String,Object>> list =loadExcelInfo(excelF.getAbsolutePath(), 10, 2, 9);
						if(null != list){
							listAll.addAll(list);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						tlog.setText(excelF.getName()+"---加载失败"+"\n");
						return;
					}
				}
				//排序
				Collections.sort(listAll, new Comparator<Map<String,Object>>() {

					@Override
					public int compare(Map<String, Object> o1,
							Map<String, Object> o2) {
						String col1 = ((String) o1.get("col0")).replaceAll("/", "");
						String col2 = ((String) o2.get("col0")).replaceAll("/", "");
						Integer result = Integer.valueOf(col1) - Integer.valueOf(col2);
						return result;
					}
				});
				//输出合并后的新文件
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFCellStyle cellStyle = wb.createCellStyle();  
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/m/d")); 
				HSSFSheet sheet = wb.createSheet("sheet0");
				FileOutputStream out = null;
				try {
					 out = new FileOutputStream(new File(dir+File.separator+"newFile.xls"));
				} catch (FileNotFoundException e1) {
					tlog.setText("输出文件创建失败"+"\n");
					return;
				}
				for(int i = 0 ; i < listAll.size();i++){
					HSSFRow row = sheet.createRow(i);
					Map<String,Object> mapRow = listAll.get(i);
					for(int colIndex = 0;colIndex<mapRow.size(); colIndex++){
						HSSFCell cell = row.createCell(colIndex);
						if(colIndex == 0){
							cell.setCellStyle(cellStyle);
						}
						cell.setCellValue(""+mapRow.get("col"+colIndex));
					}
				}
				try {
					wb.write(out);
					out.flush();
					out.close();
				} catch (IOException e1) {
					tlog.setText("新文件内容写入失败"+"\n");
					return;
				}
				
				
			}
		});
		combineBt.setText("\u5408\u5E76");
		combineBt.setBounds(493, 36, 52, 26);
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setBounds(10, 107, 574, 315);
		
		tlog = new Text(group_1, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		tlog.setBounds(10, 25, 554, 269);
	}
	public String getTime() {
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(now);
	}
	public List<Map<String,Object>> loadExcelInfo(String xlsPath,int colnum,int row,int emptynum) throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		FileInputStream fileIn = new FileInputStream(xlsPath);
		Workbook wb0 = null;
		if(xlsPath.endsWith(".xls")){
			wb0 = new HSSFWorkbook(fileIn);
		}else if(xlsPath.endsWith(".xlsx")){
			wb0 = WorkbookFactory.create(fileIn);
		}
		Sheet sht0 = wb0.getSheetAt(0);
		for(Row r: sht0){
			if(r.getRowNum() < row){
				continue;
			}
			if(isRowEmpty(r, 0, emptynum)){
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0;i<colnum;i++){
				if(null != r.getCell(i)){
					r.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
					if(null != r.getCell(i).getStringCellValue() && !"".equals(r.getCell(i).getStringCellValue())){
						if(i == 0){
							//数值类型又具体区分日期类型，单独处理
//							if(HSSFDateUtil.isCellDateFormatted(r.getCell(i))){
//								Date date = r.getCell(i).getDateCellValue();
//								map.put("col"+i, new SimpleDateFormat("yyyy/MM/dd").format(date));
								//DateJodaUtils.getDateStr(date, "yyyy-MM-dd");
//								Calendar calendar = new GregorianCalendar(1900,0,-1);  
//								Date d = calendar.getTime();  
								Calendar excelDate = new GregorianCalendar(1900,0,-1);   
								excelDate.add(Calendar.DAY_OF_YEAR, Integer.valueOf(r.getCell(i).getStringCellValue()));
								map.put("col"+i, new SimpleDateFormat("yyyy/MM/dd").format(excelDate.getTime()));
//							}else{
//								NumberToTextConverter.toText(r.getCell(i).getNumericCellValue());
//							}
						}else{
							map.put("col"+i, r.getCell(i).getStringCellValue());
						}
						
					}else{
						map.put("col"+i, "");
					}
				}else{
					map.put("col"+i, "");
				}
			}
			list.add(map);
		}
		return list;
	}
	
	public boolean isRowEmpty(Row row,int startIndex,int lastIndex){
		for(int c=startIndex;c<lastIndex;c++){
			Cell cell = row.getCell(c);
			if(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
				return false;
			}
		}
		return true;
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	 public Image getTitleImage() {
	        return new Image(getSite().getShell().getDisplay(), new Rectangle(0, 0, 1, 1));
	    }
}