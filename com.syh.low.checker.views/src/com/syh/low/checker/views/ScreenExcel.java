package com.syh.low.checker.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ScreenExcel {
	public static List<Map<String, Object>> loadExcelInfo(String xlsPath,
			int colnum, int row, int emptynum) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		FileInputStream fileIn = new FileInputStream(xlsPath);
		Workbook wb0 = null;
		if (xlsPath.endsWith(".xls")) {
			wb0 = new HSSFWorkbook(fileIn);
		} else if (xlsPath.endsWith(".xlsx")) {
			wb0 = WorkbookFactory.create(fileIn);
		}
		Sheet sht0 = wb0.getSheetAt(0);
		for (Row r : sht0) {
			if (r.getRowNum() < row) {
				continue;
			}
			if (isRowEmpty(r, 0, emptynum)) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < colnum; i++) {
				if (null != r.getCell(i)) {
					r.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
					if(i==0){
						Calendar excelDate = new GregorianCalendar(1900,0,-1);   
						excelDate.add(Calendar.DAY_OF_YEAR, Integer.valueOf(r.getCell(i).getStringCellValue()));
						map.put("col"+i, new SimpleDateFormat("yyyy-MM-dd").format(excelDate.getTime()));
					}else{
						if (null != r.getCell(i).getStringCellValue()
								&& !"".equals(r.getCell(i).getStringCellValue())) {
							map.put("col" + i, r.getCell(i).getStringCellValue());
						} else {
							map.put("col" + i, "");
						}
					}
					
				} else {
					map.put("col" + i, "");
				}
			}
			list.add(map);
		}
		return list;
	}

	public static boolean isRowEmpty(Row row, int startIndex, int lastIndex) {
		for (int c = startIndex; c < lastIndex; c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, Object>> list = loadExcelInfo(
					"C:\\Users\\syh\\Desktop\\浙江九州通6月.xlsx", 22, 1, 21);
			if (null != list) {
				listAll.addAll(list);
			}
		} catch (Exception e1) {
			System.out.println("读取失败");
			return;
		}
		// 输出合并后的新文件
		HSSFWorkbook wb = new HSSFWorkbook();
		 HSSFCellStyle cellStyle = wb.createCellStyle();
		 cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-m-d"));
		HSSFSheet sheet = wb.createSheet("sheet0");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(
					"C:\\Users\\syh\\Desktop\\九州余杭流向6月.xls"));
		} catch (Exception e) {
			return;
		}
		int rowIndex = 0;
		for (int i = 0; i < listAll.size(); i++) {
			Map<String, Object> mapRow = listAll.get(i);
			if (i != 0) {
				if (((String) mapRow.get("col14")).trim().startsWith("余杭")) {
					HSSFRow row = sheet.createRow(rowIndex++);
					for (int colIndex = 0; colIndex < mapRow.size(); colIndex++) {
						HSSFCell cell = row.createCell(colIndex);
						if (colIndex == 0) {
							cell.setCellStyle(cellStyle);
							cell.setCellValue("" + mapRow.get("col0"));
						} else if (colIndex == 1) {
							cell.setCellValue("" + mapRow.get("col3"));
						} else if (colIndex == 2) {
							cell.setCellValue("" + mapRow.get("col5")+ mapRow.get("col6"));
						} else if (colIndex == 3) {
							cell.setCellValue("" + mapRow.get("col7"));
						} else if (colIndex == 4) {
							cell.setCellValue("" + mapRow.get("col21"));
						}

					}
				}
			}else{
				HSSFRow row = sheet.createRow(rowIndex++);
				for (int colIndex = 0; colIndex < mapRow.size(); colIndex++) {
					HSSFCell cell = row.createCell(colIndex);
					if (colIndex == 0) {
						cell.setCellValue("" + mapRow.get("col0"));
					} else if (colIndex == 1) {
						cell.setCellValue("" + mapRow.get("col3"));
					} else if (colIndex == 2) {
						cell.setCellValue("" + mapRow.get("col5"));
					} else if (colIndex == 3) {
						cell.setCellValue("" + mapRow.get("col7"));
					} else if (colIndex == 4) {
						cell.setCellValue("" + mapRow.get("col21"));
					}

				}
			}

		}
		try {
			wb.write(out);
			out.flush();
			out.close();
		} catch (IOException e1) {
			return;
		}
	}
}
