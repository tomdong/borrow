package com.intalker.borrow.ui.book;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

public class BookGallery extends RelativeLayout {

	private BookShelfView mShelfView = null;
	
	public BookGallery(Context context) {
		super(context);
		
		createUI();
	}

	private void createUI()
	{
		mShelfView = new BookShelfView(this.getContext());
		RelativeLayout.LayoutParams shelfViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		this.addView(mShelfView, shelfViewLP);
	}
	
	public void clearBooks()
	{
		mShelfView.clearShelfRows();
	}
	
	public void resetBookShelf()
	{
		mShelfView.initializeShelfRows();
	}
	
	// for test
	public void fillWithRandomBooks()
	{
		mShelfView.clearShelfRows();
		mShelfView.addRandomBooks();
	}
	
	public void addBook(BookShelfItem bookItem)
	{
	}
}
