package com.intalker.borrow.services;

import android.graphics.Bitmap;

import com.intalker.borrow.services.IInProcessServiceInterface.IRequest;
import com.intalker.borrow.services.IInProcessServiceInterface.IResult;




public class BookInfoSearchContract  implements IInProcessServiceInterface.IContract{
	public class BookInfoRequest implements IInProcessServiceInterface.IRequest
	{
		private String mISBN = null;
	}
	
	public class BookInfoResult implements IInProcessServiceInterface.IResult
	{
		private String mName = null;
		private String mDescription = null;
		private Bitmap mCover = null;
		
		public void set(String name,String desp,Bitmap bitmap)
		{
			mName = name;
			mDescription = desp;
			mCover=  bitmap;
		}
		
		public String getName()
		{
			return mName;
		}
		
		public String toString()
		{
			return mName + " : " +mDescription;
		}
	}
	
	private BookInfoRequest mBookInfoReq = new BookInfoRequest();
	private BookInfoResult  mBookInfoResult = null;
	
	@Override
	public IRequest getRequest() {
		return  mBookInfoReq;
	}

	@Override
	public IResult getResult() {
		return mBookInfoResult;
	}

	@Override
	public void process() {
		// TODO: using the isbn string to query on internet and fill the result!
		mBookInfoResult = new BookInfoResult();
		mBookInfoResult.set("Name!!", "Description ", null);
	}

}
