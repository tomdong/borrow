package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.ui.UIConfig;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.LayoutUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MessageDetailDialog extends Dialog {
	private FrameLayout mContentView = null;
	private RelativeLayout mMainLayout = null;
	private int mMainLayoutId = 1;
	private int mUserId = 2;
	private int mSpliterId = 3;
	private int mBaseMsgId = 4;
	private int mReplyBtnId = 5;
	private BigUserView mUserView = null;
	private TextView mBaseMessageTextView = null;
	
	private EditText mMessageInput = null;
	private HaloButton mReplyButton = null;
	
	private MessageInfo mMessageInfo = null;
	private MessageDetailDialog mInstance = null;
	
	public MessageDetailDialog(Context context, MessageInfo msgInfo) {
		super(context, R.style.Theme_TransparentDialog);
		mInstance = this;
		mMessageInfo = msgInfo;
		createUI(context);
		addListeners();
	}

	private void createUI(Context context)
	{
		mContentView = new FrameLayout(context);
		this.setContentView(mContentView);
		
		int midMargin = LayoutUtil.getMediumMargin();

		mMainLayout = new RelativeLayout(context);
		mMainLayout.setBackgroundResource(R.drawable.login_bg);
		FrameLayout.LayoutParams mainLayoutLP = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		mainLayoutLP.width = LayoutUtil.getDetailDialogWidth();
		mainLayoutLP.height = mainLayoutLP.width; //[TODO] Change later
		mMainLayout.setLayoutParams(mainLayoutLP);
		mMainLayout.setId(mMainLayoutId);
		mContentView.addView(mMainLayout);
		
		mUserView = new BigUserView(context);
		mUserView.setId(mUserId);
		mUserView.setUserName(mMessageInfo.getHostName());
		RelativeLayout.LayoutParams userViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		userViewLP.setMargins(midMargin, midMargin, midMargin, midMargin);
		mUserView.setLayoutParams(userViewLP);
		mUserView.setGravity(Gravity.CENTER_VERTICAL);
		mMainLayout.addView(mUserView);
		
		ScrollView scrollView = new ScrollView(context);
		scrollView.setId(mBaseMsgId);
		
		LinearLayout scrollContent = new LinearLayout(this.getContext());
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(scrollContent);
		
		mBaseMessageTextView = new TextView(context);
		mBaseMessageTextView.setText(mMessageInfo.getMessage());
		mBaseMessageTextView.setTextColor(UIConfig.getLightTextColor());
		scrollContent.addView(mBaseMessageTextView);
		
		RelativeLayout.LayoutParams baseMsgLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		baseMsgLP.setMargins(0, midMargin, midMargin, 0);
		baseMsgLP.width = mainLayoutLP.width - LayoutUtil.getBigUserViewItemWidth() - (midMargin * 3);
		baseMsgLP.height = mainLayoutLP.height / 2;
		baseMsgLP.addRule(RelativeLayout.RIGHT_OF, mUserId);
		
		scrollView.setLayoutParams(baseMsgLP);
		mMainLayout.addView(scrollView);
		
		View spliter = ControlFactory.createHoriSeparatorForRelativeLayout(context,
				mainLayoutLP.width,
				0);
		spliter.setId(mSpliterId);
		RelativeLayout.LayoutParams spliterLP = (LayoutParams) spliter.getLayoutParams();
		spliterLP.addRule(RelativeLayout.BELOW, mBaseMsgId);
		mMainLayout.addView(spliter);
		
		mReplyButton = new HaloButton(context, R.drawable.reply);
		mReplyButton.setId(mReplyBtnId);
		RelativeLayout.LayoutParams replyBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		replyBtnLP.rightMargin = replyBtnLP.bottomMargin = midMargin;
		replyBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		replyBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mReplyButton.setLayoutParams(replyBtnLP);
		mMainLayout.addView(mReplyButton);
		
		mMessageInput = new EditText(context);
		mMessageInput.setBackgroundColor(Color.LTGRAY);
		mMessageInput.setLines(20);
		mMessageInput.setHint(R.string.input_hint);
		mMessageInput.setGravity(Gravity.START);

		RelativeLayout.LayoutParams msgInputLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		msgInputLP.leftMargin = msgInputLP.rightMargin = midMargin;
		msgInputLP.bottomMargin = msgInputLP.topMargin =midMargin;
		msgInputLP.addRule(RelativeLayout.BELOW, mSpliterId);
		msgInputLP.addRule(RelativeLayout.LEFT_OF, mReplyBtnId);

		mMessageInput.setLayoutParams(msgInputLP);
		mMainLayout.addView(mMessageInput);
	}
	
	private void addListeners()
	{
		mReplyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String msg = mMessageInput.getText().toString();
				if(msg.length() < 1)
				{
					Context context = HomeActivity.getApp();
					String info = context.getString(R.string.please_input_sth);
					Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
					return;
				}
				AppData.getInstance().setMessage(msg);
				CloudAPI.sendMessage(HomeActivity.getApp(), mMessageInfo.getId(), mMessageInfo.getHostId(), mMessageInfo.getISBN(), 
				new ICloudAPITaskListener() {

					@Override
					public void onFinish(int returnCode) {
						if (CloudAPI.isSuccessful(
								HomeActivity.getApp(), returnCode)) {
							mInstance.dismiss();
						}
					}

				});
			}
		});
	}
}
