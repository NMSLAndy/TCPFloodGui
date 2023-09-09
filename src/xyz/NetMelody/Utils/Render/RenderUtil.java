package xyz.NetMelody.Utils.Render;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class RenderUtil {

	public static void bindTexture(int textureId) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}

	public static void deleteTexture(int texture) {
		if (texture != 0)
			GL11.glDeleteTextures(texture);
	}

	public static void deleteTextures(IntBuffer ib) {
		ib.rewind();
		while (ib.position() < ib.limit()) {
			int i = ib.get();
			deleteTexture(i);
		}
		ib.rewind();
	}

	public static void drawFilledCircle(float xx, float yy, float radius, Color col) {
		int sections = 50;
		double dAngle = 2 * Math.PI / sections;
		float x, y;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		for (int i = 0; i < sections; i++) {
			x = (float) (radius * Math.sin((i * dAngle)));
			y = (float) (radius * Math.cos((i * dAngle)));

			GL11.glColor4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, col.getAlpha() / 255f);
			GL11.glVertex2f(xx + x, yy + y);
		}
		GL11.glColor3f(0, 0, 0);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void prepareFont() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
		if (x0 == x1 || y0 == y1)
			return;
		int Semicircle = 18;
		float f = 90.0f / Semicircle;
		float f2 = (color >> 24 & 0xFF) / 255.0f;
		float f3 = (color >> 16 & 0xFF) / 255.0f;
		float f4 = (color >> 8 & 0xFF) / 255.0f;
		float f5 = (color & 0xFF) / 255.0f;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(f3, f4, f5, f2);
		GL11.glBegin(5);
		GL11.glVertex2f(x0 + radius, y0);
		GL11.glVertex2f(x0 + radius, y1);
		GL11.glVertex2f(x1 - radius, y0);
		GL11.glVertex2f(x1 - radius, y1);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x0, y0 + radius);
		GL11.glVertex2f(x0 + radius, y0 + radius);
		GL11.glVertex2f(x0, y1 - radius);
		GL11.glVertex2f(x0 + radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x1, y0 + radius);
		GL11.glVertex2f(x1 - radius, y0 + radius);
		GL11.glVertex2f(x1, y1 - radius);
		GL11.glVertex2f(x1 - radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(6);
		float f6 = x1 - radius;
		float f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		int j = 0;
		for (j = 0; j <= Semicircle; ++j) {
			float f8 = j * f;
			GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f8))),
					(float) (f7 - radius * Math.sin(Math.toRadians(f8))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			float f9 = j * f;
			GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f9))),
					(float) (f7 - radius * Math.sin(Math.toRadians(f9))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			float f10 = j * f;
			GL11.glVertex2f((float) (f6 - radius * Math.cos(Math.toRadians(f10))),
					(float) (f7 + radius * Math.sin(Math.toRadians(f10))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x1 - radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			float f11 = j * f;
			GL11.glVertex2f((float) (f6 + radius * Math.cos(Math.toRadians(f11))),
					(float) (f7 + radius * Math.sin(Math.toRadians(f11))));
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPopMatrix();
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255F;
		float red = (hex >> 16 & 0xFF) / 255F;
		float green = (hex >> 8 & 0xFF) / 255F;
		float blue = (hex & 0xFF) / 255F;
		GL11.glColor4f(red, green, blue, alpha);
	}
}
