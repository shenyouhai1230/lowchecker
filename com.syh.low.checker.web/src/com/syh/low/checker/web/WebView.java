package com.syh.low.checker.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

public class WebView extends ViewPart{
	public WebView() {
	}
	public static final String ID = "com.syh.low.checker.webview";
	private static final String DEFAUKT_BLANK_URL="about:blank";
	private static final String BROWSER_DATA__STATUS="status";
	private List<String> invalidTitleList = new ArrayList<String>();
	private final static FormToolkit tookit = new FormToolkit(Display.getCurrent());
	private static Combo combUrl;
	private CTabFolder browserFolder;
	private CTabItem newTab;
	private CLabel statusLabel;
	private ProgressBar progressBar;
	{
		invalidTitleList.add(DEFAUKT_BLANK_URL);
		invalidTitleList.add("503 Service Unavailable");
		invalidTitleList.add("导航已取消");
		invalidTitleList.add("Internet Explorer 无法显示该网页");
		invalidTitleList.add("无法显示此网页");
	}
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());
		tookit.adapt(parent);
		tookit.paintBordersFor(parent);
		Composite composite = tookit.createComposite(parent,SWT.NO_TRIM|SWT.RESIZE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 2;
		gridLayout.numColumns = 7;
		composite.setLayout(gridLayout);
		FormData fd = new FormData();
		fd.right=new FormAttachment(100, -5);
		fd.bottom=new FormAttachment(0,40);
		fd.top=new FormAttachment(0,0);
		fd.left=new FormAttachment(0,5);
		composite.setLayoutData(fd);
		tookit.paintBordersFor(composite);
		
		Button newBt = new Button(composite, SWT.NONE);
		newBt.setToolTipText("新建浏览器");
		newBt.setText("+");
		newBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		newBt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				openNewBrowserTab();
			}
		});
		tookit.adapt(newBt,true,true);
		
		Label label = new Label(composite, SWT.SEPARATOR);
		label.setLayoutData(new GridData(SWT.DEFAULT,35));
		tookit.adapt(label,true,true);
		
		ButtonSlectionListener listener = new ButtonSlectionListener();
		
		Button backBt = new Button(composite, SWT.NONE);
		backBt.setToolTipText("后退");
		backBt.setText("←");
		backBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		backBt.addSelectionListener(listener);
		
		Button fowardBt = new Button(composite, SWT.NONE);
		fowardBt.setToolTipText("前进");
		fowardBt.setText("→");
		fowardBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		fowardBt.addSelectionListener(listener);
		
		combUrl = new Combo(composite, SWT.BORDER);
		combUrl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		combUrl.setVisibleItemCount(5);
		combUrl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openEnterUrl(false);
			}
		});
		combUrl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					openEnterUrl(true);
				}
				
			}
		});
		tookit.adapt(combUrl, true, true);
		
		Button stopdBt = new Button(composite, SWT.NONE);
		stopdBt.setToolTipText("停止");
		stopdBt.setText("stop");
		stopdBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		stopdBt.addSelectionListener(listener);
		tookit.adapt(stopdBt, true, true);
		
		Button refreshBt = new Button(composite, SWT.NONE);
		refreshBt.setToolTipText("刷新");
		refreshBt.setText("refresh");
		refreshBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		refreshBt.addSelectionListener(listener);
		tookit.adapt(refreshBt, true, true);
		
		browserFolder = new CTabFolder(parent, SWT.BORDER);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.right = new FormAttachment(100, -5);
		fd_tabFolder.top = new FormAttachment(composite, 9);
		fd_tabFolder.left = new FormAttachment(composite, 0, SWT.LEFT);
		browserFolder.setLayoutData(fd_tabFolder);
		browserFolder.setMinimumCharacters(8);
		browserFolder.setSelectionBackground(new Color(null, 227, 238, 251));
		browserFolder.setTabHeight(25);
		browserFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if(e.item == newTab){
					openNewBrowserTab();
				}else{
					Browser browser = (Browser) ((CTabItem)e.item).getControl();
					if(browser != null){
						String url = browser.getUrl();
						if("".equals(url)){
							url = DEFAUKT_BLANK_URL;
						}
						combUrl.setText(url);
						if(url.equals(DEFAUKT_BLANK_URL)){
							combUrl.setFocus();
						}
						String status = (String) browser.getData(BROWSER_DATA__STATUS);
						if(null != status){
							statusLabel.setText(status);
						}
						progressBar.setVisible(false);
					}
				}
			}
		});
		browserFolder.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				CTabItem tab = browserFolder.getItem(new Point(e.x, e.y));
				if(null != tab && tab != newTab){
					tab.dispose();
					if(browserFolder.getItemCount() == 1){
						
					}
				}else{
					
				}
			}
		});
		tookit.adapt(browserFolder, true, true);
		
		newTab = new CTabItem(browserFolder, SWT.NONE);
		newTab.setToolTipText("新选项卡");
		newTab.setText("+");
		
		statusLabel = new CLabel(parent, SWT.None);
		fd_tabFolder.bottom = new FormAttachment(statusLabel,-5, SWT.TOP);
		statusLabel.setText("状态栏");
		FormData fd_statusLabel = new FormData();
		fd_statusLabel.left = new FormAttachment(0, 5);
		fd_statusLabel.bottom = new FormAttachment(100, -5);
		statusLabel.setLayoutData(fd_statusLabel);
		tookit.adapt(statusLabel, true, true);
		
		progressBar = new ProgressBar(parent, SWT.NONE);
		fd_statusLabel.right = new FormAttachment(progressBar, -10);
		FormData fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(100, -200);
		fd_progressBar.left = new FormAttachment(0, 394);
		fd_progressBar.bottom = new FormAttachment(statusLabel, 0, SWT.BOTTOM);
		progressBar.setLayoutData(fd_progressBar);
		tookit.adapt(progressBar, true, true);
		
		
	}
