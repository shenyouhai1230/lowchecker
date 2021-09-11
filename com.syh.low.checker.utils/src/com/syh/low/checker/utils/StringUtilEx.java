package com.syh.low.checker.utils;

import java.io.UnsupportedEncodingException;

public class StringUtilEx {
	/**
	 * 将数组整合成字符串
	 * 
	 * @param strArr
	 * @param split
	 * @return
	 */
	public static String join(String[] strArr, String split) {
		if (strArr == null)
			return "";
		String _str = "";
		for (int i = 0; i < strArr.length; i++) {
			if (_str.equals(""))
				_str = strArr[i];
			else
				_str += split + strArr[i];
		}
		return _str;
	}

	/**
	 * 用特定字符填充字符串
	 * 
	 * @param sSrc
	 *            要填充的字符串
	 * @param ch
	 *            用于填充的特定字符
	 * @param nLen
	 *            要填充到的长度
	 * @param bLeft
	 *            要填充的方向：true:左边；false:右边
	 * @return 填充好的字符串
	 */
	public static String fill(String sSrc, char ch, int nLen, boolean bLeft) {
		byte[] bTmp = trimnull(sSrc.getBytes());
		sSrc = new String(bTmp);
		if (sSrc == null || sSrc.equals("")) {
			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < nLen; i++)
				sbRet.append(ch);

			return sbRet.toString();
		}
		byte[] bySrc;
		try {
			bySrc = sSrc.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			bySrc = sSrc.getBytes();
		}
		int nSrcLen = bySrc.length;
		if (nSrcLen >= nLen) {
			return sSrc;
		}
		byte[] byRet = new byte[nLen];
		if (bLeft) {
			for (int i = 0, n = nLen - nSrcLen; i < n; i++)
				byRet[i] = (byte) ch;
			for (int i = nLen - nSrcLen, n = nLen; i < n; i++)
				byRet[i] = bySrc[i - nLen + nSrcLen];
		} else {
			for (int i = 0, n = nSrcLen; i < n; i++)
				byRet[i] = bySrc[i];
			for (int i = nSrcLen, n = nLen; i < n; i++)
				byRet[i] = (byte) ch;
		}
		try {
			return new String(byRet, "GBK");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new String(byRet);
	}

	/**
	 * 去掉字符串两头的空值
	 * 
	 * @param byRet
	 *            要去除的字符串
	 * @return 去除好的字符串
	 */

	public static byte[] trimnull(byte[] byRet) {
		int startPos = 0;
		int endPos = byRet.length - 1;
		for (int i = 0; i < byRet.length; i++) {
			if (byRet[i] != 0) {
				startPos = i;
				break;
			}
			if (i == (byRet.length - 1) && byRet[i] == 0) {
				return null;
			}
		}
		for (int i = byRet.length - 1; i >= 0; i--) {
			if (byRet[i] != 0) {
				endPos = i;
				break;
			}
		}
		byte[] byTmp = new byte[endPos - startPos + 1];
		System.arraycopy(byRet, startPos, byTmp, 0, endPos - startPos + 1);
		return byTmp;
	}

	/**
	 * 如果字符串为null或者空格那么返回真
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 是否符合
	 */
	public static boolean isNullOrWhiteSpace(String str) {
		if (isNullOrEmpty(str))
			return true;

		if (str.trim().isEmpty())
			return true;

		return false;
	}

