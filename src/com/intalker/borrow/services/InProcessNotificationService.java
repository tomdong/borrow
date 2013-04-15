package com.intalker.borrow.services;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class InProcessNotificationService extends Service implements IInProcessServiceInterface{
	private IInProcessServiceInterface.IInProcessClientInterface mClient = null; // for now , we only allow one Client!!
	private IBinder mBinder = new LocalBinder();
	
	private Deque<IContract> mContracts = new LinkedList<IContract>();
	private ExecutorService  mContractExecutes = Executors.newSingleThreadExecutor();
	
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
	public void setClient(IInProcessClientInterface i) {
		mClient = i;
	}

	@Override
	public void request(IContract c) {
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

	private void processContracts()
	{
		synchronized(this.mContracts)
		{
			IContract front = mContracts.poll(); // we now process the contracts accroding to FIFO!
			if(front!=null)
			{
				front.process();
				if(null!=mClient)
					mClient.onResult(front);
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
