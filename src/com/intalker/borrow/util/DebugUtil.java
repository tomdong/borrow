package com.intalker.borrow.util;

import android.content.Context;
import android.widget.Toast;

import com.intalker.borrow.config.AppConfig;

public class DebugUtil {
	private static long startTime = 0;

	public static void startTimeRecord() {
		startTime = System.currentTimeMillis();
	}

	public static void showTimeRecordResult(Context context) {
		if (AppConfig.isDebugMode) {
			long timeSpan = System.currentTimeMillis() - startTime;
			Toast.makeText(context,
					"Time cost: " + String.valueOf(timeSpan) + " ms",
					Toast.LENGTH_LONG).show();
		}
	}
}