private void addUrlToCombo(String url){
	if(!DEFAUKT_BLANK_URL.equals(url)){
		int index = combUrl.indexOf(url);
		if(index > 0){
			combUrl.remove(index);
		}
		if(index != 0){
			combUrl.add(url, 0);
		}
		combUrl.select(0);
		if(combUrl.getItemCount() > 20){
			combUrl.remove(20, combUrl.getItemCount()-1);
		}
	}
}
	//打开网页
	private void openEnterUrl(boolean newTabItem){
		String url = combUrl.getText();
		if(!("".equals(url)|| DEFAUKT_BLANK_URL.equals(url))){
			Browser browser = null;
			CTabItem tabItem =  browserFolder.getSelection();
			String itemTitle ="";
			if(null != tabItem){
				itemTitle = tabItem.getToolTipText();
			}
			if(newTabItem && !invalidTitleList.contains(itemTitle)){
				browser = openNewBrowserTab();
			}else{
				browser = (Browser) tabItem.getControl();
			}
			browser.setUrl(url);
			CTabItem item = ((CTabFolder)browser.getParent()).getSelection();
			item.setText(StringUtils.abbreviate(url, 20));
			item.setToolTipText(url);
			addUrlToCombo(url);
		}
	}
	
	//打开一个新选项卡
	private Browser openNewBrowserTab(){
		CTabItem browserTab = new CTabItem(browserFolder, SWT.NONE, browserFolder.getItemCount()-1);
		browserTab.setShowClose(true);
		browserFolder.setSelection(browserTab);
		
		Browser browser = new Browser(browserFolder, SWT.None);
		initialize(browser);
		browserTab.setText(DEFAUKT_BLANK_URL);
		browserTab.setToolTipText(DEFAUKT_BLANK_URL);
		combUrl.setText(DEFAUKT_BLANK_URL);
		combUrl.setFocus();
		tookit.adapt(browser, true, true);
		browserTab.setControl(browser);
		return browser;
	}
	private void initialize(final Browser browser){
		browser.addOpenWindowListener(new OpenWindowListener() {
			@Override
			public void open(WindowEvent event) {
				event.browser = openNewBrowserTab();
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener() {
			
			@Override
			public void close(WindowEvent event) {
				Browser browser = (Browser) event.widget;
				((CTabFolder)browser.getParent()).getSelection().dispose();
			}
		});
		browser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent event) {
				if(browserFolder.getSelection().getControl() == browser){
					progressBar.setVisible(false);
				}
				
			}
			
			@Override
			public void changed(ProgressEvent event) {
				if(browserFolder.getSelection().getControl() == browser){
					if(!progressBar.isVisible()){
						progressBar.setVisible(true);
					}
					if(event.current < event.total){
						progressBar.setMinimum(event.total);
						progressBar.setSelection(event.current);
					}else{
						progressBar.setVisible(false);
					}
				}
				
			}
		});
		
		browser.addStatusTextListener(new StatusTextListener() {
			
			@Override
			public void changed(StatusTextEvent event) {
				browser.setData(BROWSER_DATA__STATUS, event.text);
				if(browserFolder.getSelection().getControl() == browser){
					statusLabel.setText(event.text);
				}
			}
		});
		
		browser.addTitleListener(new TitleListener() {
			
			@Override
			public void changed(TitleEvent event) {
				CTabItem item = ((CTabFolder)browser.getParent()).getSelection();
				item.setText(StringUtils.abbreviate(event.title, 20));
				item.setToolTipText(event.title);
			}
		});
		
		browser.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void changed(LocationEvent event) {
				if(event.top){
					addUrlToCombo(event.location);
				}
				
			}
		});
//		News_
	}
	class ButtonSlectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			String optionName = ((Button)e.widget).getToolTipText();
			Browser browser = (Browser) browserFolder.getSelection().getControl(); 
			if(browser != null){
				if("后退".equals(optionName)){
					if(browser.isBackEnabled()){
						browser.back();
					}
				}
			}else if("前进".equals(optionName)){
				if(browser.isForwardEnabled()){
					browser.forward();
				}
			}else if ("刷新".equals(optionName)) {
				browser.refresh();
			}else if("停止".equals(optionName)){
				browser.stop();
			}
		}
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	 public Image getTitleImage() {
	        return new Image(getSite().getShell().getDisplay(), new Rectangle(0, 0, 1, 1));
	    }
}
