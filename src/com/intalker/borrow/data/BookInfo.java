package com.intalker.borrow.data;

import com.intalker.borrow.isbn.parser.BookInfoParser;
import com.intalker.borrow.util.StorageUtil;

import android.graphics.Bitmap;

public class BookInfo {
	private String mISBN = "";
	private String mName = "";
	private String mAuthor = "";
	private String mPublisher = "";
	private String mPageCount = "";
	private String mDescription = "";
	private Bitmap mCoverImage = null;
	
	//Currently, only used for synchronizing with cloud
	private boolean mInitialized = true;

	public BookInfo() {
	}

	public BookInfo(String isbn) {
		setISBN(isbn);
	}
	
	public void setInitialized(boolean initialized) {
		mInitialized = initialized;
	}

	public void setISBN(String isbn) {
		mISBN = isbn;
	}

	public void setCoverImage(Bitmap coverImage) {
		mCoverImage = coverImage;
	}

	public void setBookName(String name) {
		mName = name;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public void setPublisher(String publisher) {
		mPublisher = publisher;
	}

	public void setPageCount(String count) {
		mPageCount = count;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}

	public String getISBN() {
		return mISBN;
	}

	public Bitmap getCoverImage() {
		return mCoverImage;
	}

	public String getBookName() {
		return mName;
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
	
	public String getDescription() {
		return mDescription;
	}
	
	public boolean getInitialized() {
		return mInitialized;
	}
	
	public void setData(BookInfoParser parser)
	{
		setISBN(parser.getISBN());
		setBookName(parser.getBookName());
		setAuthor(parser.getAuthor());
		setCoverImage(parser.getCoverImage());
		setDescription(parser.getDescription());
		setPageCount(parser.getPageCount());
		setPublisher(parser.getPublisher());
		setInitialized(true);
	}
}