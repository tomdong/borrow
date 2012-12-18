package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class BookShelfItem extends RelativeLayout{
	public static BookShelfItem lastBookForTest = null;
	private ImageView mCoverImageView = null;
	public BookShelfItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createUI();
	}
	
	public void setCoverImage(Bitmap coverImage)
	{
		mCoverImageView.setImageBitmap(coverImage);
	}

	private void createUI()
	{
		mCoverImageView = new ImageView(this.getContext());
		RelativeLayout.LayoutParams coverImgaeViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
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
	}

	public void hideBeforeLoaded() {
		this.setVisibility(View.GONE);
		lastBookForTest = this;
	}
	
	public void show() {
		this.setVisibility(View.VISIBLE);
	}
}
