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
	private int mReturnCode = CloudConfig.Return_Unset;
	private ICloudAPITaskListener mAPIListener = null;

	public CloudAPIAsyncTask(Context context, String url, String op,
			ICloudAPITaskListener apiListener) {
		super();
		mUrl = url;
		mOp = op;
		mAPIListener = apiListener;
		init(context, true);
	
	}
	public CloudAPIAsyncTask(Context context, String url, String op,
			ICloudAPITaskListener apiListener,boolean showDialog) {
		super();
		mUrl = url;
		mOp = op;
		mAPIListener = apiListener;
		init(context, showDialog);
		
	}
	
	private void init(Context context, boolean showDialog)
	{
		if(showDialog)
		{
			mProgressDialog = new TransparentProgressDialog(context, false);
			mProgressDialog.setCancelable(false);
			String message = "";
			if (mOp.compareTo(CloudConfig.API_Login) == 0) {
				message = context.getString(R.string.api_msg_login);
			} else if (mOp.compareTo(CloudConfig.API_SignUp) == 0) {
				message = context.getString(R.string.api_msg_signup);
			} else if (mOp.compareTo(CloudConfig.API_GetUserInfo) == 0) {
				message = context.getString(R.string.api_msg_getuserinfo);
			} else if (mOp.compareTo(CloudConfig.API_UploadBooks) == 0) {
				message = context.getString(R.string.api_msg_upload);
			} else if (mOp.compareTo(CloudConfig.API_GetOwnedBooks) == 0) {
				message = context.getString(R.string.api_msg_download);
			} else if (mOp.compareTo(CloudConfig.API_SynchronizeOwnedBooks) == 0) {
				message = context.getString(R.string.api_msg_sync);
			} else if (mOp.compareTo(CloudConfig.API_GetFollowings) == 0) {
				message = context.getString(R.string.api_msg_getfollowings);
			} else if (mOp.compareTo(CloudConfig.API_DeleteBook) == 0) {
				message = context.getString(R.string.api_msg_deletebook);
			} else if (mOp.compareTo(CloudConfig.API_GetAllUsers) == 0) {
				message = context.getString(R.string.api_msg_getallusers);
			} else if (mOp.compareTo(CloudConfig.API_Follow) == 0) {
				message = context.getString(R.string.api_msg_follow);
			} else if (mOp.compareTo(CloudConfig.API_UnFollow) == 0) {
				message = context.getString(R.string.api_msg_unfollow);
			} else if (mOp.compareTo(CloudConfig.API_GetBooksByOwner) == 0) {
				message = context.getString(R.string.api_msg_getbooksbyowner);
			} else if (mOp.compareTo(CloudConfig.API_GetUsersByISBN) == 0) {
				message = context.getString(R.string.api_msg_searchuserbyisbn);
			} else if (mOp.compareTo(CloudConfig.API_SendMessage) == 0) {
				message = context.getString(R.string.api_msg_sendingmessage);
			} else if (mOp.compareTo(CloudConfig.API_GetAllMessages) == 0) {
				message = context.getString(R.string.api_msg_sync);
			}  else {
				message = mOp;
			}
			mProgressDialog.setMessage(message);
			
			mProgressDialog.show();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mReturnCode = CloudConfig.Return_Unset;
	}

	@Override
	protected Void doInBackground(String... params) {
		if (mOp.compareTo(CloudConfig.API_Login) == 0) {
			mReturnCode = CloudUtility._login(mUrl);
			if (CloudConfig.Return_OK == mReturnCode) {
				mReturnCode = CloudUtility._getLoggedInUserInfo();
				if (CloudConfig.Return_OK == mReturnCode) {
					CloudUtility._getFriends();
				}
			}
		} else if (mOp.compareTo(CloudConfig.API_SignUp) == 0) {
			mReturnCode = CloudUtility._signUp(mUrl);
			if (CloudConfig.Return_OK == mReturnCode) {
				mReturnCode = CloudUtility._getLoggedInUserInfo();
				if (CloudConfig.Return_OK == mReturnCode) {
					CloudUtility._getFriends();
				}
			}
		} else if (mOp.compareTo(CloudConfig.API_GetUserInfo) == 0) {
			mReturnCode = CloudUtility._getLoggedInUserInfo();
			if (CloudConfig.Return_OK == mReturnCode) {
				CloudUtility._getFriends();
			}
		} else if (mOp.compareTo(CloudConfig.API_UploadBooks) == 0) {
			mReturnCode = CloudUtility._uploadBooks();
		} else if (mOp.compareTo(CloudConfig.API_GetOwnedBooks) == 0) {
			mReturnCode = CloudUtility._getOwnedBooks();
		} else if (mOp.compareTo(CloudConfig.API_SynchronizeOwnedBooks) == 0) {
			mReturnCode = CloudUtility._uploadBooks();
			if (CloudConfig.Return_OK == mReturnCode) {
				mReturnCode = CloudUtility._getOwnedBooks();
			}
		} else if (mOp.compareTo(CloudConfig.API_GetFollowings) == 0) {
			mReturnCode = CloudUtility._getFriends();
		} else if (mOp.compareTo(CloudConfig.API_DeleteBook) == 0) {
			mReturnCode = CloudUtility._deleteBook(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_GetAllUsers) == 0) {
			mReturnCode = CloudUtility._getAllUsers();
		} else if (mOp.compareTo(CloudConfig.API_Follow) == 0) {
			mReturnCode = CloudUtility._follow(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_UnFollow) == 0) {
			mReturnCode = CloudUtility._unFollow(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_GetBooksByOwner) == 0) {
			mReturnCode = CloudUtility._getBooksByOwner(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_GetUsersByISBN) == 0) {
			mReturnCode = CloudUtility._getUsersByISBN(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_SendMessage) == 0) {
			mReturnCode = CloudUtility._sendMessage(mUrl);
		} else if (mOp.compareTo(CloudConfig.API_GetAllMessages) == 0) {
			mReturnCode = CloudUtility._getAllMessages(mUrl);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(mProgressDialog!=null)
			mProgressDialog.dismiss();
		if (null != mAPIListener) {
			mAPIListener.onFinish(mReturnCode);
		}
	}
}