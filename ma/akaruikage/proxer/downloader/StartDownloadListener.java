package ma.akaruikage.proxer.downloader;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NoSuchObjectException;

public class StartDownloadListener implements ActionListener {

	static String vidUrl = "";
	static String url = "";
	static String path = "";
	static String name = "";
	static int episode = 0;

	static Runnable main = new Runnable() {

		@Override
		public void run() {
			if (Frame.urlValid) {
				// if(!Frame.multDownRunning) {
				// Frame.multDownRunning = true;
				// }
				if (Frame.chckbxDownloadAllEpisodes.isSelected()) {
					Frame.multDownRunning = true;
				}
				Frame.btnDownload.setEnabled(false);
				Getter g = new Getter();
				Frame.lblStatus.setFont(new Font("Ubuntu", Font.BOLD, 12));
				Frame.lblStatus.setText("Generating Downloadlink..");

				String s = "";
				url = Frame.url;
				System.out.println("DOWNLOADLISTENER: Proxer URL: " + url);
				path = Frame.path;
				try {
					name = g.getAnimeName(url);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				if (!Frame.proxyipfield.getText().equals(" ") || !Frame.proxyportfield.getText().equals(" ")) {
					Frame.proxyIP = Frame.proxyipfield.getText();
					Frame.proxyPort = Integer.parseInt(Frame.proxyportfield.getText());
					Frame.proxyEnabled = true;
				} else {
					Frame.proxyEnabled = false;
				}

				try {
					s = g.getIframeURL(url, 1000);
					System.out.println("DOWNLOADLISTENER: Iframe URL: " + s);
				} catch (NoSuchObjectException nsoe) {
					Frame.multDownRunning = false;
					NextListener.pauseMultiDownload();
					Thread.currentThread().interrupt();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (NoProxerStreamFoundException npsfe) {
					npsfe.printStackTrace();
					Frame.lblStatus.setBounds(90, 215, 250, 19);
					Frame.lblStatus.setText("This anime/episode doesn't contain a proxer stream.");
					return;
				}
				try {
					vidUrl = g.getVideoURL(s, 1000);
					System.out.println("DOWNLOADLISTENER: Video URL: " + vidUrl);
					Frame.videoUrl = vidUrl;
					Frame.updateProgressBar = new Thread(Frame.updateProgressBarR);
					Frame.updateProgressBar.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					episode = g.getEpisode(url);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Saver.save(vidUrl, path, name, episode);

			} else if (Frame.updateNameFailed) {
				Frame.retryUpdateName();
			}
		}
	};
	public static Thread t = new Thread(main);

	@Override
	public void actionPerformed(ActionEvent e) {
		if (t.isAlive()) {

		} else {
			t = new Thread(main);
			t.start();
		}
	}

	public static void startDownload() {
		t = new Thread(main);
		t.start();

	}
}
