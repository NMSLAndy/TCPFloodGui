package xyz.NetMelody.Utils;

public class Logger {
	
	public void info(String info) {
		this.appendMSG("[INFO] " + info);
	}
	
	public void warn(String info) {
		this.appendMSG("[WARNNING] " + info);
	}
	
	public void err(String info) {
		this.appendMSG("[ERROR] " + info);
	}

	private void appendMSG(String str) {
		System.out.printf(str + "\r\n", this);
	}
}
