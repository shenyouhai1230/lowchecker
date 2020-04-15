package com.syh.low.checker;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.syh.low.checker.action.C2mAction;
import com.syh.low.checker.action.CpdtAction;
import com.syh.low.checker.action.DataSourceAction;
import com.syh.low.checker.action.ExcelAction;
import com.syh.low.checker.web.ChromeAction;
import com.syh.low.checker.web.WebAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
private IWorkbenchAction action1;
private IWorkbenchAction action2;
private IWorkbenchAction action3;
private IWorkbenchAction action4;
private IWorkbenchAction action5;
private IWorkbenchAction actionExcel;
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	protected void makeActions(IWorkbenchWindow window) {
		action1 = new C2mAction(window);
		action1.setText("C2M");
		action1.setId("com.syh.low.checker.action.c2maction");
		register(action1);
		action2 = new CpdtAction(window);
		action2.setText("Cpdt");
		action2.setId("com.syh.low.checker.action.cpdtaction");
		register(action2);
		action3 = new WebAction(window);
		action3.setText("IE");
		action3.setId("com.syh.low.checker.action.webaction");
		register(action3);
		action4 = new ChromeAction();
		action4.setText("Chrome");
		action4.setId("com.syh.low.checker.chrome");
		register(action4);
		action5 = new DataSourceAction(window);
		action5.setText("DataSource");
		action5.setId("com.syh.low.checker.action.DataSourceAction");
		register(action5);
		actionExcel = new ExcelAction(window);
		actionExcel.setText("excel");
		actionExcel.setId("com.syh.low.checker.excelAction");
		register(actionExcel);
    }
protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager Menu = new MenuManager("Check");
		Menu.add(action1);
		Menu.add(new Separator());
		Menu.add(action2);
		Menu.add(new Separator());
		Menu.add(action5);
		Menu.add(new Separator());
		Menu.add(action3);
		Menu.add(new Separator());
		Menu.add(action4);
		Menu.add(new Separator());
		Menu.add(actionExcel);
		menuBar.add(Menu);
	}
}
