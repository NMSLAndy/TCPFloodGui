package xyz.NetMelody.Utils;

import java.util.List;

public class MathUtil {

	@SuppressWarnings("rawtypes")
	public static String listToString(List dick) {
		return listToString(dick, ", ");
	}

	@SuppressWarnings("rawtypes")
	public static String listToString(List list, String shabi) {
		if (list == null) {
			return "";
		} else {
			StringBuffer stringbuffer = new StringBuffer(list.size() * 5);
			for (int i = 0; i < list.size(); ++i) {
				Object object = list.get(i);
				if (i > 0)
					stringbuffer.append(shabi);
				stringbuffer.append(String.valueOf(object));
			}
			return stringbuffer.toString();
		}
	}
}