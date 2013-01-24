package com.intalker.borrow.isbn;

import java.io.InputStream;
import java.util.ArrayList;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.isbn.parser.DoubanBookInfoParser;
import com.intalker.borrow.isbn.parser.BookInfoParser;
import com.intalker.borrow.isbn.parser.OpenISBNBookInfoParser;
import com.intalker.borrow.ui.book.BookShelfItem;
import com.intalker.borrow.ui.book.BookShelfView;
import com.intalker.borrow.ui.control.TransparentProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

public class ISBNResolver {

	private static ISBNResolver instance = null;
	private static BookInfoParser parser = null;

	public static ISBNResolver getInstance() {
		if (null == instance) {
			instance = new ISBNResolver();
		}
		return instance;
	}

	public void getBookInfoByISBN(Context context, String isbn) {
		GetBookInfoTask task = new GetBookInfoTask(context, isbn);
		task.execute();
	}
	
	public void batchGetBookInfo(Context context) {
		BatchGetBookInfoTask task = new BatchGetBookInfoTask(context);
		task.execute();
	}
	
	private BookInfoParser getParser() {
		if (null == parser) {
			parser = new DoubanBookInfoParser();
			//parser = new OpenISBNBookInfoParser();
		}
		return parser;
	}

	class GetBookInfoTask extends AsyncTask<String, Void, InputStream> {
		private BookInfoParser isbnParser = null;
		private TransparentProgressDialog mProgressDialog = null;

		public GetBookInfoTask(Context context, String isbn) {
			super();
			isbnParser = getParser();
			isbnParser.reset(isbn);
			mProgressDialog = new TransparentProgressDialog(context, false);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMessage(HomeActivity.getApp().getString(R.string.searching_book_info) + isbn);
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
			mProgressDialog.dismiss();
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
	
	//Batch method
	class BatchGetBookInfoTask extends AsyncTask<String, BookInfo, InputStream> {
		private Context mContext = null;
		private BookInfoParser mParser = null;
		private TransparentProgressDialog mProgressDialog = null;
		private ArrayList<BookInfo> mToProcessBookInfoList = null;
		private int mCurProgress = 0;
		
		public BatchGetBookInfoTask(Context context) {
			super();
			mContext = context;
			mParser = getParser();
			mProgressDialog = new TransparentProgressDialog(mContext, true);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mToProcessBookInfoList = new ArrayList<BookInfo>();
			ArrayList<BookInfo> bookInfoList = AppData.getInstance().getBooks();
			int length = bookInfoList.size();
			for(int i = 0; i < length; ++i)
			{
				BookInfo bookInfo = bookInfoList.get(i);
				if(null != bookInfo && !bookInfo.getInitialized())
				{
					mToProcessBookInfoList.add(bookInfo);
				}
			}
			mProgressDialog.setMax(mToProcessBookInfoList.size());
			mProgressDialog.setProgress(0);
		}

		@Override
		protected void onProgressUpdate(BookInfo... values) {
			BookInfo bookInfo = values[0];
			if (null != bookInfo) {
				if (!bookInfo.getInitialized()) {
					mProgressDialog.setMessage(mContext
							.getString(R.string.searching_book_info)
							+ bookInfo.getISBN());
				} else {
					BookShelfView.getInstance()
							.addBookByExistingInfo(values[0]);
				}
				mProgressDialog.setProgress(mCurProgress);
			}
		}

		@Override
		protected InputStream doInBackground(String... params) {

			int length = mToProcessBookInfoList.size();
			for(int i = 0; i < length; ++i)
			{
				BookInfo bookInfo = mToProcessBookInfoList.get(i);
				publishProgress(bookInfo);

				mParser.reset(bookInfo.getISBN());
				mParser.parse();
				bookInfo.setData(mParser);
				
				mCurProgress = i;
				
				publishProgress(bookInfo);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(InputStream result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			mToProcessBookInfoList.clear();
			Toast.makeText(mContext, "Synchronize done!", Toast.LENGTH_SHORT).show();
		}
	}
}