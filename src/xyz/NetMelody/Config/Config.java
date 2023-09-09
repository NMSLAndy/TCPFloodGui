package xyz.NetMelody.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import xyz.NetMelody.Client;
import xyz.NetMelody.Utils.MathUtil;

public class Config {

	private File dir = new File(System.getProperty("user.dir"));
	private FileCoder coder = new FileCoder();

	public void load() {
		try {
			String config = MathUtil.listToString(this.read(".netMelody.cfg"), "\n");
			String content = coder.decrypt(config);
			StringTokenizer st = new StringTokenizer(content, "-=//");
			if (st.hasMoreElements()) {
				Client.core.ip = st.nextToken();
				System.out.println("[Config] IP: " + Client.core.ip);
			}
			if (st.hasMoreElements()) {
				Client.core.port = Integer.parseInt(st.nextToken());
				System.out.println("[Config] Port: " + Client.core.port);
			}
			if (st.hasMoreElements()) {
				Client.core.threads = Integer.parseInt(st.nextToken());
				System.out.println("[Config] Threads: " + Client.core.threads);
			}
			if (st.hasMoreElements()) {
				Client.core.bytes = Integer.parseInt(st.nextToken());
				System.out.println("[Config] Bytes: " + Client.core.bytes);
			}
			if (st.hasMoreElements()) {
				Client.core.useSystemProxy = Boolean.parseBoolean(st.nextToken());
				System.out.println("[Config] UseSysProxy: " + Client.core.useSystemProxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		String config = "";
		String ip = Client.core.ip + "-=//";
		String port = Client.core.port + "-=//";
		String threads = Client.core.threads + "-=//";
		String bytes = Client.core.bytes + "-=//";
		String useSysP = Client.core.useSystemProxy + "";
		config = ip + port + threads + bytes + useSysP;
		String content = coder.encrypt(config);
		this.write(".netMelody.cfg", content, false);
	}

	public List<String> read(String file) {
		List<String> out = new ArrayList<String>();
		try {
			File f = new File(this.dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			Throwable t = null;
			try {
				FileInputStream fis = new FileInputStream(f);
				try {
					InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					try {
						BufferedReader br = new BufferedReader(isr);
						try {
							String line = "";
							while ((line = br.readLine()) != null) {
								out.add(line);
							}
						} finally {
							if (br != null) {
								br.close();
							}
						}
						if (isr != null) {
							isr.close();
						}
					} finally {
						if (t == null) {
							Throwable t2 = null;
							t = t2;
						} else {
							Throwable t2 = null;
							if (t != t2) {
								t.addSuppressed(t2);
							}
						}
						if (isr != null) {
							isr.close();
						}
					}
					if (fis != null) {
						fis.close();
						return out;
					}
				} finally {
					if (t == null) {
						Throwable t3 = null;
						t = t3;
					} else {
						Throwable t3 = null;
						if (t != t3) {
							t.addSuppressed(t3);
						}
					}
					if (fis != null) {
						fis.close();
					}
				}
			} finally {
				if (t == null) {
					Throwable t4 = null;
					t = t4;
				} else {
					Throwable t4 = null;
					if (t != t4) {
						t.addSuppressed(t4);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public void write(String file, String content, boolean append) {
		try {
			File f = new File(this.dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			Throwable t = null;
			try {
				FileWriter writer = new FileWriter(f, append);
				try {
					writer.write(content);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			} finally {
				if (t == null) {
					Throwable t2 = null;
					t = t2;
				} else {
					Throwable t2 = null;
					if (t != t2) {
						t.addSuppressed(t2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
