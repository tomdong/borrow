package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookOwnersDialog extends Dialog {

	private UsersView mUsersView = null;
	private FrameLayout mContentView = null;
	private RelativeLayout mMainLayout = null;
	private TextView mTitle = null;
	private int mainLayoutId = 1;
	
	public BookOwnersDialog(Context context) {
		super(context, R.style.Theme_TransparentDialog);
		createUI(context);
	}

	private void createUI(Context context) {
		mContentView = new FrameLayout(context);
		this.setContentView(mContentView);
		
		mMainLayout = new RelativeLayout(context);
		mMainLayout.setId(mainLayoutId);
		mMainLayout.setBackgroundResource(R.drawable.login_bg);
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainLP.width = DensityAdaptor.getDensityIndependentValue(200);
		mainLP.height = DensityAdaptor.getDensityIndependentValue(300);
		mMainLayout.setLayoutParams(mainLP);
		mContentView.addView(mMainLayout);
		
		mUsersView = new UsersView(context);
		RelativeLayout.LayoutParams usersViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		usersViewLP.topMargin = DensityAdaptor.getDensityIndependentValue(40);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, mainLayoutId);
		mMainLayout.addView(mUsersView, usersViewLP);
		
		mTitle = new TextView(context);
		mTitle.setTextColor(UIConfig.getLightTextColor());
		mTitle.setTextSize(UIConfig.getTitleTextSize());
		mTitle.setText(R.string.users_ownthisbook);
		RelativeLayout.LayoutParams titleLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		titleLP.topMargin = LayoutUtil.getSmallMargin();
		titleLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		titleLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mMainLayout.addView(mTitle, titleLP);
	}
	
	@Override
	public void show() {
		mUsersView.fillWithBookOwnersData(AppData.getInstance().getTempUsers());
		super.show();
	}
}
