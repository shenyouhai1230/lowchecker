package com.syh.low.checker.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 
 * 
 */
public class BspFileUtil {

	public static String CODING = "GBK";

	public BspFileUtil() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public static List<MFDItem> getMFDItems(File file,
			String mfdname) throws Exception {

		Document doc = XmlUtil.getDocument(file.getAbsolutePath());

		Element root = doc.getRootElement();

		Element body = XmlUtil.getElementByName(root, "Body");

		Element Group = XmlUtil.getElementByName(body, "Group");

		Element _body = XmlUtil.getElementByName(Group, "Body");

		List<Element> items = XmlUtil.getElementsByName(_body, "Item");

		List<MFDItem> itemsBeans = new ArrayList<MFDItem>();

		for (Iterator<Element> iterator = items.iterator(); iterator.hasNext();) {
			Element itemelement = (Element) iterator.next();
			MFDItem item = new MFDItem();
			if (itemelement.attribute("Name") == null)
				continue;
			item.Name = itemelement.attribute("Name").getValue();

			if (itemelement.attribute("Description") != null)
				item.Description = itemelement.attribute("Description")
						.getValue();
			else
				item.Description = "列名";
			Element Head = XmlUtil.getElementByName(itemelement, "Head");
			List<Element> Propertys = XmlUtil.getElementsByName(Head,
					"Property");
			for (Iterator<Element> iterator2 = Propertys.iterator(); iterator2
					.hasNext();) {
				Element Property = (Element) iterator2.next();
				String _Name = Property.attribute("Name").getValue();
				if ("itemLen".equals(_Name)) {
					item.itemLen = Integer.parseInt(Property.attribute("Value")
							.getValue());
					break;
				}
			}
			itemsBeans.add(item);
		}

		List<Element> whileItem = XmlUtil.getElementsByName(_body, "While");

		for (Iterator<Element> iterator = whileItem.iterator(); iterator
				.hasNext();) {
			Element itemelement = (Element) iterator.next();
			MFDItem item = new MFDItem();
			int maxNum = Integer.parseInt(itemelement.attribute("MaxNum")
					.getValue());

			String colname = itemelement.attribute("Name").getValue();

			Element Body = XmlUtil.getElementByName(itemelement, "Body");
			
			Element Item = XmlUtil.getElementByName(Body, "Item");

			Element Head = XmlUtil.getElementByName(Item, "Head");

			List<Element> Propertys = XmlUtil.getElementsByName(Head,
					"Property");

			int itemLenValue = 0;
			for (Iterator<Element> iterator2 = Propertys.iterator(); iterator2
					.hasNext();) {
				Element Property = (Element) iterator2.next();
				String Name = Property.attribute("Name").getValue();
				if ("itemLen".equals(Name)) {
					itemLenValue = Integer.parseInt(Property.attribute("Value")
							.getValue());
				}
			}

			for (int i = 0; i < maxNum; i++) {
				MFDItem whileitem = new MFDItem();

				whileitem.Name = colname + i;

				whileitem.Description = "列名";

				whileitem.itemLen=itemLenValue;
				
				itemsBeans.add(whileitem);
			}

		}

		return itemsBeans;
	}

	public static List<Map<String, Object>> getContents(String fileDataStr,
			List<MFDItem> itemsBeans, String filePath) throws Exception {
		List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();

		String[] fileData = null;
		if (fileDataStr != null)
			fileData = fileDataStr.split("\n");
		else
			return null;

		int itemsLen = itemsBeans.size();
		String[] keys = new String[itemsLen];
		int[] itemLens = new int[itemsLen];
		int i = 0;
		for (MFDItem item : itemsBeans) {
			keys[i] = item.Name;
			itemLens[i] = item.itemLen;
			i++;
		}

		for (i = 0; i < fileData.length; i++) {
			if (StringUtilEx.isNullOrEmpty(fileData[i]))
				continue;
			Map<String, Object> map = new HashMap<String, Object>();
			int startIndex = 0;
			for (int j = 0; j < itemsLen; j++) {
				byte col[] = new byte[itemLens[j]];
				if (fileData[i].getBytes(CODING).length < startIndex
						|| fileData[i].getBytes(CODING).length < itemLens[j])
					break;
				System.arraycopy(fileData[i].getBytes(CODING), startIndex, col,
						0, itemLens[j]);
				map.put(keys[j], new String(col, CODING).trim());
				startIndex = startIndex + itemLens[j];
			}

			rsList.add(map);
		}
		return rsList;
	}

}
