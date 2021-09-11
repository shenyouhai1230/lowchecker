package com.syh.low.checker.views;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.text.AttributeSet.FontAttribute;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Mygroup;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.syh.low.checker.utils.FileInfo;
import com.syh.low.checker.utils.PrintError;
import com.syh.low.checker.utils.SFTPUtil;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class C2MView extends ViewPart {
	public C2MView() {
	}
	public static final String ID = "com.syh.low.checker.view";
	List<String> remoteFiles = new ArrayList<String>();
	List<FileInfo> lfiles = new ArrayList<FileInfo>();
	Map<String,String> mfiles = new HashMap<String,String>();
	StringWriter sw = new StringWriter();
	Text tlog;
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
//		composite.setBackgroundImage(SWTResourceManager.getImage(this.getClass(),"frame1.jpg"));
		Mygroup group = new Mygroup(composite, SWT.SHADOW_NONE);
		group.setBounds(10, 10, 584, 129);
		group.setForeground(new Color(null, 255, 249, 177));
		
		Mygroup loggroup = new Mygroup(composite, SWT.NONE);
		loggroup.setBounds(10, 180, 284, 220);
		Label lblNewLabel = new Label(loggroup, SWT.CENTER);
		lblNewLabel.setBounds(10, 0, 55, 17);
		lblNewLabel.setText("104 files");
		
		Mygroup loggroup1 = new Mygroup(composite, SWT.NONE);
		loggroup1.setBounds(310, 180, 284, 220);
		Label auth = new Label(group, 0);
		auth.setText("���C2M�ļ�");
		auth.setBounds(480, 15, 84, 20);
		Label jhost = new Label(group, 0);
		jhost.setText("\u6587\u4EF6\u5939");
		jhost.setBounds(66, 50, 48, 20);
		final Text thost = new Text(group, 2048);
		thost.setBounds(120, 50, 130, 20);
		tlog = new Text(loggroup, 2634);
		tlog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tlog.setBounds(5, 25, 269, 190);
		
		
		final Text diffsize = new Text(loggroup1, 2634);
		diffsize.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		diffsize.setBounds(5, 25, 269, 190);
		
		Label label = new Label(loggroup1, SWT.CENTER);
		label.setText("Different files");
		label.setBounds(10, 0, 84, 17);
		Button bt = new Button(group, 1024);
		bt.setText("\u68C0\u67E5");
		bt.setBounds(201, 99, 60, 20);
		bt.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				// ���
				String dir = thost.getText();
				File path = new File(dir);
				try {
					checkc2mfile(path);
				} catch (Exception e1) {
					PrintError.getErrorMsg(e1, sw);
					tlog.append(getTime() + "   " + sw.toString());
				}
			}
		});
		
		Button cancel = new Button(group, 1024);
		cancel.setText("�˳�");
		cancel.setBounds(280, 99, 60, 20);
		cancel.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				// �˳�����
				if (sftp != null) {
					try {
						sftp.getSession().disconnect();
					} catch (JSchException e1) {
						e1.printStackTrace();
					}
					sftp.disconnect();
					sftp.exit();
					tlog.append("���ӶϿ�\n\r");
				}
				getViewSite().getPage().hideView(getViewSite().getPage().findView("com.syh.low.checker.view"));
			}

		});
		
		Button liulan = new Button(group, 1024);
		liulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = "C:\\Windows\\Fonts\\simhei.ttf";
				File file = new File(path);
				if (file.isFile()) {
					System.out.println("文件存在");
				} else {
					System.out.println("文件不存在");
				}
//				for(int i=1;i<1000;i++){
//					try {
//						Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File(path));
////						font1.deriveFont(Font.BOLD , 7);
//						Font f = font1.deriveFont(font1.getAttributes());
//						Class c = Font.class;
//						Field ff;
//						try {
//							ff = c.getDeclaredField("font2DHandle");
//							ff.setAccessible(true);
//							ff.set(font1, null);
//							ff.set(f, null);
//						} catch (SecurityException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						
//					} catch (FontFormatException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
			}
		});
		liulan.setText("���");
		liulan.setBounds(250, 50, 60, 20);
		liulan.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				try {
					DirectoryDialog fid = new DirectoryDialog(composite.getShell());
					fid.getFilterPath();
					String save = fid.open();
					if ((save != null) && (!"".equals(save)))
						thost.setText(save);
				} catch (Exception e1) {
					PrintError.getErrorMsg(e1, sw);
					tlog.append(getTime() + "   " + sw.toString());
				}

			}
		});
