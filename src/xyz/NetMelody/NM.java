package xyz.NetMelody;

public class NM {
	
	public static NM instance = new NM();
	
	public static String author = "Andy";
	public static String name = "Net Melody";
	public static String version = "5.0";
	
	public void start() {
		Client.init();
	}
}
