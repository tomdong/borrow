package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.util.DensityAdaptor;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class UsersDialog extends Dialog {

	private UsersView mUsersView = null;
	private FrameLayout mContentView = null;
	private RelativeLayout mMainLayout = null;
	
	public UsersDialog(Context context) {
		super(context, R.style.Theme_TransparentDialog);
		createUI(context);
	}

	private void createUI(Context context) {
		mContentView = new FrameLayout(context);
//		FrameLayout.LayoutParams mainlp = new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.WRAP_CONTENT,
//				FrameLayout.LayoutParams.WRAP_CONTENT);
//		mContentView.setLayoutParams(mainlp);
		this.setContentView(mContentView);
		
		mMainLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainLP.width = DensityAdaptor.getDensityIndependentValue(200);
		mainLP.height = DensityAdaptor.getDensityIndependentValue(300);
		mMainLayout.setLayoutParams(mainLP);
		mContentView.addView(mMainLayout);
		
		mUsersView = new UsersView(context);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.width = DensityAdaptor.getDensityIndependentValue(200);
		lp.height = DensityAdaptor.getDensityIndependentValue(300);
		mMainLayout.addView(mUsersView, lp);
	}
	
	@Override
	public void show() {
		mUsersView.refreshList(AppData.getInstance().getTempUsers());
		super.show();
	}
}
