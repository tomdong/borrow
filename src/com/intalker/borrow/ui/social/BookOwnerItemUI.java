package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.AppData;

import android.content.Context;

public class BookOwnerItemUI extends UserItemUI {
	
	private boolean mBorrowed = false;
	
	public BookOwnerItemUI(Context context) {
		super(context, R.drawable.borrow);
	}

	@Override
	protected void onActionButtonClick() {
		if (mBorrowed) {
			return;
		}
		AppData.getInstance().setMessage("I want your book");
		CloudAPI.sendMessage(this.getContext(), mInfo.getId(), AppData.getInstance().getISBN(), 
				new ICloudAPITaskListener() {

					@Override
					public void onFinish(int returnCode) {
						if (CloudAPI.isSuccessful(
								HomeActivity.getApp(), returnCode)) {
							mActionBtn.setVisibility(GONE);
						}
					}

				});
	}

}
