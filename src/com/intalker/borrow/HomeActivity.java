package com.intalker.borrow;

import com.intalker.borrow.friends.FriendsNavigationVertical;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.internetUtil;
import com.intalker.tencentinterface.TencentConnection;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.tencent.tauth.TencentOpenRes;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;
import com.tencent.tauth.http.TDebug;
import com.tencent.tauth.http.RequestListenerImpl.OpenIDListener;

import android.R.color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private LinearLayout mMainLayout = null;
	
	private LinearLayout mBooksNavigationLayout = null;
	private FriendsNavigationVertical mFriendsNav = null;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
		mFriendsNav = new FriendsNavigationVertical(this);
		createHomeUI();
		setContentView(mMainLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	private void createHomeUI() {
		mMainLayout = new LinearLayout(this);
		mMainLayout.setOrientation(LinearLayout.HORIZONTAL);
		mMainLayout.setBackgroundColor(Color.GRAY);

		mMainLayout.addView(mFriendsNav.createFriendsNavigationUI());
		mMainLayout.addView(createBooksNavigation());
	}

	

	private View createBooksNavigation() {
		LinearLayout navigationBar = new LinearLayout(this);
		navigationBar.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		navigationBar.setLayoutParams(navigationBarLP);
		navigationBar.setBackgroundColor(Color.DKGRAY);

		return navigationBar;
	}
}
