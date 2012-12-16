package com.intalker.borrow;

import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.intalker.borrow.config.ResultCode;
import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;
import com.intalker.borrow.util.ScanUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
		DensityAdaptor.init(this);
		setContentView(createHomeUI());
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
		
		Button btn = new Button(this);
		btn.setText("Login via QQ");
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ScanUtil.scanBarCode(HomeActivity.this);
			}
		});
		
		navigationBar.addView(btn);
		// navigationBarLP.width =
		// DensityAdaptor.getDensityIndependentValue(200);

		for (int i = 0; i < 10; ++i) {
			navigationBar.addView(createTestFriendItemUI());
		}

		mainLayout.addView(navigationBar, navigationBarLP);

		// book gallery ui
		BookGallery bookGallery = new BookGallery(this);
		LinearLayout.LayoutParams bookGalleryLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mainLayout.addView(bookGallery, bookGalleryLP);
		
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
		
		if (requestCode == ResultCode.SCAN_RESULT_CODE)
		{
			switch (resultCode)
			{
			case RESULT_OK:
				String contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
				break;
			case RESULT_CANCELED:
				break;
			default:
				break;
			}
		}
	}
	

}
