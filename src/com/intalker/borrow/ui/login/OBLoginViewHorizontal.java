package com.intalker.borrow.ui.login;

import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OBLoginViewHorizontal extends OBLoginView{
	private ImageView mGrayBackground = null;
	private TextView mLoginName = null;
	private TextView mLoginPassword = null;
	private ImageButton mLoginBtn = null;
	private ImageButton mRegisterBtn = null;
	private ImageButton mReturnBtn = null;

	public OBLoginViewHorizontal(Context context) {
		super(context);
		
		createLoginUI(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	private void createLoginUI(Context ctx)
	{
		mGrayBackground = new ImageView(ctx);
		mGrayBackground.setBackgroundColor(Color.GRAY);
		mGrayBackground.setAlpha(0.3f);
		RelativeLayout.LayoutParams bgLayout = new RelativeLayout.LayoutParams(LayoutUtil.getLoginHBackgroundW(), RelativeLayout.LayoutParams.WRAP_CONTENT);
		bgLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
		mGrayBackground.setLayoutParams(bgLayout);
		this.addView(mGrayBackground);
		
		mLoginName = new TextView(ctx);
		mLoginName.setInputType(InputType.TYPE_CLASS_TEXT);
		RelativeLayout.LayoutParams nameLayout = new RelativeLayout.LayoutParams(LayoutUtil.getloginHNameInputW(), RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameLayout.addRule(RelativeLayout.ALIGN_LEFT);
		mLoginName.setLayoutParams(nameLayout);
		this.addView(mLoginName);
		
		mLoginPassword = new TextView(ctx);
		mLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		RelativeLayout.LayoutParams passLayout = new RelativeLayout.LayoutParams(LayoutUtil.getloginHPasswordInputW(), RelativeLayout.LayoutParams.WRAP_CONTENT);
		passLayout.addRule(RelativeLayout.ALIGN_LEFT);
		mLoginPassword.setLayoutParams(passLayout);
		this.addView(mLoginPassword);
		
	}

}
