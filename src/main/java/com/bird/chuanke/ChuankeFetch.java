package com.bird.chuanke;

import com.bird.utils.FetchUtils;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

public class ChuankeFetch {
	
	public static void fetchPage(int num) throws Exception {
		String url = "http://www.chuanke.com/course/72351163642544128______2.html?page=" + String.valueOf(num);
		HtmlPage page = FetchUtils.fetchPageEnableJS(url);
		
		String title = "";
		String href = "";
		
		DomNodeList<DomElement> elementsByTagName = page.getElementsByTagName("div");
		for (DomElement ele : elementsByTagName) {
			if (ele.getAttribute("class").equals("item-title")) {
				title = ele.getTextContent();
				href = ele.getElementsByTagName("a").get(0).getAttribute("href");
				System.out.println(title);
				fetchDetail(title, href);
			}
		}
	}
	
	
	public static void fetchDetail(String title, String href) throws Exception {
		String back_href = href;
		HtmlPage page = FetchUtils.fetchPageEnableJS(href);
		href = href.replaceAll("http://www.chuanke.com/", "");
		href = href.replaceAll(".html", "");
		String sid = href.split("-")[0];
		String courseID = href.split("-")[1];
		
		String short_desc = "";
		String detail_desc = "";
		StringBuffer sb = new StringBuffer();
		
		
		String url = "http://www.chuanke.com/?mod=course&act=show&do=brief&sid=" + sid + "&courseid=" +courseID;
		System.out.println(url);
		HtmlPage page_detail = FetchUtils.getContent(url);
		DomNodeList<DomElement> elements_detail = page_detail.getElementsByTagName("div");
		for (DomElement ele : elements_detail) {
			if (ele.getAttribute("class").equals("con-intro")) {
				//System.out.println(ele.getTextContent());
				sb.append(ele.getTextContent());
			}
			
			if (ele.getAttribute("class").equals("m-catalog")) {
				//System.out.println(ele.getTextContent());
				sb.append(ele.getTextContent());
			}
		}
		
		
		
		DomNodeList<DomElement> elementsByTagName = page.getElementsByTagName("div");
		for (DomElement ele : elementsByTagName) {
			if (ele.getAttribute("class").equals("fr details-topcon-info")) {
				short_desc = ele.getTextContent();
			}
			
			/*
			if (ele.getAttribute("class").equals("m-content")) {
				detail_desc = ele.getTextContent();
				System.out.println(ele.asXml());
			}
			*/
		}
		
		detail_desc = sb.toString();
		
		ChuankeDB.insert(title, back_href, short_desc, detail_desc);
	}
	
	
	public static void main(String[] args) throws Exception {
		for (int i = 41; i < 50; i++) {
			System.out.println("第" + i + "页");
			fetchPage(i);
		}
	}
}
