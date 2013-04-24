package com.intalker.borrow.ui.control;

import com.intalker.borrow.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;

public class SquareButton extends FrameLayout {
	private HaloButton mHaloBtn = null;
	public SquareButton(Context context, int iconResId) {
		super(context);
		createUI(context, iconResId);
	}

	private void createUI(Context context, int iconResId) {
		this.setBackgroundResource(R.drawable.black_halo_bg);
		mHaloBtn = new HaloButton(context, iconResId);
		FrameLayout.LayoutParams haloBtnLP = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		haloBtnLP.gravity = Gravity.CENTER;
		mHaloBtn.setLayoutParams(haloBtnLP);
		this.addView(mHaloBtn);
	}
}
