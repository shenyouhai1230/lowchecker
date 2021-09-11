package com.syh.low.checker.views;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.syh.low.checker.utils.IOUtil;
import com.syh.low.checker.utils.StringUtilEx;

public class DataSourceView extends ViewPart {

	Set<String> set = new HashSet<String>();
	Set<String> accSet = new HashSet<String>();
	Set<String> commu_hzSet = new HashSet<String>();
	Set<String> coreSet = new HashSet<String>();
	Set<String> CRDSet = new HashSet<String>();
	Set<String> ecifSet = new HashSet<String>();
	Set<String> midSet = new HashSet<String>();
	Set<String> newbusiSet = new HashSet<String>();
	Set<String> pubSet = new HashSet<String>();
	Set<String> sysSet = new HashSet<String>();
	Set<String> ubmsSet = new HashSet<String>();
	Set<String> usgSet = new HashSet<String>();
	Set<String> yxfwSet = new HashSet<String>();
	Set<String> zjnxPubSet = new HashSet<String>();
	String content = "";
	Document doc = null;
	private Text text;

	public DataSourceView() {
	}

	public static final String ID = "com.syh.low.checker.datasourceview";

	@Override
	public void createPartControl(final Composite shell) {
		// TODO Auto-generated method stub
		shell.setLayout(null);
		final Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.setBounds(223, 10, 80, 27);
		btnBrowse.setText("浏览zjrc目录");
		final Button btnNewButton = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = selectDir(shell);
				if (!StringUtilEx.isNullOrEmpty(path)) {
					set = new HashSet<String>();
					enterAndSearch(new File(path));
					if (set.size() == 0) {
						showInfo(shell, "提示", "该文件夹未包含引用数据源的yuml文件或cpdt文件！", 0);
						btnBrowse.setFocus();
						return;
					} else {
						showInfo(shell, "提示", "检出成功！请导出数据源名称文件！", 0);
						btnNewButton.setFocus();
					}
				}
			}

		});

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(140, 15, 61, 17);
		label.setText("选择文件夹");
		final Button btnXMLBrowse = new Button(shell, SWT.NONE);
		final Button btnNameBrowse = new Button(shell, SWT.NONE);

		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (set.size() == 0) {
					showMsg(shell, "提示", "请检出数据源！", 0);
					btnBrowse.setFocus();
					return;
				}
				midSet = new HashSet<String>();
				coreSet = new HashSet<String>();
				String[] dataSource = null;
				String name = "";
				// accattr.xml,community_hz.xml,core.xml,CRDataSource.xml,ecif.xml,
				// middle.xml,newbusi.xml,pub.xml,
				// usg.xml,yxfw.xml,zjnx_pub.xml
				for (String row : set) {
					dataSource = row.split("[|]", -1);
					name = new File(dataSource[0]).getName();
					if ("accattr.xml".equals(name)) {
						accSet.add(row);
					}
					if ("community_hz.xml".equals(name)) {
						commu_hzSet.add(row);
					}
					if ("core.xml".equals(name)) {
						coreSet.add(row);
					}
					if ("CRDataSource.xml".equals(name)) {
						CRDSet.add(row);
					}
					if ("ecif.xml".equals(name)) {
						ecifSet.add(row);
					}
					if ("middle.xml".equals(name)) {
						midSet.add(row);
					}
					if ("newbusi.xml".equals(name)) {
						newbusiSet.add(row);
					}
					if ("pub.xml".equals(name)) {
						pubSet.add(row);
					}
					if ("system.xml".equals(name)) {
						sysSet.add(row);
					}
					if ("ubms.xml".equals(name)) {
						ubmsSet.add(row);
					}
					if ("usg.xml".equals(name)) {
						usgSet.add(row);
					}
					if ("yxfw.xml".equals(name)) {
						yxfwSet.add(row);
					}
					if ("zjnx_pub.xml".equals(name)) {
						zjnxPubSet.add(row);
					}

				}
				if (accSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : accSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("accattr", saveText)) {
						return;
					}
				}

				if (commu_hzSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : commu_hzSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("community_hz", saveText)) {
						return;
					}
				}
				if (coreSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : coreSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("core", saveText)) {
						return;
					}
				}
				if (CRDSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : CRDSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("CRDataSource", saveText)) {
						return;
					}
				}
				if (ecifSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : ecifSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("ecif", saveText)) {
						return;
					}
				}
				if (midSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : midSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("middle", saveText)) {
						return;
					}
				}
				if (newbusiSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : newbusiSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("newbusi", saveText)) {
						return;
					}
				}
				if (pubSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : pubSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("pub", saveText)) {
						return;
					}
				}
				if (sysSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : sysSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("system", saveText)) {
						return;
					}
				}
				if (ubmsSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : ubmsSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("ubms", saveText)) {
						return;
					}
				}

				if (usgSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : usgSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("usg", saveText)) {
						return;
					}
				}
				if (yxfwSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : yxfwSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("yxfw", saveText)) {
						return;
					}
				}
				if (zjnxPubSet.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (String date : zjnxPubSet) {
						sb.append(date + "\n");
					}
					String saveText = sb.toString();
					if (!save("zjnx_pub", saveText)) {
						return;
					}
				}
			}

			public boolean save(String saveName, String saveText) {
				String folderPath = saveFile(shell, new String[] { "*.txt",
						"*.*" }, new String[] { "txt文件(*.txt)", "所有文件(*.*)" },
						saveName);
				if (folderPath == null || folderPath.trim().equals("")) {
					return false;
				}
				File pfile = new File(folderPath);
				if (pfile.exists() && !pfile.isDirectory()) {
					int result = showConfirmDialog(shell, "提示", "文件已存在是否覆盖？",
							SWT.OK | SWT.CANCEL);
					if (SWT.OK != result) {
						return false;
					}
				}
				if (0 == IOUtil.writeFile(saveText, folderPath, "GBK")) {
					showInfo(shell, "提示", "保存成功！", 0);
				} else {
					showInfo(shell, "提示", "保存失败！", 0);
				}
				return true;
			}
		});
		btnNewButton.setBounds(333, 10, 156, 27);
		btnNewButton.setText("导出数据源名称文件");

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				if (StringUtilEx.isNullOrEmpty(content)) {
					showMsg(shell, "提示", "请选择数据源名称文件", 0);
					btnNameBrowse.setFocus();
					return;
				}
				if (null == doc) {
					showMsg(shell, "提示", "请选择xml文件！", 0);
					btnXMLBrowse.setFocus();
					return;
				}

				String[] names = content.split("\n");
				int flag = -1;
				StringBuffer sb = new StringBuffer();
				for (String name : names) {
					name = name.replaceAll("\r", "");
					name = name.split("[|]").length == 2 ? name.split("[|]")[1]
							: name;
					flag = 1;
					Element root = doc.getRootElement();
					List<Element> list = root.elements("DataSource");
					for (Element dataSource : list) {
						if (name.equals(dataSource.attributeValue("name"))) {
							flag = 0;
							break;
						}
					}
					if (1 == flag) {
						sb.append(name + "\n");
					}
				}
				if (StringUtilEx.isNullOrEmpty(sb.toString())) {
					showMsg(shell, "提示", "数据源xml中已经包含所有新增数据", 0);
				} else {
					text.setText(sb.toString());
				}

			}
		});
		button.setText("比对");
		button.setBounds(46, 174, 80, 27);

		btnXMLBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = openFile(shell, new String[] { "*.xml" },
						new String[] { "xml文件(*.xml)" });
				if (!StringUtilEx.isNullOrEmpty(path)) {
					File file = new File(path);
					SAXReader reader = new SAXReader();
					try {
						doc = reader.read(file);
					} catch (DocumentException e1) {
						showMsg(shell, "提示", "xml文件格式有误，请选择正确的xml文件！", 0);
						text.setText(e1.getMessage());
						btnXMLBrowse.setFocus();
						return;
					}
				}

			}
		});
		btnXMLBrowse.setText("浏览");
		btnXMLBrowse.setBounds(106, 141, 56, 27);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("XML文件");
		label_1.setBounds(10, 146, 80, 17);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("数据源名称文件");
		label_2.setBounds(10, 94, 93, 17);

		btnNameBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = openFile(shell, new String[] { "*.*" },
						new String[] { "所有文件(*.*)" });
				if (!StringUtilEx.isNullOrEmpty(path)) {
					content = IOUtil.getFileContent(path);
					btnNameBrowse.setFocus();
					return;
				}

			}
		});
		btnNameBrowse.setText("浏览");
		btnNameBrowse.setBounds(106, 89, 56, 27);

		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setBounds(206, 91, 399, 252);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("\u2460");
		label_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_3.setBounds(306, 15, 21, 17);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("\u2461");
		label_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_4.setBounds(495, 15, 21, 17);

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setText("\u2462");
		label_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_5.setBounds(168, 94, 21, 17);

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("\u2463");
		label_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_6.setBounds(168, 146, 21, 17);

		Label label_7 = new Label(shell, SWT.NONE);
		label_7.setText("\u2464");
		label_7.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_7.setBounds(140, 179, 21, 17);

	}

	public Image getTitleImage() {
		return new Image(getSite().getShell().getDisplay(), new Rectangle(0, 0,
				1, 1));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public String selectDir(Composite parent) {
		DirectoryDialog fileSelect = new DirectoryDialog(parent.getShell(),
				SWT.OPEN);
		return fileSelect.open();
	}

	public static String saveFile(Composite parent, String[] extensions,
			String[] names, String fileName) {
		FileDialog fd = new FileDialog(parent.getShell(), SWT.SAVE);
		String desktop = FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath();
		fd.setFilterPath(desktop);
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(names);
		fd.setFileName(fileName);
		String path = fd.open();
		return path;
	}

	public static void showMsg(Composite composite, String title,
			String message, int type) {
		MessageBox box = new MessageBox(composite.getShell(), type);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}

	/**
	 * 确认
	 * 
	 * @param parant
	 * @param title
	 * @param msg
	 * @param type
	 * @return
	 */
	public static int showConfirmDialog(Composite composite, String title,
			String msg, int type) {
		MessageBox box = new MessageBox(composite.getShell(), type);
		box.setMessage(msg);
		box.setText("提示");
		return box.open();
	}

	/**
	 * 文件过滤器，选出所有*.yuml & *.cpdt的
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void filter(File file) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile()
						&& (file.getName().endsWith(".yuml") || file.getName()
								.endsWith(".cpdt"));
			}
		};
		File[] subs = file.listFiles(filter);
		for (File sub : subs) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(sub);
				getNodes(doc.getRootElement());
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 遍历文件夹下的所有文件，并选出*.yuml & *.cpdt的文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void enterAndSearch(File file) {
		if (file.isDirectory()) {
			filter(file);
			File[] subs = file.listFiles();
			for (File sub : subs) {
				enterAndSearch(sub);
			}
		}
	}

	/**
	 * 迭代获取所有节点
	 * 
	 * @param node
	 */
	public void getNodes(Element node) {
		Attribute attr = null;
		if ("ComboBox".equals(node.getName())) {
			attr = node.attribute("dataSourceFile");
			if (attr != null) {
				set.add(attr.getValue());
			}
		} else if ("Column".equals(node.getName())) {
			attr = node.attribute("dataSourceFile");
			if (attr != null) {
				set.add(attr.getValue());
			}
		} else if ("Parameter".equals(node.getName())) {
			String data = node.getText();
			if (data.startsWith("Type=DataSource")) {
				data = data.split(";")[1].replaceAll("Name=", "");
				set.add(data);
			}
		}
		List<Element> listElement = node.elements();
		for (Element e : listElement) {
			getNodes(e);
		}
	}

	/**
	 * 提示
	 * 
	 * @param Composite
	 * @param title
	 * @param message
	 * @param type
	 */
	public static void showInfo(Composite Composite, String title,
			String message, int type) {
		MessageBox box = new MessageBox(Composite.getShell(), type);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}

	/**
	 * 选择文件
	 * 
	 * @param Composite
	 * @param extensions
	 * @param names
	 * @return
	 */
	public static String openFile(Composite Composite, String[] extensions,
			String[] names) {
		FileDialog fd = new FileDialog(Composite.getShell(), SWT.OPEN);
		String desktop = FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath();
		fd.setFilterPath(desktop);
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(names);
		String path = fd.open();
		return path;
	}

}
