package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BookShelfView extends ScrollView {
	
	private int ROW_HEIGHT = DensityAdaptor.getDensityIndependentValue(200);
	
	public BookShelfView(Context context) {
		super(context);
		
		initializeUI();
		//this.setVerticalScrollBarEnabled(true);
		this.setBackgroundResource(R.drawable.bookshelf_bg);
	}

	private void initializeUI()
	{
//		TextView tv = new TextView(this.getContext());
//		tv.setText("Create");
//		this.addView(tv);
		
		LinearLayout mScrollContent = new LinearLayout(this.getContext());
		mScrollContent.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < 10; ++i)
		{
			BookShelfRow row = new BookShelfRow(this.getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.height = ROW_HEIGHT;
			mScrollContent.addView(row, lp);
			
			TextView tv = new TextView(this.getContext());
			tv.setText("Create");
			mScrollContent.addView(tv);
		}
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		mScrollContent.setLayoutParams(lp);
		this.addView(mScrollContent);
	}
}
