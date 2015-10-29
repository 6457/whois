package utils;

public class Timer {
	static long stime;
	static long etime;

	public static long on() {
		return stime = System.currentTimeMillis();
	}

	public static long off() {
		return etime = System.currentTimeMillis();
	}

	public static double get() {
		return (System.currentTimeMillis() - stime) / 1000.0;
	}

	public static long disp() {
		System.err.println("Time elapsed: " + (etime - stime) / 1000.0);
		return etime;
	}

	public static long show() {
		long ctime = System.currentTimeMillis();
		System.err.println("Time elapsed: " + (ctime - stime) / 1000.0);
		return ctime;
	}
}