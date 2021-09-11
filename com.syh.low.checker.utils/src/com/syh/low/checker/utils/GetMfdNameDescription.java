package com.syh.low.checker.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GetMfdNameDescription {
	public static void getMfdNameDescription(String mfdname,String path,String tablecolumnPath) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("文件不存在！");
			return;
		}
		List<String> list = new ArrayList<String>();
		String filecontent = "";
		try {
			List<MFDItem> itemsBeans = BspFileUtil.getMFDItems(file,
					mfdname);
			for (MFDItem item : itemsBeans) {
				System.out.println(item.Name + "," + item.Description);
				list.add(item.Name + "," + item.Description);
			}
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					filecontent = list.get(i);
				} else {
					filecontent = filecontent + "\n" + list.get(i);
				}
			}
			IOUtil.writeFile(filecontent,tablecolumnPath,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
