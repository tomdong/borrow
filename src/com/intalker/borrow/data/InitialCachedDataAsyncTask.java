package com.intalker.borrow.data;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.util.DBUtil;
import com.intalker.borrow.util.StorageUtil;

import android.os.AsyncTask;
import android.widget.Toast;

public class InitialCachedDataAsyncTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		StorageUtil.loadCachedBooks();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		HomeActivity.getApp().getBookGallery().initialWithCachedData();
		if (CloudAPI.setAccessToken(DBUtil.loadToken())) {
			CloudAPI.getLoggedInUserInfo(HomeActivity.getApp(), new ICloudAPITaskListener() {

				@Override
				public void onFinish(int returnCode) {
					HomeActivity app = HomeActivity.getApp();
					switch (returnCode) {
					case CloudAPI.Return_OK:
						app.getBookGallery().updateTopPanel(UserInfo.getCurLoggedinUser());
						app.getSocialPanel().getFriendsView().refreshList();
						break;
					case CloudAPI.Return_NoSuchUser:
						Toast.makeText(app, "No such user.", Toast.LENGTH_SHORT).show();
						break;
					case CloudAPI.Return_BadToken:
						Toast.makeText(app, "Bad token.", Toast.LENGTH_SHORT).show();
						break;
					case CloudAPI.Return_NetworkError:
						Toast.makeText(app, "Network error.", Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(app, "Unknown error.", Toast.LENGTH_SHORT).show();
						break;
					}
				}

			});
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}