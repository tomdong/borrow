package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.data.BookInfo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BookDetailDialog extends Dialog {

	private RelativeLayout mLayout = null;
	private ImageView mCoverImage = null;
	
	public BookDetailDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(true);
		
		mLayout = new RelativeLayout(context);
		
		mCoverImage = new ImageView(context);
		mCoverImage.setImageResource(R.drawable.bookcover_unknown);
		mLayout.addView(mCoverImage);
		
		mLayout.setBackgroundColor(Color.GREEN);
		
		this.setContentView(mLayout);
	}
	
	public void setInfo(BookInfo bookInfo)
	{
		if(null != bookInfo)
		{
			mCoverImage.setImageBitmap(bookInfo.getCoverImage());
		}
	}

}
