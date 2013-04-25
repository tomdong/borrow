package com.intalker.borrow.services;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class InProcessNotificationService extends Service implements IInProcessServiceInterface{
	private IBinder mBinder = new LocalBinder();
	
	private Deque<IExecuator> mContracts = new LinkedList<IExecuator>();
	private ExecutorService  mContractExecutes = Executors.newSingleThreadExecutor();
	
	private List<ITimerTimeout> mTimeout = new ArrayList<ITimerTimeout>();
	
	private Timer mTimerUtil = null;
	
	private boolean mActive = false;
	
	public class LocalBinder extends Binder {
		public InProcessNotificationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return InProcessNotificationService.this;
        }
    }
	
	@Override
	public void onCreate() {
		mActive = true;
		
		mTimerUtil = new Timer();
		mTimerUtil.schedule(new TimerTask() {

			@Override
			public void run() {
				onTimerTimeout();
			}

		}, 300, 6000);
    }
	
	@Override
	public void onDestroy() {
		mActive = false;
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void request(IExecuator c) {
		synchronized(this.mContracts)
		{
			mContracts.push(c);
		}
		
		mContractExecutes.execute(new Runnable()
		{
			@Override
			public void run() {
				processContracts();
			}
		});
	}
	
	@Override
	public void requestTimeout(ITimerTimeout t) {
		synchronized(this.mTimeout)
		{
			this.mTimeout.add(t);
		}
	}

	@Override
	public void removeTimeout(ITimerTimeout t) {
		synchronized(this.mTimeout)
		{
			this.mTimeout.remove(t);
		}
	}	
	
	private void onTimerTimeout()
	{
		synchronized(this.mTimeout)
		{
			int n = mTimeout.size();
			for(int i=0;i<n;i++)
			{
				mTimeout.get(i).onTimeOut();
			}
		}
	}

	private void processContracts()
	{
		synchronized(this.mContracts)
		{
			IExecuator front = mContracts.poll(); // we now process the contracts accroding to FIFO!
			if(front!=null)
			{
				front.process();
			}
		}
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
	public boolean onUnbind(Intent intent) {
        return false;
    }
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	
}
