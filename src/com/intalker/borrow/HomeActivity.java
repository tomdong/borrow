package com.intalker.borrow;

import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.config.ResultCode;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.friends.FriendsNavigationVertical;
import com.intalker.borrow.isbn.ISBNResolver;
import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.book.BookShelfView;
import com.intalker.borrow.ui.control.sliding.SlidingMenu;
import com.intalker.borrow.ui.login.RegisterView;
import com.intalker.borrow.ui.navigation.NavigationPanel;
import com.intalker.borrow.ui.social.SocialPanel;
import com.intalker.borrow.util.DBUtil;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.StorageUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private static HomeActivity app = null;
	private BookGallery mBookGallery = null;
	private NavigationPanel mNavigationPanel = null;
	private SocialPanel mSocialPanel = null;

	private RegisterView mReg = null;
	private FriendsNavigationVertical mFriendsNavigation = null; // it also
																	// contains
																	// self info
																	// and
																	// action
																	// button!
	private SlidingMenu mSlidingMenu = null;

	public void toggleLeftPanel() {
		mSlidingMenu.toggleLeftView();
	}

	public void toggleRightPanel() {
		mSlidingMenu.toggleRightView();
	}

	public static HomeActivity getApp() {
		return app;
	}

	public BookGallery getBookGallery() {
		return mBookGallery;
	}

	public SocialPanel getSocialPanel() {
		return mSocialPanel;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		app = this;
		StorageUtil.initialize();
		AppData.getInstance().initialize();
		DensityAdaptor.init(this);
		StorageUtil.loadCachedBooks();
		CloudAPI.CloudToken = "";

		setContentView(initializeWithSlidingStyle());
		
		tryAutoLogin();
	}
	
	private void tryAutoLogin() {
		if (CloudAPI.setAccessToken(DBUtil.loadToken())) {
			CloudAPI.getLoggedInUserInfo(this, new ICloudAPITaskListener() {

				@Override
				public void onFinish(int returnCode) {
					doAfterGetUserInfoByToken(returnCode);
				}

			});
		}
	}

	private View initializeWithSlidingStyle() {
		mSlidingMenu = new SlidingMenu(this);
		mSocialPanel = new SocialPanel(this);
		mNavigationPanel = new NavigationPanel(this);

		mSlidingMenu.setLeftView(mNavigationPanel);
		mSlidingMenu.setRightView(mSocialPanel);
		mSlidingMenu.setCenterView(createHomeUI());
		return mSlidingMenu;
	}

	// private View createLeftNavigationPanel()
	// {
	// RelativeLayout naviPanel = new RelativeLayout(this);
	// Button btn = new Button(this);
	// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.WRAP_CONTENT);
	// lp.width = LayoutUtil.getNavigationPanelWidth();
	// naviPanel.addView(btn, lp);
	// return naviPanel;
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_home, menu);
		menu.addSubMenu("Settings");
		menu.addSubMenu("Help");
		menu.addSubMenu("About");
		menu.addSubMenu("Exit");
		return true;
	}

	private void doAfterGetUserInfoByToken(int returnCode) {
		switch (returnCode) {
		case CloudAPI.Return_OK:
			mBookGallery.updateTopPanel();
			mSocialPanel.getFriendsView().refreshList();
			break;
		case CloudAPI.Return_NoSuchUser:
			Toast.makeText(this, "No such user.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_BadToken:
			Toast.makeText(this, "Bad token.", Toast.LENGTH_SHORT).show();
			break;
		case CloudAPI.Return_NetworkError:
			Toast.makeText(this, "Network error.", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(this, "Unknown error.", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	public void switchToSignUpPanel() {
		mSlidingMenu.toggleLeftView();
		mBookGallery.setVisibility(View.GONE);
		mReg.setVisibility(View.VISIBLE);
	}

	public View createHomeUI() {
		BookShelfItem.lastBookForTest = null;

		LinearLayout mainLayout = new LinearLayout(this);

		// book gallery ui
		mBookGallery = new BookGallery(this);
		LinearLayout.LayoutParams bookGalleryLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mainLayout.addView(mBookGallery, bookGalleryLP);

		mBookGallery.initialWithCachedData();

		mReg = new RegisterView(this);
		mainLayout.addView(mReg);
		mReg.setVisibility(View.GONE);
		mReg.setRegisterListener(new RegisterView.OnRegisterListener() {

			@Override
			public void onSuccess() {
				mReg.setVisibility(View.GONE);
				mBookGallery.setVisibility(View.VISIBLE);
			}

			@Override
			public void onBack() {
				mReg.setVisibility(View.GONE);
				mBookGallery.setVisibility(View.VISIBLE);
			}
		});

		// Test settings
		mainLayout.setBackgroundColor(Color.GRAY);
		return mainLayout;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ResultCode.SCAN_RESULT_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				String isbn = data.getStringExtra("SCAN_RESULT");
				int length = isbn.length();
				if (10 == length || 13 == length) {
					if(AppConfig.useSQLiteForCache)
					{
						BookInfo bookInfo = DBUtil.getBookInfo(isbn);
						if (null != bookInfo) {
							AppData.getInstance().addBook(bookInfo);
							BookShelfView.getInstance().addBookByExistingInfo(
									bookInfo);
							break;
						}
					}
					ISBNResolver.getInstance().getBookInfoByISBN(this, isbn);
				} else {
					Toast.makeText(this, this.getString(R.string.invalid_isbn),
							Toast.LENGTH_SHORT).show();
				}
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