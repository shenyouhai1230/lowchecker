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
				icon = new ImageIcon(getClass().getResource("/images/������1.jpg"));
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
		// ������Ϣ��ʾ����
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 224, 300);
		panel.add(scrollPane);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("sdf", Font.BOLD, 13));
		scrollPane.setViewportView(textArea);
		// ��������
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 347, 224, 97);
		panel.add(scrollPane_1);
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);// �����Զ����й���
		textArea_1.setWrapStyleWord(true);// ������в����ֹ���
		scrollPane_1.setViewportView(textArea_1);
		scrollPane_1.setFocusable(true);
		textArea_1.setFocusable(true);
		// �رհ�ť
		final JButton btnNewButton = new JButton("\u5173\u95ED");
		btnNewButton.setBounds(20, 448, 77, 30);
		panel.add(btnNewButton);
		// ���Ͱ�ť
		JButton btnNewButton_1 = new JButton("\u53D1\u9001");
		btnNewButton_1.setBounds(124, 448, 77, 30);
		panel.add(btnNewButton_1);
		// ���߿ͻ��б�
		listmodel = new UUListModel(onlines);
		list = new JList(listmodel);
		list.setCellRenderer(new CellRenderer());
		list.setOpaque(false);
		Border etch = BorderFactory.createEtchedBorder();
		list.setBorder(BorderFactory.createTitledBorder(etch, "���߿ͻ�:",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("sdf",
						Font.BOLD, 20), Color.green));
		JScrollPane scrollPane_2 = new JScrollPane(list);
		scrollPane_2.setBounds(248, 10, 198, 375);
		scrollPane_2.setOpaque(false);
		scrollPane_2.getViewport().setOpaque(false);
		panel.add(scrollPane_2);
		// �ļ�������
		progressBar = new JProgressBar();
		progressBar.setBounds(248, 393, 198, 15);
		progressBar.setMinimum(1);
		progressBar.setMaximum(100);
		panel.add(progressBar);
		// ���ӷ���
		try {
			clientSocket = new Socket("localhost", 8520);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		// �ļ�������ʾ
		lblNewLabel = new JLabel("\u6587\u4EF6\u4F20\u9001\u4FE1\u606F\u680F:");
		lblNewLabel.setFont(new Font("SimSun", Font.PLAIN, 12));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(248, 410, 245, 15);
		panel.add(lblNewLabel);
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			// ��¼���߿ͻ�����Ϣ��catbean�У������͸�������
			CatBean bean = new CatBean();
			bean.setType(0);
			bean.setName(name);
			bean.setTimer(CatUtil.getTimer());
			oos.writeObject(bean);
			oos.flush();
			// ��Ϣ��ʾ����
			cb = getClass().getResource("/sounds/��ŷ.wav");
			aau = Applet.newAudioClip(cb);
			// ������ʾ����
			cb2 = getClass().getResource("/sounds/��.wav");
			aau2 = Applet.newAudioClip(cb2);

			// �����ͻ������߳�
			new ClientInputThread().start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���Ͱ�ť
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
					JOptionPane.showMessageDialog(panel, "��ѡ���������");
					return;
				}
				if (to.toString().contains(name + "(��)")) {
					JOptionPane
							.showMessageDialog(panel, "�������Լ�������Ϣ");
					return;
				}
				if (info.equals("")) {
					JOptionPane.showMessageDialog(panel, "���ܷ��Ϳ���Ϣ");
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

				// �Լ���������ҲҪ��ʵ���Լ�����Ļ����
				textArea.append(time + " �Ҷ�" + to + "˵:\r\n" + info + "\r\n");

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
				// ��ͣ�Ĵӷ�����������Ϣ
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					final CatBean bean = (CatBean) ois.readObject();
					switch (bean.getType()) {
					case 0: {
						// �����б�
						onlines.clear();
						HashSet<String> clients = bean.getClients();
						Iterator<String> it = clients.iterator();
						while (it.hasNext()) {
							String ele = it.next();
							if (name.equals(ele)) {
								onlines.add(ele + "(��)");
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
								+ " �� " + bean.getClients() + "˵:\r\n";
						if (info.contains(name)) {
							info = info.replace(name, "��");
						}
						aau.play();
						textArea.append(info + bean.getInfo() + "\r\n");
						textArea.selectAll();
						break;
					}
					case 2: {
						// ���ڵȴ�Ŀ��ͻ�ȷ���Ƿ�����ļ��Ǹ�����״̬�������������̴߳���
						new Thread() {
							public void run() {
								// ��ʾ�Ƿ�����ļ��Ի���
								int result = JOptionPane.showConfirmDialog(
										panel, bean.getInfo());
								switch (result) {
								case 0: { // �����ļ�
									JFileChooser chooser = new JFileChooser();
									chooser.setDialogTitle("�����ļ���"); // ����Ŷ...
									// Ĭ���ļ����ƻ��з��ڵ�ǰĿ¼��
									chooser.setSelectedFile(new File(bean
											.getFileName()));
									chooser.showDialog(panel, "����"); // ���ǰ�ť������..
									// ����·��
									String saveFilePath = chooser
											.getSelectedFile().toString();

									// �����ͻ�CatBean
									CatBean clientBean = new CatBean();
									clientBean.setType(3);
									clientBean.setName(name); // �����ļ��Ŀͻ�����
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(saveFilePath);
									clientBean.setInfo("ȷ�������ļ�");

									// �ж�Ҫ���͸�˭
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set); // �ļ���Դ
									clientBean.setTo(bean.getClients());// ����Щ�ͻ������ļ�

									// �����µ�tcp socket ��������, �������ӵĹ���
									try {
										ServerSocket ss = new ServerSocket(0); // 0���Ի�ȡ���еĶ˿ں�

										clientBean.setIp(clientSocket
												.getInetAddress()
												.getHostAddress());
										clientBean.setPort(ss.getLocalPort());
										sendMessage(clientBean); // ��ͨ�����������߷��ͷ�,
																	// �����ֱ�ӷ����ļ�����������...
										isReceiveFile = true;
										// �ȴ��ļ���Դ�Ŀͻ��������ļ�....Ŀ��ͻ��������϶�ȡ�ļ�����д�ڱ�����
										Socket sk = ss.accept();
										textArea.append(CatUtil.getTimer()
												+ "  " + bean.getFileName()
												+ "�ļ�������.\r\n");
										DataInputStream dis = new DataInputStream( // �������϶�ȡ�ļ�
												new BufferedInputStream(
														sk.getInputStream()));
										DataOutputStream dos = new DataOutputStream( // д�ڱ�����
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
												lblNewLabel.setText("���ؽ���:"
														+ count + "/"
														+ bean.getSize()
														+ "  ����" + index + "%");
											} else {
												lblNewLabel
														.setText("���ؽ���:"
																+ count
																+ "/"
																+ bean.getSize()
																+ "  ����:"
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

										// ���ļ���Դ�ͻ�������ʾ���ļ��������
										PrintWriter out = new PrintWriter(
												sk.getOutputStream(), true);
										out.println(CatUtil.getTimer() + " ���͸�"
												+ name + "���ļ�["
												+ bean.getFileName() + "]"
												+ "�ļ��������.\r\n");
										out.flush();
										dos.flush();
										dos.close();
										out.close();
										dis.close();
										sk.close();
										ss.close();
										textArea.append(CatUtil.getTimer()
												+ "  " + bean.getFileName()
												+ "�ļ��������.���λ��Ϊ:"
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
									clientBean.setName(name); // �����ļ��Ŀͻ�����
									clientBean.setTimer(CatUtil.getTimer());
									clientBean.setFileName(bean.getFileName());
									clientBean.setInfo(CatUtil.getTimer()
											+ "  " + name + "ȡ�������ļ�["
											+ bean.getFileName() + "]");

									// �ж�Ҫ���͸�˭
									HashSet<String> set = new HashSet<String>();
									set.add(bean.getName());
									clientBean.setClients(set); // �ļ���Դ
									clientBean.setTo(bean.getClients());// ����Щ�ͻ������ļ�

									sendMessage(clientBean);

									break;

								}
								}
							};
						}.start();
						break;
					}
					case 3: { // Ŀ��ͻ�Ը������ļ���Դ�ͻ���ʼ��ȡ�����ļ������͵�������
						textArea.append(bean.getTimer() + "  " + bean.getName()
								+ "ȷ�������ļ�" + ",�ļ�������..\r\n");
						new Thread() {
							public void run() {

								try {
									isSendFile = true;
									// ����Ҫ�����ļ��Ŀͻ��׽���
									Socket s = new Socket(bean.getIp(),
											bean.getPort());
									DataInputStream dis = new DataInputStream(
											new FileInputStream(filePath)); // ���ض�ȡ�ÿͻ��ղ�ѡ�е��ļ�
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(
													s.getOutputStream())); // ����д���ļ�

									int size = dis.available();

									int count = 0; // ��ȡ����
									int num = size / 100;
									int index = 0;
									while (count < size) {

										int t = dis.read();
										dos.write(t);
										count++; // ÿ��ֻ��ȡһ���ֽ�

										if (num > 0) {
											if (count % num == 0 && index < 100) {
												progressBar.setValue(++index);

											}
											lblNewLabel.setText("�ϴ�����:" + count
													+ "/" + size + "  ����"
													+ index + "%");
										} else {
											lblNewLabel
													.setText("�ϴ�����:"
															+ count
															+ "/"
															+ size
															+ "  ����:"
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
									// ��ȡĿ��ͻ�����ʾ������ϵ���Ϣ...
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

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// ������Ϊ5�Ŀհױ߿�

		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("images//1.jpg"));
		}
		if (isSelected) {
			setBackground(new Color(255, 255, 153));// ���ñ���ɫ
			setForeground(Color.black);
		} else {
			// ����ѡȡ��ȡ��ѡȡ��ǰ���뱳����ɫ.
			setBackground(Color.white); // ���ñ���ɫ
			setForeground(Color.black);
		}
		setEnabled(list.isEnabled());
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}
