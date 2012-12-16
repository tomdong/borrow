package com.intalker.borrow.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebUtil {
	private static WebUtil instance = null;
	public static WebUtil getInstance()
	{
		if(null == instance)
		{
			instance = new WebUtil();
		}
		return instance;
	}
	public void getBookInfoByISBN(Activity app, String isbn) {
		GetBookInfoTask task = new GetBookInfoTask(app, isbn);
		task.execute();
	}

	class GetBookInfoTask extends AsyncTask<String, Void, String> {
		private Activity mActivity = null;
		private String mISBN = null;
		private ProgressDialog mProgressDialog = null;
		public GetBookInfoTask(Activity app, String isbn) {
			super();
			mActivity = app;
			mISBN = isbn;
			mProgressDialog = new ProgressDialog(app);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setTitle("Pulling data");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.show();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			String url = "http://api.douban.com/book/subject/isbn/" + mISBN;
			HttpGet get = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			String resultString = "";
			try {
				HttpResponse response = client.execute(get);
				resultString = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
			}
			
			return resultString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressDialog.hide();
			Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
		}
	}
}