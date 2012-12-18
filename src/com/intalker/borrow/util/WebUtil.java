package com.intalker.borrow.util;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.book.BookShelfView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class WebUtil {
	private final static String ISBN_SEARCHURL_DOUBAN = "http://api.douban.com/book/subject/isbn/";
	private static WebUtil instance = null;

	public static WebUtil getInstance() {
		if (null == instance) {
			instance = new WebUtil();
		}
		return instance;
	}

	public void getBookInfoByISBN(Activity app, String isbn) {
		GetBookInfoTask task = new GetBookInfoTask(app, isbn);
		task.execute();
	}

	private String parseDoubanXML(InputStream inputStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String imageURL = "";
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputStream);
			NodeList nodes = doc.getElementsByTagName("link");
			int length = nodes.getLength();
			for (int i = 0; i < length; ++i) {
				Node node = nodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				Node relAttr = attrs.getNamedItem("rel");
				if (relAttr.getNodeValue().compareTo("image") == 0) {
					imageURL = attrs.getNamedItem("href").getNodeValue();
					imageURL = imageURL.replace("spic", "lpic");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageURL;
	}

	public static Bitmap getImage(String strUrl) {
		Bitmap bmp = null;
		try {
			URL url = new URL(strUrl);
			InputStream in = url.openStream();
			bmp = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
		}
		return bmp;
	}

	class GetBookInfoTask extends AsyncTask<String, Void, InputStream> {
		private String mISBN = null;
		private ProgressDialog mProgressDialog = null;
		private Bitmap mCoverImage = null;

		public GetBookInfoTask(Activity app, String isbn) {
			super();
			mISBN = isbn;
			mProgressDialog = new ProgressDialog(app);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setTitle(HomeActivity.getApp().getString(R.string.please_wait));
			mProgressDialog.setMessage(HomeActivity.getApp().getString(R.string.searching_book_info));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			//TODO: should change to use BookGallery later.
			BookShelfView.getInstance().addBookForLoading();
		}

		@Override
		protected InputStream doInBackground(String... params) {

			String url = ISBN_SEARCHURL_DOUBAN + mISBN;
			HttpGet get = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			InputStream inputStream = null;
			try {
				HttpResponse response = client.execute(get);
				inputStream = response.getEntity().getContent();
			} catch (Exception e) {
			}
			String imageURL = parseDoubanXML(inputStream);
			mCoverImage = getImage(imageURL);
			return inputStream;
		}

		@Override
		protected void onPostExecute(InputStream result) {
			super.onPostExecute(result);
			mProgressDialog.hide();
			if (null != mCoverImage) {
				BookShelfItem lastBook = BookShelfItem.lastBookForTest;
				if(null != lastBook)
				{
					lastBook.setCoverImage(mCoverImage);
					lastBook.show();
				}
			}
		}
	}
}