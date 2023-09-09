package xyz.NetMelody;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import xyz.NetMelody.Config.Config;
import xyz.NetMelody.Gui.Lisence;
import xyz.NetMelody.Gui.ShutDown;
import xyz.NetMelody.Gui.Button.ButtonManager;
import xyz.NetMelody.Utils.Logger;
import xyz.NetMelody.Utils.OSUtils;
import xyz.NetMelody.Utils.WindowsNotification;
import xyz.NetMelody.Utils.GLSL.BackgroundShader;
import xyz.NetMelody.Utils.System.SysGraph;
import xyz.NetMelody.Utils.network.NetWorkUtil;

public class Client {

	public static Logger logger = new Logger();
	public static GuiRenderer core;
	public static ButtonManager buttonManager;

	public static boolean shutdown = false;
	public static boolean stopping = false;

	public static int mouseX = 0;
	public static int mouseY = 0;

	private static int translateY = 0;

	public static Config cfg;

	private static Thread sysListener;
	public static double cpuPercent = 0;
	public static double ramPercent = 0;

	public static void init() {
		createDisplay(800, 450);
		
		sysListener = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(200);
					cpuPercent = OSUtils.cpuUsage();
					ramPercent = OSUtils.ramUsage();
					SysGraph.onCpu((int) cpuPercent);
					SysGraph.onRam((int) ramPercent);
					if (cpuPercent > 95 && GuiRenderer.shouldTarget()) {
						WindowsNotification.show("Net Melody" + NM.version,
								"Center Processor Overloaded, Auto Stopped.");
						buttonManager.buttonList.get(0).setEnabled(true);
						buttonManager.buttonList.get(1).setEnabled(false);
						GuiRenderer.setShouldTarget(false);
						logger.err("System Overloaded, Stopping.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "System Listener");
		sysListener.start();

		GuiRenderer.width = 800;
		GuiRenderer.height = 450;

		core = new GuiRenderer();
		cfg = new Config();
		cfg.load();
		core.font.initFonts();
		buttonManager = new ButtonManager();
		buttonManager.initStartUPButton();
		NetWorkUtil.initListener();
		
		GL11.glTranslatef(0, 5, 0);
		while (true) {

			System.setProperty("java.net.useSystemProxies", String.valueOf(core.useSystemProxy));
			
			if (GuiRenderer.width != Display.getWidth() || GuiRenderer.height != Display.getHeight()) {
				GL11.glTranslatef(0, 5, 0);
				GuiRenderer.width = Display.getWidth();
				GuiRenderer.height = Display.getHeight();
			}
			mouseX = Mouse.getX();
			mouseY = (Mouse.getY() > 225 ? 225 + (225 - Mouse.getY()) : Math.abs(Mouse.getY() - 225) + 225);
			GL11.glPushMatrix();
			GL11.glClear(16640);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			BackgroundShader.BACKGROUND_SHADER.draw();
			if (!stopping) {
				if (GuiRenderer.accepted) {
					if (translateY < 501)
						translateY += 30;
					else if (translateY > 500 && translateY != 114514) {
						buttonManager.initButtons();
						translateY = 114514;
					}
					core.draw();
				}
				if (translateY < 500) {
					GL11.glPushMatrix();
					GL11.glTranslated(0, translateY, 0);
					Lisence.drawLisenceGui();
					buttonManager.drawButtons(true);
					GL11.glPopMatrix();
				}

				if (translateY > 500)
					buttonManager.drawButtons(!GuiRenderer.typing);

				if (GuiRenderer.typing && GuiRenderer.typingGui != null) {
					GuiRenderer.typingGui.processGui();
				}
			}

			if (stopping && GuiRenderer.accepted)
				ShutDown.draw();
			else if (stopping)
				System.exit(-1);

			GL11.glPopMatrix();
			Display.update();
			Display.sync(60);
			
			if (Display.isCloseRequested()) {
				stopping = true;
				logger.info("Shutting Down...");
			}

			if (shutdown)
				break;

		}
		WindowsNotification.stop();
		Display.destroy();
		System.exit(0);
	}

	public static void createDisplay(int w, int h) {
		try {
			WindowsNotification.init();
			Display.setDisplayMode(new DisplayMode(w, h));
			logger.info("Set Display Name to: " + NM.name + " - " + NM.version);
			Display.setTitle(NM.name + " - " + NM.version);
			logger.info("Setting Window Icon...");
			setWindowIcon();
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, w, h);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, w, h, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private static void setWindowIcon() {
		OSUtils.EnumOS util$enumos = OSUtils.getOSType();

		if (util$enumos != OSUtils.EnumOS.OSX) {
			InputStream inputstream = null;
			InputStream inputstream1 = null;

			try {
				inputstream = GuiRenderer.getAssetStream("icon_16x16.png");
				inputstream1 = GuiRenderer.getAssetStream("icon_32x32.png");

				if (inputstream != null && inputstream1 != null) {
					Display.setIcon(
							new ByteBuffer[] { readImageToBuffer(inputstream), readImageToBuffer(inputstream1) });
					logger.info("Window Icon Set to Melody_Default.");
				}
			} catch (IOException ioexception) {
				logger.warn("Couldn't set icon: " + ioexception.getMessage());
			} finally {
				try {
					inputstream.close();
					inputstream1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0,
				bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

		for (int i : aint) {
			bytebuffer.putInt(i << 8 | i >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}
}
