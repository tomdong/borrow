package com.intalker.borrow.friends;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.tencentinterface.TencentConnection;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendsNavigationVertical {
	private Activity mParentOwner = null;
	private LinearLayout mFriendsNavigationLayout = null;
	private LinearLayout mSelfNavigationLayout = null;	
	
	private ImageView mLoginBtn = null;
	private LinearLayout mSelfInfoPanel = null;
	private ImageView mProfileImg = null;
	private TextView mProfileNick = null;
	
	private TencentConnection mTencentConnection = null;
	private TencentEventHandler mTencentHandler = null;
	
	public FriendsNavigationVertical(Activity parent)
	{
		mParentOwner = parent;
		
		mTencentConnection = new TencentConnection(mParentOwner);
		mTencentHandler = new TencentEventHandler();
		mTencentConnection.regEventHandler(mTencentHandler);
	}
	
	public View createFriendsNavigationUI() {
		mFriendsNavigationLayout = new LinearLayout(mParentOwner);
		mFriendsNavigationLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mFriendsNavigationLayout.setLayoutParams(navigationBarLP);
		mFriendsNavigationLayout.setBackgroundColor(Color.DKGRAY);

		mFriendsNavigationLayout.addView(createSelfNavigation());

		for (int i = 0; i < 10; ++i) {
			mFriendsNavigationLayout.addView(createTestFriendItemUI());
		}

		return mFriendsNavigationLayout;
	}

	private View createSelfNavigation() {
		mSelfNavigationLayout = new LinearLayout(mParentOwner);
		mSelfNavigationLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mSelfNavigationLayout.setLayoutParams(navigationBarLP);
		mSelfNavigationLayout.setBackgroundColor(Color.DKGRAY);

		if (!mTencentConnection.containsValidCacheToken())
			mSelfNavigationLayout.addView(createLoginButton());
		else
			mSelfNavigationLayout.addView(createSelfPanel());

		return mSelfNavigationLayout;
	}

	private View createLoginButton() {
		mLoginBtn = new ImageView(mParentOwner);
		// loginBtn.setText("Login");
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mLoginBtn.setLayoutParams(navigationBarLP);

		mLoginBtn.setImageDrawable(mTencentConnection.getLoginButton(0));
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTencentConnection.populateLogin();
			}
		});
		return mLoginBtn;
	}

	private View createSelfPanel() {
		mSelfInfoPanel = new LinearLayout(mParentOwner);
		mSelfInfoPanel.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mSelfInfoPanel.setLayoutParams(navigationBarLP);
		mSelfInfoPanel.setBackgroundColor(Color.DKGRAY);

		mSelfInfoPanel.addView(createProfileImg());
		mSelfInfoPanel.addView(createProfileNick());

		return mSelfInfoPanel;
	}

	private View createProfileImg() {
		mProfileImg = new ImageView(mParentOwner);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mProfileImg.setLayoutParams(navigationBarLP);

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				mProfileImg.setImageBitmap(internetUtil.getBitmapFromURLContent(mTencentConnection.getUserInfo().getIcon_100()));
//			}
//		}).start();

		mProfileImg.setImageResource(R.drawable.ic_launcher);
		return mProfileImg;
	}

	private View createProfileNick() {
		mProfileNick = new TextView(mParentOwner);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mProfileNick.setLayoutParams(navigationBarLP);

		mProfileNick.setText(mTencentConnection.getUserInfo().getNickName());
		// mProfileNick.setText(mTencentConnection.getUserProfile().getRealName());
		return mProfileNick;
	}
	
	private View createTestFriendItemUI() {
		// RelativeLayout item = new RelativeLayout(this);
		//
		// LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// itemLP.width = DensityAdaptor.getDensityIndependentValue(240);
		// itemLP.height = DensityAdaptor.getDensityIndependentValue(64);
		//
		// item.setBackgroundColor(ColorUtil.generateRandomColor());
		// item.setLayoutParams(itemLP);

		ImageView avatar = new ImageView(mParentOwner);
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
	
	private class TencentEventHandler extends Handler {
		// @override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TencentConnection.TENCENT_LOGIN_SUCC:
				mTencentConnection.requestUserInfo();
				// mTencentConnection.requestUserProfile();
				break;
			case TencentConnection.TENCENT_LOGIN_FAIL:
				break;
			case TencentConnection.TENCENT_GETUSERINFO_SUCC:
				mSelfNavigationLayout.removeView(mLoginBtn);
				mSelfNavigationLayout.addView(createSelfPanel());
				break;
			case TencentConnection.TENCENT_GETUSERINFO_FAIL:
				break;
			case TencentConnection.TENCENT_GETUSERPROFILE_SUCC:
				mSelfNavigationLayout.removeView(mLoginBtn);
				mSelfNavigationLayout.addView(createSelfPanel());
				break;
			case TencentConnection.TENCENT_GETUSERPROFILE_FAIL:
				break;
			default:
				break;
			}

		}
	}

}
