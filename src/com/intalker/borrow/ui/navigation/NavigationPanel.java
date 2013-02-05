package com.intalker.borrow.ui.navigation;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.ui.control.HaloButton;
import com.intalker.borrow.ui.login.LoginDialog;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationPanel extends RelativeLayout{

	private Context mContext = null;
	private HaloButton mSignUpBtn = null;
	private HaloButton mLoginBtn = null;
	private HaloButton mLogOffBtn = null;
	private TextView mCurUserNameTextView = null;
	
	public NavigationPanel(Context context) {
		super(context);
		mContext = context;
		createUI();
	}

	private void createUI()
	{
		int panelWidth = LayoutUtil.getNavigationPanelWidth();
		this.setBackgroundResource(R.drawable.stone_narrow_bg);
		RelativeLayout.LayoutParams navigationBarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		navigationBarLP.width = LayoutUtil.getNavigationPanelWidth();

		this.setLayoutParams(navigationBarLP);

		View logoView = new View(mContext);
		logoView.setBackgroundResource(R.drawable.logo);
		LinearLayout.LayoutParams logoLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		logoLP.width = LayoutUtil.getNavigationPanelWidth();
		logoLP.height = LayoutUtil.getNavigationPanelWidth();

		this.addView(logoView, logoLP);

		this.addView(ControlFactory
				.createHoriSeparatorForRelativeLayout(mContext, panelWidth, logoLP.height));
		
		//Create buttons
		// Sign up
		mSignUpBtn = new HaloButton(mContext, R.drawable.register);
		mSignUpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeActivity.getApp().toggleSignUpPanel(true);
			}
		});

		RelativeLayout.LayoutParams regBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		int y = logoLP.height + DensityAdaptor.getDensityIndependentValue(10);
		int buttonGap = DensityAdaptor.getDensityIndependentValue(34);
		regBtnLP.topMargin = y;
		regBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(mSignUpBtn, regBtnLP);
		
		this.addView(ControlFactory
				.createHoriSeparatorForRelativeLayout(mContext, panelWidth, y + regBtnLP.height + buttonGap));
		
		// Login
		mLoginBtn = new HaloButton(mContext, R.drawable.login);
		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginDialog loginDialog = new LoginDialog(v.getContext());
				loginDialog.show();
			}
		});
		RelativeLayout.LayoutParams loginBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		y += buttonGap + DensityAdaptor.getDensityIndependentValue(8);
		loginBtnLP.topMargin = y;
		loginBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(mLoginBtn, loginBtnLP);
		
		this.addView(ControlFactory
				.createHoriSeparatorForRelativeLayout(mContext, panelWidth, y + loginBtnLP.height + buttonGap));
		
		// Test token
		HaloButton testTokenBtn = new HaloButton(mContext, R.drawable.test);
		testTokenBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String testToken = "7a3cf000-0662-715b-a6a8-89feb8466014";
				CloudAPI.setAccessToken(testToken);
				CloudAPI.getLoggedInUserInfo(v.getContext(),
						new ICloudAPITaskListener() {

							@Override
							public void onFinish(int returnCode) {
								doAfterGetUserInfoByToken(returnCode);
							}

						});
			}
		});
		RelativeLayout.LayoutParams testTokenBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		y += buttonGap + DensityAdaptor.getDensityIndependentValue(8);
		testTokenBtnLP.topMargin = y;
		testTokenBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(testTokenBtn, testTokenBtnLP);
		
		this.addView(ControlFactory
				.createHoriSeparatorForRelativeLayout(mContext, panelWidth, y + loginBtnLP.height + buttonGap));
		
		// Clear button
//		HaloButton clearBtn = new HaloButton(mContext, R.drawable.clear);
//		clearBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				HomeActivity.getApp().getBookGallery().resetBookShelf();
//			}
//		});
//		RelativeLayout.LayoutParams clearBtnLP = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		y += buttonGap + DensityAdaptor.getDensityIndependentValue(8);
//		clearBtnLP.topMargin = y;
//		clearBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		this.addView(clearBtn, clearBtnLP);
	}
	
	private void doAfterUplaod(int returnCode) {
		switch (returnCode) {
		case CloudAPI.Return_OK:
			Toast.makeText(mContext, "Upload done!", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_BadToken:
			Toast.makeText(mContext, "Bad token.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_NetworkError:
			Toast.makeText(mContext, "Network error.", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(mContext, "Unknown error.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void doAfterGetOwnedBooks(int returnCode) {
		switch (returnCode) {
		case CloudAPI.Return_OK:
			Toast.makeText(mContext, "Sync done!", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_BadToken:
			Toast.makeText(mContext, "Bad token.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_NetworkError:
			Toast.makeText(mContext, "Network error.", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(mContext, "Unknown error.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void doAfterGetUserInfoByToken(int returnCode) {
		switch (returnCode) {
		case CloudAPI.Return_OK:
			HomeActivity.getApp().getBookGallery().updateTopPanel(UserInfo.getCurLoggedinUser());
			HomeActivity.getApp().getSocialPanel().getFriendsView().refreshList();
			break;
		case CloudAPI.Return_NoSuchUser:
			Toast.makeText(mContext, "No such user.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_BadToken:
			Toast.makeText(mContext, "Bad token.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_NetworkError:
			Toast.makeText(mContext, "Network error.", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(mContext, "Unknown error.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
