package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.ui.social.SendMessageDialog.ISendHandler;

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
		SendMessageDialog dialog = new SendMessageDialog(this.getContext(), new ISendHandler(){

			@Override
			public void onSend(String msg) {
				AppData.getInstance().setMessage(msg);
				CloudAPI.sendMessage(HomeActivity.getApp(), "0", mInfo.getId(), AppData.getInstance().getISBN(), 
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
			
		});
		dialog.show();
	}

}
