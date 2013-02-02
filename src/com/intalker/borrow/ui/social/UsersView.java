package com.intalker.borrow.ui.social;

import java.util.ArrayList;

import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.ui.control.ControlFactory;
import com.intalker.borrow.util.DensityAdaptor;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class UsersView extends ScrollView {

	private LinearLayout mUserList = null;

	public UsersView(Context context) {
		super(context);
		mUserList = new LinearLayout(context);
		mUserList.setOrientation(LinearLayout.VERTICAL);
		
		this.addView(mUserList);
	}

	public void refreshList() {
		mUserList.removeAllViews();

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.height = DensityAdaptor.getDensityIndependentValue(40);

		ArrayList<UserInfo> users = AppData.getInstance().getAllUsers();
		boolean isFirst = true;
		for (UserInfo userInfo : users) {
			if (!isFirst) {
				mUserList.addView(ControlFactory.createHoriSeparatorForLinearLayout(
								this.getContext()));
			}
			isFirst = false;
			UserItemUI userItemUI = new UserItemUI(this.getContext());
			userItemUI.setInfo(userInfo);
			mUserList.addView(userItemUI, itemLP);
		}
	}
}
