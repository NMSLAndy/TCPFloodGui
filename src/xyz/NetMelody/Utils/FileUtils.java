package xyz.NetMelody.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	private static Logger logger = new Logger();

	public static void prepareDirectories() {
		File container = new File(getDataDir() + "/natives");
		if (!container.exists()) {
			logger.warn("Native Container Not Exist, Making DIR.");
			container.mkdir();
		}
		logger.info("Native Container Ready.");
	}

	public static void extractJNILibs() {
		if (OSUtils.isWindows()) {
			logger.info("Extracting Windows -> lwjgl.dll");
			copy("/natives/windows/lwjgl.dll", getDataDir() + "/natives/lwjgl.dll");
			logger.info("Extracting Windows -> lwjgl64.dll");
			copy("/natives/windows/lwjgl64.dll", getDataDir() + "/natives/lwjgl64.dll");
		} else if (OSUtils.isLinux()) {
			logger.info("Extracting Linux -> liblwjgl.so");
			copy("/natives/linux/liblwjgl.so", getDataDir() + "/natives/liblwjgl.so");
			logger.info("Extracting Linux -> liblwjgl64.so");
			copy("/natives/linux/liblwjgl64.so", getDataDir() + "/natives/liblwjgl64.so");
		} else if (OSUtils.isMacOS()) {
			logger.info("Extracting MacOSX -> liblwjgl.dylib");
			copy("/natives/macosx/liblwjgl.dylib", getDataDir() + "/natives/liblwjgl.dylib");
		}
	}

	public static String getDataDir() {
		File directory = new File("");
		try {
			return directory.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR: " + e.getMessage();
		}
	}

	public static void copy(String source, String dest) {
		try {
			File destFile = new File(dest);
			InputStream in = FileUtils.class.getResourceAsStream(source);
			OutputStream out = new FileOutputStream(destFile);
			int bufferSize = 1024;
			byte[] buf = new byte[bufferSize];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
