package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

/**
 * The top level node of our test structure for XML -> object parsing
 * 
 * @author kevin
 */
@SuppressWarnings({ "rawtypes" })
public class GameData {
	/** The list of entities added */
	private ArrayList entities = new ArrayList();

	/**
	 * Dump this object to sysout
	 * 
	 * @param prefix The prefix to apply to all lines
	 */
	public void dump(String prefix) {
		System.out.println(prefix+"GameData");
		for (int i=0;i<entities.size();i++) {
			((Entity) entities.get(i)).dump(prefix+"\t");
		}
	}
}
