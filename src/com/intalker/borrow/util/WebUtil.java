package com.intalker.borrow.util;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebUtil {
	public static Bitmap getImageFromURL(String strUrl) {
		Bitmap bmp = null;
		try {
			URL url = new URL(strUrl);
			InputStream in = url.openStream();
			bmp = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
		}
		return bmp;
	}
}
