package com.syh.low.checker.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOUtil {
	
	/**
	 * 将字符串以指定编码写入文件
	 * @param filecontent 字符串
	 * @param path 文件路径
	 * @return
	 */
	public static int writeFile(String filecontent,String path,String charsetName) {
		BufferedWriter brw = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStreamWriter osw = null;
		File f = new File(path);
		try {
			if (f.exists()) {
				f.delete();
			}
			isr = new InputStreamReader(getStringStream(filecontent,charsetName), charsetName);
			br = new BufferedReader(isr);
			osw = new OutputStreamWriter(new FileOutputStream(path), charsetName);
			brw = new BufferedWriter(osw);
			String str = "";
			while ((str = br.readLine()) != null) {
				brw.write(str);
				brw.newLine();
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
			if (brw != null) {
				try {
					brw.close();
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
		}
	}
	
	/**
	 * 将字符串转换为输入流
	 * 
	 * @param inputString
	 * @return
	 * @throws Exception
	 */
	private static InputStream getStringStream(String inputString,String charsetName)
			throws Exception {
		if (!StringUtilEx.isNullOrEmpty(inputString)) {
			ByteArrayInputStream inputStringStream = new ByteArrayInputStream(
					inputString.getBytes(charsetName));
			return inputStringStream;
		}
		return null;
	}
	
	/**
	 * 将输入流变成字符串
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String InputStream2String(InputStream is,String charset) throws Exception {
		try {
			if (is == null)
				return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			is.skip(0);
			byte[] buff = new byte[20*1024*1024];
			int bufLength = -1;
			while ((bufLength = is.read(buff)) >= 0) {
				baos.write(buff, 0, bufLength);
			}
			return baos.toString(charset);
		} catch (Exception e) {
			return null;
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	/**
	 * 将文件以字符串
	 * 
	 * @param datafile
	 * @return
	 * @throws Exception
	 */
	public static String getFileContent(String datafile) {
		// 获取文件内容
		InputStream is;
		String content = "";
		try {
			is = new FileInputStream(datafile);
			content = InputStream2String(is, "GBK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
