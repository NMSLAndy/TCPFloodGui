package xyz.NetMelody.Utils;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.imageio.ImageIO;

import xyz.NetMelody.Client;
import xyz.NetMelody.GuiRenderer;

public class WindowsNotification {

	private static Image image;
	private static TrayIcon trayIcon;
	private static SystemTray tray = SystemTray.getSystemTray();
	private static int num = 0;

	public static void init() {
		new Thread(() -> {
			try {
				image = ImageIO.read(GuiRenderer.class.getResource("/assets/wi/ic (0).png"));
				trayIcon = new TrayIcon(image);
				trayIcon.setImageAutoSize(true);
				trayIcon.setToolTip("MelodySky");
				tray.add(trayIcon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (true) {
				try {
					animate();
				} catch (Exception e) {
					break;
				}
			}
		}, "Icon Controller").start();

	}

	private static void animate() throws Exception {

		Thread.sleep(100);

		if (num == 12)
			num = 0;

		if (num == 1)
			Thread.sleep(2000);

		image = ImageIO.read(GuiRenderer.class.getResource("/assets/wi/ic (" + num + ").png"));
		tray.getTrayIcons()[0].setImage(image);
		num++;
	}

	public static void show(String title, String message) {
		if (SystemTray.isSupported()) {
			displayTray(title, message);
		} else {
			Client.logger.err("System tray not supported!");
		}
	}

	private static void displayTray(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
	}

	public static void stop() {
		if (trayIcon != null && tray != null)
			tray.remove(trayIcon);
	}
}