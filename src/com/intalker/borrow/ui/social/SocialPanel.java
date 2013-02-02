package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SocialPanel extends RelativeLayout {

	private RelativeLayout mMainLayout = null;
	private RelativeLayout mTopBanner = null;
	private RelativeLayout mBottomBanner = null;
	private HaloButton mFriendBtn = null;
	private HaloButton mMessageBtn = null;
	private HaloButton mMakeFriendsBtn = null;
	private HaloButton mReturnBtn = null;
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
		
		mTopBanner = new RelativeLayout(this.getContext());
		mTopBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams topBannerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		topBannerLP.height = LayoutUtil.getGalleryTopPanelHeight();
		mMainLayout.addView(mTopBanner, topBannerLP);
		
//		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(context,
//				LayoutUtil.getSocialPanelWidth(),
//				DensityAdaptor.getDensityIndependentValue(32)));
		createFriendsView();
		createUsersView();
		mUsersView.setVisibility(INVISIBLE);
		
		mBottomBanner = new RelativeLayout(this.getContext());
		mBottomBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams bottomBannerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomBannerLP.height = LayoutUtil.getGalleryBottomPanelHeight();
		bottomBannerLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mMainLayout.addView(mBottomBanner, bottomBannerLP);
		
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
		mTopBanner.addView(mFriendBtn);

		mMessageBtn = new HaloButton(this.getContext(), R.drawable.message);
		RelativeLayout.LayoutParams msgBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		msgBtnLP.rightMargin = largeMargin;
		msgBtnLP.topMargin = smallMargin;
		mMessageBtn.setLayoutParams(msgBtnLP);
		mTopBanner.addView(mMessageBtn);
		
		mMakeFriendsBtn = new HaloButton(this.getContext(), R.drawable.group);
		RelativeLayout.LayoutParams bottomCenterLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomCenterLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		mMakeFriendsBtn.setLayoutParams(bottomCenterLP);
		mBottomBanner.addView(mMakeFriendsBtn);
		
		mMakeFriendsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloudAPI.getAllUsers(v.getContext(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						switch (returnCode) {
						case CloudAPI.Return_OK:
							turnOnUsersView();
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
		
		mReturnBtn = new HaloButton(this.getContext(), R.drawable.back);
		mReturnBtn.setLayoutParams(bottomCenterLP);
		mBottomBanner.addView(mReturnBtn);
		
		mReturnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloudAPI.getFriends(v.getContext(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						switch (returnCode) {
						case CloudAPI.Return_OK:
							turnOffUsersView();
							mFriendsView.refreshList();
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

		mReturnBtn.setVisibility(GONE);
	}

	private void createFriendsView() {
		mFriendsView = new FriendListView(this.getContext());

		RelativeLayout.LayoutParams friendsViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		friendsViewLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
		friendsViewLP.bottomMargin = LayoutUtil.getGalleryBottomPanelHeight();

		mFriendsView.setLayoutParams(friendsViewLP);

		mMainLayout.addView(mFriendsView);
	}
	
	private void createUsersView() {
		mUsersView = new UsersView(this.getContext());

		RelativeLayout.LayoutParams usersViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		usersViewLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
		usersViewLP.bottomMargin = LayoutUtil.getGalleryBottomPanelHeight();

		mUsersView.setLayoutParams(usersViewLP);

		mMainLayout.addView(mUsersView);
	}

	public FriendListView getFriendsView() {
		return mFriendsView;
	}
	
	public UsersView getUsersView() {
		return mUsersView;
	}

	public void turnOnUsersView() {
		mUsersView.setVisibility(VISIBLE);
		mFriendsView.setVisibility(GONE);
		mMakeFriendsBtn.setVisibility(GONE);
		mReturnBtn.setVisibility(VISIBLE);
	}

	public void turnOffUsersView() {
		mUsersView.setVisibility(GONE);
		mFriendsView.setVisibility(VISIBLE);
		mMakeFriendsBtn.setVisibility(VISIBLE);
		mReturnBtn.setVisibility(GONE);
	}
}
