package xyz.NetMelody;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Gui.TypingGui;
import xyz.NetMelody.Utils.Render.RenderUtil;
import xyz.NetMelody.Utils.System.SysGraph;
import xyz.NetMelody.Utils.font.CFont;
import xyz.NetMelody.Utils.network.NetGraph;

public class GuiRenderer {

	public static boolean accepted = false;

	public int threads = 1000;
	public String ip = "127.0.0.1";
	public int port = 1145;
	public int bytes = 1024;

	public boolean useSystemProxy = false;
	
	public static String message = "Info: Ready.";

	public ArrayList<Thread> httpThreads = new ArrayList<>();
	public ArrayList<Thread> tcpThreads = new ArrayList<>();
	public ArrayList<Thread> udpThreads = new ArrayList<>();
	private static boolean shouldTarget;

	public int mode = 0;

	public CFont font = new CFont();

	public static int width = 0;
	public static int height = 0;
	
	public static boolean typing = false;
	public static TypingGui typingGui = null;

	@SuppressWarnings("static-access")
	public void draw() {

		if (!this.shouldTarget) {
			this.message = "Info: Ready.";
			this.httpThreads.clear();
			this.tcpThreads.clear();
			this.udpThreads.clear();
		}
		
		//Horizon
		RenderUtil.drawFastRoundedRect(36, 234.5F, 402 + 366, 235.5F, 0, new Color(200, 200, 200, 190).getRGB());
		RenderUtil.drawFastRoundedRect(36, 35.5F, 402 + 366, 36.5F, 0, new Color(200, 200, 200, 190).getRGB());
		
		//Vertical
		RenderUtil.drawFastRoundedRect(35.5F, 35.5F, 36.5F, 235.5F, 0, new Color(200, 200, 200, 190).getRGB());
		RenderUtil.drawFastRoundedRect(29 + 365.5F, 35.5F, 29 + 366.5F, 235.5F, 0, new Color(200, 200, 200, 190).getRGB());
		RenderUtil.drawFastRoundedRect(402 + 365.5F, 35.5F, 402 + 366.5F, 235.5F, 0, new Color(200, 200, 200, 190).getRGB());
		
		//Rect
		RenderUtil.drawFastRoundedRect(36, 38, 402 + 366, 238, 2, new Color(20, 20, 20, 75).getRGB());
		
		NetGraph.onRender();
		SysGraph.onRender();

		font.s3.drawString(10, 3, this.message, new SlickColor(186, 181, 211));
		
		font.s3.drawString(650, 3, ": " + this.useSystemProxy, new SlickColor(186, 181, 211));

		int appendY = 230;
		font.s13.drawString(36, 30 + appendY, "Max Threads: ", new SlickColor(186, 181, 211, 190));
		font.s13.drawString(36, 70 + appendY, "Target IP:", new SlickColor(186, 181, 211, 190));
		font.s13.drawString(36, 110 + appendY, "Target Port:", new SlickColor(186, 181, 211, 190));
		font.s13.drawString(36, 150 + appendY, "Packet Size:", new SlickColor(186, 181, 211, 190));

		font.s10.drawString(205, 31 + appendY, this.threads + "", new SlickColor(250, 250, 250, 190));
		if (this.ip.length() >= 10 && this.ip.length() < 13)
			font.s5.drawString(159, 72 + appendY, this.ip, new SlickColor(250, 250, 250, 190));
		else if (this.ip.length() >= 13 && this.ip.length() < 16)
			font.s3.drawString(159, 73 + appendY, this.ip, new SlickColor(250, 250, 250, 190));
		else if (this.ip.length() >= 16 && this.ip.length() < 19)
			font.s2.drawString(158, 74 + appendY, this.ip, new SlickColor(250, 250, 250, 190));
		else if (this.ip.length() >= 19)
			font.small.drawString(155, 76 + appendY, this.ip, new SlickColor(250, 250, 250));
		else
			font.s10.drawString(160, 71 + appendY, this.ip, new SlickColor(250, 250, 250, 190));
		font.s10.drawString(190, 111 + appendY, this.port + "", new SlickColor(250, 250, 250, 190));
		font.s10.drawString(190, 151 + appendY, this.bytes + "", new SlickColor(250, 250, 250, 190));
	}

	public static String getAssetPath(String name) {
		return GuiRenderer.class.getResource("/assets/" + name).getPath();
	}

	public static InputStream getAssetStream(String name) {
		try {
			return GuiRenderer.class.getResource("/assets/" + name).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean shouldTarget() {
		return shouldTarget;
	}

	public static void setShouldTarget(boolean shouldTarget) {
		GuiRenderer.shouldTarget = shouldTarget;
	}
}
