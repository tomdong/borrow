package com.intalker.borrow.ui.social;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import android.content.Context;

public class StrangerUserItemUI extends UserItemUI {

	private boolean mAdded = false;
	
	public StrangerUserItemUI(Context context) {
		super(context, R.drawable.add);
	}

	@Override
	protected void onActionButtonClick() {
		if (mAdded) {
			return;
		}
		CloudAPI.follow(this.getContext(), mInfo.getId(),
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