//
		Button b_MD5 = new Button(group, 1024);
		b_MD5.setText("У���ļ�");
		b_MD5.setBounds(310, 50, 60, 20);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel_1.setBounds(376, 50, 20, 17);
		lblNewLabel_1.setText("\u2462");
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("\u2461");
		label_1.setBounds(175, 99, 20, 17);
		
		Label label_2 = new Label(group, SWT.NONE);
		label_2.setText("\u2460 ");
		label_2.setBounds(40, 50, 20, 17);
		b_MD5.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				try {
					if (sftp != null) {
						try {
							sftp.getSession().disconnect();
						} catch (JSchException e1) {
							e1.printStackTrace();
						}
						sftp.disconnect();
						sftp.exit();
					}
					sftp = SFTPUtil.connect("158.220.21.72", "btopzsc", "daban123456",tlog);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//��ȡ��ǰ������û���
				String username = System.getProperty("user.name");
				File localf = new File("C:\\Users\\"+username+"\\Desktop\\JDJD");
				if(!localf.exists()){
					localf.mkdirs();
				}
				for(File file : localf.listFiles()){
					file.delete();
				}
				for(String file : remoteFiles){
					try {
						SFTPUtil.download("BIPS_A/workspace/zjrc/classes/"+file.replaceAll("\\\\", "/"),localf.getAbsolutePath(),sftp);
					} catch (SftpException e1) {
						e1.printStackTrace();
					}
				}
				try {
					newMFiles("C:\\Users\\"+username+"\\Desktop\\JDJD");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(FileInfo lfi : lfiles){
					if(!lfi.getMD5().equals(mfiles.get(lfi.getName()))){
						diffsize.append(lfi.getPath()+"\n\r");
					}
				}
			}
		});
		Properties p = System.getProperties();
    	System.out.println("************swingri.home"+p.getProperty("swingri.home"));
	}
	public void checkc2mfile(File path) throws Exception {
		if (!path.exists()) {
			return;
		}
		if (path.isFile()) {
			String c2m = path.getName().substring(
					path.getName().indexOf(".") + 1);
			if ("c2m".equals(c2m)) {
				String[] p = path.getPath().split("classes");
				if (104l == path.length()) {
					tlog.append(p[1].substring(1) + "\n\r");
				} else {
					remoteFiles.add(p[1].substring(1));
					FileInfo lfinfo = new FileInfo();
					lfinfo.setName(path.getName());
					lfinfo.setMD5(getMD5(path));
					lfinfo.setPath(p[1].substring(1));
					lfiles.add(lfinfo);
				}
			}
			return;
		}
		File[] files = path.listFiles();
		for (File f : files) {
			checkc2mfile(f);
		}
	}
	public String getTime() {
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(now);
	}
	public String getMD5(String path) throws Exception {
		File a = new File(path);
		FileInputStream in = null;
		String s = null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			in = new FileInputStream(a);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(
					FileChannel.MapMode.READ_ONLY, 0, a.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			byte[] tmp = md5.digest();// MD5
			char[] str = new char[16 * 2];// 16������2���ַ��ʾ
			int k = 0;// ת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < tmp.length; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// ȡ��4λ
				str[k++] = hexDigits[byte0 & 0xf];// ȡ��4λ
			}
			s = new String(str);
			return s;
		} catch (Exception e1) {
			PrintError.getErrorMsg(e1, sw);
  	      	tlog.append(getTime() + "   " + sw.toString());
			return null;
		} finally{
			if(null != in){
				in.close();
			}
		}
	}
	public String getMD5(File a) throws Exception {
		FileInputStream in = null;
		String s = null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			in = new FileInputStream(a);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(
					FileChannel.MapMode.READ_ONLY, 0, a.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			byte[] tmp = md5.digest();// MD5
			char[] str = new char[16 * 2];// 16������2���ַ��ʾ
			int k = 0;// ת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < tmp.length; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// ȡ��4λ
				str[k++] = hexDigits[byte0 & 0xf];// ȡ��4λ
			}
			s = new String(str);
			return s;
		} catch (Exception e1) {
			PrintError.getErrorMsg(e1, sw);
  	      	tlog.append(getTime() + "   " + sw.toString());
			return null;
		} finally{
			if(null != in){
				in.close();
			}
		}
	}
	public void newMFiles(String path) throws Exception {
		File f = new File(path);
		if (!f.exists()) {
			return;
		}
		File[] files = f.listFiles();
		for (File f1 : files) {
			mfiles.put(f1.getName(),getMD5(f1));
		}
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