package com.intalker.borrow.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OBLogin extends ViewGroup{
	private ImageView mGrayBackground = null;
	private TextView mLoginName = null;
	private TextView mLoginPassword = null;
	private ImageButton mLoginBtn = null;
	private ImageButton mRegisterBtn = null;
	private ImageButton mReturnBtn = null;

	public OBLogin(Context context) {
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
		
		mLoginName = new TextView(ctx);
		mLoginName.setInputType(InputType.TYPE_CLASS_TEXT);
		
		mLoginPassword = new TextView(ctx);
		mLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
	}

}
