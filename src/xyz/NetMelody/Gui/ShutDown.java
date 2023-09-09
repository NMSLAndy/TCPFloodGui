package xyz.NetMelody.Gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import xyz.NetMelody.Client;
import xyz.NetMelody.Utils.TimerUtil;
import xyz.NetMelody.Utils.Animating.LinearTranslator;
import xyz.NetMelody.Utils.Render.RenderUtil;

public class ShutDown {

	private static LinearTranslator sb = new LinearTranslator(-480);
	private static TimerUtil timer = new TimerUtil();
	private static String message = "Saving Config...";
	private static boolean saved = false;

	public static void draw() {
		GL11.glPushMatrix();

		GL11.glTranslatef(0, sb.getCurrent(), 0);
		sb.interp(0, 30);

		if (sb.getCurrent() == 0) {
			if (timer.hasReached(100)) {
				if(!saved) {
					Client.cfg.save();
					saved = true;
				}
				if (timer.hasReached(150)) {
					message = "Done.";
					Client.shutdown = true;
					timer.reset();
				}
			}
		} else {
			timer.reset();
		}

		RenderUtil.drawFastRoundedRect(0, -20, 800, 600, 2, new Color(47, 48, 54).getRGB());

		RenderUtil.prepareFont();
		Client.core.font.s13.drawString(400 - (Client.core.font.s13.getWidth(message) / 2F) - 15, 200, message);
		GL11.glPopMatrix();
	}

}
