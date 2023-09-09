package xyz.NetMelody.Module.http;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import xyz.NetMelody.GuiRenderer;

/* 
 * ©2021 - 2021 
 * author: Andy
 * I am an idiot!
 *     -----Andy
 */

public class HttpRunnable implements Runnable {

	private String ip;
	private int bytes;
	
	public HttpRunnable(String ip, int bytes) {
		this.ip = ip;
		this.bytes = bytes;
	}
	
	public void run() {
		while (GuiRenderer.shouldTarget()) {
			try {
				URL url = new URL(this.ip);
				URLConnection conn = url.openConnection();

				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				byte[] bytes = new byte[this.bytes];
				int len = -1;
				StringBuffer sb = new StringBuffer();

				if (bis != null) {
					if ((len = bis.read()) != -1) {
						GuiRenderer.message = "Info: Http Request Sent.";
						sb.append(new String(bytes, 0, len));
						bis.close();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				GuiRenderer.message = "ERROR: " + e.getMessage();
			}
		}
	}
}
