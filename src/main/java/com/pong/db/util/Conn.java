/**
 * 
 */
package com.pong.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Description static class to get db connection {{@link #getConn()}
 * @author canpong wu
 * @date 2017年5月26日 下午8:30:52 
 */
public class Conn {
	private static String user = "scott";
	private static String password = "123456";
	private static String url = "jdbc:oracle:thin:@192.168.0.106:1521:demo";
	private static Connection conn = null;
	public static Connection getConn(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection( url , user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
