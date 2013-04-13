package com.intalker.borrow.ui.social;

import java.util.ArrayList;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.intalker.borrow.data.MessageInfo;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.util.DensityAdaptor;

public class MessagesView extends ScrollView {

	private LinearLayout mMessageList = null;

	public MessagesView(Context context) {
		super(context);
		mMessageList = new LinearLayout(context);
		mMessageList.setOrientation(LinearLayout.VERTICAL);
		
		this.addView(mMessageList);
	}

	public void fillWithIncomeMessages(ArrayList<MessageInfo> messages) {
		mMessageList.removeAllViews();

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.height = DensityAdaptor.getDensityIndependentValue(40);

		boolean isFirst = true;
		for (MessageInfo msgInfo : messages) {
			if (!isFirst) {
				mMessageList.addView(ControlFactory.createHoriSeparatorForLinearLayout(
								this.getContext()));
			}
			isFirst = false;
			MessageItemUI msgItemUI = new MessageItemUI(this.getContext());
			msgItemUI.setInfo(msgInfo);
			mMessageList.addView(msgItemUI, itemLP);
		}
	}
}
