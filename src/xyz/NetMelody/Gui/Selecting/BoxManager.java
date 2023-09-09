package xyz.NetMelody.Gui.Selecting;

import java.util.ArrayList;
import java.util.Comparator;

public class BoxManager {

	public ArrayList<SelectBox> buttonList = new ArrayList<SelectBox>();

	public void initButtons() {
		this.buttonList.clear();


		this.buttonList.sort(Comparator.comparingDouble(button -> button.id));
	}

	public void drawButtons(boolean tick) {
		for (SelectBox b : this.buttonList) {
			b.draw(tick);
		}
	}
}
