package com.intalker.borrow.ui.social;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

public class IncomeMessagesDialog extends Dialog {

	private MessagesView mMessagesView = null;
	private FrameLayout mContentView = null;
	private RelativeLayout mMainLayout = null;
	private TextView mTitle = null;
	private int mainLayoutId = 1;
	
	private ArrayList<MessageInfo> mMessages = null;
	
	public IncomeMessagesDialog(Context context) {
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
		mainLP.width = LayoutUtil.getDetailDialogWidth();
		mainLP.height = DensityAdaptor.getDensityIndependentValue(300);
		mMainLayout.setLayoutParams(mainLP);
		mContentView.addView(mMainLayout);
		
		mMessagesView = new MessagesView(context);
		RelativeLayout.LayoutParams usersViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		usersViewLP.topMargin = DensityAdaptor.getDensityIndependentValue(40);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, mainLayoutId);
		usersViewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, mainLayoutId);
		mMainLayout.addView(mMessagesView, usersViewLP);
		
		mTitle = new TextView(context);
		mTitle.setTextColor(UIConfig.getLightTextColor());
		mTitle.setTextSize(UIConfig.getTitleTextSize());
		mTitle.setText(R.string.message_fromfriends);
		RelativeLayout.LayoutParams titleLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		titleLP.topMargin = LayoutUtil.getSmallMargin();
		titleLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		titleLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mMainLayout.addView(mTitle, titleLP);
	}
	
	public void setMessages(ArrayList<MessageInfo> messages) {
		mMessages = messages;
	}
	
	@Override
	public void show() {
		mMessagesView.fillWithIncomeMessages(mMessages);
		super.show();
	}
}