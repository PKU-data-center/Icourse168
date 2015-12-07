package com.bird.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class FetchUtils {
	
	/**
	 * 根据提供的URL返回抓取的Page<br/>
	 * 提供解析JS的功能
	 * 
	 * @param url
	 * @return
	 */
	public static HtmlPage fetchPageEnableJS(String url) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(true);
		try {
			return webClient.getPage(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//webClient.closeAllWindows();
		}
		return null;
	}
}
