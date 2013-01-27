package com.intalker.borrow.ui.social;

import com.intalker.borrow.R;
import com.intalker.borrow.util.ColorUtil;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//Change base class to scroll later.
public class FriendListView extends ScrollView{

	private LinearLayout mListView = null;
	
	public FriendListView(Context context) {
		super(context);
		
		mListView = new LinearLayout(context);
		mListView.setOrientation(LinearLayout.VERTICAL);

		this.addView(mListView);
		
		this.setBackgroundColor(Color.BLACK);
		testData();
	}
	
	private void testData()
	{
		for(int i = 0; i < 30; ++i)
		{
			LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLP.height = DensityAdaptor.getDensityIndependentValue(52);
			mListView.addView(new FriendItemUI(this.getContext()), itemLP);
		}
	}
}
