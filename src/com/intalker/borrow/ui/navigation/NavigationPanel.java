package com.intalker.borrow.ui.navigation;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.cloud.CloudUtility;
import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.notification.NotificationManager;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.ui.control.SquareButton;
import com.intalker.borrow.ui.login.LoginDialog;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class NavigationPanel extends RelativeLayout {

	private Context mContext = null;
	private RelativeLayout mTopBanner = null;
	private RelativeLayout mBottomBanner = null;
	private HaloButton mSignUpBtn = null;
	private HaloButton mLoginBtn = null;
	private HaloButton mLogoutBtn = null;

	private HaloButton mAvatarBtn = null;
	private TextView mNameTextView = null;
	
	private SquareButton mSearchBtn = null;
	private SquareButton mGalleryBtn = null;
	private SquareButton mMessageBtn = null;
	private SquareButton mHelpBtn = null;

	public NavigationPanel(Context context) {
		super(context);
		mContext = context;
		// createUI_OldStyle();
		createUI(context);
		addListeners();
	}

	private void createUI(Context context) {
//		RelativeLayout.LayoutParams navigationBarLP = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//		this.setLayoutParams(navigationBarLP);
		this.setBackgroundResource(R.drawable.stone_bg);

		createTopBanner(context);
		createBottomBanner(context);
		createButtons(context);
	}

	private void createTopBanner(Context context) {
		int smallMargin = LayoutUtil.getSmallMargin();
		int midMargin = LayoutUtil.getMediumMargin();
		
		mTopBanner = new RelativeLayout(context);
		mTopBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams topViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		topViewLP.width = LayoutUtil.getNavigationPanelWidth();
		topViewLP.height = LayoutUtil.getGalleryTopPanelHeight();
		mTopBanner.setLayoutParams(topViewLP);
		this.addView(mTopBanner, topViewLP);
		
		mAvatarBtn = new HaloButton(context, R.drawable.avatar_2);
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.leftMargin = smallMargin;
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mAvatarBtn.setLayoutParams(avatarLP);
		mTopBanner.addView(mAvatarBtn);
		
		mNameTextView = new TextView(context);
		mNameTextView.setText("Tom");
		mNameTextView.setTextSize(UIConfig.getUserLabelTextSize());
		mNameTextView.setTextColor(UIConfig.getLightTextColor());
		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = DensityAdaptor.getDensityIndependentValue(36);
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mNameTextView.setLayoutParams(nameTextLP);
		mTopBanner.addView(mNameTextView);
		
		
		View separator = ControlFactory
				.createVertSeparatorForRelativeLayout(context);
		RelativeLayout.LayoutParams separatorLP = (LayoutParams) separator
				.getLayoutParams();
		separatorLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		separatorLP.rightMargin = DensityAdaptor.getDensityIndependentValue(48);
		separator.setLayoutParams(separatorLP);
		mTopBanner.addView(separator);
		
		mLogoutBtn = new HaloButton(context, R.drawable.logout);
		RelativeLayout.LayoutParams logoutLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		logoutLP.rightMargin = midMargin;
		logoutLP.width = DensityAdaptor.getDensityIndependentValue(32);
		logoutLP.height = DensityAdaptor.getDensityIndependentValue(32);
		logoutLP.addRule(RelativeLayout.CENTER_VERTICAL);
		logoutLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mLogoutBtn.setLayoutParams(logoutLP);
		mTopBanner.addView(mLogoutBtn);
		
		mLoginBtn = new HaloButton(context, R.drawable.login);
		RelativeLayout.LayoutParams loginLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		loginLP.rightMargin = midMargin;
		loginLP.width = DensityAdaptor.getDensityIndependentValue(32);
		loginLP.height = DensityAdaptor.getDensityIndependentValue(32);
		loginLP.addRule(RelativeLayout.CENTER_VERTICAL);
		loginLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mLoginBtn.setLayoutParams(loginLP);
		mTopBanner.addView(mLoginBtn);
	}
	
	public void updateLoginStatus() {
		UserInfo userInfo = UserInfo.getCurLoggedinUser();
		if (null == userInfo) {
			mNameTextView.setText(R.string.app_name);
			HomeActivity app = HomeActivity.getApp();
			app.getBookGallery().clearLoggedinStatus();
			app.getSocialPanel().getFriendsView().clear();
			app.getSocialPanel().getUsersView().clear();
			
			mLoginBtn.setVisibility(VISIBLE);
			mLogoutBtn.setVisibility(GONE);
		} else {
			mNameTextView.setText(userInfo.getDisplayName());
			mLoginBtn.setVisibility(GONE);
			mLogoutBtn.setVisibility(VISIBLE);
		}
	}
	
	private void addListeners() {
		mLogoutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CloudAPI.logout();
				updateLoginStatus();
			}
		});
		
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginDialog loginDialog = new LoginDialog(v.getContext());
				loginDialog.show();
			}
		});
		
		mSearchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				NotificationManager.getInstance().markDirty();
