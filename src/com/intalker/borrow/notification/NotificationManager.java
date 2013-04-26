package com.intalker.borrow.notification;

import java.io.IOException;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import android.app.Service;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class NotificationManager {
	private boolean mIsInitializing = false;
	private boolean mDirty = false;
	private boolean mViaSound = true;
	private boolean mViaVibrate = true;
	private static NotificationManager mInstance = null;

	public static NotificationManager getInstance() {
		if (null == mInstance) {
			mInstance = new NotificationManager();
		}
		return mInstance;
	}

	public void setIsInitializing(boolean val) {
		mIsInitializing = val;
	}
	
	public void markDirty() {
		if(mIsInitializing) {
			return;
		}
		mDirty = true;
	}

	public void fire() {
		if (!mDirty) {
			return;
		}
		if (mViaVibrate) {
			vibrate();
		}
		if (mViaSound) {
			ring();
		}
		mDirty = false;
	}

	private void ring() {
		MediaPlayer mp = MediaPlayer.create(HomeActivity.getApp(), R.raw.ring);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();
	}

	private void vibrate() {
		HomeActivity app = HomeActivity.getApp();
		Vibrator vib = (Vibrator) app
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(500);
	}
}
