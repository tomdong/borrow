package com.intalker.borrow.cloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;

public class CloudAPIAsyncTask extends AsyncTask<String, Void, Void> {

	public interface ICloudAPITaskListener {
		public void onFinish(int returnCode);
	}

	private ProgressDialog mProgressDialog = null;
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
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle(context.getString(R.string.please_wait));
		mProgressDialog.setIcon(R.drawable.appicon_128);
		mProgressDialog.setMessage(op);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
			}
		} else if (mOp.compareTo(CloudAPI.API_SignUp) == 0) {
			mReturnCode = CloudAPI._signUp(mUrl);
			if (CloudAPI.Return_OK == mReturnCode) {
				mReturnCode = CloudAPI._getLoggedInUserInfo();
			}
		} else if (mOp.compareTo(CloudAPI.API_GetUserInfo) == 0) {
			mReturnCode = CloudAPI._getLoggedInUserInfo();
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