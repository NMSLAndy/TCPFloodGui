package xyz.NetMelody.Gui.Button;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Client;
import xyz.NetMelody.GuiRenderer;
import xyz.NetMelody.Gui.StringObject;
import xyz.NetMelody.Gui.TypingGui;
import xyz.NetMelody.Module.http.HttpRunnable;
import xyz.NetMelody.Module.tcp.SynRunnable;
import xyz.NetMelody.Module.udp.UDPRunable;

public class ButtonManager {

	public ArrayList<Button> buttonList = new ArrayList<Button>();

	public void initStartUPButton() {
		this.buttonList.clear();
		this.buttonList.add(new Button(0, "Accept", 240, 300, 120, 40, true, () -> {
			GuiRenderer.accepted = true;
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(1, "Quit", 400, 300, 120, 40, true, () -> {
			Display.destroy();
			System.exit(0);
		}, new SlickColor(112, 163, 180)));
	}

	public void initButtons() {
		this.buttonList.clear();
		this.buttonList.add(new Button(0, "Start", 400, 265, 364, 40, true, () -> {
			this.buttonList.get(0).setEnabled(false);
			this.buttonList.get(1).setEnabled(true);

			Client.logger.info("TargetIP: " + Client.core.ip);
			Client.logger.info("Target Port: " + Client.core.port);
			Client.logger.info("Max Threads: " + Client.core.threads);
			Client.logger.info("Bytes: " + Client.core.bytes);
			Client.logger.warn("Starting...");
			Client.logger.info("Calling From Main Thread...");

			GuiRenderer.setShouldTarget(true);

			if (Client.core.mode == 0) {
				String target = Client.core.ip;
				int port = Client.core.port;
				int threadCount = Client.core.threads;

				Client.core.tcpThreads.clear();

				for (int p = 0; p < threadCount; p++) {
					if (!GuiRenderer.shouldTarget())
						break;
					Client.core.tcpThreads.add(new Thread(new SynRunnable(target, port)));
				}

				new Thread(() -> {
					try {
						for (Thread thread : Client.core.tcpThreads)
							thread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, "TCP Thread Caller").start();

			} else if (Client.core.mode == 1) {
				String target = Client.core.ip;
				int port = Client.core.port;
				int threadCount = 1;

				Client.core.udpThreads.clear();

				for (int p = 0; p < threadCount; p++) {
					if (!GuiRenderer.shouldTarget())
						break;
					Client.core.udpThreads.add(new Thread(new UDPRunable(target, port, new byte[Client.core.bytes])));
				}

				new Thread(() -> {
					try {
						for (Thread thread : Client.core.udpThreads)
							thread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, "UDP Thread Caller").start();

			} else if (Client.core.mode == 2) {
				String target = Client.core.ip;
				int threadCount = Client.core.threads;
				Client.core.httpThreads.clear();

				for (int p = 0; p < threadCount; p++) {
					if (!GuiRenderer.shouldTarget())
						break;
					Client.core.httpThreads.add(new Thread(new HttpRunnable(target, Client.core.bytes)));
				}

				new Thread(() -> {
					try {
						for (Thread thread : Client.core.httpThreads)
							thread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, "HTTP Thread Caller").start();
			}
		}, new SlickColor(114, 177, 115)));

		this.buttonList.add(new Button(1, "Stop", 400, 317, 364, 38, false, () -> {
			Client.logger.info("Stopping...");
			this.buttonList.get(0).setEnabled(true);
			this.buttonList.get(1).setEnabled(false);

			GuiRenderer.setShouldTarget(false);
			System.gc();
			Client.logger.info("Stopped!");
			GuiRenderer.message = "Info: Stopped.";
		}, new SlickColor(177, 112, 115)));

		this.buttonList.add(new Button(2, "TCP", 400, 369, 364, 38, true, () -> {
			if (Client.core.mode < 2)
				Client.core.mode++;
			else if (Client.core.mode >= 2)
				Client.core.mode = 0;
			String md = "ERROR";
			switch (Client.core.mode) {
			case 0: {
				md = "TCP";
				break;
			}
			case 1: {
				md = "UDP";
				break;
			}
			case 2: {
				md = "HTTP";
				break;
			}
			}
			this.buttonList.get(2).setText(md);
			Client.logger.info("Mode Set to " + md);
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(3, "=", 340, 260, 38, 38, true, () -> {
			new Thread(() -> {
				try {
					StringObject obj = new StringObject(Client.core.threads + "");

					GuiRenderer.typingGui = new TypingGui("Threads: ", obj);
					GuiRenderer.typing = true;

					while (GuiRenderer.typing)
						Thread.sleep(100);

					String input = obj.getText();
					if (input == null || input == " " || Integer.parseInt(input) == 0)
						input = "1000";
					Client.core.threads = Integer.parseInt(input);
					Client.logger.info("Max Threads Set to " + Client.core.threads);
				} catch (Exception e) {
					Client.logger.err(e.getMessage());
					Client.core.threads = 1000;
					Client.logger.info("Max Threads Set to " + Client.core.threads);
				}
			}, "Typing").start();
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(4, "=", 340, 300, 38, 38, true, () -> {
			new Thread(() -> {
				StringObject obj = new StringObject(Client.core.ip);
				GuiRenderer.typingGui = new TypingGui("IP: ", obj);
				GuiRenderer.typing = true;
				try {
					while (GuiRenderer.typing)
						Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String input = obj.getText();
				if (input == null || input == " ")
					input = "127.0.0.1";
				Client.core.ip = input;
				Client.logger.info("Target IP Set to " + Client.core.ip);

			}, "Typing").start();
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(5, "=", 340, 340, 38, 38, true, () -> {
			new Thread(() -> {
				try {
					StringObject obj = new StringObject(Client.core.port + "");

					GuiRenderer.typingGui = new TypingGui("Port: ", obj);
					GuiRenderer.typing = true;

					while (GuiRenderer.typing)
						Thread.sleep(100);

					String input = obj.getText();
					if (input == null || input == " " || Integer.parseInt(input) == 0)
						input = "1";
					Client.core.port = Integer.parseInt(input);
					Client.logger.info("Target Port Set to " + Client.core.port);
				} catch (Exception e) {
					Client.logger.err(e.getMessage());
					Client.core.port = 1145;
					Client.logger.info("Target Port Set to " + Client.core.port);
				}
			}, "Typing").start();
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(6, "=", 340, 380, 38, 38, true, () -> {
			new Thread(() -> {
				try {
					StringObject obj = new StringObject(Client.core.bytes + "");

					GuiRenderer.typingGui = new TypingGui("Bytes per Packet: ", obj);
					GuiRenderer.typing = true;

					while (GuiRenderer.typing)
						Thread.sleep(100);

					String input = obj.getText();
					if (input == null || input == " " || Integer.parseInt(input) == 0)
						input = "1";
					Client.core.bytes = Integer.parseInt(input);
					Client.logger.info("Packet Size Set to " + Client.core.bytes);
				} catch (Exception e) {
					Client.logger.err(e.getMessage());
					Client.core.bytes = 1024;
					Client.logger.info("Packet Size Set to " + Client.core.bytes);
				}
			}, "Typing").start();
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(7, "System Proxy", 465, 5, 180, 30, true, () -> {
			Client.core.useSystemProxy = !Client.core.useSystemProxy;
		}, new SlickColor(112, 163, 180)));

		this.buttonList.sort(Comparator.comparingDouble(button -> button.id));
	}

	public void drawButtons(boolean tick) {
		for (Button b : this.buttonList) {
			b.draw(tick);
		}
	}
}
