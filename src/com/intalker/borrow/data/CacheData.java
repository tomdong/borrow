package com.intalker.borrow.data;

import java.util.Collection;
import java.util.HashMap;

public class CacheData {
	private static HashMap<String, BookInfo> mBookCacheMap = null;

	public static void initialize() {
		mBookCacheMap = new HashMap<String, BookInfo>();
	}
	
	public static void clearCachedBooks() {
		mBookCacheMap.clear();
	}
	
	public static Collection<BookInfo> getCachedBooks()
	{
		return mBookCacheMap.values();
	}

	public static void cacheBookInfo(BookInfo bookInfo) {
		String isbn = bookInfo.getISBN();
		if (mBookCacheMap.containsKey(isbn)) {
			mBookCacheMap.remove(isbn);
		}
		mBookCacheMap.put(isbn, bookInfo.clone());
	}

	public static BookInfo getCachedBookInfo(String isbn) {
		if (mBookCacheMap.containsKey(isbn)) {
			return mBookCacheMap.get(isbn).clone();
		}
		return null;
	}
}
