package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.util.LayoutUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class BookDetailDialog extends Dialog {

	private RelativeLayout mContent = null;
	private RelativeLayout mLayout = null;
	private ImageView mCoverImage = null;
	
	public BookDetailDialog(Context context) {
		super(context, R.style.Theme_TransparentDialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(true);

		mContent = new RelativeLayout(context);
		this.setContentView(mContent);
		
		mLayout = new RelativeLayout(context);
		mLayout.setBackgroundColor(Color.GREEN);
		RelativeLayout.LayoutParams mainLayoutLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainLayoutLP.width = LayoutUtil.getDetailDialogWidth();
		mainLayoutLP.height = LayoutUtil.getDetailDialogHeight();
		mContent.addView(mLayout, mainLayoutLP);
		
		mCoverImage = new ImageView(context);
		RelativeLayout.LayoutParams coverImageLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		coverImageLP.width = LayoutUtil.getDetailDialogWidth() / 3;
		coverImageLP.height = LayoutUtil.getDetailDialogHeight() / 3;
		mCoverImage.setImageResource(R.drawable.bookcover_unknown);
		mCoverImage.setScaleType(ScaleType.FIT_CENTER);
		mCoverImage.setBackgroundColor(Color.BLUE);
		mLayout.addView(mCoverImage, coverImageLP);
		
		


	}
	
	public void setInfo(BookInfo bookInfo)
	{
		if(null != bookInfo)
		{
			mCoverImage.setImageBitmap(bookInfo.getCoverImage());
		}
	}

}
