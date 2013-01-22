package com.intalker.borrow.isbn.parser;

import android.graphics.Bitmap;

public class BookInfoParser {
	protected String mISBN = "";
	protected String mBookName = "";
	protected String mDescription = "";
	protected String mAuthor = "";
	protected String mPublisher = "";
	protected String mPageCount = "";
	protected Bitmap mCoverImage = null;

	public BookInfoParser() {
	}
	
	public void reset(String isbn) {
		mISBN = isbn;
		mBookName = "";
		mDescription = "";
		mAuthor = "";
		mPublisher = "";
		mPageCount = "";
		mCoverImage = null;
	}
	
	public void parse() {
	}

	public String getISBN() {
		return mISBN;
	}

	public String getBookName() {
		return mBookName;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public String getPublisher() {
		return mPublisher;
	}

	public String getPageCount() {
		return mPageCount;
	}

	public Bitmap getCoverImage() {
		return mCoverImage;
	}
}