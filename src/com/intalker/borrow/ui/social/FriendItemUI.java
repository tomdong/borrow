package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendItemUI extends RelativeLayout {

	private ImageView mAvatar = null;
	private TextView mNameTextView = null;

	public FriendItemUI(Context context) {
		super(context);
		this.setBackgroundColor(ColorUtil.generateRandomColor());
		createUI();
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
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mAvatar.setLayoutParams(avatarLP);

		this.addView(mAvatar);

		mNameTextView = new TextView(this.getContext());
		mNameTextView.setText("test");
		mNameTextView.setTextColor(Color.YELLOW);

		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = DensityAdaptor.getDensityIndependentValue(80);
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mNameTextView.setLayoutParams(nameTextLP);

		this.addView(mNameTextView);
	}

}