	/**
	 * 如果字符串为null或者空字符串那么返回真
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 是否符合
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null)
			return true;

		if (str.isEmpty())
			return true;

		return false;
	}

	/**
	 * 不区分大小写比较字符串
	 * 
	 */
	public static boolean equalsNotCL(String str1, String str2) {
		if (str1.regionMatches(true, 0, str2, 0, str2.length())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 比较字符串A出现key的index是否在字符串B的index同位置也出现
	 * 如字符串A为"0001001";字符串B为"1101001";key为'1'返回true；key为'0'返回false
	 * 
	 * @param _queryTypeA
	 * @param _querytypeB
	 * @param key
	 * @return
	 */
	public static boolean Check2SKeyAtSameIndex(String _queryTypeA,
			String _querytypeB, char key) {
		boolean flag = false;
		if (_queryTypeA == null) {
			return flag;
		}
		char[] querytypes = _queryTypeA.toCharArray();
		char[] _queryTypes = _querytypeB.toCharArray();
		int count = 0;
		for (int i = 0; i < querytypes.length; i++) {
			if (querytypes[i] == key) {
				flag = true;
				flag = flag && (_queryTypes[i] == key);
				count++;
			}
		}
		if (count == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检查字符串A按照split切分后的字符串是否包含在字符串B被split切分后的数组中
	 * 
	 * @param _queryTypeA
	 * @param _querytypeB
	 * @param split
	 * @return
	 */
	public static boolean CheckBcontentA(String _queryTypeA,
			String _querytypeB, String split) {
		boolean flag = false;

		String[] querytypes = _queryTypeA.split(split);
		String[] _queryTypes = _querytypeB.split(split);
		int count = 0;
		for (int i = 0; i < querytypes.length; i++) {
			for (int j = 0; j < _queryTypes.length; j++) {
				if (querytypes[i].equals(_queryTypes[j])) {
					flag = true;
					flag = flag && (querytypes[i].equals(_queryTypes[j]));
					count++;
				}
			}
		}
		if (count == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 将指定byte数组以16进制的形式打印到控制台
	 * 
	 * @param hint
	 *            String
	 * @param b
	 *            byte[]
	 * @return void
	 */
	public static String printHexString(String hint, byte[] b, int len) {
		StringBuffer ret = new StringBuffer();
		byte[] data = new byte[len];
		ret.append(hint + ":\n");
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase() + " ");
			// 过滤0A 0D
			if (hex.toUpperCase().equals("0A")
					|| hex.toUpperCase().equals("0D")) {
				data[j] = 0x20;
			} else {
				data[j] = b[i];
			}
			if (i > 0 && (i + 1) % len == 0) {
				try {
					ret.append("; " + new String(data, "gbk") + "\n");
				} catch (UnsupportedEncodingException e) {
				}
				j = 0;
				data = new byte[len];
				continue;
			}
			j++;
		}
		if (j > 0) {
			try {
				ret.append("; " + new String(data, "gbk") + "\n");
			} catch (UnsupportedEncodingException e) {
			}
		}
		ret.append("\n");
		return ret.toString();
	}

	/**
	 * str是否存在与String数组中
	 * 
	 * @param array
	 *            String数组
	 * @param str
	 * @return
	 */
	public static boolean isinArrays(String[] array, String str) {
		if (array.length < 1 || StringUtilEx.isNullOrEmpty(str)) {
			return false;
		}

		for (String data : array) {
			if (data.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author cn
	 * @param s
	 *            要截取的字符串
	 * @param length
	 *            要截取字符串的长度->是字节一个汉字2个字节 return 返回length长度的字符串（含汉字）
	 */
	public static String chineseSubstring(String s, int length) {
		try {
			byte[] bytes = s.getBytes("GBK");
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}

			}
			// 如果i为奇数时，处理成偶数
			/*
			 * if (i % 2 == 1){ // 该UCS2字符是汉字时，去掉这个截一半的汉字 if (bytes[i - 1] != 0)
			 * i = i - 1; // 该UCS2字符是字母或数字，则保留该字符 else i = i + 1; }
			 */
			// 将截一半的汉字要保留
			if (i % 2 == 1) {
				i = i + 1;
			}
			return new String(bytes, 0, i, "GBK");
		} catch (Exception e) {
			// TODO: handle exception
			return s;
		}
	}

	/**
	 * @author cn
	 * @param s
	 *            要截取的字符串
	 * @param length
	 *            要截取字符串的长度->是字节一个汉字2个字节 return 返回length长度的字符串（含汉字）
	 */
	public static String[] chineseSubstring2args(String s, int length) {
		try {
			byte[] bytes = s.getBytes("GBK");
			if (bytes.length < length)
				return new String[] { s };
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}

			}
			// 如果i为奇数时，处理成偶数
			/*
			 * if (i % 2 == 1){ // 该UCS2字符是汉字时，去掉这个截一半的汉字 if (bytes[i - 1] != 0)
			 * i = i - 1; // 该UCS2字符是字母或数字，则保留该字符 else i = i + 1; }
			 */
			// 将截一半的汉字要保留
			if (i % 2 == 1) {
				i = i + 1;
			}

			String a1 = new String(bytes, 0, i, "GBK");

			return new String[] { a1,
					new String(bytes, i, bytes.length - i, "GBK") };

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 去零 00011-> 11
	 * 
	 * @param str
	 * @return
	 */
	public String delZreo(String str) {
		if (StringUtilEx.isNullOrEmpty(str)) {
			return "";
		}
		String newstr = str.replaceAll("^(0+)", "");
		return newstr;
	}

	/**
	 * 将object类型转化为字符串，当o为null时，返回空串
	 * 
	 * @param o
	 * @return
	 */
	public static String ObjectToString(Object o) {
		if (o == null) {
			return "";
		} else {
			return String.valueOf(o);
		}
	}

	/**
	 * 将byte数组转化为16进制字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String encode(byte[] b) {
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}

	/**
	 * 将16进制字符串转化为byte数组
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] decode(String s) {
		int size = s.length();
		byte[] res = new byte[size / 2];
		int j = 0;
		for (int i = 0; i < size; i += 2) {
			String term = s.substring(i, i + 2);
			res[j++] = (byte) Integer.parseInt(term, 16);
		}
		return res;
	}

	/**
	 * 去字符串前的0
	 * 
	 * @param mtdt
	 * @return
	 */
	public static String fun_del_zeronew(String mtdt) // 去字符串前的0
	{
		int len, length;
		int i, star;
		len = mtdt.length();
		length = len;
		star = 0;
		for (i = 0; i < len; i++) {
			if (!"0".equals(mtdt.substring(i, i + 1))) {
				star = i;
				i = len + 1;
			}
		}
		if (star == 0 && "0".equals(mtdt.substring(0, 1))) {
			return "";
		} else {
			mtdt = mtdt.substring(star, length);
			return mtdt;
		}
	}
}
