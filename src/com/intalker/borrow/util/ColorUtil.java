package com.intalker.borrow.util;

import android.graphics.Color;

public class ColorUtil {
	public static int generateRandomColor() {
		int randomColor = Color.argb(0xFF, getRandomNum(), getRandomNum(),
				getRandomNum());
		return randomColor;
	}

	private static int getRandomNum() {
		return (int) (Math.random() * 0xFF);
	}
}
