package xyz.NetMelody.Utils.Render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Scissor {
	
	public static void start(float x1, float y1, float x2, float y2) {
		float temp;
		if (y1 > y2) {
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		GL11.glScissor((int)x1, (int)(Display.getHeight()-y2), (int)(x2-x1), (int)(y2-y1));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void end() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
}
