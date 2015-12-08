package com.bird.jike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.bird.utils.DBUtils;
import com.bird.utils.FetchUtils;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JikeFetch {
	
	public static void fetchPage(int num) throws Exception {
		String url = "http://www.jikexueyuan.com/course/?pageNum=" + String.valueOf(num);
		HtmlPage page = FetchUtils.fetchPageEnableJS(url);
		System.out.println("爬取目录" + url);
		
		String title = "";
		String href = "";
		String shortDesc = "";
		
		
		List<DomElement> elementsByName = page.getElementsByTagName("h2");
		for (DomElement ele : elementsByName) {
			if (ele.getAttribute("class").equals("lesson-info-h2")) {
				title = ele.getTextContent().trim();
				href = ele.getElementsByTagName("a").get(0).getAttribute("href");
				shortDesc  = ele.getNextElementSibling().getTextContent().trim();
				fetchDetail(href, title, shortDesc);
			}
			
		}
	}
	
	
	public static void fetchDetail(String url, String title, String shortDesc) throws Exception {
		HtmlPage page = FetchUtils.fetchPageEnableJS(url);
		System.out.println("正在爬取" + url);
		
		String detailInfo = "";
		String lessonSection = "";
		
		DomNodeList<DomElement> elementsByTagName = page.getElementsByTagName("div");
		for (DomElement ele : elementsByTagName) {
			if (ele.getAttribute("class").equals("bc-box")) {
				detailInfo = ele.getTextContent().trim();
			}
			
			if (ele.getAttribute("class").equals("lesson-box")) {
				lessonSection = ele.getTextContent().trim();
				String[] args = lessonSection.split("\n");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < args.length - 1; i++) {
					sb.append(args[i]);
				}
				if (!lessonSection.equals("")) {
					break;
				}
				lessonSection = sb.toString().replaceAll(" ", "").replaceAll("只有成为VIP会员才能提问&回复，快登录吧！如果你还没有账号你可以注册一个账号。", "")
						.replaceAll("\n", "").replaceAll("\r", "");
			}
		}
		//System.out.println(lessonSection);
		insertDB(title, shortDesc, detailInfo, lessonSection, url);
	}
	
	
	public static void insertDB(String title, String shortDesc, String detailInfo,
			String lessonSec, String url) throws Exception {
		Connection connection = DBUtils.getConnection();
		String sql = "insert into jikexueyuan(title, short_desc, detail_info, lesson_sec, url) values(?, ?, ?, ?, ?)";
		PreparedStatement pre = connection.prepareStatement(sql);
		pre.setString(1, title);
		pre.setString(2, shortDesc);
		pre.setString(3, detailInfo);
		pre.setString(4, lessonSec);
		pre.setString(5, url);
		pre.execute();
		pre.close();
		connection.close();
	}
	
	public static void main(String[] args) throws Exception {
		for (int i = 80; i < 90; i++) {
			System.out.println("第" + i +"页");
			JikeFetch.fetchPage(i);
		}
		//insertDB("123","45","123","123","435");
	}
}
