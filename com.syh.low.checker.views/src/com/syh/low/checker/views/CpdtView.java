package com.syh.low.checker.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;

import org.dom4j.Document;
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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.syh.low.checker.utils.SFTPUtil;
import com.syh.low.checker.utils.StringUtilEx;

public class CpdtView extends ViewPart {

	static List<File> list = new ArrayList<File>();
	static List<File> listDir = new ArrayList<File>();
	private Text text_method;
	static Set<String> yuptSet = new HashSet<String>();
	static Set<String> cpdtSet = new HashSet<String>();
	String cpdtPath = "";
	String yuptPath = "";
	String savePath = "";
	String selectPath = "";
	private Text text_src;
	private Text text_missing;
	ChannelSftp sftp;
	Session session;
	private Text text_log;

	public CpdtView() {
	}

	public static final String ID = "com.syh.low.checker.cpdtview";

	@Override
	public void createPartControl(final Composite shell) {
		shell.setLayout(null);
		text_method = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		text_method.setEditable(false);
		text_method.setBounds(468, 64, 452, 230);
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 15, 61, 17);
		label.setText("选择文件夹");
		final Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.setBounds(96, 10, 102, 27);
		btnBrowse.setText("浏览zjrc目录");
		text_src = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		text_src.setEditable(false);
		text_src.setBounds(10, 64, 452, 230);
		text_missing = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		text_missing.setEditable(false);
		text_missing.setBounds(10, 323, 452, 230);
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 41, 61, 17);
		lblNewLabel.setText("资源路径");
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(468, 41, 61, 17);
		lblNewLabel_1.setText("打印方法");
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(10, 300, 61, 17);
		lblNewLabel_2.setText("遗漏资源");

		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_missing.setText("");
				text_log.setText("");
				String[] templates = text_method.getText().split("\n");
				String[] pathes = text_src.getText().split("\n");
				String yuptName = "", cpdtName = "", path = "";
				for (int i = 0; i < templates.length; i++) {
					String[] methods = templates[i].split(",", -1);
					if (methods.length == 5) {
						if (methods.length < 3) {
							continue;
						}
						yuptName = methods[2];
						if (!yuptName.contains("\"")) {
							path = pathes[i].trim().replaceAll("\n", "")
									.replaceAll("\r", "");
							StringBuffer sb = new StringBuffer();
							String[] name = null;
							try {
								readWantedText(path, yuptName.trim(), sb);
								String[] yuptnames = sb.toString()
										.replaceAll("\r", "").split("\n");
								for (String names : yuptnames) {
									name = names.split("=", -1);
									if (name.length != 2) {
										continue;
									}
									if (!StringUtilEx
											.isNullOrWhiteSpace(name[1]
													.replaceAll("\"", "")
													.trim())) {
										yuptName = name[1].replaceAll("\"", "")
												.replaceAll(";", "").trim();
										String[] yuptNames = yuptName.split(
												"/", -1);
										if (yuptNames.length == 1) {
											yuptName = yuptName
													.replaceAll("/", "\\\\")
													.replaceAll("\"", "")
													.replaceAll(".yupt", "")
													+ ".yupt";
										} else if (yuptNames.length == 2) {
											yuptName = yuptNames[0].replaceAll(
													"\"", "")
													+ "\\"
													+ yuptNames[1]
															.replaceAll("\"",
																	"")
															.replaceAll(
																	".yupt", "")
													+ ".yupt";
										} else if (yuptNames.length == 3) {
											yuptName = yuptNames[0].replaceAll(
													"\"", "")
													+ "\\"
													+ yuptNames[1].replaceAll(
															"\"", "")
													+ "\\"
													+ yuptNames[2]
															.replaceAll("\"",
																	"")
															.replaceAll(
																	".yupt", "")
													+ ".yupt";
										}
										yuptSet.add(yuptName);
									}
								}
								text_log.setText(sb.toString());
								continue;
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								text_log.setText(e1.getMessage());
								return;
							}
						}
						String[] names = yuptName.split("/", -1);
						if (names.length == 1) {
							yuptName = yuptName.replaceAll("/", "\\\\")
									.replaceAll("\"", "")
									.replaceAll(".yupt", "")
									+ ".yupt";
						} else if (names.length == 2) {
							yuptName = names[0].replaceAll("\"", "")
									+ "\\"
									+ names[1].replaceAll("\"", "").replaceAll(
											".yupt", "") + ".yupt";
						} else if (names.length == 3) {
							yuptName = names[0].replaceAll("\"", "")
									+ "\\"
									+ names[1].replaceAll("\"", "")
									+ "\\"
									+ names[2].replaceAll("\"", "").replaceAll(
											".yupt", "") + ".yupt";
						}
						yuptSet.add(yuptName);
					} else {
						if (methods.length < 2) {
							continue;
						}
						cpdtName = methods[1].replaceAll("\"", "");
						String[] names = cpdtName.split("/", -1);
						if (names.length == 1) {
							cpdtName = cpdtName.replaceAll("\"", "")
									.replaceAll(".cpdt", "") + ".cpdt";
						} else if (names.length == 2) {
							cpdtName = names[0].replaceAll("\"", "")
									+ "\\"
									+ names[1].replaceAll("\"", "").replaceAll(
											".cpdt", "") + ".cpdt";
						} else if (names.length == 3) {
							if (StringUtilEx.isNullOrEmpty(names[0])) {
								cpdtName = names[1].replaceAll("\"", "")
										+ "\\"
										+ names[2].replaceAll("\"", "")
												.replaceAll(".cpdt", "")
										+ ".cpdt";
							} else {
								cpdtName = names[0].replaceAll("\"", "")
										+ "\\"
										+ names[1].replaceAll("\"", "")
										+ "\\"
										+ names[2].replaceAll("\"", "")
												.replaceAll(".cpdt", "")
										+ ".cpdt";
							}
						}
						cpdtSet.add(cpdtName);
					}

				}
				try {
					if (sftp != null) {
						try {
							sftp.getSession().disconnect();
						} catch (JSchException e1) {
							text_log.append(e1.getMessage() + "\n");
						}
						sftp.disconnect();
						sftp.exit();
					}
					String host = "158.220.21.72";
					String user = "btopzsc";
					String password = "daban123456";
					session = SFTPUtil.creatSession(host, user, password,
							text_log);
					sftp = SFTPUtil.connect(session, text_log);
				} catch (Exception e1) {
					text_log.append(e1.getMessage() + "\n");
					return;
				}
				for (String yupt : yuptSet) {
					if (yupt.equals(".yupt")) {
						continue;
					}
					File yuptFile = new File(yuptPath + "\\" + yupt);
					if (!yuptFile.exists()) {
						checkRemoteFile(yupt, "yupt");
					} else {
						text_log.append("生产包包含：BIPS_A/workspace/zjrc/yupt/"
								+ yupt.replaceAll("\\\\", "/") + "\n");
					}
				}
				for (String cpdt : cpdtSet) {
					if (cpdt.equals(".cpdt")) {
						continue;
					}
					File cpdtFile = new File(cpdtPath + "\\" + cpdt);
					if (!cpdtFile.exists()) {
						checkRemoteFile(cpdt, "cpdt");
					} else {
						text_log.append("生产包包含：BIPS_A/workspace/zjrc/cpdt/"
								+ cpdt.replaceAll("\\\\", "/") + "\n");
						try {
							SAXReader reader = new SAXReader();
							Document doc = reader.read(cpdtFile);
							List<Element> dataTemplates = doc.getRootElement()
									.elements("DataTemplate");
							for (Element dataTemplate : dataTemplates) {
								List<Element> styles = dataTemplate
										.elements("Style");
								for (Element style : styles) {
									if ("template".equals(style
											.attributeValue("name"))) {
										String yupt = style.getTextTrim()
												.replaceAll("/", "\\\\");
										File yuptFile = new File(yuptPath
												+ "\\" + yupt);
										if (!yuptFile.exists()) {
											checkRemoteFile(yupt, "yupt");
										} else {
											text_log.append("生产包包含：BIPS_A/workspace/zjrc/yupt/"
													+ yupt.replaceAll("\\\\",
															"/") + "\n");
										}
									}
								}
							}
						} catch (Exception e1) {
							text_log.append(e1.getMessage() + "\n");
							return;
						}
					}
				}
				if (sftp != null) {
					try {
						text_log.append("Session disconnect\n");
						sftp.getSession().disconnect();
					} catch (JSchException e1) {
						text_log.append(e1.getMessage() + "\n");
					}
					sftp.disconnect();
					sftp.exit();
					text_log.append("sftp exit");
				}
				if (StringUtilEx.isNullOrEmpty(text_missing.getText())) {
					showMsg("提示", "无遗漏资源！", 0, shell);
				} else {
					showMsg("提示", "检索完成！", 0, shell);
				}
			}
		});
		btnSearch.setBounds(260, 10, 80, 27);
		btnSearch.setText("检索模板");

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("日志");
		label_1.setBounds(468, 300, 61, 17);

		text_log = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		text_log.setEditable(false);
		text_log.setBounds(468, 323, 452, 230);

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_2.setText("\u2460");
		label_2.setBounds(204, 15, 32, 17);

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_3.setText("\u2461");
		label_3.setBounds(346, 15, 32, 17);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listDir = new ArrayList<File>();
				list = new ArrayList<File>();
				yuptSet = new HashSet<String>();
				cpdtSet = new HashSet<String>();
				cpdtPath = "";
				yuptPath = "";
				text_method.setText("");
				text_src.setText("");
				text_missing.setText("");
				text_log.setText("");
				selectPath = selectDir(shell);
				if (!StringUtilEx.isNullOrEmpty(selectPath)) {
					list = new ArrayList<File>();
					enterAndSearch(new File(selectPath));
					if (list.size() == 0) {
						showMsg("提示", "该文件夹下没有java文件！", 0, shell);
						return;
					}
					for (File file : list) {
						try {
							readWantedText(file.getPath(), "PrintUtil");
						} catch (Exception e1) {
							text_log.append(e1.getMessage() + "\n");
							return;
						}
					}

					enter(new File(selectPath));
					if (listDir.size() != 0) {
						cpdtPath = listDir.get(0).getParent() + "\\cpdt";
						yuptPath = listDir.get(0).getParent() + "\\yupt";
					} else {

					}
				}
			}
		});

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

	/**
	 * 遍历文件夹下的所有文件，并选出*.java的文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void enterAndSearch(File file) {
		if (file.isDirectory()) {
			filter(file);
			File[] subs = file.listFiles();
			if (subs == null) {
				return;
			}
			for (File sub : subs) {
				enterAndSearch(sub);
			}
		}
	}

	/**
	 * 遍历文件夹下的所有文件，并选出*.java的文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void enter(File file) {
		if (file.isDirectory()) {
			dirFilter(file);
			File[] subs = file.listFiles();
			if (subs == null) {
				return;
			}
			for (File sub : subs) {
				enterAndSearch(sub);
			}
		}
	}

	/**
	 * 文件过滤器，选出所有*.java的
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void filter(File file) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".java")
						&& !file.getName().endsWith("Yuda.java");
			}
		};
		File[] subs = file.listFiles(filter);
		if (subs == null) {
			return;
		}
		for (File f : subs) {
			list.add(f);
		}
	}

	/**
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void dirFilter(File file) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() && file.getName().equals("classes");
			}
		};
		File[] subs = file.listFiles(filter);
		if (subs == null) {
			return;
		}
		for (File f : subs) {
			listDir.add(f);
		}
	}

	public static void showMsg(String title, String message, int type,
			Composite parent) {
		MessageBox box = new MessageBox(parent.getShell(), type);
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
	public static int showConfirmDialog(String title, String msg,
			Composite parent) {
		MessageBox box = new MessageBox(parent.getShell(), SWT.OK | SWT.CANCEL);
		box.setMessage(msg);
		box.setText("提示");
		return box.open();
	}

	public void readWantedText(String path, String wanted) throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String temp = "";
		int flag = 0;
		while ((temp = br.readLine()) != null) {
			if (temp.startsWith("import")) {
				continue;
			}
			if (temp.replaceAll("\t", "").replaceAll(" ", "").startsWith("//")) {
				continue;
			}
			if (flag == 1) {
				if (temp.contains(";")) {
					text_method.append(temp.replaceAll("\t", "").replaceAll(
							" ", "")
							+ "\n");
					flag = 0;
				} else {
					text_method.append(temp.replaceAll("\t", "").replaceAll(
							" ", ""));
					flag = 1;
				}
			}
			if (temp.contains(wanted)) {
				if (temp.contains("ZjrcPrintUtil")) {
					continue;
				}
				text_src.append(path + "\n");
				if (temp.contains(";")) {
					// path+":\n"+
					text_method.append(temp.replaceAll("\t", "").replaceAll(
							" ", "")
							+ "\n");
					flag = 0;
				} else {
					text_method.append(temp.replaceAll("\t", "").replaceAll(
							" ", ""));
					flag = 1;
				}
			}
		}
		br.close();
		isr.close();
		fis.close();
	}

	public void readWantedText(String path, String wanted, StringBuffer sb)
			throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String temp = "";
		int flag = 0;
		while ((temp = br.readLine()) != null) {
			if (temp.startsWith("import")) {
				continue;
			}
			if (temp.replaceAll("\t", "").replaceAll(" ", "").startsWith("//")) {
				continue;
			}
			if (flag == 1) {
				if (temp.contains(";")) {
					sb.append(temp.replaceAll("\t", "").replaceAll(" ", "")
							+ "\n");
					flag = 0;
				} else {
					sb.append(temp.replaceAll("\t", "").replaceAll(" ", ""));
					flag = 1;
				}
			}
			if (temp.contains(wanted)) {
				if (temp.contains("ZjrcPrintUtil")) {
					continue;
				}
				if (temp.contains(";")) {
					// path+":\n"+
					sb.append(temp.replaceAll("\t", "").replaceAll(" ", "")
							.replaceAll("String", "")
							+ "\n");
					flag = 0;
				} else {
					sb.append(temp.replaceAll("\t", "").replaceAll(" ", ""));
					flag = 1;
				}
			}
		}
		br.close();
		isr.close();
		fis.close();
	}

	public void checkRemoteFile(String name, String type) {
		name = name.replaceAll("\\\\", "/");
		String path = "BIPS_A/workspace/zjrc/" + type;
		String[] names = name.split("/", -1);
		if (names.length == 2) {
			path = path + "/" + names[0];
			name = names[1];
		}
		if (names.length == 3) {
			path = path + "/" + names[0] + "/" + names[1];
			name = names[2];
		}
		String cmd = "find " + path + " -name " + name;
		String result = SFTPUtil.exec(session, cmd);
		if (StringUtilEx.isNullOrEmpty(result)) {
			text_missing.append(path + "/" + name + "\n");
		} else {
			text_log.append("验证存在:" + result);
			if ("cpdt".equals(type)) {
				try {
					SFTPUtil.download(path + "/" + name, selectPath, sftp);
					SAXReader reader = new SAXReader();
					Document doc = reader.read(selectPath + File.separator
							+ name);
					List<Element> dataTemplates = doc.getRootElement()
							.elements("DataTemplate");
					for (Element dataTemplate : dataTemplates) {
						List<Element> styles = dataTemplate.elements("Style");
						for (Element style : styles) {
							if ("template".equals(style.attributeValue("name"))) {
								String yupt = style.getTextTrim().replaceAll(
										"/", "\\\\");
								File yuptFile = new File(yuptPath + "\\" + yupt);
								if (!yuptFile.exists()) {
									checkRemoteFile(yupt, "yupt");
								} else {
									text_log.append("生产包包含：BIPS_A/workspace/zjrc/yupt/"
											+ yupt.replaceAll("\\\\", "/")
											+ "\n");
								}
							}
						}
					}
					File file = new File(selectPath + File.separator + name);
					if (file.exists())
						file.delete();
				} catch (Exception e) {
					text_log.append(e.getMessage() + "\n");
				}
			}
		}
	}
}
