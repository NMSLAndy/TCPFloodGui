package xyz.NetMelody.Module.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;

import xyz.NetMelody.GuiRenderer;

public class UDPRunable implements Runnable {

	private DatagramSocket dataSocket;
	private DatagramPacket packet;
	private String ip;
	private int port;
	private byte[] bytes;

	public UDPRunable(String ip, int port, byte[] bytes) {
		this.ip = ip;
		this.port = port;
		this.bytes = bytes;
	}

	@Override
	public void run() {
		try {
			int sourcePort = this.random(0, 65535);

			dataSocket = new DatagramSocket(null);
			this.dataSocket.bind(new InetSocketAddress(InetAddress.getByName(this.randomIP()), sourcePort));

			byte[] arr = new byte[bytes.length];
			packet = new DatagramPacket(arr, arr.length, InetAddress.getByName(this.ip), this.port);

			while (GuiRenderer.shouldTarget()) {
				dataSocket.send(packet);
				GuiRenderer.message = "Info: UDP Packet Sent.";
			}
			dataSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			GuiRenderer.message = "ERROR: " + e.getMessage();
		}
	}

	public String randomIP() {
		boolean bFlag = true;
		while (bFlag) {
			bFlag = false;

			int nIPa = this.random(0, 255);
			int nIPb = this.random(0, 255);
			int nIPc = this.random(0, 255);
			int nIPd = this.random(0, 255);

			if (nIPa == 0 || nIPa == 10 || nIPd == 0) {
				bFlag = true;
			}
			if (nIPa == 172 && nIPb >= 16 && nIPb <= 30) {
				bFlag = true;
			}
			if (nIPa == 192 && nIPb == 168) {
				bFlag = true;
			}
			if (nIPa == 127 && nIPb == 0 && nIPc == 0 && nIPd == 1) {
				bFlag = true;
			}
			if (bFlag == false) {
				String result = nIPa + "." + nIPb + "." + nIPc + "." + nIPd;
				return result;
			}
		}
		return "0.0.0.0";
	}

	public int random(int min, int max) {
		Random random = new Random();
		return min + (int) (random.nextDouble() * (max - min));
	}

}
