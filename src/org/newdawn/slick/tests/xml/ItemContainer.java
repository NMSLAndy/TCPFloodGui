package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

/**
 * A test example of some object data that can be configured via XML
 * 
 * @author kevin
 */
@SuppressWarnings({ "rawtypes" })
public class ItemContainer extends Item {
	/** The items held in this container */
	private ArrayList items = new ArrayList();

	/**
	 * Dump this object to sysout
	 * 
	 * @param prefix The prefix to apply to all lines
	 */
	public void dump(String prefix) {
		System.out.println(prefix+"Item Container "+name+","+condition);
		for (int i=0;i<items.size();i++) {
			((Item) items.get(i)).dump(prefix+"\t");
		}
	}
}
