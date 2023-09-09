package xyz.NetMelody.Gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickColor;

import xyz.NetMelody.Client;
import xyz.NetMelody.GuiRenderer;
import xyz.NetMelody.Gui.Button.Button;
import xyz.NetMelody.Utils.TimerUtil;
import xyz.NetMelody.Utils.Animating.LinearTranslator;
import xyz.NetMelody.Utils.Animating.Translate;
import xyz.NetMelody.Utils.Render.RenderUtil;
import xyz.NetMelody.Utils.Render.Scissor;

public class TypingGui {

	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private Translate translate = new Translate(0, 0);
	private LinearTranslator line = new LinearTranslator(1);

	private TimerUtil timer = new TimerUtil();
	private TimerUtil cvTimer = new TimerUtil();
	private TimerUtil backTimer = new TimerUtil();
	private TimerUtil lableTimer = new TimerUtil();

	private boolean selectAll = false;
	private String info;
	private String text;
	private StringObject strObj;

	private boolean close = false;
	private boolean cancelled = false;

	public TypingGui(String info, StringObject text) {
		this.info = info;
		this.text = text.getText();
		this.strObj = text;
		this.initTypingButton(text);
	}

	public void processGui() {
		if (!close) {
			this.line.interp(170, 15);
			this.translate.interpolate(800, 450, 15);
		} else {
			this.line.interp(0, 15);
			this.translate.interpolate(0, 0, 15);
		}

		if (this.close && this.translate.getX() == 0 && this.translate.getY() == 0 && this.line.getCurrent() == 0) {
			if (this.cancelled) {
				strObj.setText(strObj.getText());
				this.reset();
				GuiRenderer.typing = false;
			} else {
				strObj.setText(this.text);
				this.reset();
				GuiRenderer.typing = false;
			}
			return;
		}

		float xmod = 800 / 2 - (translate.getX() / 2);
		float ymod = 450 / 2 - (translate.getY() / 2);

		RenderUtil.drawFastRoundedRect(-10, -10, 810, 460, 0,
				new Color(10, 10, 10, (int) this.line.getCurrent()).getRGB());

		GL11.glPushMatrix();
		GL11.glTranslatef(xmod, ymod, 0);
		GL11.glScalef(this.translate.getX() / 800, this.translate.getY() / 450, 0);

		this.processTyping();
		Scissor.start(170, 0, 595, 450);
		RenderUtil.drawFastRoundedRect(170, 205, 595, 245, 10,
				this.selectAll ? new Color(200, 250, 200, 189).getRGB() : new Color(200, 200, 200, 189).getRGB());

		int labx = Client.core.font.s5.getWidth(this.text) > 405 ? 405 : Client.core.font.s5.getWidth(this.text) + 5;
		if (this.lableTimer.hasReached(600)) {
			RenderUtil.drawFastRoundedRect(177 + labx, 210, 179 + labx, 240, 0, new Color(255, 255, 255).getRGB());
			if (this.lableTimer.hasReached(1200))
				this.lableTimer.reset();
		}

		this.drawButtons();

		RenderUtil.prepareFont();
		int apx = Client.core.font.s5.getWidth(this.text) > 405 ? Client.core.font.s5.getWidth(this.text) - 405 : 0;
		Client.core.font.s5.drawString(400 - Client.core.font.s5.getWidth(this.info) / 2 - 10, 160, info);
		Client.core.font.s5.drawString(180 - apx, 210, text, new SlickColor(1));
		Scissor.end();
		GL11.glPopMatrix();
	}

	private void processTyping() {
		if (this.timer.hasReached(50)) {
			Keyboard.enableRepeatEvents(true);
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& this.cvTimer.hasReached(150)) {
				this.selectAll = !this.selectAll;
				this.cvTimer.reset();
				return;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)
					&& this.cvTimer.hasReached(300)) {
				if (this.selectAll) {
					this.text = this.getClipBoard();
					this.selectAll = false;
				} else
					this.text += this.getClipBoard();

				this.cvTimer.reset();
				return;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				this.close = true;
				this.cancelled = false;
				return;
			}

			if (this.selectAll && Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
				this.text = "";
				this.selectAll = false;
				return;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && backTimer.hasReached(75)) {
				String txt = "";
				char[] arr = this.text.toCharArray();
				int len = arr.length;
				for (int i = 0; i < len; i++) {
					if (i == len - 1)
						continue;
					txt += arr[i];
				}
				this.text = txt;
				this.backTimer.reset();
				return;
			}

			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {

					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
						return;

					int key = Keyboard.getEventKey();
					String kn = Keyboard.getKeyName(key);

					if (this.selectAll) {
						this.text = "";
						this.selectAll = false;
					}

					if (kn.equals("0")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += ")";
						else
							this.text += "0";
						return;
					}
					if (kn.equals("9")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "(";
						else
							this.text += "9";
						return;
					}
					if (kn.equals("8")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "*";
						else
							this.text += "8";
						return;
					}
					if (kn.equals("7")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "&";
						else
							this.text += "7";
						return;
					}
					if (kn.equals("6")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "^";
						else
							this.text += "6";
						return;
					}
					if (kn.equals("5")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "%";
						else
							this.text += "5";
						return;
					}
					if (kn.equals("4")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "$";
						else
							this.text += "4";
						return;
					}
					if (kn.equals("3")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "#";
						else
							this.text += "3";
						return;
					}
					if (kn.equals("2")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "@";
						else
							this.text += "2";
						return;
					}
					if (kn.equals("1")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "!";
						else
							this.text += "1";
						return;
					}
					if (kn.contains("EQUALS")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += '+';
						else
							this.text += "=";
						return;
					}
					if (kn.contains("MINUS")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += '_';
						else
							this.text += "-";
						return;
					}
					if (kn.length() == 1)
						this.text += Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? kn.toUpperCase() : kn.toLowerCase();
					if (kn.equals("SLASH")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "?";
						else
							this.text += "/";
					}
					if (kn.contains("PERIOD"))
						this.text += ".";
					if (kn.contains("LBRACKET")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "{";
						else
							this.text += "[";
					}
					if (kn.contains("RBRACKET")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += "}";
						else
							this.text += "]";
					}
					if (kn.contains("SEMICOLON")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += ":";
						else
							this.text += ";";
					}
					if (kn.contains("APOSTROPHE")) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
							this.text += '"';
						else
							this.text += "'";
					}
				}
			}
			this.timer.reset();
		}
	}

	private String getClipBoard() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
					return ret;
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
		}
		return "";
	}

	private void reset() {
		translate = new Translate(0, 0);
		line = new LinearTranslator(0);
		this.buttonList.clear();
	}

	private void initTypingButton(StringObject target) {
		this.buttonList.add(new Button(0, "Done", 250, 270, 120, 40, true, () -> {
			this.close = true;
			this.cancelled = false;
		}, new SlickColor(112, 163, 180)));

		this.buttonList.add(new Button(1, "Cancel", 390, 270, 120, 40, true, () -> {
			this.close = true;
			this.cancelled = true;
		}, new SlickColor(112, 163, 180)));
	}

	private void drawButtons() {
		for (Button b : this.buttonList) {
			b.draw(true);
		}
	}
}
