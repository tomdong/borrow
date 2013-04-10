package com.intalker.borrow.ui.social;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.intalker.borrow.R;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

public class UserItemUI extends RelativeLayout {

	private HaloButton mAvatarBtn = null;
	private TextView mNameTextView = null;
	protected HaloButton mActionBtn = null;
	protected UserInfo mInfo = null;
	protected int mActionIconResId = 0;

	public UserItemUI(Context context, int iconResId) {
		super(context);
		mActionIconResId = iconResId;
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
		mNameTextView.setTextSize(UIConfig.getUserLabelTextSize());
		mNameTextView.setTextColor(UIConfig.getLightTextColor());

		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = DensityAdaptor.getDensityIndependentValue(70);
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mNameTextView.setLayoutParams(nameTextLP);

		this.addView(mNameTextView);

		mActionBtn = new HaloButton(this.getContext(), mActionIconResId);

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
		mActionBtn.setLayoutParams(followBtnLP);
		this.addView(mActionBtn);

		mActionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onActionButtonClick();
			}
		});
	}

	protected void onActionButtonClick() {
	}
}
