package com.intalker.borrow.ui.social;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intalker.borrow.R;
import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

public class MessageItemUI extends RelativeLayout {

	private TextView mNameTextView = null;
	private TextView mMessageTextView = null;
	protected MessageInfo mMessageInfo = null;
	private int mNameViewId = 1;
	private String mSaid = "";
	private String mDontReplyToYourSelf = "";
	private MessageItemUI mInstance = null;
	private boolean mIsMine = false;

	public MessageItemUI(Context context) {
		super(context);
		mInstance = this;
		mSaid = context.getString(R.string.said);
		mDontReplyToYourSelf = context.getString(R.string.dont_reply_to_yourself);
		createUI();
		addListeners();
	}

	public void setInfo(MessageInfo info) {
		mIsMine = info.getHostId().compareTo(UserInfo.getCurUserId()) == 0;
		mMessageInfo = info;
		String displayName = "";
		if (mIsMine) {
			displayName = this.getContext().getString(R.string.i);
		} else {
			displayName = info.getHostName();
		}
		mNameTextView.setText(displayName + " " + mSaid);
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
	
	private void addListeners()
	{
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mIsMine) {
					Toast.makeText(v.getContext(), mDontReplyToYourSelf, Toast.LENGTH_SHORT).show();
					return;
				}
				MessageDetailDialog dlg = new MessageDetailDialog(v.getContext(), mMessageInfo);
				dlg.show();
			}
		});
		
		this.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					mInstance.setBackgroundColor(Color.LTGRAY);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
					mInstance.setBackgroundColor(Color.TRANSPARENT);
					break;
				}
				return false;
			}
		});
	}
}
