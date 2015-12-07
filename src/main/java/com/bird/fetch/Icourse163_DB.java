package com.bird.fetch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Icourse163_DB {

	static {
		try {
			// 加载MySql的驱动类
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		// 连接MySql数据库，用户名和密码都是root
		String url = "jdbc:mysql://101.200.236.220:3306/mooc?useUnicode=true&characterEncoding=utf8";
		String username = "root";
		String password = "mysql";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}
		return con;
	}

	public void insertRecord(String title, String shortdesc, String desc, String courseTime, String teacher_Info) throws Exception {
		Connection connection = getConnection();
		String sql = "insert into icourse168(title, short_desc, desc_detail, course_time, teacher_info) values(?, ?, ?, ?, ?)";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, title);
		pre.setString(2, shortdesc);
		pre.setString(3, desc);
		pre.setString(4, courseTime);
		pre.setString(5, teacher_Info);
		pre.execute();
		pre.close();
		connection.close();
	}
	
	public static void main(String[] args) throws Exception {
		Icourse163_DB icourse163_DB = new Icourse163_DB();
		icourse163_DB.insertRecord("123", "adw", "dwadawd", "dwad", "dawd");
	}
}
