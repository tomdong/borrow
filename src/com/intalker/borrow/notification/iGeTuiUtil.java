package com.intalker.borrow.notification;

import android.content.Context;

import com.igexin.slavesdk.MessageManager;

public class iGeTuiUtil {
	public static void iniGexin(Context context) {
		MessageManager.getInstance()
				.initialize(context.getApplicationContext());
	}
}
