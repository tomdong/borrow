package com.intalker.borrow.isbn.parser;

import android.graphics.Bitmap;

public class ISBNParserBase {
	protected String mISBN = "";
	protected String mBookName = "";
	protected String mDescription = "";
	protected String mAuthor = "";
	protected String mPublisher = "";
	protected String mPageCount = "";
	protected Bitmap mCoverImage = null;

	public ISBNParserBase(String isbn) {
		mISBN = isbn;
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