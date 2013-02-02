package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class SocialPanel extends RelativeLayout {

	private RelativeLayout mMainLayout = null;
	private HaloButton mFriendBtn = null;
	private HaloButton mMessageBtn = null;
	private FriendListView mFriendsView = null;
	private UsersView mUsersView = null;

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
		createButtons();
		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(context,
				LayoutUtil.getSocialPanelWidth(),
				DensityAdaptor.getDensityIndependentValue(32)));
		createFriendsView();
		createUsersView();
		mUsersView.setVisibility(INVISIBLE);
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
		mFriendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloudAPI.getAllUsers(v.getContext(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						switch (returnCode) {
						case CloudAPI.Return_OK:
							mUsersView.setVisibility(VISIBLE);
							mFriendsView.setVisibility(GONE);
							mUsersView.refreshList();
							break;
						case CloudAPI.Return_BadToken:
							Toast.makeText(HomeActivity.getApp(), "Bad token.",
									Toast.LENGTH_SHORT).show();
							break;
						case CloudAPI.Return_NetworkError:
							Toast.makeText(HomeActivity.getApp(), "Network error.",
									Toast.LENGTH_SHORT).show();
							break;
						default:
							Toast.makeText(HomeActivity.getApp(), "Unknown error.",
									Toast.LENGTH_SHORT).show();
							break;
						}
					}
					
				});
			}
		});

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

	private void createFriendsView() {
		mFriendsView = new FriendListView(this.getContext());

		RelativeLayout.LayoutParams friendsViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		friendsViewLP.leftMargin = DensityAdaptor.getDensityIndependentValue(5);
		friendsViewLP.rightMargin = DensityAdaptor.getDensityIndependentValue(5);
		friendsViewLP.topMargin = DensityAdaptor.getDensityIndependentValue(36);
		friendsViewLP.bottomMargin = DensityAdaptor
				.getDensityIndependentValue(5);

		mFriendsView.setLayoutParams(friendsViewLP);

		mMainLayout.addView(mFriendsView);
	}
	
	private void createUsersView() {
		mUsersView = new UsersView(this.getContext());

		RelativeLayout.LayoutParams usersViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		usersViewLP.leftMargin = DensityAdaptor.getDensityIndependentValue(5);
		usersViewLP.rightMargin = DensityAdaptor.getDensityIndependentValue(5);
		usersViewLP.topMargin = DensityAdaptor.getDensityIndependentValue(36);
		usersViewLP.bottomMargin = DensityAdaptor
				.getDensityIndependentValue(5);

		mUsersView.setLayoutParams(usersViewLP);

		mMainLayout.addView(mUsersView);
	}

	public FriendListView getFriendsView() {
		return mFriendsView;
	}
	
	public UsersView getUsersView() {
		return mUsersView;
	}

}
