package xyz.NetMelody.Utils.network;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Client;
import xyz.NetMelody.Utils.TimerUtil;
import xyz.NetMelody.Utils.Render.RenderUtil;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetGraph {

	private static List<BlockObject> rcvBlocks = new CopyOnWriteArrayList<>();
	private static List<BlockObject> outBlocks = new CopyOnWriteArrayList<>();
	private static TimerUtil timerUtil = new TimerUtil(), secTimerUtil = new TimerUtil();

	private static int rcvPackets;
	private static int sendPackets;

	private static int rrmps = 0;
	private static int rsmps = 0;

	public void clear() {
		rcvPackets = 0;
		sendPackets = 0;
		rcvBlocks.clear();
		outBlocks.clear();
		timerUtil.reset();
		secTimerUtil.reset();
	}

	public static void onClientPacket(int c) {
		if (c < 0)
			c = Math.abs(c / 4);
		c /= 8;

		rcvPackets = 0;
		rcvPackets += c;
	}

	public static void onServerPacket(int c) {
		if (c < 0)
			c = Math.abs(c / 4);
		c /= 8;
		
		sendPackets = 0;
		sendPackets += c;
	}

	public static void onRender() {

		GL11.glPushMatrix();
		
		RenderUtil.prepareFont();
		Client.core.font.s5.drawString(40, 40, "U:" + rsmps + "m/s", new SlickColor(150, 150, 250));
		Client.core.font.s5.drawString(385 - Client.core.font.s5.getWidth("D:" + rrmps + "m/s"), 40,
				"D:" + rrmps + "m/s", new SlickColor(142, 255, 190));

		if (timerUtil.hasReached(40)) {

			int ps = sendPackets;
			int pr = rcvPackets;

			if (rsmps > 400)
				ps /= 2;
			if (rrmps > 400)
				pr /= 2;

			if (rsmps > 600)
				ps /= 2;
			if (rrmps > 600)
				pr /= 2;

			if (rsmps > 700)
				ps /= 1.5f;
			if (rrmps > 700)
				pr /= 1.5f;

			outBlocks.forEach(blockObject -> blockObject.x--);
			outBlocks.add(new BlockObject(36 + 354, Math.min(ps, 190)));

			rcvBlocks.forEach(blockObject -> blockObject.x--);
			rcvBlocks.add(new BlockObject(36 + 354, Math.min(pr, 190)));
			timerUtil.reset();
		}

		if (secTimerUtil.hasReached(1000)) {
			rrmps = (int) (rcvPackets * 2.5);
			rsmps = (int) (sendPackets * 2.5);
			secTimerUtil.reset();
		}

		for (int i = 0; i < 2; i++) {
			drawGraph(i, 36, 230);
		}

		rcvBlocks.removeIf(block -> block.x < 38 || block.x > 36 + 354);
		outBlocks.removeIf(block -> block.x < 38 || block.x > 36 + 354);
		GL11.glPopMatrix();
	}

	private static void drawGraph(int mode, int x, int y) {
		boolean isClient = mode == 0;
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1.5f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBegin(GL11.GL_LINES);
		List<BlockObject> list = isClient ? rcvBlocks : outBlocks;
		for (BlockObject block : list) {
			if (mode == 1) {
				Color col = block.height > 0 ? new Color(150, 150, 250, 170) : new Color(150, 150, 250, 100);
				RenderUtil.glColor(col.getRGB());
			} else {
				Color col =  block.height > 0 ? new Color(50, 250, 50, 170) : new Color(50, 250, 50, 100);
				RenderUtil.glColor(col.getRGB());
			}

			GL11.glVertex2d(block.x, y - block.height);
			try {
				BlockObject lastBlock = list.get(list.indexOf(block) + 1);
				GL11.glVertex2d(block.x + 1, y - lastBlock.height);
			} catch (Exception ignored) {
			}
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}
}
