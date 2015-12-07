package com.bird.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {

	static {
		try {
			// 加载MySql的驱动类
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		// 连接MySql数据库，用户名和密码都是root
		String url = "jdbc:mysql://101.200.236.220:3306/mooc?useUnicode=true&characterEncoding=utf8";
		String username = "root";
		String password = "";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}
		return con;
	}
}
