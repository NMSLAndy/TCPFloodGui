package xyz.NetMelody.Gui.Selecting;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Client;
import xyz.NetMelody.Gui.Circle;
import xyz.NetMelody.Utils.Render.RenderUtil;
import xyz.NetMelody.Utils.Render.Scissor;

public class SelectBox {

	public int id;
	private String text;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean hovered;
	private boolean pressed;
	private Action action;
	private boolean enabled;
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private boolean clicked = false;

	public SelectBox(int id, String text, int x, int y, int width, int height, boolean enabled, Action action) {
		this.id = id;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.enabled = enabled;
		this.action = action;
	}

	public void draw(boolean withTick) {

		if (withTick)
			this.tick();

		GL11.glPushMatrix();
		Scissor.start(x, y, x + width, y + height);

		if (this.pressed) {
			if (!this.clicked) {
				this.circles.add(new Circle(Client.mouseX, Client.mouseY, this.width));
				this.clicked = true;
			}
		} else {
			if (this.clicked) {
				this.clicked = false;
			}
		}

		RenderUtil
				.drawFastRoundedRect(this.getX(), this.getY() - 4, this.getX() + this.getWidth(),
						this.getY() + this.getHeight() - 6, 2,
						this.isEnabled()
								? (this.isHovered()
										? (this.isPressed() ? new Color(70, 71, 80, 170).getRGB()
												: new Color(84, 85, 94, 170).getRGB())
										: new Color(74, 75, 84, 170).getRGB())
								: new Color(54, 55, 64, 170).getRGB());

		for (Circle c : this.circles)
			c.draw();

		this.circles.removeIf(c -> c.op.getCurrent() == this.width + 10);

		RenderUtil.prepareFont();
		Client.core.font.s12.drawString(
				this.getX() + this.getWidth() / 2 - (Client.core.font.s12.getWidth(this.getText()) / 2),
				this.getY() + this.getHeight() / 2 - (Client.core.font.s12.getHeight(this.getText()) / 2) - 4,
				this.getText(), new SlickColor(120, 120, 120));

		Scissor.end();
		GL11.glPopMatrix();
	}

	private void tick() {
		int mouseX = Client.mouseX;
		int mouseY = Client.mouseY;
		if (this.isEnabled()
				&& this.isButtonHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
			this.setHovered(true);
			if (Mouse.isButtonDown(0)) {
				this.setPressed(true);
			}
			if (this.isPressed() && !Mouse.isButtonDown(0)) {
				this.setPressed(false);
				this.perform();
			}
		} else {
			this.setHovered(false);
			this.setPressed(false);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void perform() {
		this.action.perform();
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isHovered() {
		return hovered;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@FunctionalInterface
	public interface Action {
		void perform();
	}

	private boolean isButtonHovered(int mouseX, int mouseY, int bx, int by, int width, int height) {
		return (mouseX > bx && mouseX < (bx + width)) && (mouseY > by && mouseY < (by + height));
	}
}
