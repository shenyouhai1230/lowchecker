package com.syh.low.checker.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class DataSourceAction extends Action implements IWorkbenchAction {
	private IWorkbenchWindow workbenchWindow;
	public DataSourceAction(IWorkbenchWindow window) {
		if (window == null) {
			throw new IllegalArgumentException();
		}
		workbenchWindow = window;
	}

	@Override
	public void run() {
		if (workbenchWindow != null) {
			try {
				workbenchWindow.getActivePage().showView("com.syh.low.checker.datasourceview");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
