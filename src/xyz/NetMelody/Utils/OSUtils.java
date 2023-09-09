package xyz.NetMelody.Utils;

import java.lang.management.ManagementFactory;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.sun.management.OperatingSystemMXBean;

public class OSUtils {

	private static OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	public static final String TWO_DECIMAL = "0.00";

	public static double cpuUsage() {
		double cpuLoad = osBean.getSystemCpuLoad();
		double useage = cpuLoad * 100;
		DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
		df.setRoundingMode(RoundingMode.HALF_UP);

		if (useage > 100)
			useage = 100.00;

		return Double.parseDouble(df.format(useage));
	}

	public static double ramUsage() {
		double totalvirtualMemory = osBean.getTotalPhysicalMemorySize();
		double freePhysicalMemorySize = osBean.getFreePhysicalMemorySize();
		double value = freePhysicalMemorySize / totalvirtualMemory;
		double useage = (1 - value) * 100;
		DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
		df.setRoundingMode(RoundingMode.HALF_UP);

		if (useage > 100)
			useage = 100.00;

		return Double.parseDouble(df.format(useage));
	}

	public static boolean isWindows() {
		return getOSType() == EnumOS.WINDOWS;
	}

	public static boolean isLinux() {
		return getOSType() == EnumOS.LINUX;
	}

	public static boolean isMacOS() {
		return getOSType() == EnumOS.OSX;
	}

	public static EnumOS getOSType() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? EnumOS.WINDOWS
				: (s.contains("mac") ? EnumOS.OSX
						: (s.contains("solaris") ? EnumOS.SOLARIS
								: (s.contains("sunos") ? EnumOS.SOLARIS
										: (s.contains("linux") ? EnumOS.LINUX
												: (s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
	}

	public static enum EnumOS {
		LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN;
	}

}
