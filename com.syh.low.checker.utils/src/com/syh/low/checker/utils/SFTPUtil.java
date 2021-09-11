package com.syh.low.checker.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.swt.widgets.Text;

public class SFTPUtil
{
  public static ChannelSftp connect(String host, String username, String password, Text text)
    throws Exception
  {
    ChannelSftp sftp = null;
    JSch jsch = new JSch();
    Session sshSession = jsch.getSession(username, host);
    sshSession.setPassword(password);
    Properties sshConfig = new Properties();
    sshConfig.put("StrictHostKeyChecking", "no");
    sshSession.setConfig(sshConfig);
    sshSession.connect();
    text.append("Session connected.\n\r");
    Channel channel = sshSession.openChannel("sftp");
    channel.connect();
    sftp = (ChannelSftp)channel;
    sftp.setFilenameEncoding("GBK");
    text.append("成功连接到主机 "+ host + "\n\r");
    return sftp;
  }

  public static void upload(String directory, String FILENAME, String uploadFile, ChannelSftp sftp)
    throws SftpException
  {
    sftp.cd(directory);
    sftp.put(uploadFile, FILENAME);
  }

  public static void download(String downloadFile, String saveFile, ChannelSftp sftp)
    throws SftpException
  {
    sftp.get(downloadFile, saveFile);
  }

  public static void delete(String directory, String deleteFile, ChannelSftp sftp)
    throws SftpException
  {
    sftp.cd(directory);
    sftp.rm(deleteFile);
  }

  public static Vector listFiles(String directory, ChannelSftp sftp)
    throws SftpException
  {
    return sftp.ls(directory);
  }
  
	public static Session creatSession(String host, String username,
			String password,Text t_log) throws Exception {
		// TODO Auto-generated method stub
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, host);
		t_log.append("Session created.\n");
		session.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.connect();
		t_log.append("Session connected.\n");
		return session;
	}
	
	/**
	 * 执行命令
	 * @param sshSession
	 * @param command 命令
	 * @return
	 */
	public static String exec(Session sshSession, String command) {
		String result = "";
		ChannelExec openChannel = null;
		try {
			openChannel = (ChannelExec) sshSession.openChannel("exec");
			openChannel.setCommand(command);
			openChannel.connect();
			InputStream in = openChannel.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = br.readLine()) != null) {
				result += line + "\n";
			}
		} catch (Exception e) {
			result = "-1";
		} finally {
			if (openChannel != null && !openChannel.isClosed()) {
				openChannel.disconnect();
			}
		}
		return result;
	}
	
	public static ChannelSftp connect(Session session,Text t_log) throws Exception {
		ChannelSftp sftp = null;
		Channel channel = session.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		sftp.setFilenameEncoding("GBK");
		t_log.append("Connected success .\n");
		return sftp;
	}
}