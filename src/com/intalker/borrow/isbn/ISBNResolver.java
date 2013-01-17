package com.intalker.borrow.isbn;

import java.io.InputStream;
import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.isbn.parser.DoubanBookInfoParser;
import com.intalker.borrow.isbn.parser.BookInfoParser;
import com.intalker.borrow.isbn.parser.OpenISBNBookInfoParser;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.book.BookShelfView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
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
		private BookInfoParser isbnParser = null;
		private ProgressDialog mProgressDialog = null;

		public GetBookInfoTask(Activity app, String isbn) {
			super();
			isbnParser = new DoubanBookInfoParser(isbn);
			//isbnParser = new OpenISBNBookInfoParser(isbn);
			mProgressDialog = new ProgressDialog(app);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setTitle(HomeActivity.getApp().getString(R.string.please_wait));
			mProgressDialog.setMessage(HomeActivity.getApp().getString(R.string.searching_book_info) + isbn);
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
				lastBook.setDetailInfo(isbnParser.getBookName(),
						isbnParser.getAuthor(), isbnParser.getPublisher(),
						isbnParser.getPageCount(),
						isbnParser.getDescription());
				if (null != coverImage) {
					lastBook.setCoverImage(coverImage);
				}
				else {
					lastBook.setCoverAsUnknown();
				}
				lastBook.show();
			}
		}
	}
}