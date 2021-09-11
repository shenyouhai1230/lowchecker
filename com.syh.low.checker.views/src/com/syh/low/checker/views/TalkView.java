package com.syh.low.checker.views;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import cat.function.CatBean;

import com.jcraft.jsch.ChannelSftp;
import com.syh.low.checker.utils.FileInfo;
import javax.swing.SwingConstants;

public class TalkView extends ViewPart {
	private static JTextArea textArea;
	private static AbstractListModel listmodel;
	private static JList list;
	private static Vector onlines = new Vector();;
	private static JProgressBar progressBar;
	private static JLabel lblNewLabel;
	private static ObjectOutputStream oos;
	private static URL cb, cb2;
	private static AudioClip aau, aau2;
	private Socket clientSocket;
	private static ObjectInputStream ois;
	private static String name = "zfy";
	private JPanel panel;
	private static boolean isSendFile = false;
	private static boolean isReceiveFile = false;
	private static String filePath;

	public TalkView() {
	}

	public static final String ID = "com.syh.low.checker.talkView";
	List<String> remoteFiles = new ArrayList<String>();
	List<FileInfo> lfiles = new ArrayList<FileInfo>();
	Map<String, String> mfiles = new HashMap<String, String>();
	StringWriter sw = new StringWriter();
	ChannelSftp sftp;

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
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		composite.setBounds(0, 0, 594, 469);
		Frame frame = SWT_AWT.new_Frame(composite);
		JApplet applet = new JApplet();
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			ImageIcon icon;
			java.awt.Image img;

