package com.intalker.borrow.cloud;

import android.content.Context;
import android.os.AsyncTask;

import com.intalker.borrow.ui.control.TransparentProgressDialog;

public class CloudAPIAsyncTask extends AsyncTask<String, Void, Void> {

	public interface ICloudAPITaskListener {
		public void onFinish(int returnCode);
	}

	private TransparentProgressDialog mProgressDialog = null;
	private String mUrl = "";
	private String mOp = "";
	private int mReturnCode = CloudAPI.Return_Unset;
	private ICloudAPITaskListener mAPIListener = null;

	public CloudAPIAsyncTask(Context context, String url, String op,
			ICloudAPITaskListener apiListener) {
		super();
		mUrl = url;
		mOp = op;
		mAPIListener = apiListener;
		mProgressDialog = new TransparentProgressDialog(context, false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(op);
		mProgressDialog.show();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mReturnCode = CloudAPI.Return_Unset;
	}

	@Override
	protected Void doInBackground(String... params) {
		if (mOp.compareTo(CloudAPI.API_Login) == 0) {
			mReturnCode = CloudAPI._login(mUrl);
			if (CloudAPI.Return_OK == mReturnCode) {
				mReturnCode = CloudAPI._getLoggedInUserInfo();
				if (CloudAPI.Return_OK == mReturnCode) {
					CloudAPI._getFriends();
				}
			}
		} else if (mOp.compareTo(CloudAPI.API_SignUp) == 0) {
			mReturnCode = CloudAPI._signUp(mUrl);
			if (CloudAPI.Return_OK == mReturnCode) {
				mReturnCode = CloudAPI._getLoggedInUserInfo();
				if (CloudAPI.Return_OK == mReturnCode) {
					CloudAPI._getFriends();
				}
			}
		} else if (mOp.compareTo(CloudAPI.API_GetUserInfo) == 0) {
			mReturnCode = CloudAPI._getLoggedInUserInfo();
			if (CloudAPI.Return_OK == mReturnCode) {
				CloudAPI._getFriends();
			}
		} else if (mOp.compareTo(CloudAPI.API_UploadBooks) == 0) {
			mReturnCode = CloudAPI._uploadBooks();
		} else if (mOp.compareTo(CloudAPI.API_GetOwnedBooks) == 0) {
			mReturnCode = CloudAPI._getOwnedBooks();
		} else if (mOp.compareTo(CloudAPI.API_SynchronizeOwnedBooks) == 0) {
			mReturnCode = CloudAPI._uploadBooks();
			if (CloudAPI.Return_OK == mReturnCode) {
				mReturnCode = CloudAPI._getOwnedBooks();
			}
		} else if (mOp.compareTo(CloudAPI.API_GetFriends) == 0) {
			mReturnCode = CloudAPI._getFriends();
		} else if (mOp.compareTo(CloudAPI.API_DeleteBookFromServer) == 0) {
			mReturnCode = CloudAPI._deleteBookFromServer(mUrl);
		} else if (mOp.compareTo(CloudAPI.API_GetAllUsers) == 0) {
			mReturnCode = CloudAPI._getAllUsers();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if (null != mAPIListener) {
			mAPIListener.onFinish(mReturnCode);
		}
	}
}