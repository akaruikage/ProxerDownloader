package ma.akaruikage.proxer.downloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Getter {

	ArrayList<String> ifr = new ArrayList<String>();
	ArrayList<String> vid = new ArrayList<String>();
	ArrayList<String> ep = new ArrayList<String>();
	String[] epAm;

	String preIframeURL;
	String iframeURL;
	String preVidURL;
	String vidURL;
	String preEP;
	String EP;
	int pos1;
	int pos2;
	ArrayList<String> nmn = new ArrayList<String>();

	@SuppressWarnings("resource")
	public String getIframeURL(String url, int waittime)
			throws MalformedURLException, IOException, NoSuchObjectException, NoProxerStreamFoundException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(waittime);
		String p = page.asXml();
		if (!p.contains("Proxer-Stream")) {
			throw new NoProxerStreamFoundException("This anime doesnt contain a proxer stream");
		}
		ifr.add(page.getByXPath("//iframe[@scrolling='no']").toString());
		if (ifr.get(0).indexOf("//stream") == -1) {
			throw new NoSuchObjectException("capthca solving is needed");
		} else {
			System.out.println("GETTER: " + ifr.get(0).indexOf("//stream"));
		}
		try {
			if (ifr.get(0).contains("cpmstar")) {
				iframeURL = "http:" + ifr.get(0).substring(ifr.get(0).indexOf("//stream"));
			} else {
				iframeURL = "http:" + ifr.get(0).substring(ifr.get(0).indexOf("//stream"));
			}
		} catch (StringIndexOutOfBoundsException sioobe) {

		}
		return iframeURL;
	}

	@SuppressWarnings("resource")
	public String getVideoURL(String url, int waittime) throws MalformedURLException, IOException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		TextPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(waittime);
		String[] content;
		content = page.getContent().split(",");
		String out = content[0].substring(content[0].indexOf("http:"), content[0].length() - 1);
		return out;
	}

	@SuppressWarnings("resource")
	public String getAnimeName(String url) throws MalformedURLException, IOException {
		StringBuilder sb = new StringBuilder();
		String temp;
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10);
		temp = (page.getTitleText());
		String[] s;
		s = temp.split("\\s");
		boolean endOfTitle = false;
		for (int i = 0; i < s.length; i++) {
			if (!endOfTitle) {
				if (s[i].contains("Episode")) {
					endOfTitle = true;
				} else {
					sb.append(s[i] + " ");
				}
			}
		}
		return sb.toString().replaceAll("\\s$", "");
	}

	@SuppressWarnings("resource")
	public String getFullName(String url) throws MalformedURLException, IOException {
		StringBuilder sb = new StringBuilder();
		String temp;
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10);
		temp = (page.getTitleText());
		String[] s;
		s = temp.split("\\s");
		boolean endOfTitle = false;
		for (int i = 0; i < s.length; i++) {
			if (!endOfTitle) {
				if (s[i].contains("-")) {
					if (s[i + 1].contains("Proxer")) {
						endOfTitle = true;
					}
				} else {
					sb.append(s[i] + " ");
				}
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("resource")
	public int getEpisodeAmmount(String url) throws MalformedURLException, IOException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(1000);
		epAm = page.asText().split("-");

		for (int i = 0; i < epAm.length; i++) {
			try {
				System.out.println("GETTER: Amount text:" + epAm[3]);
			} catch(ArrayIndexOutOfBoundsException aioobe) {
				throw new NumberFormatException();
			}
		}

		if (epAm[3].length() == 6) {
			System.out.println("GETTER: Calc Am: " + Integer.parseInt(epAm[3].substring(3, 5)));
			return Integer.parseInt(epAm[3].substring(3, 5));
		} else if (epAm[3].length() == 8) {
			System.out.println("GETTER: Calc Am: " + (epAm[3].substring(4, 6)));
			return Integer.parseInt(epAm[3].substring(4, 6));
		} else if (epAm[3].length() == 5) {
			System.out.println("GETTER: Calc Am: " + (epAm[3].substring(3, 4)));
			return Integer.parseInt(epAm[3].substring(3, 4));
		} else {
			return 0;
		}
	}

	@SuppressWarnings("resource")
	public int getEpisode(String url) throws MalformedURLException, IOException, NumberFormatException {
		StringBuilder sb = new StringBuilder();
		String temp;
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
		if (Frame.proxyEnabled) {
			webClient.getOptions().setProxyConfig(new ProxyConfig(Frame.proxyIP, Frame.proxyPort));
		}
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10);
		temp = (page.getTitleText());
		String[] s;
		s = temp.split("\\s");
		boolean endOfTitle = false;
		for (int i = 0; i < s.length; i++) {
			if (!endOfTitle) {
				if (s[i].contains("GerSub") || s[i].contains("EngSub")) {
					endOfTitle = true;
				} else {
					sb.append(s[i]);
				}
			}
		}
		return Integer.parseInt(sb.toString().replaceAll("\\D", ""));
	}

	public int getFileSize(String videoUrl) throws MalformedURLException, IOException {
		URL url = new URL(videoUrl);
		if (Frame.proxyEnabled) {
			System.getProperties().put("http.proxySet", "true");
			System.getProperties().put("http.proxyHost", Frame.proxyIP);
			System.getProperties().put("http.proxyPort", Frame.proxyPort + "");

		}
		URLConnection urlConnection = url.openConnection();
		urlConnection.connect();
		int file_size = urlConnection.getContentLength();
		return file_size;
	}

	public static String getStringFromBoolean(boolean in) {
		if (in) {
			return "true";
		} else {
			return "false";
		}
	}

}
