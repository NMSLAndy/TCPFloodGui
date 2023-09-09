package xyz.NetMelody.Gui;

import java.awt.Color;

import xyz.NetMelody.Client;
import xyz.NetMelody.Utils.Render.RenderUtil;

public class Lisence {
	
	public static void drawLisenceGui() {
		
		RenderUtil.drawFastRoundedRect(0, -20, 800, 600, 2, new Color(47, 48, 54).getRGB());
		
		RenderUtil.prepareFont();
		Client.core.font.s13.drawString(210, 30, "NetMelody - Announcement");
		Client.core.font.small.drawString(20, 410, "Copyright (c) 2019-2023 MelodySky WorkGroup.");
		Client.core.font.small.drawString(665, 410, "by NMSLAndy.");
		
		Client.core.font.s3.drawString(125, 100, "The author is not responsible for any consequences");
		Client.core.font.s3.drawString(125, 140, "caused by your use of this software.");
		
		Client.core.font.s3.drawString(160, 180, "DO NOT use this software in any illegal ways.");
		Client.core.font.s3.drawString(250, 220, "USE AS YOUR OWN RISK.");
	}

}
