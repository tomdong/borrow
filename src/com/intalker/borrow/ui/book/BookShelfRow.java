package com.intalker.borrow.ui.book;

import com.intalker.borrow.R;
import com.intalker.borrow.util.DensityAdaptor;
import com.intalker.borrow.util.LayoutUtil;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class BookShelfRow extends RelativeLayout {

	public BookShelfRow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createUI();
	}

	private void createUI()
	{
		View board = new View(this.getContext());
		RelativeLayout.LayoutParams boardLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		boardLP.height = LayoutUtil.getShelfBoardHeight();
		boardLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		board.setBackgroundResource(R.drawable.bookshelf_row);
		this.addView(board, boardLP);
		
		// add book test items
		int bookItemWidth = LayoutUtil.getShelfBookItemWidth();
		int bookItemHeight = LayoutUtil.getShelfBookItemHeight();
		int bookItemGap = LayoutUtil.getShelfBookGap();
		for (int i = 0; i < 4; ++i)
		{
			BookShelfItem bookShelfItem = new BookShelfItem(this.getContext());
			RelativeLayout.LayoutParams bookLP = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			bookLP.width = bookItemWidth;
			bookLP.height = bookItemHeight;
			bookLP.leftMargin = (bookItemGap + bookItemWidth) * i + bookItemGap;
			bookLP.topMargin = LayoutUtil.getShelfBookTopMargin();
			this.addView(bookShelfItem, bookLP);
		}
	}
}
