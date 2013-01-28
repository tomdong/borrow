package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
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
		mMainLayout.setBackgroundColor(Color.BLUE);

		this.addView(mMainLayout);

		createFriendView();
		createButtons();
	}

	private void createButtons() {
		int margin = DensityAdaptor.getDensityIndependentValue(5);
		mFriendBtn = new HaloButton(this.getContext(), R.drawable.cloud);
		mFriendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CloudAPI.getFriends(v.getContext(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						mFriendView.refreshList();
					}
					
				});
			}
		});
		RelativeLayout.LayoutParams friendBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		friendBtnLP.leftMargin = margin;
		friendBtnLP.topMargin = margin;
		mFriendBtn.setLayoutParams(friendBtnLP);
		mMainLayout.addView(mFriendBtn);

		mMessageBtn = new HaloButton(this.getContext(), R.drawable.cloud);
		RelativeLayout.LayoutParams msgBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		msgBtnLP.rightMargin = margin;
		msgBtnLP.topMargin = margin;
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
