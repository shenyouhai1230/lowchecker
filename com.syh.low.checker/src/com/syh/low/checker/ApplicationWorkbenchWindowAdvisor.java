package com.syh.low.checker;


import java.lang.reflect.Method;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.wb.swt.SWTResourceManager;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
private ApplicationActionBarAdvisor aab;
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		aab = new ApplicationActionBarAdvisor(configurer);
		return aab;
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(900, 600));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowProgressIndicator(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowMenuBar(true);
		configurer.setTitle("Low Checker");
		int style = getWindowConfigurer().getShellStyle();
		getWindowConfigurer().setShellStyle(style);
	}
	 /**
	  * @wbp.parser.entryPoint
	  */
	 public void postWindowOpen() {
		 WorkbenchPage page = (WorkbenchPage) getWindowConfigurer().getWindow().getActivePage();
			Composite client = page.getClientComposite();
			client.getShell().setBackground(new Color(null, 199, 237, 204));
			client.getShell().setBackgroundMode(SWT.INHERIT_DEFAULT);
			 WorkbenchWindow w = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			 MenuManager bar =  w.getMenuBarManager();
			 Menu menu = bar.getMenu();
			 Color c = new Color(null, 158, 228, 231);
			 Method m = null;
			 try {
				m = menu.getClass().getDeclaredMethod("setForeground", c.getClass());
				m.setAccessible(true);
				m.invoke(menu, new Color(null, 100, 0, 0));
				m = menu.getClass().getDeclaredMethod("setBackground", c.getClass());
				m.setAccessible(true);
				m.invoke(menu, c);
			} catch (Exception e) {
				e.printStackTrace();
			} 
	 }
	 
	public void setEditorAreaBG(){
		WorkbenchPage page = (WorkbenchPage) getWindowConfigurer().getWindow().getActivePage();
		Composite client = page.getClientComposite();
		Control[] Children=  client.getChildren();
		Composite child = (Composite) Children[0];
		child.setBackground(new Color(null, 69, 96, 66));
		Control[] controls = child.getChildren();
		for(Control c:controls){
			if(c instanceof CTabFolder){
				CTabFolder cf = (CTabFolder) c;
				cf.setBackground(new Color(null, 69, 96, 66));
			}
		}
	}
	
}
