package com.intalker.borrow;

import com.intalker.borrow.cloud.CloudApi;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.config.ResultCode;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.friends.FriendsNavigationVertical;
import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.control.sliding.SlidingMenu;
import com.intalker.borrow.ui.login.LoginDialog;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;
import com.intalker.borrow.util.StorageUtil;
import com.intalker.borrow.util.ScanUtil;
import com.intalker.borrow.util.WebUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private static HomeActivity app = null;
	private BookGallery mBookGallery = null;
	private FriendsNavigationVertical mFriendsNavigation = null; // it also
																	// contains
																	// self info
																	// and
																	// action
																	// button!
	private SlidingMenu mSlidingMenu = null;
	
	public static HomeActivity getApp() {
		return app;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
		app = this;
		DensityAdaptor.init(this);
		StorageUtil.initialize();
		StorageUtil.loadCachedBooks();
		
		setContentView(initializeWithSlidingStyle());
	}
	
	private View initializeWithSlidingStyle()
	{
		mSlidingMenu = new SlidingMenu(this);
		
		mSlidingMenu.setLeftView(createNavigationPanel());
		mSlidingMenu.setCenterView(createHomeUI());
		return mSlidingMenu;
	}
	
//	private View createLeftNavigationPanel()
//	{
//		RelativeLayout naviPanel = new RelativeLayout(this);
//		Button btn = new Button(this);
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		lp.width = LayoutUtil.getNavigationPanelWidth();
//		naviPanel.addView(btn, lp);
//		return naviPanel;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	private void doAfterSignUp(boolean isSuccessful) {
		if (isSuccessful) {
			Toast.makeText(this, UserInfo.getCurLoginUser().toString(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "User name occupied.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void doAfterGetUserInfoByToken(boolean isSuccessful) {
		if (isSuccessful) {
			Toast.makeText(this, UserInfo.getCurLoginUser().toString(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Bad token.", Toast.LENGTH_SHORT).show();
		}
	}

	public View createHomeUI() {
		BookShelfItem.lastBookForTest = null;
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);

		//Add this listener to make the whole gallery be dragable.
		mainLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(arg0.getContext(), "Test", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		// book gallery ui
		mBookGallery = new BookGallery(this);
		LinearLayout.LayoutParams bookGalleryLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mainLayout.addView(mBookGallery, bookGalleryLP);
		
		mBookGallery.initialWithCachedData();

		// Test settings
		mainLayout.setBackgroundColor(Color.GRAY);
		return mainLayout;
	}
	
	private View createNavigationPanel()
	{
		LinearLayout navigationBar = new LinearLayout(this);
		navigationBar.setBackgroundColor(Color.DKGRAY);
		navigationBar.setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams navigationBarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		navigationBarLP.width = LayoutUtil.getNavigationPanelWidth();
		
		navigationBar.setLayoutParams(navigationBarLP);

		Button b = new Button(this);
		b.setText("<<");
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSlidingMenu.showLeftView();
			}
			
		});
		navigationBar.addView(b);
		// Sign up test
		ImageButton btn0 = new ImageButton(this);
		// btn0.setText("Reg");
		btn0.setImageResource(R.drawable.register);
		btn0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Login API test
				CloudApi.signUp(v.getContext(),
						"abc1041@openlib.com",
						"test1",
						"newUser2",
						new ICloudAPITaskListener(){

							@Override
							public void onFinish(boolean isSuccessful) {
								doAfterSignUp(isSuccessful);
							}
					
				});
			}
		});

		navigationBar.addView(btn0);

		ImageButton btn = new ImageButton(this);
		// btn.setText("Login");
		btn.setImageResource(R.drawable.login);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: it is possible to add annimation to shown an dialog ?
				LoginDialog loginDialog = new LoginDialog(v.getContext());
				loginDialog.show();
			}
		});
		navigationBar.addView(btn);

		if (AppConfig.isDebugMode) {
			Button btn_loginbysession = new Button(this);
			btn_loginbysession.setText("Login by test token");
			btn_loginbysession.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Login API test
					String testToken = "7a3cf000-0662-715b-a6a8-89feb8466014";
					CloudApi.setAccessToken(testToken);
					CloudApi.updateLoggedInUserInfo(v.getContext(), new ICloudAPITaskListener(){

						@Override
						public void onFinish(boolean isSuccessful) {
							doAfterGetUserInfoByToken(isSuccessful);
						}
						
					});
				}
			});

			navigationBar.addView(btn_loginbysession);
		}

		ImageButton btn1 = new ImageButton(this);
		// btn1.setText("Scan");
		btn1.setImageResource(R.drawable.scan);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ScanUtil.scanBarCode(HomeActivity.this);
			}
		});

		navigationBar.addView(btn1);

		if (AppConfig.isDebugMode) {
			Button btn2 = new Button(this);
			btn2.setText("Clear");
			btn2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mBookGallery.resetBookShelf();
				}
			});

			navigationBar.addView(btn2);
		}
		//
		// Button btn3 = new Button(this);
		// btn3.setText("Random");
		// btn3.setOnClickListener(new OnClickListener(){
		//
		// @Override
		// public void onClick(View v) {
		// mBookGallery.fillWithRandomBooks();
		// }
		// });
		//
		// navigationBar.addView(btn3);
//		mFriendsNavigation = new FriendsNavigationVertical(this);
//		navigationBar.addView(mFriendsNavigation.createFriendsNavigationUI());

		 for (int i = 0; i < 4; ++i) {
		 navigationBar.addView(createTestFriendItemUI());
		 }
		return navigationBar;
	}

	private View createTestFriendItemUI() {
		RelativeLayout item = new RelativeLayout(this);

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.width = LayoutUtil.getNavigationPanelWidth();
		itemLP.height = DensityAdaptor.getDensityIndependentValue(64);

		item.setBackgroundColor(ColorUtil.generateRandomColor());
		item.setLayoutParams(itemLP);

		TextView t = new TextView(this);
		t.setText("Test");
		item.addView(t);

		ImageView avatar = new ImageView(this);
		avatar.setImageResource(R.drawable.avatar);
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(48);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(48);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		avatarLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		avatarLP.rightMargin = DensityAdaptor.getDensityIndependentValue(8);
		item.addView(avatar, avatarLP);
		return item;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ResultCode.SCAN_RESULT_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				String isbn = data.getStringExtra("SCAN_RESULT");
				WebUtil.getInstance()
						.getBookInfoByISBN(HomeActivity.this, isbn);
				break;
			case RESULT_CANCELED:
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		StorageUtil.saveCachedBooks();
	}
}