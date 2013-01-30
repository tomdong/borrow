package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.data.FriendInfo;
import com.intalker.borrow.util.DensityAdaptor;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendItemUI extends RelativeLayout {

	private ImageView mAvatar = null;
	private TextView mNameTextView = null;
	private FriendInfo mInfo = null;

	public FriendItemUI(Context context) {
		super(context);
		createUI();
	}
	
	public FriendInfo getInfo() {
		return mInfo;
	}
	
	public void setInfo(FriendInfo info) {
		mInfo = info;
		mNameTextView.setText(info.getDisplayName());
	}

	private void createUI() {
		mAvatar = new ImageView(this.getContext());
		mAvatar.setImageResource(R.drawable.avatar_2);

		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int margin = DensityAdaptor.getDensityIndependentValue(2);
		avatarLP.leftMargin = margin;
		avatarLP.topMargin = margin;
		avatarLP.bottomMargin = margin;
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mAvatar.setLayoutParams(avatarLP);

		this.addView(mAvatar);

		mNameTextView = new TextView(this.getContext());
		mNameTextView.setTextSize(16.0f);
		mNameTextView.setTextColor(Color.YELLOW);

		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = DensityAdaptor.getDensityIndependentValue(40);
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mNameTextView.setLayoutParams(nameTextLP);

		this.addView(mNameTextView);
	}

}
