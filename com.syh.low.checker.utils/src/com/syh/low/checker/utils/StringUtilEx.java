package com.syh.low.checker.utils;

import java.io.UnsupportedEncodingException;

public class StringUtilEx {
	/**
	 * ���������ϳ��ַ���
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
	 * ���ض��ַ�����ַ���
	 * 
	 * @param sSrc
	 *            Ҫ�����ַ���
	 * @param ch
	 *            ���������ض��ַ�
	 * @param nLen
	 *            Ҫ��䵽�ĳ���
	 * @param bLeft
	 *            Ҫ���ķ���true:��ߣ�false:�ұ�
	 * @return ���õ��ַ���
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
	 * ȥ���ַ�����ͷ�Ŀ�ֵ
	 * 
	 * @param byRet
	 *            Ҫȥ�����ַ���
	 * @return ȥ���õ��ַ���
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
	 * ����ַ���Ϊnull���߿ո���ô������
	 * 
	 * @param str
	 *            Ҫ�жϵ��ַ���
	 * @return �Ƿ����
	 */
	public static boolean isNullOrWhiteSpace(String str) {
		if (isNullOrEmpty(str))
			return true;

		if (str.trim().isEmpty())
			return true;

		return false;
	}

	/**
	 * ����ַ���Ϊnull���߿��ַ�����ô������
	 * 
	 * @param str
	 *            Ҫ�жϵ��ַ���
	 * @return �Ƿ����
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null)
			return true;

		if (str.isEmpty())
			return true;

		return false;
	}

	/**
	 * �����ִ�Сд�Ƚ��ַ���
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
	 * �Ƚ��ַ���A����key��index�Ƿ����ַ���B��indexͬλ��Ҳ����
	 * ���ַ���AΪ"0001001";�ַ���BΪ"1101001";keyΪ'1'����true��keyΪ'0'����false
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
	 * ����ַ���A����split�зֺ���ַ����Ƿ�������ַ���B��split�зֺ��������
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
	 * ��ָ��byte������16���Ƶ���ʽ��ӡ������̨
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
			// ����0A 0D
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
	 * str�Ƿ������String������
	 * 
	 * @param array
	 *            String����
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
	 *            Ҫ��ȡ���ַ���
	 * @param length
	 *            Ҫ��ȡ�ַ����ĳ���->���ֽ�һ������2���ֽ� return ����length���ȵ��ַ����������֣�
	 */
	public static String chineseSubstring(String s, int length) {
		try {
			byte[] bytes = s.getBytes("GBK");
			int n = 0; // ��ʾ��ǰ���ֽ���
			int i = 2; // Ҫ��ȡ���ֽ������ӵ�3���ֽڿ�ʼ
			for (; i < bytes.length && n < length; i++) {
				// ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�
				if (i % 2 == 1) {
					n++; // ��UCS2�ڶ����ֽ�ʱn��1
				} else {
					// ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�
					if (bytes[i] != 0) {
						n++;
					}
				}

			}
			// ���iΪ����ʱ�������ż��
			/*
			 * if (i % 2 == 1){ // ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ��� if (bytes[i - 1] != 0)
			 * i = i - 1; // ��UCS2�ַ�����ĸ�����֣��������ַ� else i = i + 1; }
			 */
			// ����һ��ĺ���Ҫ����
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
	 *            Ҫ��ȡ���ַ���
	 * @param length
	 *            Ҫ��ȡ�ַ����ĳ���->���ֽ�һ������2���ֽ� return ����length���ȵ��ַ����������֣�
	 */
	public static String[] chineseSubstring2args(String s, int length) {
		try {
			byte[] bytes = s.getBytes("GBK");
			if (bytes.length < length)
				return new String[] { s };
			int n = 0; // ��ʾ��ǰ���ֽ���
			int i = 2; // Ҫ��ȡ���ֽ������ӵ�3���ֽڿ�ʼ
			for (; i < bytes.length && n < length; i++) {
				// ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�
				if (i % 2 == 1) {
					n++; // ��UCS2�ڶ����ֽ�ʱn��1
				} else {
					// ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�
					if (bytes[i] != 0) {
						n++;
					}
				}

			}
			// ���iΪ����ʱ�������ż��
			/*
			 * if (i % 2 == 1){ // ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ��� if (bytes[i - 1] != 0)
			 * i = i - 1; // ��UCS2�ַ�����ĸ�����֣��������ַ� else i = i + 1; }
			 */
			// ����һ��ĺ���Ҫ����
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
	 * ȥ�� 00011-> 11
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
	 * ��object����ת��Ϊ�ַ�������oΪnullʱ�����ؿմ�
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
	 * ��byte����ת��Ϊ16�����ַ���
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
	 * ��16�����ַ���ת��Ϊbyte����
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
	 * ȥ�ַ���ǰ��0
	 * 
	 * @param mtdt
	 * @return
	 */
	public static String fun_del_zeronew(String mtdt) // ȥ�ַ���ǰ��0
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
