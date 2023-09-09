
import static xyz.NetMelody.Utils.FileUtils.getDataDir;

import java.io.File;

import xyz.NetMelody.NM;
import xyz.NetMelody.Utils.FileUtils;
import xyz.NetMelody.Utils.Logger;

public class Main {

	private static Logger logger = new Logger();

	public static void main(String[] args) throws Exception {

		logger.info("CurrentDir: " + getDataDir());
		logger.info("Calling File Init Thread.");
		Thread t = new Thread(() -> {
			logger.info("Preparing Directories...");
			FileUtils.prepareDirectories();
			logger.info("Extracting Natives...");
			FileUtils.extractJNILibs();
		}, "File Init Thread");
		t.start();

		while (t.isAlive())
			Thread.sleep(10);

		System.out.println(new File(FileUtils.getDataDir() + "/natives").getAbsolutePath());
		System.setProperty("org.lwjgl.librarypath", new File(FileUtils.getDataDir() + "/natives").getAbsolutePath());
		System.setProperty("java.library.path", new File(FileUtils.getDataDir() + "/natives").getAbsolutePath());

		NM.instance.start();
	}
}