			public void paintComponent(Graphics g) {
				icon = new ImageIcon(getClass().getResource("/images/聊天室1.jpg"));
				img = icon.getImage();
				super.paintComponent(g);
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		frame.add(applet);
		applet.add(panel);
//		frame.add(panel);
		panel.setBounds(0, 0, 594, 469);
		panel.setLayout(null);
		// 聊天信息显示区域
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 224, 300);
		panel.add(scrollPane);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("sdf", Font.BOLD, 13));
		scrollPane.setViewportView(textArea);
		// 发言区域
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 347, 224, 97);
		panel.add(scrollPane_1);
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);// 激活自动换行功能
		textArea_1.setWrapStyleWord(true);// 激活断行不断字功能
		scrollPane_1.setViewportView(textArea_1);
		scrollPane_1.setFocusable(true);
		textArea_1.setFocusable(true);
		// 关闭按钮
		final JButton btnNewButton = new JButton("\u5173\u95ED");
		btnNewButton.setBounds(20, 448, 77, 30);
		panel.add(btnNewButton);
		// 发送按钮
		JButton btnNewButton_1 = new JButton("\u53D1\u9001");
		btnNewButton_1.setBounds(124, 448, 77, 30);
		panel.add(btnNewButton_1);
		// 在线客户列表
		listmodel = new UUListModel(onlines);
		list = new JList(listmodel);
		list.setCellRenderer(new CellRenderer());
		list.setOpaque(false);
		Border etch = BorderFactory.createEtchedBorder();
		list.setBorder(BorderFactory.createTitledBorder(etch, "在线客户:",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("sdf",
						Font.BOLD, 20), Color.green));
		JScrollPane scrollPane_2 = new JScrollPane(list);
		scrollPane_2.setBounds(248, 10, 198, 375);
		scrollPane_2.setOpaque(false);
		scrollPane_2.getViewport().setOpaque(false);
		panel.add(scrollPane_2);
		// 文件传输栏
		progressBar = new JProgressBar();
		progressBar.setBounds(248, 393, 198, 15);
		progressBar.setMinimum(1);
		progressBar.setMaximum(100);
		panel.add(progressBar);
		// 连接服务
		try {
			clientSocket = new Socket("localhost", 8520);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		// 文件传输提示
		lblNewLabel = new JLabel("\u6587\u4EF6\u4F20\u9001\u4FE1\u606F\u680F:");
		lblNewLabel.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(248, 410, 245, 15);
		panel.add(lblNewLabel);
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			// 记录上线客户的信息在catbean中，并发送给服务器
			CatBean bean = new CatBean();
			bean.setType(0);
			bean.setName(name);
			bean.setTimer(CatUtil.getTimer());
			oos.writeObject(bean);
			oos.flush();
			// 消息提示声音
			cb = getClass().getResource("/sounds/呃欧.wav");
			aau = Applet.newAudioClip(cb);
			// 上线提示声音
			cb2 = getClass().getResource("/sounds/叮.wav");
			aau2 = Applet.newAudioClip(cb2);

			// 启动客户接收线程
			new ClientInputThread().start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 发送按钮
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String info = textArea_1.getText();
				
				Object[] ob =list.getSelectedValues();
				List to = new ArrayList();
				for(Object obo:ob){
					to.add(obo);
				}
				if (to.size() < 1) {
					JOptionPane.showMessageDialog(panel, "请选择聊天对象");
					return;
				}
				if (to.toString().contains(name + "(我)")) {
					JOptionPane
							.showMessageDialog(panel, "不能向自己发送信息");
					return;
				}
				if (info.equals("")) {
					JOptionPane.showMessageDialog(panel, "不能发送空信息");
					return;
				}

				CatBean clientBean = new CatBean();
				clientBean.setType(1);
				clientBean.setName(name);
				String time = CatUtil.getTimer();
				clientBean.setTimer(time);
				clientBean.setInfo(info);
				HashSet set = new HashSet();
				set.addAll(to);
				clientBean.setClients(set);

				// 自己发的内容也要现实在自己的屏幕上面
				textArea.append(time + " 我对" + to + "说:\r\n" + info + "\r\n");

				sendMessage(clientBean);
				textArea_1.setText(null);
				textArea_1.requestFocus();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}

	public Image getTitleImage() {
		return new Image(getSite().getShell().getDisplay(), new Rectangle(0, 0,
				1, 1));
	}

	class ClientInputThread extends Thread {
		@Override
		public void run() {
			try {
				// 不停的从服务器接收信息
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					final CatBean bean = (CatBean) ois.readObject();
					switch (bean.getType()) {
					case 0: {
						// 更新列表
						onlines.clear();
						HashSet<String> clients = bean.getClients();
						Iterator<String> it = clients.iterator();
						while (it.hasNext()) {
							String ele = it.next();
							if (name.equals(ele)) {
								onlines.add(ele + "(我)");
							} else {
								onlines.add(ele);
							}
						}

						listmodel = new UUListModel(onlines);
						list.setModel(listmodel);
						aau2.play();
						textArea.append(bean.getInfo() + "\r\n");
						textArea.selectAll();
						break;
					}
					case -1: {
						return;
					}
					case 1: {
						String info = bean.getTimer() + "  " + bean.getName()
								+ " 对 " + bean.getClients() + "说:\r\n";
						if (info.contains(name)) {
							info = info.replace(name, "我");
						}
						aau.play();
						textArea.append(info + bean.getInfo() + "\r\n");
						textArea.selectAll();
						break;
					}
					case 2: {
						// 由于等待目标客户确认是否接收文件是个阻塞状态，所以这里用线程处理
						new Thread() {
							public void run() {
								// 显示是否接收文件对话框
								int result = JOptionPane.showConfirmDialog(
										panel, bean.getInfo());
								switch (result) {
								case 0: { // 接收文件
									JFileChooser chooser = new JFileChooser();
									chooser.setDialogTitle("保存文件框"); // 标题哦...
									// 默认文件名称还有放在当前目录下
									chooser.setSelectedFile(new File(bean
											.getFileName()));
									chooser.showDialog(panel, "保存"); // 这是按钮的名字..
									// 保存路径
									String saveFilePath = chooser
											.getSelectedFile().toString();

									// 创建客户CatBean
									CatBean clientBean = new CatBean();
									clientBean.setType(3);
									clientBean.setName(name); // 接收文件的客户名字
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(saveFilePath);
									clientBean.setInfo("确定接收文件");

									// 判断要发送给谁
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set); // 文件来源
									clientBean.setTo(bean.getClients());// 给这些客户发送文件

									// 创建新的tcp socket 接收数据, 额外增加的功能
									try {
										ServerSocket ss = new ServerSocket(0); // 0可以获取空闲的端口号

										clientBean.setIp(clientSocket
												.getInetAddress()
												.getHostAddress());
										clientBean.setPort(ss.getLocalPort());
										sendMessage(clientBean); // 先通过服务器告诉发送方,
																	// 你可以直接发送文件到我这里了...
										isReceiveFile = true;
										// 等待文件来源的客户，输送文件....目标客户从网络上读取文件，并写在本地上
										Socket sk = ss.accept();
										textArea.append(CatUtil.getTimer()
												+ "  " + bean.getFileName()
												+ "文件保存中.\r\n");
										DataInputStream dis = new DataInputStream( // 从网络上读取文件
												new BufferedInputStream(
														sk.getInputStream()));
										DataOutputStream dos = new DataOutputStream( // 写在本地上
												new BufferedOutputStream(
														new FileOutputStream(
																saveFilePath)));

										int count = 0;
										int num = bean.getSize() / 100;
										int index = 0;
										while (count < bean.getSize()) {
											int t = dis.read();
											dos.write(t);
											count++;

											if (num > 0) {
												if (count % num == 0
														&& index < 100) {
													progressBar
															.setValue(++index);
												}
												lblNewLabel.setText("下载进度:"
														+ count + "/"
														+ bean.getSize()
														+ "  整体" + index + "%");
											} else {
												lblNewLabel
														.setText("下载进度:"
																+ count
																+ "/"
																+ bean.getSize()
																+ "  整体:"
																+ new Double(
																		new Double(
																				count)
																				.doubleValue()
																				/ new Double(
																						bean.getSize())
																						.doubleValue()
																				* 100)
																		.intValue()
																+ "%");
												if (count == bean.getSize()) {
													progressBar.setValue(100);
												}
											}

										}

										// 给文件来源客户发条提示，文件保存完毕
										PrintWriter out = new PrintWriter(
												sk.getOutputStream(), true);
										out.println(CatUtil.getTimer() + " 发送给"
												+ name + "的文件["
												+ bean.getFileName() + "]"
												+ "文件保存完毕.\r\n");
										out.flush();
										dos.flush();
										dos.close();
										out.close();
										dis.close();
										sk.close();
										ss.close();
										textArea.append(CatUtil.getTimer()
												+ "  " + bean.getFileName()
												+ "文件保存完毕.存放位置为:"
												+ saveFilePath + "\r\n");
										isReceiveFile = false;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									break;
								}
								default: {
									CatBean clientBean = new CatBean();
									clientBean.setType(4);
									clientBean.setName(name); // 接收文件的客户名字
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(bean.getFileName());
									clientBean.setInfo(CatUtil.getTimer()
											+ "  " + name + "取消接收文件["
											+ bean.getFileName() + "]");

									// 判断要发送给谁
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set); // 文件来源
									clientBean.setTo(bean.getClients());// 给这些客户发送文件

									sendMessage(clientBean);

									break;

								}
								}
							};
						}.start();
						break;
					}
					case 3: { // 目标客户愿意接收文件，源客户开始读取本地文件并发送到网络上
						textArea.append(bean.getTimer() + "  " + bean.getName()
								+ "确定接收文件" + ",文件传送中..\r\n");
						new Thread() {
							public void run() {

								try {
									isSendFile = true;
									// 创建要接收文件的客户套接字
									Socket s = new Socket(bean.getIp(),
											bean.getPort());
									DataInputStream dis = new DataInputStream(
											new FileInputStream(filePath)); // 本地读取该客户刚才选中的文件
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(
													s.getOutputStream())); // 网络写出文件

									int size = dis.available();

									int count = 0; // 读取次数
									int num = size / 100;
									int index = 0;
									while (count < size) {

										int t = dis.read();
										dos.write(t);
										count++; // 每次只读取一个字节

										if (num > 0) {
											if (count % num == 0 && index < 100) {
												progressBar.setValue(++index);

											}
											lblNewLabel.setText("上传进度:" + count
													+ "/" + size + "  整体"
													+ index + "%");
										} else {
											lblNewLabel
													.setText("上传进度:"
															+ count
															+ "/"
															+ size
															+ "  整体:"
															+ new Double(
																	new Double(
																			count)
																			.doubleValue()
																			/ new Double(
																					size)
																					.doubleValue()
																			* 100)
																	.intValue()
															+ "%");
											if (count == size) {
												progressBar.setValue(100);
											}
										}
									}
									dos.flush();
									dis.close();
									// 读取目标客户的提示保存完毕的信息...
									BufferedReader br = new BufferedReader(
											new InputStreamReader(
													s.getInputStream()));
									textArea.append(br.readLine() + "\r\n");
									isSendFile = false;
									br.close();
									s.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							};
						}.start();
						break;
					}
					case 4: {
						textArea.append(bean.getInfo() + "\r\n");
						break;
					}
					default: {
						break;
					}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void sendMessage(CatBean clientBean) {
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(clientBean);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class UUListModel extends AbstractListModel {

	private Vector vs;

	public UUListModel(Vector vs) {
		this.vs = vs;
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return vs.get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return vs.size();
	}

}

class CellRenderer extends JLabel implements ListCellRenderer {
	CellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框

		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("images//1.jpg"));
		}
		if (isSelected) {
			setBackground(new Color(255, 255, 153));// 设置背景色
			setForeground(Color.black);
		} else {
			// 设置选取与取消选取的前景与背景颜色.
			setBackground(Color.white); // 设置背景色
			setForeground(Color.black);
		}
		setEnabled(list.isEnabled());
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}
