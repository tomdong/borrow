package com.intalker.tencentinterface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.tencent.tauth.TencentOpenRes;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;
import com.tencent.tauth.http.TDebug;

public class TencentConnection {
	private static final String TAG = "Tencent";

	
	
	// The connection can only be used in an
	// activiy now and the activity must
	// implment some defined behavior. TODO: abstract
	private Activity mParent = null;

	private String mAppid = "100347785";

	private String mScope = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";// 授权范围
	private AuthReceiver mReceiver;

	private String mAccessToken, mOpenId, mErrorReturn, mErrorDes, mExpireInfo,
			mRaw;

	public TencentConnection(Activity parent) {
		mAccessToken = "";
		mOpenId = "";
		mErrorReturn = "";
		mErrorDes = "";
		mExpireInfo = "";
		mParent = parent;
		loadCacheToken();
	}
	
	public boolean containsValidCacheToken()
	{
		if(mAccessToken==null ||mAccessToken.isEmpty())
			return false;
		
		// TODO: Check the validation of the cache token
		return true;
	}
	
	public void populateLogin()
	{
		populateLogin(true);
	}
	
	public Drawable getLoginButton(int size)
	{
		Drawable loginImage = null;
		switch(size)
		{
			case 0:
				loginImage = TencentOpenRes.getBigLoginBtn(mParent.getAssets());
				break;
			case 1:
				loginImage = TencentOpenRes.getLoginBtn(mParent.getAssets());
				break;
			case 2:
				loginImage = TencentOpenRes.getSmallLoginBtn(mParent.getAssets());
				break;
			default:
				loginImage = TencentOpenRes.getLoginBtn(mParent.getAssets());
		}
		
		return loginImage;
	}
	
	public Drawable getLogoutButton(int size)
	{
		Drawable logoutImage = null;
		switch(size)
		{
			case 0:
				logoutImage = TencentOpenRes.getLogoutBtn(mParent.getAssets()); // no big button now!
				break;
			case 1:
				logoutImage = TencentOpenRes.getLogoutBtn(mParent.getAssets());
				break;
			case 2:
				logoutImage = TencentOpenRes.getSmallLogoutBtn(mParent.getAssets());
				break;
			default:
				logoutImage = TencentOpenRes.getLogoutBtn(mParent.getAssets());
		}
		return logoutImage;
	}
	
	private void populateLogin(boolean reg)
	{
		TencentOpenAPI2.logIn(mParent.getApplicationContext(), mOpenId, mScope, mAppid, "_self", null, null, null);
		if(reg)
			registerIntentReceivers();
	}
	
	private void loadCacheToken()
	{
		// TODO: 
	}
	
	private void registerIntentReceivers() {
		Log.i(TAG, "REG");
		mReceiver =  new AuthReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TencentOpenHost.AUTH_BROADCAST);
		mParent.registerReceiver(mReceiver, filter);
	}
	
	private void unregisterIntentReceivers() {
		Log.i(TAG, "UNREG");
		mParent.unregisterReceiver(mReceiver);
	}
	
	private class AuthReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle exts = intent.getExtras();
			mRaw = exts.getString("raw");
			mAccessToken = exts.getString(TencentOpenHost.ACCESS_TOKEN);
			mExpireInfo = exts.getString(TencentOpenHost.EXPIRES_IN);
			mErrorReturn = exts.getString(TencentOpenHost.ERROR_RET);
			mErrorDes = exts.getString(TencentOpenHost.ERROR_DES);
			Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s",
					mRaw, mAccessToken, mExpireInfo));
			
			if(null !=mErrorReturn && !mErrorReturn.isEmpty())
			{
				//TODO: warning the error
				populateLogin(false); // relogin again!
			}
			else
			{
				unregisterIntentReceivers();
			}
		}

	}

}
