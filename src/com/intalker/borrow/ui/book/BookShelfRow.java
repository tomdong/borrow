package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

public class BookShelfRow extends RelativeLayout {

	public BookShelfRow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createUI();
	}

	private void createUI()
	{
//		View v1 = new View(this.getContext());
//		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.FILL_PARENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		lp1.height = DensityAdaptor.getDensityIndependentValue(10);
//		lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		v1.setBackgroundColor(Color.GREEN);
//		this.addView(v1, lp1);
		
		View v2 = new View(this.getContext());
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp2.height = DensityAdaptor.getDensityIndependentValue(80);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		v2.setBackgroundResource(R.drawable.bookshelf_row);
		this.addView(v2, lp2);
	}
}
