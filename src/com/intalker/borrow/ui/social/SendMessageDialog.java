package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SendMessageDialog extends Dialog {

	private FrameLayout mContentView = null;
	private RelativeLayout mMainLayout = null;
	private EditText mMessageInput = null;
	private int mMainLayoutId = 1;
	private ISendHandler mOnSendHander = null;
	private HaloButton mSendButton = null;
	private SendMessageDialog mDialogInstance = null;
	
	public interface ISendHandler {
		public void onSend(String msg);
	}

	public SendMessageDialog(Context context, ISendHandler sendHandler) {
		super(context, R.style.Theme_TransparentDialog);
		mOnSendHander = sendHandler;
		mDialogInstance = this;
		createUI(context);
		addListeners();
	}

	private void createUI(Context context) {
		mContentView = new FrameLayout(context);
		this.setContentView(mContentView);

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
		
		mMessageInput = new EditText(context);
		mMessageInput.setBackgroundColor(Color.LTGRAY);
		mMessageInput.setLines(20);
		mMessageInput.setHint(R.string.input_hint);
		mMessageInput.setText(R.string.i_want_yourbook);
		RelativeLayout.LayoutParams msgInputLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		int midMargin = LayoutUtil.getMediumMargin();
		msgInputLP.leftMargin = msgInputLP.rightMargin = msgInputLP.topMargin = midMargin;
		msgInputLP.bottomMargin = DensityAdaptor.getDensityIndependentValue(50);
		mMessageInput.setLayoutParams(msgInputLP);
		mMainLayout.addView(mMessageInput);
		
		mSendButton = new HaloButton(context, R.drawable.send);
		RelativeLayout.LayoutParams btnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		btnLP.bottomMargin = midMargin;
		btnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mSendButton.setLayoutParams(btnLP);
		mMainLayout.addView(mSendButton);
	}
	
	private void addListeners() {
		mSendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = mMessageInput.getText().toString();
				if(msg.length() < 1)
				{
					Context context = HomeActivity.getApp();
					String info = context.getString(R.string.please_input_sth);
					Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
				}
				if (null != mOnSendHander) {
					mOnSendHander.onSend(msg);
				}
				mDialogInstance.dismiss();
			}
		});
	}
}
