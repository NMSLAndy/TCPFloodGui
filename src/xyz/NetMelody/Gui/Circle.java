package xyz.NetMelody.Gui;

import java.awt.Color;

import xyz.NetMelody.Utils.Animating.LinearTranslator;
import xyz.NetMelody.Utils.Render.RenderUtil;

public class Circle {

	public LinearTranslator op = new LinearTranslator(0);
	public float hmx = 0;
	public float hmy = 0;
	public float width;

	public Circle(int mouseX, int mouseY, int width) {
		this.hmx = mouseX;
		this.hmy = mouseY;
		this.width = width;
	}

	public void draw() {
		if (this.op.getCurrent() < this.width + 10)
			this.op.interp(this.width + 10, this.width / (this.width / 9));
		else
			this.op.interp(0, 0);

		if (this.op.getCurrent() > 0) {
			int a = (int) (255 - (this.op.getCurrent() * 2.5F / this.width * 100));
			RenderUtil.drawFilledCircle(this.hmx, this.hmy, this.op.getCurrent(),
					new Color(130, 130, 130, a > 0 ? a : 0));
		}
	}

}
