package com.intalker.borrow.ui.book;

import java.util.ArrayList;

import com.intalker.borrow.R;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class BookShelfRow extends RelativeLayout {
	public static BookShelfRow firstRow = null;
	private ArrayList<BookShelfItem> mBooks = new ArrayList<BookShelfItem>();
	
	public BookShelfRow(Context context, boolean createRandomBooks) {
		super(context);
		createUI(createRandomBooks);
		if(null == firstRow)
		{
			firstRow = this;
		}
	}

	private void createUI(boolean createRandomBooks)
	{
		View board = new View(this.getContext());
		RelativeLayout.LayoutParams boardLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		boardLP.height = LayoutUtil.getShelfBoardHeight();
		boardLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		board.setBackgroundResource(R.drawable.bookshelf_row);
		this.addView(board, boardLP);
		if (createRandomBooks) {
			// add book test items
			for (int i = 0; i < 4; ++i) {
				addBook(true);
			}
		}
	}
	
	public void addBook(boolean showAtOnce) {
		int bookItemWidth = LayoutUtil.getShelfBookItemWidth();
		int bookItemHeight = LayoutUtil.getShelfBookItemHeight();
		int bookItemGap = LayoutUtil.getShelfBookGap();
		
		BookShelfItem bookShelfItem = new BookShelfItem(
				this.getContext());
		RelativeLayout.LayoutParams bookLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bookLP.width = bookItemWidth;
		bookLP.height = bookItemHeight;
		bookLP.leftMargin = (bookItemGap + bookItemWidth) * mBooks.size()
				+ bookItemGap;
		bookLP.topMargin = LayoutUtil.getShelfBookTopMargin();
		if (!showAtOnce) {
			bookShelfItem.hideBeforeLoaded();
		}
		this.addView(bookShelfItem, bookLP);
		mBooks.add(bookShelfItem);
	}
	
	public boolean isFull() {
		return mBooks.size() >= LayoutUtil.getRowBookCount();
	}
}