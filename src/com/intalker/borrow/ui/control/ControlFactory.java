package com.intalker.borrow.ui.control;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.intalker.borrow.R;

public class ControlFactory {
	public static View createHoriSeparatorForRelativeLayout(Context context,
			int w, int y) {
		ImageView v = new ImageView(context);
		v.setImageResource(R.drawable.hori_separator);
		v.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lp.topMargin = y;
		lp.width = w;

		v.setLayoutParams(lp);
		return v;
	}

	public static View createHoriSeparatorForLinearLayout(Context context) {
		ImageView v = new ImageView(context);
		v.setImageResource(R.drawable.hori_separator);
		v.setScaleType(ScaleType.FIT_XY);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		v.setLayoutParams(lp);
		return v;
	}

	public static View createVertSeparatorForRelativeLayout(Context context) {
		ImageView v = new ImageView(context);
		v.setImageResource(R.drawable.vert_separator);
		v.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		v.setLayoutParams(lp);
		return v;
	}
}
