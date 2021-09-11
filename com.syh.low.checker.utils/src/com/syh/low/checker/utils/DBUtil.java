package com.syh.low.checker.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws Exception{
		Connection con = DriverManager.getConnection("jdbc:mysql://158.222.67.70:3309/office", "root", "root");
		return con;
	}
	public static void closeConnection(Connection conn) throws Exception{
		if(null != conn){
			conn.close();
		}
	}
}
