package ma.akaruikage.proxer.downloader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

// TODO : Penetrate hard in the but.

public class Saver {

	URLConnection conn;
	OutputStream outStream;
	static URL url;
	InputStream is;
	public static String filePath = "";
	public static int episodeVar;

	private static String pattern = "\\?|\\:|<|>|/|\\\\";

	public static FileOutputStream fos;
	public static ReadableByteChannel rbc;

	public static void save(String address, String path, String name, int episode) {
		String folderName = name;
		if (Frame.proxyEnabled) {
			System.getProperties().put("http.proxySet", "true");
			System.getProperties().put("http.proxyHost", Frame.proxyIP);
			System.getProperties().put("http.proxyPort", Frame.proxyPort + "");

		}
		try {
			url = new URL(address);
			try {
				rbc = Channels.newChannel(url.openStream());
			} catch (IOException ioe) {
				Frame.lblStatus.setText("Unable to connect to the video page!");
			}
			System.out.println("SAVER: Video URL: " + url.toString());
			File folder = new java.io.File(path + "/" + folderName);
			System.out.println("SAVER: Folder Name before correcting: " + folderName);
			if (!folder.exists()) {
				if (folder.mkdirs()) {
					System.out.println("SAVER: Folder Created");
				} else if (folderName.contains(":")||folderName.contains("?")||folderName.contains("\\")||folderName.contains("/")||folderName.contains("<")||folderName.contains(">")) {
					folderName = folderName.replaceAll(pattern, "");
					System.out.println("SAVER: Folder Name after correcting: " + folderName);
					folder = new File(path + "/" + folderName);
					if (folder.mkdirs()) {
						System.out.println("SAVER: Folder Created");
					} else {
						Frame.lblStatus.setText("Something went horrobly wrong here!");
					}
				}
			}
			filePath = path + "/" + folderName + "/Episode " + episode + ".mp4";
			try {
				fos = new FileOutputStream(filePath);
			} catch (FileNotFoundException fnfe) {
				Frame.lblStatus.setText("Something went horrobly wrong here!");
				return;
			}
			Frame.lblStatus.setText("Downloading..");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			Frame.btnDownload.setEnabled(true);
			Frame.lblStatus.setText("DONE!");
			Frame.updateProgressBar.interrupt();
			Frame.progressBar.setValue(0);
//			rbc.close();
//			fos.close();
//			fos = null;

			Frame.reEnableNextPrevBNTS();

		} catch (Exception e) {
			Frame.btnDownload.setEnabled(true);
			e.printStackTrace();
		}

		if (Frame.chckbxDownloadAllEpisodes.isSelected() && Frame.multDownRunning) {
			NextListener.incrementEpisode();
			StartDownloadListener.startDownload();
		}

	}

}