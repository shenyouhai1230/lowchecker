package com.syh.low.checker.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;
import org.eclipse.wb.swt.SWTResourceManager;

import com.syh.low.checker.utils.DBUtil;

public class LoginSplashHandler extends AbstractSplashHandler{
	private Composite loginComposite;
	private Text usernameTextBox;
	private Text passwordTextBox;
	private boolean isAuthenticated;
	private Label usernameLabel;
	private Label passwordLabel;
	private Label OKLabel;
	private Label cancelLabel;
	public LoginSplashHandler() {
		isAuthenticated = false;
	}
	public void init(Shell splash) {
		super.init(splash);
		configureUISplash();
		createUI();
		createUIListeners();
		//ÏÔÊ¾
		splash.layout(true);
		while(isAuthenticated == false){
			if(splash.getDisplay().readAndDispatch() == false){
				splash.getDisplay().sleep();
			}
		}
	}
	
	private void configureUISplash(){
		FillLayout layout = new FillLayout();
		getSplash().setLayout(layout);
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
	
	private void createUI(){
		loginComposite = new Composite(getSplash(), SWT.BORDER);
		loginComposite.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		loginComposite.setLayout(null);
		
		usernameLabel = new Label(loginComposite, SWT.NONE);
		usernameLabel.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 10, SWT.BOLD));
		usernameLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		usernameLabel.setBounds(364, 157, 110, 23);
		usernameLabel.setText("&Hero's name:");
		
		usernameTextBox = new Text(loginComposite, SWT.BORDER);
		usernameTextBox.setBounds(480, 157, 101, 23);
		
		passwordLabel = new Label(loginComposite, 0);
		passwordLabel.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 10, SWT.BOLD));
		passwordLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		passwordLabel.setBounds(364, 187, 101, 23);
		passwordLabel.setText("&Hero's pwd:");
		
		int style = SWT.PASSWORD | SWT.BORDER;
		passwordTextBox = new Text(loginComposite, style);
		passwordTextBox.setBounds(480, 187, 101, 23);
		
		OKLabel = new Label(loginComposite, SWT.BORDER | SWT.CENTER);
		OKLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		OKLabel.setFont(SWTResourceManager.getFont("»ªÎÄ·ÂËÎ", 10, SWT.BOLD));
		OKLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		OKLabel.setAlignment(SWT.CENTER);
		OKLabel.setBounds(451, 216, 61, 23);
		OKLabel.setText("OK");
		
		cancelLabel = new Label(loginComposite, SWT.BORDER | SWT.CENTER);
		cancelLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		cancelLabel.setFont(SWTResourceManager.getFont("»ªÎÄ·ÂËÎ", 10, SWT.BOLD));
		cancelLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		cancelLabel.setText("Cancel");
		cancelLabel.setBounds(518, 216, 61, 23);
		
		Label titleLabel = new Label(loginComposite, SWT.NONE);
		titleLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		titleLabel.setFont(SWTResourceManager.getFont("¿¬Ìå", 14, SWT.ITALIC));
		titleLabel.setBounds(158, 10, 329, 23);
		titleLabel.setText("ONE FOR ALL , ALL FOR ONE");
		
	}
	
	private void createUIListeners(){
		OKLabel.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				handleButtonOKWidgetSelected();
			}
		});
		OKLabel.addMouseTrackListener(new MouseTrackListener() {
			@Override
			public void mouseHover(MouseEvent e) {
				OKLabel.setBackground(new Color(null,  255, 249, 177));
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				OKLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
				
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				OKLabel.setBackground(new Color(null,  255, 249, 177));
			}
		});
		cancelLabel.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				getSplash().getDisplay().close();
				System.exit(0);
			}
		});
		cancelLabel.addMouseTrackListener(new MouseTrackListener() {
			@Override
			public void mouseHover(MouseEvent e) {
				cancelLabel.setBackground(new Color(null,  255, 249, 177));
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				cancelLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				cancelLabel.setBackground(new Color(null,  255, 249, 177));
			}
		});
	}
	private void handleButtonOKWidgetSelected(){
		String name = usernameTextBox.getText();
		String pwd = passwordTextBox.getText();
		String spwd =null;
		isAuthenticated = true;
//		try {
//			Connection conn = DBUtil.getConnection();
//			PreparedStatement pstat = conn.prepareStatement("select * from user where name = '"+name+"'");
//			ResultSet rs = pstat.executeQuery();
//			while(rs.next()){
//				spwd = rs.getString("pwd");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(!pwd.equals(spwd)){
//			MessageDialog.openError(getSplash(), "µÇÂ¼Ê§°Ü", "ÓÃ»§»òÃÜÂë´íÎó");
//		}else{
//			if(true){
//				isAuthenticated = true;
//			}else{
//				
//			}
//		}
	}
}
