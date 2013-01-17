package com.intalker.borrow.ui.login;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class RegisterView extends LinearLayout{
//	private final static int LEFTMARGIN = 20;
//	private final static int TOPMARGIN = 20;
//	private final static int BETWEENMARGIN = 5;
	
	private TextView mTitleLabel = null;
	private TextView mTitleBack = null;
	private TextView mNameLabel = null;
	private EditText mNameInput = null;
	private TextView mPasswordLabel = null;
	private EditText mPasswordInput = null;
	private TextView mPasswordConfirmLabel = null;
	private EditText mPasswordConfirmInput = null;
	
	private CheckBox mAgreementCheck = null;
	
	private Button 	 mRegisterButton = null;
	public RegisterView(Context context) {
		super(context);
		
		init(context);
	}
	
	
	private void init(Context c)
	{
		LinearLayout.LayoutParams lpff = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
		LinearLayout.LayoutParams lpfw = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lpww = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams rpwwc = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rpwwc.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		RelativeLayout.LayoutParams rpwwl = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rpwwl.addRule(RelativeLayout.ALIGN_LEFT);
		this.setBackgroundResource(R.drawable.detail_bk);
//		this.setAlpha(0.7f);
		this.setOrientation(LinearLayout.VERTICAL);		
		this.setLayoutParams(lpff); 
		
		RelativeLayout titleLine = new RelativeLayout(c);		
		titleLine.setLayoutParams(lpfw);
		titleLine.setBackgroundColor(Color.DKGRAY);
		this.addView(titleLine);
		mTitleBack = new TextView(c);
		mTitleBack.setText(R.string.reg_back);
		mTitleBack.setTextSize(12);
		mTitleBack.setTextColor(Color.WHITE);
		mTitleBack.setLayoutParams(rpwwl);		
		titleLine.addView(mTitleBack);	
		mTitleLabel = new TextView(c);
		mTitleLabel.setText(R.string.reg_title);
		mTitleLabel.setTextSize(12);
		mTitleLabel.setTextColor(Color.WHITE);	
		mTitleLabel.setLayoutParams(rpwwc);		
		titleLine.addView(mTitleLabel);
		
		mNameLabel = new TextView(c);
		mNameLabel.setText(R.string.reg_name); 
		mNameLabel.setLayoutParams(lpww);
		this.addView(mNameLabel);
		mNameInput = new EditText(c);
		mNameInput.setLayoutParams(lpfw);
		mNameInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		this.addView(mNameInput);
		
		mPasswordLabel = new TextView(c);
		mPasswordLabel.setText(R.string.reg_passowrd); 
		mPasswordLabel.setLayoutParams(lpww);
		this.addView(mPasswordLabel);
		mPasswordInput = new EditText(c);
		mPasswordInput.setLayoutParams(lpfw);
		mPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(mPasswordInput);
		
		mPasswordConfirmLabel = new TextView(c);
		mPasswordConfirmLabel.setText(R.string.reg_password_confirm); 
		mPasswordConfirmLabel.setLayoutParams(lpww);
		this.addView(mPasswordConfirmLabel);
		mPasswordConfirmInput = new EditText(c);
		mPasswordConfirmInput.setLayoutParams(lpfw);
		mPasswordConfirmInput.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(mPasswordConfirmInput);
		
		mAgreementCheck = new CheckBox(c);
		mAgreementCheck.setLayoutParams(lpww);
		mAgreementCheck.setText(R.string.reg_agreement);
		this.addView(mAgreementCheck);
		
		ImageView sep = new ImageView(this.getContext());
		sep.setImageResource(R.drawable.hori_separator);
		sep.setScaleType(ScaleType.FIT_XY);
		sep.setLayoutParams(lpfw);
		this.addView(sep);
		
		mRegisterButton = new Button(c);
		mRegisterButton.setText(R.string.reg_commit); 
		mRegisterButton.setLayoutParams(lpfw);
		this.addView(mRegisterButton);
	}
}
