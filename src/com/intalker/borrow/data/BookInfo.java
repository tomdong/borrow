package com.intalker.borrow.data;

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

	public BookInfo() {

	}

	public BookInfo(String isbn) {
		setISBN(isbn);
	}

	public void setISBN(String isbn) {
		mISBN = isbn;
		tryLoadCachedCoverImage();
	}

	private void tryLoadCachedCoverImage() {
		String imagePath = StorageUtil.CacheImagePath + "/" + mISBN + ".png";
		mCoverImage = StorageUtil.getLocalImage(imagePath);
	}

	public void setCoverImage(Bitmap coverImage) {
		mCoverImage = coverImage;
	}

	public void setName(String name) {
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

	public String getName() {
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
}