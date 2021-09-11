package com.syh.low.checker.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(readZipFile("D:\\BIPC-UAT_A\\files\\1.zip"));
//		unZipAllFiles("D:\\BIPC-UAT_A\\files\\1.zip", "D:" + File.separator
//				+ "BIPC-UAT_A" + File.separator + "files" + File.separator);
	}

	/**
	 * ��ѹ���ͻ���zip
	 * 
	 * @param zipPath
	 *            ѹ���ļ�·���������ļ�����
	 * @param outfilepath
	 *            ��ѹ·���������ļ�����
	 * @param filename
	 *            (��ѹ�ļ���)
	 */
	public static boolean unClientZip(String zipPath, String outfilepath,
			String filename) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File file = new File(zipPath);// ѹ���ļ�·�����ļ���
			File outFile = new File(outfilepath);// ��ѹ��·�����ļ���
			if (outFile.exists()) {
				outFile.delete();
			}
			ZipFile zipFile = new ZipFile(file);
			ZipEntry entry = zipFile.getEntry(filename);// ����ѹ���ļ���
			in = zipFile.getInputStream(entry);
			out = new FileOutputStream(outFile);
			byte[] bufs = new byte[1024];
			int length = 0;
			while ((length = in.read(bufs)) != -1) {
				out.write(bufs, 0, length);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in)
					in.close();
				if (null != out)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static List<String> readZipFile(String zipPath) {
		InputStream in = null;
		ZipInputStream zis = null;
		List<String> list = new ArrayList<String>();
		try {
			in = new BufferedInputStream(new FileInputStream(zipPath));
			zis = new ZipInputStream(in);
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				list.add(ze.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (zis != null) {
					zis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}

	public static void unZipAllFiles(String zipPath, String savepath) {
		List<String> list = readZipFile(zipPath);
		for (String name : list) {
			unClientZip(zipPath, savepath + name, name);
		}
	}
}
