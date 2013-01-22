package com.intalker.borrow.ui.login;

import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

public class RegisterView extends LinearLayout implements OnClickListener{
	
	public interface OnRegisterListener
	{
		public abstract void onSuccess();
		public abstract void onBack();
	}
//	private final static int LEFTMARGIN = 20;
//	private final static int TOPMARGIN = 20;
//	private final static int BETWEENMARGIN = 5;
	
	private TextView mTitleLabel = null;
	private TextView mTitleBack = null;
	private TextView mNameLabel = null;
	private EditText mNameInput = null;
	private TextView mNickNameLabel = null;
	private EditText mNickNameInput = null;
	private TextView mPasswordLabel = null;
	private EditText mPasswordInput = null;
	private TextView mPasswordConfirmLabel = null;
	private EditText mPasswordConfirmInput = null;
	
	private CheckBox mAgreementCheck = null;
	
	private Button 	 mRegisterButton = null;
	
	private String mName = "";
	private String mPassword = "";
	private String mConfirmPassword = "";
	
	private OnRegisterListener mRegListener = null;
	public RegisterView(Context context) {
		super(context);
		
		init(context);
	}
	
	public void setRegisterListener(OnRegisterListener l)
	{
		mRegListener = l;
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
		mTitleBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				hide_keyboard();
				if(mRegListener!=null)
					mRegListener.onBack();
			}
		});
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
		mNameInput.setHint(R.string.reg_name_hint);
		mNameInput.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				String inputname = mNameInput.getText().toString();
				if(!isValidName(inputname))
				{
					mNameInput.setTextColor(Color.RED);
					mRegisterButton.setEnabled(false);
				}	
				else
				{
					mNameInput.setTextColor(Color.BLACK);
					mName = inputname;
					tryEnableRegButton();
				}
			}
		});
		this.addView(mNameInput);
		
		mNickNameLabel = new TextView(c);
		mNickNameLabel.setText(R.string.reg_nickname); 
		mNickNameLabel.setLayoutParams(lpww);
		this.addView(mNickNameLabel);
		mNickNameInput = new EditText(c);
		mNickNameInput.setLayoutParams(lpfw);
		mNickNameInput.setHint(R.string.reg_nickname_hint);
		
		this.addView(mNickNameInput);
		
		mPasswordLabel = new TextView(c);
		mPasswordLabel.setText(R.string.reg_passowrd); 
		mPasswordLabel.setLayoutParams(lpww);
		this.addView(mPasswordLabel);
		mPasswordInput = new EditText(c);
		mPasswordInput.setLayoutParams(lpfw);
		mPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mPasswordInput.setHint(R.string.reg_password_hint);
		mPasswordInput.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				String inputpass = mPasswordInput.getText().toString();
				if(!isValidPassword(inputpass))
				{
					mPasswordInput.setTextColor(Color.RED);
					mRegisterButton.setEnabled(false);
				}	
				else
				{
					mPasswordInput.setTextColor(Color.BLACK);
					mPassword = inputpass;
					tryEnableRegButton();
				}
			}
		});
		this.addView(mPasswordInput);
		
		mPasswordConfirmLabel = new TextView(c);
		mPasswordConfirmLabel.setText(R.string.reg_password_confirm); 
		mPasswordConfirmLabel.setLayoutParams(lpww);
		this.addView(mPasswordConfirmLabel);
		mPasswordConfirmInput = new EditText(c);
		mPasswordConfirmInput.setLayoutParams(lpfw);
		mPasswordConfirmInput.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mPasswordConfirmInput.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {				
			}

			@Override
			public void afterTextChanged(Editable s) {
				String input = mPasswordConfirmInput.getText().toString();
				if(!isValidPassword(input))
				{
					mPasswordConfirmInput.setTextColor(Color.RED);
					mRegisterButton.setEnabled(false);
				}	
				else
				{
					mPasswordConfirmInput.setTextColor(Color.BLACK);
					mConfirmPassword = input;
					tryEnableRegButton();
				}
			}
		});
		this.addView(mPasswordConfirmInput);
		
		mAgreementCheck = new CheckBox(c);
		mAgreementCheck.setLayoutParams(lpww);
		mAgreementCheck.setText(R.string.reg_agreement);
		mAgreementCheck.setChecked(true);
		this.addView(mAgreementCheck);
		
		ImageView sep = new ImageView(this.getContext());
		sep.setImageResource(R.drawable.hori_separator);
		sep.setScaleType(ScaleType.FIT_XY);
		sep.setLayoutParams(lpfw);
		this.addView(sep);
		
		mRegisterButton = new Button(c);
		mRegisterButton.setText(R.string.reg_commit); 
		mRegisterButton.setLayoutParams(lpfw);
		mRegisterButton.setOnClickListener(this);
		mRegisterButton.setEnabled(false);
		this.addView(mRegisterButton);
	}
	
	private boolean isValidName(String s)
	{
		if(s.contains(" ") ||!s.contains("@"))
			return false;
		
		return true;
	}
	
	private boolean isValidPassword(String s)
	{
		if(s.contains(" "))
			return false;
		if(s.length()<5 || s.length()>20)
			return false;
		
		return true;
	}
	private void tryEnableRegButton()
	{
		if(mPassword.contentEquals(mConfirmPassword) && this.mAgreementCheck.isChecked())
			mRegisterButton.setEnabled(true);
	}


	@Override
	public void onClick(View v) {
		 CloudAPI.signUp(v.getContext(),
		 mName,
		 mPassword,
		 mNickNameInput.getText().toString(),
		 new ICloudAPITaskListener(){
		
			 @Override
			 public void onFinish(int returnCode) {
				 switch (returnCode) {
					case CloudAPI.Return_OK:
						hide_keyboard();
						if(mRegListener!=null)
							mRegListener.onSuccess();
						break;
					case CloudAPI.Return_UserNameOccupied:
						Toast.makeText(mNameInput.getContext(), "User name occupied.", Toast.LENGTH_SHORT)
								.show();
						break;
					case CloudAPI.Return_NetworkError:
						Toast.makeText(mNameInput.getContext(), "Network error.", Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(mNameInput.getContext(), "Unknown error.", Toast.LENGTH_SHORT).show();
						break;
				 }	
			 }		
		 });
	}

	private void hide_keyboard() {
		InputMethodManager imManager = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imManager.hideSoftInputFromWindow(this.getApplicationWindowToken(), 0, null);
	}
}
