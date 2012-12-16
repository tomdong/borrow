package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.util.ColorUtil;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class BookShelfItem extends RelativeLayout{

	private ImageView mCoverImageView = null;
	public BookShelfItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createUI();
	}

	private void createUI()
	{
		mCoverImageView = new ImageView(this.getContext());
		RelativeLayout.LayoutParams coverImgaeViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		mCoverImageView.setScaleType(ScaleType.FIT_END);
		double random = Math.random();
		
		int resId = 0;
		if(random < 0.3)
		{
			resId = R.drawable.bookcover_test;
		}
		else if(random < 0.7)
		{
			resId = R.drawable.bookcover_test1;
		}
		else
		{
			resId = R.drawable.bookcover_test2;
		}
		mCoverImageView.setImageResource(resId);
		
		coverImgaeViewLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		coverImgaeViewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(mCoverImageView, coverImgaeViewLP);
		//this.setBackgroundColor(ColorUtil.generateRandomColor());
	}
}
