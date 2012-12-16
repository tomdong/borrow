package com.intalker.borrow;

import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
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
	private static final String CALLBACK = "auth://tauth.qq.com/";
	private static final String mAppID = "100347785"; //"222222";
	
	private String scope = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";//ÊéàÊùÉËåÉÂõ¥
	private String mOpenID="";
	
	private LinearLayout mMainLayout = null;
	private LinearLayout mFriendsNavigationLayout = null;
	private LinearLayout mBooksNavigationLayout = null;
	
	private String mAccessToken;

	private Object mTencentOpenAPI;
	private BroadcastReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
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
		
		 //登录大按钮
		 loginBtn.setImageDrawable(TencentOpenRes.getBigLoginBtn(getAssets()));	
		 //登录中按钮 
		 //loginBtn.setImageDrawable(TencentOpenRes.getLoginBtn(getAssets()));
		 //登录小按钮
		 //loginBtn.setImageDrawable(TencentOpenRes.getSmallLoginBtn(getAssets()));
//		 
//		 ImageView logoutBtn = (ImageView) findViewById(R.id.logout);
//		 //退出中按钮
//		 loginBtn.setImageDrawable(TencentOpenRes.getLogoutBtn(getAssets()));
//		 //退出小按钮
//		 loginBtn.setImageDrawable(TencentOpenRes.getSmallLogoutBtn(getAssets()));
		 
		 loginBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TencentOpenAPI2.logIn(getApplicationContext(), mOpenID, scope, mAppID, "_self", CALLBACK, null, null);
					auth(mAppID,"_self");
				}
			});
		 
		 registerIntentReceivers();
		
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
	
	private void auth(String clientId, String target) 
	   {
	       Intent intent = new Intent(HomeActivity.this, com.tencent.tauth.TAuthView.class);	
	       intent.putExtra(TencentOpenHost.CLIENT_ID, clientId);	
	       intent.putExtra(TencentOpenHost.SCOPE, scope);	
	       intent.putExtra(TencentOpenHost.TARGET, target);	
	       intent.putExtra(TencentOpenHost.CALLBACK, CALLBACK);			
	       startActivity(intent);		
	   }
	
	 public class AuthReceiver extends BroadcastReceiver 
	   {
	      private static final String TAG="AuthReceiver";
	      @Override
	      public void onReceive(Context context, Intent intent)
	      {
	         Bundle exts = intent.getExtras();
	         String raw =  exts.getString("raw");
	         String access_token =  exts.getString("access_token");
	         String expires_in =  exts.getString("expires_in");
	         Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s", raw, access_token, expires_in));
	         if (access_token != null) 
	         {
	          //获取到access token
	          mAccessToken = access_token;
	         // ((TextView)findViewById(R.id.access_token)).setText(access_token);
	          TDebug.msg("正在获取OpenID...", getApplicationContext());
	          //用access token 来获取open id
	          TencentOpenAPI.openid(access_token, new Callback() {
	        	  @Override
	        	  public void onSuccess(final Object obj) 
	        	  {
	        	     runOnUiThread(new Runnable() 
	        	     {
	        	         @Override
	        	         public void run() 
	        	         {
	        	              //setOpenIdText(((OpenId)obj).getOpenId());
	        	         }
	        	      });
	        	   }

	        	   @Override
	        	   public void onFail(int ret, final String msg)
	        	   {
	        	      runOnUiThread(new Runnable() 
	        	      {
	        	         @Override
	        	         public void run() 
	        	         {
	        	            TDebug.msg(msg, getApplicationContext());
	        	          }
	        	       });
	        	    }

	        	    @Override
	        	    public void onCancel(int flag)
	        	    {
	        	    }

	        	});
	         }
	      }
	   }
	 private void registerIntentReceivers() {
			receiver = new AuthReceiver();
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(TencentOpenHost.AUTH_BROADCAST);
			registerReceiver(receiver, filter);
		}
		
		private void unregisterIntentReceivers() {
			unregisterReceiver(receiver);
		}
}
