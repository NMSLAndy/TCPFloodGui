package xyz.NetMelody.Utils.GLSL;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class BackgroundShader extends Shader {

	public static int deltaTime;
	public static BackgroundShader BACKGROUND_SHADER = new BackgroundShader();

	private float time;

	// Fragment by LiquidBounce
	public BackgroundShader() {
		super("frag/background.frag");
	}

	@Override
	public void setupUniforms() {
		setupUniform("iResolution");
		setupUniform("iTime");
	}

	@Override
	public void updateUniforms() {
		int resolutionID = getUniform("iResolution");
		if (resolutionID > -1)
			GL20.glUniform2f(resolutionID, Display.getWidth(), Display.getHeight());

		int timeID = getUniform("iTime");
		if (timeID > -1)
			GL20.glUniform1f(timeID, time);

		time += 0.01F;
	}

	public void draw() {
		GL11.glPushMatrix();
		BackgroundShader.BACKGROUND_SHADER.startShader();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-20, -20);
		GL11.glVertex2f(-20, Display.getHeight() + 20);
		GL11.glVertex2f(Display.getWidth() + 20, Display.getHeight() + 20);
		GL11.glVertex2f(Display.getWidth() + 20, -20);
		GL11.glEnd();

		BackgroundShader.BACKGROUND_SHADER.stopShader();
		GL11.glPopMatrix();
	}
}
