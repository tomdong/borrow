package com.intalker.borrow.data;

import com.intalker.borrow.util.StorageUtil;

import android.graphics.Bitmap;

public class BookInfo {
	private String mISBN = "";
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

	public String getISBN() {
		return mISBN;
	}

	public Bitmap getCoverImage() {
		return mCoverImage;
	}
}