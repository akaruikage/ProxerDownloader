package ma.akaruikage.proxer.downloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextListener implements ActionListener {

	private static int episode;
	private static String url;
	@Override
	public void actionPerformed(ActionEvent e) {
		episode = Frame.episode;
		url = Frame.url;
		int i = url.indexOf("/" + episode + "/");
		System.out.println("NEXTLISTENER: Previous Episode:" + episode);
		String newUrl;
		if (episode < 10) {
			newUrl = url.substring(0, i + 1) + (episode + 1) + url.substring(i + 2, url.length());
		} else {
			newUrl = url.substring(0, i + 1) + (episode + 1) + url.substring(i + 3, url.length());
		}
		Frame.url = newUrl;
		Frame.textField.setText(newUrl);
	}

	public static void incrementEpisode() {
		episode = Frame.episode;
		url = Frame.url;
		System.out.println("NEXTLISTENER: Max EP: " + Frame.epMax);
		if (episode != Frame.epMax) {
			int i = url.indexOf("/" + episode + "/");

			System.out.println("NEXTLISTENER: Episode: " + episode);
			String newUrl;
			if (episode < 10) {
				newUrl = url.substring(0, i + 1) + (episode + 1) + url.substring(i + 2, url.length());
			} else {
				newUrl = url.substring(0, i + 1) + (episode + 1) + url.substring(i + 3, url.length());
			}
			System.out.println("NEXTLISTENER: New URL: " + newUrl);
			Frame.url = newUrl;
			Frame.textField.setText(newUrl);
		} else {

		}
	}

	public static void pauseMultiDownload() {
		Frame.btnDownload.setText("Continue");
		Frame.multDownRunning = false;
	}

}
