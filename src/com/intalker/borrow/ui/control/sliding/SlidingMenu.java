package com.intalker.borrow.ui.control.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SlidingMenu extends RelativeLayout {

	private SlidingView mSlidingView;
	private View mLeftView;
	private View mRightView;

	public SlidingMenu(Context context) {
		super(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

//	public void addViews(View left, View center, View right) {
//		setLeftView(left);
//		setRightView(right);
//		setCenterView(center);
//	}

	public void setLeftView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		addView(view, behindParams);
		mLeftView = view;
	}

	public void setRightView(View view) {
//		if(null == view)
//		{
//			return;
//		}
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(view, behindParams);
		mRightView = view;
	}

	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mSlidingView = new SlidingView(getContext());
		addView(mSlidingView, aboveParams);
		mSlidingView.setView(view);
		mSlidingView.invalidate();
		mSlidingView.setLeftView(mLeftView);
		mSlidingView.setRightView(mRightView);
	}

	public void toggleLeftView() {
		mSlidingView.toggleLeftView();
	}

	public void toggleRightView() {
		mSlidingView.toggleRightView();
	}
}
