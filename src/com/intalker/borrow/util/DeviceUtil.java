package com.intalker.borrow.util;

public class DeviceUtil {
	public static int getOSVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static boolean isFroyo() {
		if (getOSVersion() == android.os.Build.VERSION_CODES.FROYO) {
			return true;
		}
		return false;
	}
}
