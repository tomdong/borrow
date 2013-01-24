package com.intalker.borrow.ui.control;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransparentProgressDialog extends Dialog {

	private TextView mMessageTextView = null;
	private ProgressBar mProgressBar = null;
	private boolean mShowProgressVal = true;

	public TransparentProgressDialog(Context context, boolean showProgressVal) {
		super(context, R.style.Theme_TransparentDialog);
		mShowProgressVal = showProgressVal;
		this.setContentView(createUI());
	}

	public View createUI() {
		Context context = this.getContext();
		RelativeLayout bkLayout = new RelativeLayout(context);

		RelativeLayout mainLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mainLP.width = DensityAdaptor.getDensityIndependentValue(300);
		mainLP.height = DensityAdaptor.getDensityIndependentValue(100);

		mainLayout.setBackgroundResource(R.drawable.progress_bk);
		bkLayout.addView(mainLayout, mainLP);

		ProgressBar spinner = new ProgressBar(context);
		RelativeLayout.LayoutParams spinnerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		spinnerLP.width = DensityAdaptor.getDensityIndependentValue(50);
		spinnerLP.height = DensityAdaptor.getDensityIndependentValue(50);
		spinnerLP.leftMargin = DensityAdaptor.getDensityIndependentValue(25);
		spinnerLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mainLayout.addView(spinner, spinnerLP);

		mMessageTextView = new TextView(context);
		RelativeLayout.LayoutParams textLayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		textLayout.leftMargin = DensityAdaptor.getDensityIndependentValue(88);
		textLayout.rightMargin = DensityAdaptor.getDensityIndependentValue(25);
		if (mShowProgressVal) {
			textLayout.topMargin = DensityAdaptor
					.getDensityIndependentValue(25);
		} else {
			textLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		}
		mMessageTextView.setTextColor(Color.BLACK);
		mainLayout.addView(mMessageTextView, textLayout);

		if (mShowProgressVal) {
			mProgressBar = new ProgressBar(context, null,
					android.R.attr.progressBarStyleHorizontal);
			RelativeLayout.LayoutParams progressLP = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			progressLP.width = DensityAdaptor.getDensityIndependentValue(190);
			progressLP.height = DensityAdaptor.getDensityIndependentValue(10);
			progressLP.leftMargin = DensityAdaptor
					.getDensityIndependentValue(88);
			progressLP.rightMargin = DensityAdaptor
					.getDensityIndependentValue(25);
			progressLP.topMargin = DensityAdaptor
					.getDensityIndependentValue(65);
			progressLP.bottomMargin = DensityAdaptor
					.getDensityIndependentValue(25);
			mainLayout.addView(mProgressBar, progressLP);
		}
		return bkLayout;
	}

	public void setMessage(String msg) {
		mMessageTextView.setText(msg);
	}

	public void setMax(int max) {
		if (null != mProgressBar) {
			mProgressBar.setMax(max);
		}
	}

	public void setProgress(int val) {
		if (null != mProgressBar) {
			mProgressBar.setProgress(val);
		}
	}
}
