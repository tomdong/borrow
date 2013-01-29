package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

public class SocialPanel extends RelativeLayout {

	private RelativeLayout mMainLayout = null;
	private HaloButton mFriendBtn = null;
	private HaloButton mMessageBtn = null;
	private FriendListView mFriendView = null;

	public SocialPanel(Context context) {
		super(context);
		mMainLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		mainLP.width = LayoutUtil.getSocialPanelWidth();
		mMainLayout.setLayoutParams(mainLP);
		mMainLayout.setBackgroundResource(R.drawable.stone_bg);

		this.addView(mMainLayout);

		createFriendView();
		createButtons();
	}

	private void createButtons() {
		int smallMargin = LayoutUtil.getSmallMargin();
		int largeMargin = LayoutUtil.getLargeMargin();
		mFriendBtn = new HaloButton(this.getContext(), R.drawable.friend);
		RelativeLayout.LayoutParams friendBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		friendBtnLP.leftMargin = largeMargin;
		friendBtnLP.topMargin = smallMargin;
		mFriendBtn.setLayoutParams(friendBtnLP);
		mMainLayout.addView(mFriendBtn);

		mMessageBtn = new HaloButton(this.getContext(), R.drawable.message);
		RelativeLayout.LayoutParams msgBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		msgBtnLP.rightMargin = largeMargin;
		msgBtnLP.topMargin = smallMargin;
		mMessageBtn.setLayoutParams(msgBtnLP);
		mMainLayout.addView(mMessageBtn);
	}

	private void createFriendView() {
		mFriendView = new FriendListView(this.getContext());

		RelativeLayout.LayoutParams friendViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		friendViewLP.leftMargin = DensityAdaptor.getDensityIndependentValue(5);
		friendViewLP.rightMargin = DensityAdaptor.getDensityIndependentValue(5);
		friendViewLP.topMargin = DensityAdaptor.getDensityIndependentValue(36);
		friendViewLP.bottomMargin = DensityAdaptor
				.getDensityIndependentValue(5);

		mFriendView.setLayoutParams(friendViewLP);

		mMainLayout.addView(mFriendView);
	}

	public FriendListView getFriendView() {
		return mFriendView;
	}

}
