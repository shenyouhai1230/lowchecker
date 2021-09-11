package com.syh.low.checker.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class C2mAction extends Action implements IWorkbenchAction{
	private IWorkbenchWindow workbenchWindow;
	public C2mAction(IWorkbenchWindow window){
		if(window == null){
			throw new IllegalArgumentException();
		}
		workbenchWindow = window;
	}
	
	@Override
	public void run() {
		if(workbenchWindow != null){
			try {
				workbenchWindow.getActivePage().showView("com.syh.low.checker.view");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public void dispose() {
		workbenchWindow = null;
	}

}
