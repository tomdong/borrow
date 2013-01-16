package com.intalker.borrow.isbn;

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
import com.intalker.borrow.isbn.parser.ISBNDoubanParser;
import com.intalker.borrow.isbn.parser.ISBNParserBase;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.book.BookShelfView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ISBNResolver {

	private static ISBNResolver instance = null;

	public static ISBNResolver getInstance() {
		if (null == instance) {
			instance = new ISBNResolver();
		}
		return instance;
	}

	public void getBookInfoByISBN(Activity app, String isbn) {
		GetBookInfoTask task = new GetBookInfoTask(app, isbn);
		task.execute();
	}

	class GetBookInfoTask extends AsyncTask<String, Void, InputStream> {
		private ISBNParserBase isbnParser = null;
		private ProgressDialog mProgressDialog = null;

		public GetBookInfoTask(Activity app, String isbn) {
			super();
			isbnParser = new ISBNDoubanParser(isbn);
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
			BookShelfItem lastBook = BookShelfItem.lastBookForTest;
			if(null != lastBook)
			{
				lastBook.setISBN(isbnParser.getISBN());
			}
		}

		@Override
		protected InputStream doInBackground(String... params) {

			isbnParser.parse();
			
			return null;
		}

		@Override
		protected void onPostExecute(InputStream result) {
			super.onPostExecute(result);
			mProgressDialog.hide();
			BookShelfItem lastBook = BookShelfItem.lastBookForTest;
			if(null != lastBook)
			{
				Bitmap coverImage = isbnParser.getCoverImage();
				if (null != coverImage) {
					lastBook.setCoverImage(coverImage);
					lastBook.setDetailInfo(isbnParser.getBookName(),
							isbnParser.getAuthor(), isbnParser.getPublisher(),
							isbnParser.getPageCount(),
							isbnParser.getDescription());
				}
				else {
					lastBook.setCoverAsUnknown();
				}
				lastBook.show();
			}
		}
	}
}