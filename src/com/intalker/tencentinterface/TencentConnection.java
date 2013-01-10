package com.intalker.tencentinterface;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.intalker.borrow.util.Debug;
import com.tencent.tauth.TAuthView;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.TencentOpenHost;
import com.tencent.tauth.TencentOpenRes;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.bean.UserInfo;
import com.tencent.tauth.bean.UserProfile;
import com.tencent.tauth.http.Callback;
import com.tencent.tauth.http.TDebug;

public class TencentConnection {
	// The parent activity must support following dialog!
	public static final int DIALOG_PROGRESS = 0;

	public static final int TENCENT_LOGIN_SUCC = 0;
	public static final int TENCENT_LOGIN_FAIL = 1;
	public static final int TENCENT_GETUSERINFO_SUCC = 2;
	public static final int TENCENT_GETUSERINFO_FAIL = 3;
	public static final int TENCENT_GETUSERPROFILE_SUCC = 4;
	public static final int TENCENT_GETUSERPROFILE_FAIL = 5;

	private static final String TAG = "Tencent";

	// The connection can only be used in an
	// activiy now and the activity must
	// implment some defined behavior. TODO: abstract
	private Activity mParent = null;

	private String mAppid = "100347785";// "222222";//

	private String mScope = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";// 授权范围
	private AuthReceiver mReceiver;

	private String mAccessToken, mOpenId, mErrorReturn, mErrorDes, mExpireInfo,
			mRaw;
	private UserInfo mUserInfo = null;
	private UserProfile mUserProfile= null;

	// TODO: add support for multiple handler!
	private ArrayList<Handler> mTencentEventHandlers = new ArrayList<Handler>();

	public TencentConnection(Activity parent) {
		mAccessToken = "";
		mOpenId = "";
		mErrorReturn = "";
		mErrorDes = "";
		mExpireInfo = "";
		mParent = parent;
		loadCacheToken();
	}
	public void regEventHandler(Handler h)
	{
		mTencentEventHandlers.add(h);
	}
	public void unRegEventHandler(Handler h)
	{
		mTencentEventHandlers.remove(h);
	}

	public boolean containsValidCacheToken() {
		if (mAccessToken == null || mAccessToken.length() == 0)
			return false;

		// TODO: Check the validation of the cache token
		return true;
	}

	public void populateLogin() {
		populateLogin(true);
	}

	public Drawable getLoginButton(int size) {
		Drawable loginImage = null;
		switch (size) {
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

	public Drawable getLogoutButton(int size) {
		Drawable logoutImage = null;
		switch (size) {
		case 0:
			logoutImage = TencentOpenRes.getLogoutBtn(mParent.getAssets()); // no
																			// big
																			// button
																			// now!
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
	public String getOpenId()
	{
		return mOpenId;
	}

	public void requestOpenId() {
		if (mOpenId != null && (mOpenId.length() > 0)) {
			sendMessage(TENCENT_LOGIN_SUCC);
			return;
		}

		TencentOpenAPI.openid(mAccessToken, new Callback() {

			public void onCancel(int flag) {
			}

			@Override
			public void onSuccess(final Object obj) {
				mOpenId = ((OpenId) obj).getOpenId();
				sendMessage(TENCENT_LOGIN_SUCC);
			}

			@Override
			public void onFail(int ret, final String msg) {
				Debug.toast(mParent, msg);
				mErrorReturn = msg;
				sendMessage(TENCENT_LOGIN_FAIL);
			}
		});
	}
	
	public UserInfo getUserInfo()
	{
		return mUserInfo;
	}
	
	public void requestUserInfo() {
		if (mOpenId == null || (mOpenId.length() == 0) || mAccessToken== null|| (mAccessToken.length() == 0)) {
			sendMessage(TENCENT_GETUSERINFO_FAIL);
			return;
		}

		TencentOpenAPI.userInfo(mAccessToken, mAppid, mOpenId, new Callback() {

			@Override
			public void onSuccess(final Object obj) {
				mUserInfo = (UserInfo) obj;
				sendMessage(TENCENT_GETUSERINFO_SUCC);
			}

			@Override
			public void onFail(int ret, final String msg) {
				Debug.toast(mParent, msg);
				mErrorReturn = msg;
				sendMessage(TENCENT_GETUSERINFO_FAIL);
			}
		});
	}
	
	public UserProfile getUserProfile()
	{
		return mUserProfile;
	}
	
	public void requestUserProfile() {
		if (mOpenId == null || (mOpenId.length() == 0) || mAccessToken== null|| (mAccessToken.length() == 0)) {
			sendMessage(TENCENT_GETUSERINFO_FAIL);
			return;
		}

		TencentOpenAPI.userProfile(mAccessToken, mAppid, mOpenId, new Callback() {

			@Override
			public void onSuccess(final Object obj) {
				mUserProfile = (UserProfile) obj;
				sendMessage(TENCENT_GETUSERINFO_SUCC);
			}

			@Override
			public void onFail(int ret, final String msg) {
				Debug.toast(mParent, msg);
				mErrorReturn = msg;
				sendMessage(TENCENT_GETUSERINFO_FAIL);
			}
		});
	}

	private void populateLogin(boolean reg) {
		auth();
		if (reg)
			registerIntentReceivers();
	}
	
	private void auth() {
//		TencentOpenAPI2.logIn(mParent.getApplicationContext(), mOpenId, mScope,
//		mAppid, "_self", null, null, null);
		
		
		Intent intent = new Intent(mParent.getApplicationContext(), com.tencent.tauth.TAuthView.class);
		
		intent.putExtra(TAuthView.CLIENT_ID, mAppid);
		intent.putExtra(TAuthView.SCOPE, mScope);
		intent.putExtra(TAuthView.TARGET, "_self");
		
		mParent.startActivity(intent);		
	}

	private void loadCacheToken() {
		// TODO:
	}

	private void registerIntentReceivers() {
		Log.i(TAG, "REG");
		mReceiver = new AuthReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(TAuthView.AUTH_BROADCAST);
		mParent.registerReceiver(mReceiver, filter);
	}

	private void unregisterIntentReceivers() {
		Log.i(TAG, "UNREG");
		mParent.unregisterReceiver(mReceiver);
	}

	private void sendMessage(int what) {
		for(Handler h: mTencentEventHandlers)
		{
			h.sendEmptyMessage(what);
		}
	}

	private class AuthReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle exts = intent.getExtras();
			mRaw = exts.getString("raw");
			mAccessToken = exts.getString(TAuthView.ACCESS_TOKEN);
			mExpireInfo = exts.getString(TAuthView.EXPIRES_IN);
			mErrorReturn = exts.getString(TAuthView.ERROR_RET);
			mErrorDes = exts.getString(TAuthView.ERROR_DES);
			Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s",
					mRaw, mAccessToken, mExpireInfo));

			if (null != mErrorReturn && (mErrorReturn.length() > 0)) {
				// TODO: warning the error
				populateLogin(false); // relogin again!
			} else {
				requestOpenId();
				unregisterIntentReceivers();
			}
		}

	}

}
