package org.eclipse.swt.widgets;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.internal.win32.DRAWITEMSTRUCT;
import org.eclipse.swt.internal.win32.OS;


public class Mygroup extends Group{
	@SuppressWarnings("restriction")
	public Mygroup(Composite parent, int style) {
		super(parent, style);
		int osStyle = OS.GetWindowLong(handle, OS.GWL_STYLE);
		osStyle |= OS.BS_OWNERDRAW;
		OS.SetWindowLong(handle, OS.GWL_STYLE, osStyle);
		PaintListener pl = new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.setForeground(new Color(null, 255, 249, 177));
				gc.drawRectangle(e.x, e.y+7, e.width-1, e.height-8);
				gc.dispose();
			}
		};
		addPaintListener(pl);
	}
}
