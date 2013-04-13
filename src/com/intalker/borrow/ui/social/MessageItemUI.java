package com.intalker.borrow.ui.social;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intalker.borrow.R;
import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

public class MessageItemUI extends RelativeLayout {

	private TextView mNameTextView = null;
	private TextView mMessageTextView = null;
	protected MessageInfo mMessageInfo = null;
	private int mNameViewId = 1;
	private String mSays = "";

	public MessageItemUI(Context context) {
		super(context);
		mSays = context.getString(R.string.says);
		createUI();
	}

	public void setInfo(MessageInfo info) {
		mMessageInfo = info;
		mNameTextView.setText(info.getHostName() + " " + mSays);
		mMessageTextView.setText(info.getMessage());
	}

	private void createUI() {
		int midMargin = LayoutUtil.getMediumMargin();
		
		mNameTextView = new TextView(this.getContext());
		mNameTextView.setId(mNameViewId);
		mNameTextView.setTextSize(UIConfig.getUserLabelTextSize());
		mNameTextView.setTextColor(UIConfig.getMessageUserNameTextColor());

		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.leftMargin = midMargin;
		nameTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mNameTextView.setLayoutParams(nameTextLP);
		this.addView(mNameTextView);
		
		mMessageTextView = new TextView(this.getContext());
		mMessageTextView.setTextSize(UIConfig.getUserLabelTextSize());
		mMessageTextView.setTextColor(UIConfig.getLightTextColor());

		RelativeLayout.LayoutParams msgTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgTextLP.leftMargin = midMargin;
		msgTextLP.addRule(RelativeLayout.RIGHT_OF, mNameViewId);
		msgTextLP.addRule(RelativeLayout.CENTER_VERTICAL);

		mMessageTextView.setLayoutParams(msgTextLP);
		this.addView(mMessageTextView);
	}
}
