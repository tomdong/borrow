package com.intalker.borrow.services;

import com.intalker.borrow.isbn.ISBNResolver;

import android.content.Context;
import android.graphics.Bitmap;

public class BookInfoSearchExecuator  implements IInProcessServiceInterface.IExecuator{
	public BookInfoSearchExecuator(Context c,String isbn)
	{
		mContext = c;
		mISBN = isbn;
	}

	@Override
	public void process() {
		// serach one book info on internet here!
		ISBNResolver.getInstance().getBookInfoByISBN(mContext, mISBN);
	}

	private String mISBN = null;
	private Context mContext = null;
}
