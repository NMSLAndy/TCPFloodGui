package xyz.NetMelody.Utils.font;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;

import xyz.NetMelody.GuiRenderer;

public class CFont {

	public TrueTypeFont small;
	public TrueTypeFont s1;
	public TrueTypeFont s2;
	public TrueTypeFont s3;
	public TrueTypeFont s5;
	public TrueTypeFont s10;
	public TrueTypeFont s12;
	public TrueTypeFont s13;

	public void initFonts() {
		small = init(16);
		s1 = init(17);
		s2 = init(18);
		s3 = init(20);
		s5 = init(22);
		s10 = init(23);
		s12 = init(24);
		s13 = init(24);
	}

	private TrueTypeFont init(int size) {
		try {
			InputStream inputStream = GuiRenderer.getAssetStream("Montserrat.ttf");
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(size + .0F);
			return new TrueTypeFont(awtFont, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Font awtFont = new Font("Montserrat", Font.BOLD, size);
		return new TrueTypeFont(awtFont, true);
	}

}
