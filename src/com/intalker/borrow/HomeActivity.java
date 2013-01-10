package com.intalker.borrow;

import com.intalker.borrow.cloud.CloudApi;
import com.intalker.borrow.config.ResultCode;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.friends.FriendsNavigationVertical;
import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.ui.book.BookShelfItem;
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
	public boolean isUIDebugMode = false;

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

		BookShelfItem.lastBookForTest = null;
		mFriendsNavigation = new FriendsNavigationVertical(this);
		setContentView(createHomeUI());

		this.mBookGallery.initialWithCachedData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	private View createHomeUI() {
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout navigationBar = new LinearLayout(this);
		navigationBar.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams navigationBarLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		navigationBarLP.width = LayoutUtil.getNavigationPanelWidth();

		// Sign up test
		ImageButton btn0 = new ImageButton(this);
		// btn0.setText("Reg");
		btn0.setImageResource(R.drawable.register);
		btn0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Login API test
				v.setEnabled(false);
				String email = "abc104@openlib.com";
				String pwd = "test1";
				String nickName = "newUser2";
				if (CloudApi.signUp(email, pwd, nickName)) {
					Toast.makeText(v.getContext(),
							"Sign up successful!\nNow logging in...",
							Toast.LENGTH_SHORT).show();
					if (CloudApi.UpdateLoggedInUserInfo()) {
						Toast.makeText(v.getContext(),
								UserInfo.getCurLoginUser().toString(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(v.getContext(), "Fail.",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(v.getContext(), "User name occupied.",
							Toast.LENGTH_SHORT).show();
				}
				v.setEnabled(true);
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
				// Login API test
				// if(CloudApi.login("ryan.shao@openlib.com", "shao"))
				// {
				// if(CloudApi.UpdateLoggedInUserInfo())
				// {
				// Toast.makeText(v.getContext(),
				// UserInfo.getCurLoginUser().toString(),
				// Toast.LENGTH_SHORT).show();
				// }
				// else
				// {
				// Toast.makeText(v.getContext(), "Fail.",
				// Toast.LENGTH_SHORT).show();
				// }
				// }
				// else
				// {
				// Toast.makeText(v.getContext(), "Wrong username or pwd.",
				// Toast.LENGTH_SHORT).show();
				// }
			}
		});
		navigationBar.addView(btn);

		if (isUIDebugMode) {
			Button btn_loginbysession = new Button(this);
			btn_loginbysession.setText("Login by test token");
			btn_loginbysession.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Login API test
					String testToken = "7a3cf000-0662-715b-a6a8-89feb8466014";
					CloudApi.setAccessToken(testToken);
					if (CloudApi.UpdateLoggedInUserInfo()) {
						Toast.makeText(v.getContext(),
								UserInfo.getCurLoginUser().toString(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(v.getContext(), "Fail.",
								Toast.LENGTH_SHORT).show();
					}
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

		if (isUIDebugMode) {
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

		navigationBar.addView(mFriendsNavigation.createFriendsNavigationUI());

		// for (int i = 0; i < 10; ++i) {
		// navigationBar.addView(createTestFriendItemUI());
		// }

		mainLayout.addView(navigationBar, navigationBarLP);

		// book gallery ui
		mBookGallery = new BookGallery(this);
		LinearLayout.LayoutParams bookGalleryLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mainLayout.addView(mBookGallery, bookGalleryLP);

		// Test settings
		mainLayout.setBackgroundColor(Color.GRAY);
		navigationBar.setBackgroundColor(Color.DKGRAY);
		return mainLayout;
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ResultCode.SCAN_RESULT_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				String isbn = data.getStringExtra("SCAN_RESULT");
				// String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				// Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
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