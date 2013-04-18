package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BigUserView extends RelativeLayout {
	private HaloButton mUserAvatar = null;
	private TextView mUserNameTextView = null;
	private int mAvatarId = 1;
	public BigUserView(Context context) {
		super(context);
		createUI(context);
	}
	
	private void createUI(Context context) {
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(mainLP);
		
		int smallMargin = LayoutUtil.getSmallMargin();
		
		mUserAvatar = new HaloButton(context, R.drawable.avatar_2);
		mUserAvatar.setId(mAvatarId);
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.width = LayoutUtil.getBigUserViewItemWidth();
		avatarLP.height = LayoutUtil.getBigUserViewItemHeight();
		mUserAvatar.setLayoutParams(avatarLP);
		this.addView(mUserAvatar);
		
		mUserNameTextView = new TextView(context);
		mUserNameTextView.setText("User Name");
		mUserNameTextView.setTextColor(UIConfig.getTipTextColor());
		RelativeLayout.LayoutParams nameLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameLP.addRule(RelativeLayout.BELOW, mAvatarId);
		nameLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		nameLP.topMargin = smallMargin;
		mUserNameTextView.setLayoutParams(nameLP);
		
		this.addView(mUserNameTextView);
	}
	
	public void setUserName(String userName) {
		mUserNameTextView.setText(userName);
	}

}
