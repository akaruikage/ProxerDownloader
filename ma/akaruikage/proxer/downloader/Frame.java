package ma.akaruikage.proxer.downloader;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Setup variables
	public static String url;
	public static String path;
	public static String videoUrl;
	public static boolean all;
	public static int episode;
	public static int epMax;
	public static String title = "", prevTitle = "";
	public static boolean multDownRunning = false;
	public static int fileSize = 0;
	public static boolean updateNameFailed = false;
	public static boolean multDown = false, multDownPrev = false;

	public static boolean proxyEnabled = false;
	public static String proxyIP;
	public static int proxyPort;

	Getter get = new Getter();

	public static boolean urlValid = false;

	// JFrame variables
	public JFrame frame;
	public static JTextField textField = new JTextField();
	public JTextField textField_1 = new JTextField();
	public JLabel lblProxermeDownloaderBy = new JLabel("Proxer.me Downloader by AkaruiKage");
	public JLabel lblEnterProxerUrl = new JLabel("Enter Proxer URL here:");
	public static JButton btnDownload = new JButton("Download");
	public JLabel lblSaveTo = new JLabel("Save to:");
	public static JProgressBar progressBar = new JProgressBar();
	public JButton btnApply = new JButton("Apply");
	public static JLabel lblName = new JLabel("Anime name will be displayed here");
	public static JLabel lblStatus = new JLabel("");
	public static JCheckBox chckbxDownloadAllEpisodes = new JCheckBox("Download all Episodes");
	JLabel lblNext = new JLabel("Next");
	JLabel lblPrev = new JLabel("Prev");
	static JButton button = new JButton(">");
	static JButton button_1 = new JButton("<");
	static JLabel proxyInfo = new JLabel("Enter proxy settings (leave empty if no proxy is available)");
	static JTextField proxyipfield = new JTextField();
	static JTextField proxyportfield = new JTextField();

	private Runnable updateR = new Runnable() {

		@Override
		public void run() {
			Getter g = new Getter();
			String lastUrl = "";
			while (true) {
				// Gathering the save path, the url and the all
				path = textField_1.getText();
				url = textField.getText();
				all = chckbxDownloadAllEpisodes.isSelected();

				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// check if the url has changed
				if (url.equals(lastUrl)) {

				} else {
					try {
						// Disable any further clicking of buttons
						button.setEnabled(false);
						button_1.setEnabled(false);
						btnDownload.setEnabled(false);

						// load the anime name into the necessary text
						lblName.setText("Refreshing...");
						if (!Frame.proxyipfield.getText().equals("") || !Frame.proxyportfield.getText().equals("")) {
							Frame.proxyEnabled = true;
							Frame.proxyIP = Frame.proxyipfield.getText();
							Frame.proxyPort = Integer.parseInt(Frame.proxyportfield.getText());
						} else {
							Frame.proxyEnabled = false;
						}

						episode = g.getEpisode(url);
						System.out.println("FRAME: Current Ep: " + episode);
						epMax = g.getEpisodeAmmount(url);
						if (!chckbxDownloadAllEpisodes.isSelected() || episode != epMax) {
							lblName.setText(g.getFullName(url));
							// Reenable next, prev and download buttons
							reEnableNextPrevBNTS();
							btnDownload.setEnabled(true);
							urlValid = true;
						} else {
							lblName.setText(g.getAnimeName(url));
							reEnableNextPrevBNTS();
							btnDownload.setEnabled(true);
							btnDownload.setText("Download");
							urlValid = true;
						}
					} catch (MalformedURLException mue) {
						lblName.setText("The entered URL is not valid!");
						urlValid = false;
					} catch (IOException ioe) {
						lblStatus.setBounds(43, 215, 350, 19);
						lblStatus.setText("I can't connect to the Proxer Server :/ Check proxy settings.");
						btnDownload.setText("Retry");
						btnDownload.setEnabled(true);
						updateNameFailed = true;
						ioe.printStackTrace();
					} catch (NumberFormatException nfe) {
						lblStatus.setBounds(35, 215, 380, 19);
						lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
						lblStatus.setText("Either you need to solve a Captcha or the entered URL is no proxer link.");
						btnDownload.setText("Retry");
						btnDownload.setEnabled(true);
						updateNameFailed = true;
						nfe.printStackTrace();
					}

				}
				lastUrl = url;
			}

		}
	};
	public Thread update = new Thread(updateR);

	public static Runnable updateProgressBarR = new Runnable() {

		@Override
		public void run() {
			Getter g = new Getter();

			int currentSize = 0;

			try {
				fileSize = g.getFileSize(videoUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (true) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("FRAME: Main download thread stopped.");
				}
				File f = new File(Saver.filePath);
				currentSize = (int) f.length();
				Frame.progressBar.setMaximum(fileSize);
				Frame.progressBar.setValue(currentSize);
			}
		}
	};
	public static Thread updateProgressBar = new Thread(updateProgressBarR);

	static Getter g = new Getter();

	public static void retryUpdateName() {
		try {

			if (!chckbxDownloadAllEpisodes.isSelected()) {
				button.setEnabled(false);
				button_1.setEnabled(false);
				btnDownload.setEnabled(false);
				lblName.setText("Refreshing...");
				Frame.proxyIP = Frame.proxyipfield.getText();
				Frame.proxyPort = Integer.parseInt(Frame.proxyportfield.getText());

				if (Frame.proxyIP.isEmpty() || Frame.proxyPort != 0) {
					Frame.proxyEnabled = true;
				} else {
					Frame.proxyEnabled = false;
				}
				lblName.setText(g.getFullName(url));
				episode = g.getEpisode(url);
				System.out.println("FRAME: Episode:" + episode);
				epMax = g.getEpisodeAmmount(url);
				title = "3";
				if (episode != epMax) {
					button.setEnabled(true);
				}
				if (episode != 1) {
					button_1.setEnabled(true);
				}
				updateNameFailed = false;
				btnDownload.setText("Download");
				btnDownload.setEnabled(true);
				lblStatus.setText("");
				urlValid = true;
			} else {
				button.setEnabled(false);
				button_1.setEnabled(false);
				if (title.equals(prevTitle)) {
					lblName.setText("Refreshing...");
					title = lblName.getText();
					lblName.setText(g.getAnimeName(url));
					prevTitle = title;
				}
				episode = g.getEpisode(url);
				System.out.println("FRAME: Episode" + episode);
				updateNameFailed = false;
				btnDownload.setText("Download");
				btnDownload.setEnabled(true);
				urlValid = true;
			}
		} catch (MalformedURLException mue) {
			lblName.setText("The entered URL is not valid!");
			urlValid = false;
		} catch (IOException ioe) {
			lblStatus.setText("The servers are tryling to defete us! Try again in 10 min.");
			ioe.printStackTrace();
		} catch (NumberFormatException nfe) {
			lblStatus.setBounds(25, 215, 390, 19);
			lblStatus.setText("Please solve the Captcha at " + url);
			btnDownload.setText("Retry");
			btnDownload.setEnabled(true);
			updateNameFailed = true;
		}
	}

	public Frame() {
		setTitle("Proxer.me Downloader");
		update.start();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setEnabled(true);
		setResizable(false);
		setBounds(100, 100, 450, 420);

		String appdata = System.getenv("APPDATA");
		String iconPath = appdata + "\\ProxerDownloader\\index.png";
		File indexPath = new File(appdata + "\\ProxerDownloader");
		System.out.println(iconPath);
		File icon = new File(iconPath);
		if (!icon.exists()) {
			FileDownloaderNEW fd = new FileDownloaderNEW();
			indexPath.mkdirs();
			try {
				fd.download("http://brecher.be/downloads/files/ProxerDownloader/res/index.png", iconPath, true, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageIcon imgicon = new ImageIcon(iconPath);
		setIconImage(imgicon.getImage());
		getContentPane().setLayout(null);
		lblProxermeDownloaderBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProxermeDownloaderBy.setBounds(10, 11, 303, 19);
		getContentPane().add(lblProxermeDownloaderBy);
		textField = new JTextField();
		textField.setBounds(10, 63, 414, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		lblEnterProxerUrl.setBounds(10, 49, 180, 14);
		getContentPane().add(lblEnterProxerUrl);
		btnDownload.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDownload.setBounds(123, 148, 190, 35);
		btnDownload.addActionListener(new StartDownloadListener());
		getContentPane().add(btnDownload);
		textField_1 = new JTextField();
		textField_1.setBounds(10, 350, 414, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		lblSaveTo.setBounds(10, 335, 65, 14);
		getContentPane().add(lblSaveTo);
		progressBar.setMaximum(1024);
		progressBar.setValue(0);
		progressBar.setBounds(123, 194, 190, 14);
		getContentPane().add(progressBar);
		proxyInfo.setBounds(60, 280, 350, 20);
		getContentPane().add(proxyInfo);
		proxyipfield.setBounds(100, 300, 170, 20);
		proxyipfield.setText("");
		getContentPane().add(proxyipfield);
		proxyportfield.setBounds(290, 300, 50, 20);
		proxyportfield.setText("");
		getContentPane().add(proxyportfield);
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Arial", Font.PLAIN, 16));
		lblName.setBounds(10, 94, 414, 43);
		getContentPane().add(lblName);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(123, 215, 190, 19);
		getContentPane().add(lblStatus);
		chckbxDownloadAllEpisodes.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		chckbxDownloadAllEpisodes.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxDownloadAllEpisodes.setBounds(123, 250, 190, 23);
		getContentPane().add(chckbxDownloadAllEpisodes);
		lblNext.setBounds(323, 160, 27, 14);
		getContentPane().add(lblNext);
		lblPrev.setBounds(85, 160, 27, 14);
		getContentPane().add(lblPrev);
		button.setBounds(352, 156, 41, 23);
		button.addActionListener(new NextListener());
		getContentPane().add(button);
		button_1.setBounds(34, 156, 41, 23);
		button_1.addActionListener(new PrevListener());
		getContentPane().add(button_1);
		String home = System.getProperty("user.home");
		String[] s;
		StringBuilder sb = new StringBuilder();
		s = (home.split("\\\\"));
		for (int i = 0; i < s.length; i++) {
			sb.append(s[i] + "/");
		}
		textField_1.setText(sb.toString() + "Downloads/Proxer");
		setVisible(true);
	}

	public static void reEnableNextPrevBNTS() {
		System.out.println(episode + "/" + epMax);
		if (episode > 1) {
			button_1.setEnabled(true);
		}
		if (episode < epMax) {
			button.setEnabled(true);
		}
		if (episode == epMax) {
			chckbxDownloadAllEpisodes.setSelected(false);
			chckbxDownloadAllEpisodes.setEnabled(false);
		} else {
			chckbxDownloadAllEpisodes.setEnabled(true);
		}
	}

	public void getRiddOfWarningXD() {
	}

}
