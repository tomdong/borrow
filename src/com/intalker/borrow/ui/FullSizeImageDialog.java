package com.intalker.borrow.ui;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FullSizeImageDialog extends Dialog implements View.OnClickListener{

	private FrameLayout mBackgroundLayout = null;
	private ImageView mContentView = null;

	public FullSizeImageDialog(Context context) {
		super(context, R.style.Theme_TransparentDialog);

		mBackgroundLayout = new FrameLayout(context);
		this.setContentView(mBackgroundLayout);

		mContentView = new ImageView(context);
		mContentView.setScaleType(ScaleType.FIT_CENTER);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.width = DensityAdaptor.getScreenWidth();
		lp.height = DensityAdaptor.getScreenHeight();
		mBackgroundLayout.addView(mContentView, lp);
		
		mContentView.setOnClickListener(this);
	}

	public void setImage(Bitmap image) {
		if (null == image) {
			return;
		}
		mContentView.setImageBitmap(image);
	}
	
	public void setImage(int imageResId) {
		mContentView.setImageResource(imageResId);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