//				NotificationManager.getInstance().fire();
			}
		});
	}

	private void createBottomBanner(Context context) {
		mBottomBanner = new RelativeLayout(this.getContext());
		mBottomBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams bottomBannerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomBannerLP.width = LayoutUtil.getNavigationPanelWidth();
		bottomBannerLP.height = LayoutUtil.getGalleryBottomPanelHeight();
		bottomBannerLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(mBottomBanner, bottomBannerLP);
	}

	private void createUI_OldStyle() {
		int panelWidth = LayoutUtil.getNavigationPanelWidth();
		this.setBackgroundResource(R.drawable.stone_narrow_bg);
		RelativeLayout.LayoutParams navigationBarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		navigationBarLP.width = LayoutUtil.getNavigationPanelWidth();

		this.setLayoutParams(navigationBarLP);

		View logoView = new View(mContext);
		logoView.setBackgroundResource(R.drawable.logo);
		LinearLayout.LayoutParams logoLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		logoLP.width = LayoutUtil.getNavigationPanelWidth();
		logoLP.height = LayoutUtil.getNavigationPanelWidth();

		this.addView(logoView, logoLP);

		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(
				mContext, panelWidth, logoLP.height));

		// Create buttons
		// Sign up
		mSignUpBtn = new HaloButton(mContext, R.drawable.register);
		mSignUpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeActivity.getApp().toggleSignUpPanel(true);
			}
		});

		RelativeLayout.LayoutParams regBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		int y = logoLP.height + DensityAdaptor.getDensityIndependentValue(10);
		int buttonGap = DensityAdaptor.getDensityIndependentValue(34);
		regBtnLP.topMargin = y;
		regBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(mSignUpBtn, regBtnLP);

		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(
				mContext, panelWidth, y + regBtnLP.height + buttonGap));

		// Login
		mLoginBtn = new HaloButton(mContext, R.drawable.login);
		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginDialog loginDialog = new LoginDialog(v.getContext());
				loginDialog.show();
			}
		});
		RelativeLayout.LayoutParams loginBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		y += buttonGap + DensityAdaptor.getDensityIndependentValue(8);
		loginBtnLP.topMargin = y;
		loginBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(mLoginBtn, loginBtnLP);

		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(
				mContext, panelWidth, y + loginBtnLP.height + buttonGap));

		if (AppConfig.isDebugMode) {
			HaloButton testTokenBtn = new HaloButton(mContext, R.drawable.test);
			testTokenBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String testToken = "7a3cf000-0662-715b-a6a8-89feb8466014";
					CloudUtility.setAccessToken(testToken);
					CloudAPI.getLoggedInUserInfo(v.getContext(),
							new ICloudAPITaskListener() {

								@Override
								public void onFinish(int returnCode) {
									doAfterGetUserInfoByToken(returnCode);
								}

							});
				}
			});
			RelativeLayout.LayoutParams testTokenBtnLP = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			y += buttonGap + DensityAdaptor.getDensityIndependentValue(8);
			testTokenBtnLP.topMargin = y;
			testTokenBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
			this.addView(testTokenBtn, testTokenBtnLP);

			this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(
					mContext, panelWidth, y + loginBtnLP.height + buttonGap));
		}
	}
	
	private void createButtons(Context context) {
		int panelWidth = LayoutUtil.getNavigationPanelWidth();
		int smallMargin = DensityAdaptor.getDensityIndependentValue(1);
		int buttonSideLength = (panelWidth - smallMargin * 3) / 2;
		int topOffset = LayoutUtil.getGalleryTopPanelHeight();
		
//		ImageView crossSeparator = new ImageView(context);
//		crossSeparator.setImageResource(R.drawable.cross_separator);
//		LayoutParams crossSeparatorLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		crossSeparatorLP.leftMargin = (panelWidth - DensityAdaptor.getDensityIndependentValue(100)) / 2;
//		crossSeparatorLP.topMargin = DensityAdaptor.getDensityIndependentValue(120);
//		crossSeparator.setLayoutParams(crossSeparatorLP);
//		this.addView(crossSeparator);
		
		mSearchBtn = new SquareButton(context, R.drawable.search_140);
		RelativeLayout.LayoutParams searchBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		searchBtnLP.leftMargin = smallMargin;
		searchBtnLP.topMargin = topOffset + smallMargin;
		searchBtnLP.width = searchBtnLP.height = buttonSideLength;
		mSearchBtn.setLayoutParams(searchBtnLP);
		this.addView(mSearchBtn);
		
		mGalleryBtn = new SquareButton(context, R.drawable.gallery_browse_140);
		RelativeLayout.LayoutParams galleryBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		galleryBtnLP.leftMargin = smallMargin * 2 + buttonSideLength;
		galleryBtnLP.topMargin = searchBtnLP.topMargin;
		galleryBtnLP.width = galleryBtnLP.height = buttonSideLength;
		mGalleryBtn.setLayoutParams(galleryBtnLP);
		this.addView(mGalleryBtn);
		
		mMessageBtn = new SquareButton(context, R.drawable.message_140);
		RelativeLayout.LayoutParams messageBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		messageBtnLP.leftMargin = searchBtnLP.leftMargin;
		messageBtnLP.topMargin = topOffset + smallMargin * 2 + buttonSideLength;
		messageBtnLP.width = messageBtnLP.height = buttonSideLength;
		mMessageBtn.setLayoutParams(messageBtnLP);
		this.addView(mMessageBtn);
		
		mHelpBtn = new SquareButton(context, R.drawable.question_140);
		RelativeLayout.LayoutParams helpBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		helpBtnLP.leftMargin = galleryBtnLP.leftMargin;
		helpBtnLP.topMargin = messageBtnLP.topMargin;
		helpBtnLP.width = helpBtnLP.height = buttonSideLength;
		mHelpBtn.setLayoutParams(helpBtnLP);
		this.addView(mHelpBtn);
		
		// Sign up
		mSignUpBtn = new HaloButton(mContext, R.drawable.register);
		mSignUpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeActivity.getApp().toggleSignUpPanel(true);
			}
		});

		RelativeLayout.LayoutParams regBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		regBtnLP.topMargin = DensityAdaptor.getDensityIndependentValue(150);
		regBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(mSignUpBtn, regBtnLP);
	}
	
	private void doAfterGetUserInfoByToken(int returnCode) {
		if (CloudAPI.isSuccessful(mContext, returnCode)) {
			HomeActivity.getApp().getBookGallery()
					.updatePanels(UserInfo.getCurLoggedinUser());
			HomeActivity.getApp().getSocialPanel().getFriendsView()
					.refreshList();
		}
	}

}
