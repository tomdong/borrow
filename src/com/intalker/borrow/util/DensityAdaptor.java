package com.intalker.borrow.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DensityAdaptor {

	private static float mFactor = 1.0f;
	private static int mScreenWidth = 0;
	private static int mScreenHeight = 0;

	public static void init(Activity app) {
		int defaultdpi = 160;
		int dpi = 0;

		if (app != null) {
			DisplayMetrics dm = new DisplayMetrics();

			app.getWindowManager().getDefaultDisplay().getMetrics(dm);

			mScreenWidth = dm.widthPixels;
			mScreenHeight = dm.heightPixels;

			dpi = dm.densityDpi;
		}

		mFactor = (float) dpi / (float) defaultdpi;
	}

	public static int getDensityIndependentValue(int value) {

		int outvalue = (int) ((float) value * mFactor);

		return outvalue;

	}

	public static float getDensityIndependentValue(float value) {

		float outvalue = value * mFactor;

		return outvalue;
	}

	public static int getScreenWidth() {
		return mScreenWidth;
	}

	public static int getScreenHeight() {
		return mScreenHeight;
	}
}