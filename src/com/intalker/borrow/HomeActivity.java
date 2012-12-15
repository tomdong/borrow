package com.intalker.borrow;

import com.intalker.borrow.ui.book.BookGallery;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_home);
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
				// TODO Auto-generated method stub
				
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
		itemLP.width = DensityAdaptor.getDensityIndependentValue(240);
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
}
