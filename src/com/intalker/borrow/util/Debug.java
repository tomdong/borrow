package com.intalker.borrow.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Debug {
	public static void toast(Context container, String message) {
		((Activity) container).runOnUiThread(new ToastHelper((Activity) container, message));

	}
}

class ToastHelper implements Runnable {
	private Activity mContainer = null;
	private String mMessage = null;

	public ToastHelper(Activity cont, String message) {
		mContainer = cont;
		mMessage = message;
	}

	@Override
	public void run() {
		Toast.makeText(mContainer.getApplicationContext(), mMessage,
				Toast.LENGTH_SHORT).show();
	}
}
