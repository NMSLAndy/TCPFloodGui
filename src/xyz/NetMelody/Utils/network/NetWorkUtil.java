package xyz.NetMelody.Utils.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.StringTokenizer;

import xyz.NetMelody.Utils.TimerUtil;

public class NetWorkUtil {
	private static int SLEEP_TIME = 1 * 1000;

	private static TimerUtil avgTimer = new TimerUtil();
	public static int avgSend = 0;
	public static int avgRecieve = 0;

	public static void initListener() {
		new Thread(() -> {
			while (true) {
				Map<String, String> result = getNetworkDownUp();
				int upload = (int) Double.parseDouble(result.get("txPercent"));
				int download = (int) Double.parseDouble(result.get("rxPercent"));

				if (avgTimer.hasReached(1000)) {
					avgSend = 0;
					avgRecieve = 0;
					avgTimer.reset();
				}
				avgSend += upload;
				avgRecieve += download;
				NetGraph.onServerPacket(upload / 124);
				NetGraph.onClientPacket(download / 124);
			}
		}, "NetWork Listener").start();
	}

	public static Map<String, String> getNetworkDownUp() {
		Properties props = System.getProperties();
		String os = props.getProperty("os.name").toLowerCase();
		os = os.startsWith("win") ? "windows" : "linux";
		Map<String, String> result = new HashMap<>();
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		BufferedReader input = null;
		String rxPercent = "";
		String txPercent = "";
		try {
			String command = "windows".equals(os) ? "netstat -e" : "ifconfig";
			pro = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long result1[] = readInLine(input, os);
			pro.destroy();
			input.close();
			pro = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long result2[] = readInLine(input, os);
			rxPercent = ((result2[0] - result1[0]) / (1024.0 * (SLEEP_TIME / 1000))) * 10 + ""; // upload (kB/s)
			txPercent = ((result2[1] - result1[1]) / (1024.0 * (SLEEP_TIME / 1000))) * 10 + ""; // download (kB/s)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Optional.ofNullable(pro).ifPresent(p -> p.destroy());
		}
		result.put("rxPercent", rxPercent);
		result.put("txPercent", txPercent);
		return result;

	}

	private static long[] readInLine(BufferedReader input, String osType) {
		long arr[] = new long[2];
		StringTokenizer tokenStat = null;
		try {
			if (osType.equals("linux")) {
				long rx = 0, tx = 0;
				String line = null;
				while ((line = input.readLine()) != null) {
					if (line.indexOf("RX packets") >= 0) {
						rx += Long.parseLong(line.substring(line.indexOf("RX packets") + 11,
								line.indexOf(" ", line.indexOf("RX packets") + 11)));
					} else if (line.indexOf("TX packets") >= 0) {
						tx += Long.parseLong(line.substring(line.indexOf("TX packets") + 11,
								line.indexOf(" ", line.indexOf("TX packets") + 11)));
					}
				}
				arr[0] = rx;
				arr[1] = tx;
			} else {
				input.readLine();
				input.readLine();
				input.readLine();
				input.readLine();
				tokenStat = new StringTokenizer(input.readLine());
				tokenStat.nextToken();
				arr[0] = Long.parseLong(tokenStat.nextToken());
				arr[1] = Long.parseLong(tokenStat.nextToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
}
