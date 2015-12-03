package com.bird.fetch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.xalan.xsltc.compiler.sym;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * fetch 中国大学MOOC
 * 
 * @author xianlu
 *
 */
public class Fetch_icourse163 {

	private static final String url = "http://www.icourse163.org/course/index.htm";
	private static Logger logger = LoggerFactory.getLogger(Fetch_icourse163.class);

	/**
	 * 根据提供的URL返回抓取的Page<br/>
	 * 提供解析JS的功能
	 * 
	 * @param url
	 * @return
	 */
	private static HtmlPage fetchPageEnableJS(String url) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(true);
		try {
			return webClient.getPage(url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// e.printStackTrace();
		} finally {
			//webClient.closeAllWindows();
		}
		return null;
	}
	
	private static List<String> getAllCourseList() throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		String path = "file:///Users/xianlu/Documents/crawl/gongchengjishu.html";
		HtmlPage page = webClient.getPage(path);
		List<String> list = new ArrayList<String>();
		DomNodeList<DomElement> elementsByTagName = page.getElementsByTagName("div");
		for (DomElement element : elementsByTagName) {
			String classAttrName = "u-clist f-bg f-cb f-pr j-href";
			if (element.getAttribute("class").equals(classAttrName)) {
				list.add(element.getAttribute("data-href"));
			}
		}
		return list;
	}
	
	private static void getDetailInfo() throws Exception {
		List<String> list = getAllCourseList();
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		String prex_url = "http://www.icourse163.org";
		
		String title = "";
		String shortdesc = "";
		String desc = "";
		//开课时间
		String courseTime = "";
		//课程信息类型
		String teacher_Info = "";
		Icourse163_DB db = new Icourse163_DB();
		
		
		for (String url : list) {
			url = prex_url + url;
			HtmlPage page = webClient.getPage(url);
			//获取title
			DomNodeList<DomElement> elementsByTagName = page.getElementsByTagName("h2");
			for (DomElement element : elementsByTagName) {
				System.out.println("开始爬取" + url);
				if (element.getAttribute("class").equals("f-fl")) {
					title = element.getTextContent();
					//System.out.println(title);
					break;
				}
			}
			
			//获取短描述
			elementsByTagName = page.getElementsByTagName("p");
			for (DomElement element : elementsByTagName) {
				if (element.getAttribute("class").equals("f-fc6")) {
					shortdesc = element.getTextContent();
					shortdesc = shortdesc.replaceAll("spContent=", "");
					//System.out.println(shortdesc);
					break;
				}
			}
			
			//获取课程详细信息描述
			elementsByTagName = page.getElementsByTagName("div");
			for (DomElement element : elementsByTagName) {
				if (element.getAttribute("class").equals("g-mn2c f-bg m-infomation")) {
					desc = element.getTextContent();
					//System.out.println(desc);
					break;
				}
			}
			
			//获取课程开课时间
			elementsByTagName = page.getElementsByTagName("div");
			for (DomElement element : elementsByTagName) {
				if (element.getAttribute("class").equals("m-termInfo f-cb")) {
					courseTime = element.getTextContent();
					courseTime = new String(courseTime.getBytes("ISO-8859-1"), "UTF-8");
					//System.out.println(courseTime);
					break;
				}
			}
			
			//获取课程开课时间
			elementsByTagName = page.getElementsByTagName("div");
			for (DomElement element : elementsByTagName) {
				if (element.getAttribute("class").equals("m-teachers")) {
					teacher_Info = element.getTextContent();
					//System.out.println(teacher_Info);
					break;
				}
			}
			
			db.insertRecord(title, shortdesc, desc, courseTime, teacher_Info);
		}
		
	}

	public static void main(String[] args) throws Exception {
		getDetailInfo();
	}
}
