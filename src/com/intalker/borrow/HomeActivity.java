package com.intalker.borrow;

import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
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
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
	private LinearLayout mFriendsNavigationLayout = null;
	private LinearLayout mBooksNavigationLayout = null;
	private TencentConnection mTencentConnection= null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
		mTencentConnection = new TencentConnection(this);
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
		
		mMainLayout.addView(createFriendsNavigation());
		mMainLayout.addView(createBooksNavigation());
	}
	
	private View createFriendsNavigation()
	{
		LinearLayout navigationBar = new LinearLayout(this);
		navigationBar.setOrientation(LinearLayout.VERTICAL);	
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		navigationBar.setLayoutParams(navigationBarLP);
		navigationBar.setBackgroundColor(Color.DKGRAY);
		
		navigationBar.addView(createLoginButton());

		for (int i = 0; i < 10; ++i) {
			navigationBar.addView(createTestFriendItemUI());
		}
		
		return navigationBar;
	}
	
	private View createLoginButton()
	{
		ImageView loginBtn = new ImageView(this);
		//loginBtn.setText("Login");
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		loginBtn.setLayoutParams(navigationBarLP);
		
		 loginBtn.setImageDrawable(mTencentConnection.getLoginButton(0));	
		 loginBtn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					mTencentConnection.populateLogin();
				}
			});		
		return loginBtn;
	}
	
	private View createBooksNavigation()
	{
		LinearLayout navigationBar = new LinearLayout(this);
		navigationBar.setOrientation(LinearLayout.VERTICAL);	
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		navigationBar.setLayoutParams(navigationBarLP);
		navigationBar.setBackgroundColor(Color.DKGRAY);
		
		return navigationBar;
	}

	private View createTestFriendItemUI() {
//		RelativeLayout item = new RelativeLayout(this);
//
//		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		itemLP.width = DensityAdaptor.getDensityIndependentValue(240);
//		itemLP.height = DensityAdaptor.getDensityIndependentValue(64);
//		
//		item.setBackgroundColor(ColorUtil.generateRandomColor());
//		item.setLayoutParams(itemLP);

	
		
		ImageView avatar = new ImageView(this);
		avatar.setImageResource(R.drawable.avatar);
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(48);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(48);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		avatarLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		avatarLP.rightMargin = DensityAdaptor.getDensityIndependentValue(8);
		avatar.setLayoutParams(avatarLP);
		return avatar;
	}
}
