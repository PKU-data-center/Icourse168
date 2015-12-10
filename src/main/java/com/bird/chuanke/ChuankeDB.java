package com.bird.chuanke;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bird.utils.DBUtils;

public class ChuankeDB {
	
	private static Connection con = null;
	
	public static void insert(String title, String href, String short_desc, String detail_desc) throws Exception {
		if (con == null) {
			con = DBUtils.getConnection();
		}
		String sql = "insert into chuanke(title, href, short_desc, detail_desc) values(?, ?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, title);
		ps.setString(2, href);
		ps.setString(3, short_desc);
		ps.setString(4, detail_desc);
		ps.execute();
		ps.close();
	//	con.close();
	}
}
