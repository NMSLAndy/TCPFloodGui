package xyz.NetMelody.Module.tcp;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import xyz.NetMelody.GuiRenderer;

/* 
 * ©2021 - 2021 
 * author: Andy
 * I am an idiot!
 *     -----Andy
 */

public class SynRunnable implements Runnable {

	private String host;
	private int port;
	
	public SynRunnable(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		while (GuiRenderer.shouldTarget()) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(host, port), 2500);
				GuiRenderer.message = "Info: TCP Connection Established.";
				Thread.sleep(20);
				socket.close();
			} catch (Exception e) {
				if (e instanceof UnknownHostException) {
					e.printStackTrace();
					GuiRenderer.message = "ERROR: " + e.getMessage();
					break;
				} else if (e instanceof SocketTimeoutException) {
					e.printStackTrace();
					GuiRenderer.message = "ERROR: " + e.getMessage();
				} else {
					e.printStackTrace();
					GuiRenderer.message = "ERROR: " + e.getMessage();
				}
			}
		}
	}
}
