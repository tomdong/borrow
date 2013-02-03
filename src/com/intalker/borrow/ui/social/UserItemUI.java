package com.intalker.borrow.ui.social;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

public class UserItemUI extends RelativeLayout {

	private HaloButton mAvatarBtn = null;
	private HaloButton mFollowBtn = null;
	private TextView mNameTextView = null;
	private UserInfo mInfo = null;
	private boolean mAdded = false;

	public UserItemUI(Context context) {
		super(context);
		createUI();
	}
	
	public UserInfo getInfo() {
		return mInfo;
	}
	
	public void setInfo(UserInfo info) {
		mInfo = info;
		mNameTextView.setText(info.getDisplayName());
	}

	private void createUI() {
		mAvatarBtn = new HaloButton(this.getContext(), R.drawable.avatar_2);

		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int margin = DensityAdaptor.getDensityIndependentValue(2);
		int largeMargin = LayoutUtil.getLargeMargin();
		avatarLP.leftMargin = largeMargin;
		avatarLP.topMargin = margin;
		avatarLP.bottomMargin = margin;
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mAvatarBtn.setLayoutParams(avatarLP);
		this.addView(mAvatarBtn);
		
		mAvatarBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mNameTextView = new TextView(this.getContext());
		mNameTextView.setTextSize(16.0f);
		mNameTextView.setTextColor(Color.YELLOW);

		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = DensityAdaptor.getDensityIndependentValue(70);
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mNameTextView.setLayoutParams(nameTextLP);

		this.addView(mNameTextView);
		
		mFollowBtn = new HaloButton(this.getContext(), R.drawable.add);

		RelativeLayout.LayoutParams followBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		followBtnLP.rightMargin = largeMargin;
		followBtnLP.topMargin = margin;
		followBtnLP.bottomMargin = margin;
		followBtnLP.width = DensityAdaptor.getDensityIndependentValue(32);
		followBtnLP.height = DensityAdaptor.getDensityIndependentValue(32);
		followBtnLP.addRule(RelativeLayout.CENTER_VERTICAL);
		followBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mFollowBtn.setLayoutParams(followBtnLP);
		this.addView(mFollowBtn);
		
		mFollowBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAdded) {
					return;
				}
				CloudAPI.makeFriends(v.getContext(), mInfo.getId(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						switch (returnCode) {
						case CloudAPI.Return_OK:
							mFollowBtn.setVisibility(GONE);
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
	}

}