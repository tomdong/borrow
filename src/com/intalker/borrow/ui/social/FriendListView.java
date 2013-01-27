package com.intalker.borrow.ui.social;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

//Change base class to scroll later.
public class FriendListView extends RelativeLayout{

	public FriendListView(Context context) {
		super(context);
		
		this.setBackgroundColor(Color.BLACK);
		testData();
	}
	
	private void testData()
	{
		this.addView(new FriendItemUI(this.getContext()));
	}

}
