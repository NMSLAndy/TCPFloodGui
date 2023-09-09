package xyz.NetMelody.Utils.System;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Client;
import xyz.NetMelody.Utils.TimerUtil;
import xyz.NetMelody.Utils.Render.RenderUtil;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SysGraph {

	private static List<BlockObject> cpuBlocks = new CopyOnWriteArrayList<>();
	private static List<BlockObject> outBlocks = new CopyOnWriteArrayList<>();
	private static TimerUtil timerUtil = new TimerUtil(), secTimerUtil = new TimerUtil();

	private static int ram;
	private static int cpu;

	private static int ramSec = 0;
	private static int cpuSec = 0;

	public void clear() {
		ram = 0;
		cpu = 0;
		cpuBlocks.clear();
		outBlocks.clear();
		timerUtil.reset();
		secTimerUtil.reset();
	}

	public static void onRam(int c) {
		ram = 0;
		ram += c;
	}

	public static void onCpu(int c) {
		cpu = 0;
		cpu += c;
	}

	public static void onRender() {
		GL11.glPushMatrix();
		RenderUtil.drawFastRoundedRect(402, 229.5F, 402 + 366, 230.5F, 0, -1);

		RenderUtil.prepareFont();
		Client.core.font.s5.drawString(404, 40, "Cpu:" + cpuSec + "%", new SlickColor(150, 150, 250));
		Client.core.font.s5.drawString(404 + 352 - Client.core.font.s5.getWidth("Ram:" + ramSec + "%"), 40,
				"Ram:" + ramSec + "%", new SlickColor(142, 255, 190));

		if (timerUtil.hasReached(40)) {

			int ps = cpu;
			int pr = ram;

			outBlocks.forEach(blockObject -> blockObject.x--);
			outBlocks.add(new BlockObject(420 + 342, Math.min((int) (ps * 1.55), 190)));

			cpuBlocks.forEach(blockObject -> blockObject.x--);
			cpuBlocks.add(new BlockObject(420 + 342, Math.min((int) (pr * 1.55), 190)));
			timerUtil.reset();
		}

		if (secTimerUtil.hasReached(1000)) {
			ramSec = ram;
			cpuSec = cpu;
			secTimerUtil.reset();
		}

		for (int i = 0; i < 2; i++) {
			drawGraph(i, 36, 230);
		}

		cpuBlocks.removeIf(block -> block.x < 400);
		outBlocks.removeIf(block -> block.x < 400);
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
		List<BlockObject> list = isClient ? cpuBlocks : outBlocks;
		for (BlockObject block : list) {
			if (mode == 1) {
				Color col = block.height > 0 ? new Color(150, 150, 250, 170) : new Color(150, 150, 250, 100);
				RenderUtil.glColor(col.getRGB());
			} else {
				Color col = block.height > 0 ? new Color(50, 250, 50, 170) : new Color(50, 250, 50, 100);
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
