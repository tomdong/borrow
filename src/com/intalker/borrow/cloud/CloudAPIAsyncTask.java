package com.intalker.borrow.cloud;

import android.content.Context;
import android.os.AsyncTask;

import com.intalker.borrow.R;
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

		String message = "";
		if (mOp.compareTo(CloudAPI.API_Login) == 0) {
			message = context.getString(R.string.api_msg_login);
		} else if (mOp.compareTo(CloudAPI.API_SignUp) == 0) {
			message = context.getString(R.string.api_msg_signup);
		} else if (mOp.compareTo(CloudAPI.API_GetUserInfo) == 0) {
			message = context.getString(R.string.api_msg_getuserinfo);
		} else if (mOp.compareTo(CloudAPI.API_UploadBooks) == 0) {
			message = context.getString(R.string.api_msg_upload);
		} else if (mOp.compareTo(CloudAPI.API_GetOwnedBooks) == 0) {
			message = context.getString(R.string.api_msg_download);
		} else if (mOp.compareTo(CloudAPI.API_SynchronizeOwnedBooks) == 0) {
			message = context.getString(R.string.api_msg_sync);
		} else if (mOp.compareTo(CloudAPI.API_GetFollowings) == 0) {
			message = context.getString(R.string.api_msg_getfollowings);
		} else if (mOp.compareTo(CloudAPI.API_DeleteBook) == 0) {
			message = context.getString(R.string.api_msg_deletebook);
		} else if (mOp.compareTo(CloudAPI.API_GetAllUsers) == 0) {
			message = context.getString(R.string.api_msg_getallusers);
		} else if (mOp.compareTo(CloudAPI.API_Follow) == 0) {
			message = context.getString(R.string.api_msg_follow);
		} else if (mOp.compareTo(CloudAPI.API_UnFollow) == 0) {
			message = context.getString(R.string.api_msg_unfollow);
		} else if (mOp.compareTo(CloudAPI.API_GetBooksByOwner) == 0) {
			message = context.getString(R.string.api_msg_getbooksbyowner);
		} else {
			message = mOp;
		}
		mProgressDialog.setMessage(message);
		
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
		} else if (mOp.compareTo(CloudAPI.API_GetFollowings) == 0) {
			mReturnCode = CloudAPI._getFriends();
		} else if (mOp.compareTo(CloudAPI.API_DeleteBook) == 0) {
			mReturnCode = CloudAPI._deleteBook(mUrl);
		} else if (mOp.compareTo(CloudAPI.API_GetAllUsers) == 0) {
			mReturnCode = CloudAPI._getAllUsers();
		} else if (mOp.compareTo(CloudAPI.API_Follow) == 0) {
			mReturnCode = CloudAPI._follow(mUrl);
		} else if (mOp.compareTo(CloudAPI.API_UnFollow) == 0) {
			mReturnCode = CloudAPI._unFollow(mUrl);
		} else if (mOp.compareTo(CloudAPI.API_GetBooksByOwner) == 0) {
			mReturnCode = CloudAPI._getBooksByOwner(mUrl);
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