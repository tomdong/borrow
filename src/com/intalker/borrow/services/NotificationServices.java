package com.intalker.borrow.services;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public class NotificationServices extends Service{
	
	@Override
	public void onCreate() {
    }
	
	@Override
	public void onDestroy() {
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    }
    
	@Override
    public void onLowMemory() {
    }
	
	@Override
    public void onTrimMemory(int level) {
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
        return false;
    }
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      return START_STICKY;
	  }


}
