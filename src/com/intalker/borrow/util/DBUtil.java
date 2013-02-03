package com.intalker.borrow.util;

import java.util.ArrayList;

import com.intalker.borrow.data.BookInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class DBUtil {

	private static SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(StorageUtil.DatabasePath
				+ "/openshelf.db", null);
	}

	public static void initialize() {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();

			// Initialize bookinfo table
			String sql = "CREATE TABLE IF NOT EXISTS `bookinfo` ("
					+ "`isbn` TEXT PRIMARY KEY,"
					+ "`bookname` TEXT," + "`publisher` TEXT,"
					+ "`pagecount` TEXT," + "`author` TEXT,"
					+ "`summary` TEXT);";

			db.execSQL(sql);

			// Initialize book table
			sql = "CREATE TABLE IF NOT EXISTS `book` ("
					+ "`id` INTEGER PRIMARY KEY," + "`isbn` TEXT,"
					+ "`quantity` INTEGER DEFAULT 1,"
					+ "`publiclevel` TEXT DEFAULT 'all',"
					+ "`status` TEXT DEFAULT 'available',"
					+ "`description` TEXT);";

			db.execSQL(sql);

			// Initialize profile table
			sql = "CREATE TABLE IF NOT EXISTS `profile` ("
					+ "`id` INTEGER PRIMARY KEY," + "`token` TEXT);";

			db.execSQL(sql);

		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public static void saveToken(String token) {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();
			db.delete("profile", null, null);

			ContentValues vals = new ContentValues();
			vals.put("token", token);
			db.insert("profile", null, vals);
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public static String loadToken() {
		String token = "";
		SQLiteDatabase db = null;
		try {
			db = openDatabase();
			Cursor cursor = db.query("profile", new String[] { "token" }, "",
					null, null, null, null);
			while (cursor.moveToNext()) {
				token = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
		return token;
	}

	private static void addBookData(BookInfo bookInfo, SQLiteDatabase db) {
		if(null == bookInfo)
		{
			return;
		}
		ContentValues bookVals = new ContentValues();
		bookVals.put("isbn", bookInfo.getISBN());
		bookVals.put("quantity", bookInfo.getQuantity());

		// Hard code now
		bookVals.put("publiclevel", "all");
		bookVals.put("status", "available");
		bookVals.put("description", "");

		db.insert("book", null, bookVals);
		
		addBookOfficialInfo(bookInfo, db);
	}
	
	private static void addBookOfficialInfo(BookInfo bookInfo, SQLiteDatabase db) {
		if(null == bookInfo)
		{
			return;
		}

		ContentValues bookInfoVals = new ContentValues();
		bookInfoVals.put("isbn", bookInfo.getISBN());
		bookInfoVals.put("bookname", bookInfo.getBookName());
		bookInfoVals.put("publisher", bookInfo.getPublisher());
		bookInfoVals.put("pagecount", bookInfo.getPageCount());
		bookInfoVals.put("author", bookInfo.getAuthor());
		bookInfoVals.put("summary", bookInfo.getSummary());
		
		db.insertWithOnConflict("bookinfo", null, bookInfoVals, SQLiteDatabase.CONFLICT_IGNORE);
	}

	public static void saveOwnedBook(BookInfo bookInfo) {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();
			addBookData(bookInfo, db);
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}
	
	public static void saveBooksOfficialInfo(ArrayList<BookInfo> othersBooks)
	{
		SQLiteDatabase db = null;
		try {
			db = openDatabase();

			for (BookInfo bookInfo : othersBooks) {
				addBookOfficialInfo(bookInfo, db);
			}
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public static void saveOwnedBooks(ArrayList<BookInfo> books) {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();

			for (BookInfo bookInfo : books) {
				addBookData(bookInfo, db);
			}
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public static void clearOwnedBooks() {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();
			db.delete("book", null, null);
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public static ArrayList<BookInfo> loadOwnedBooks() {
		SQLiteDatabase db = null;
		ArrayList<BookInfo> ownedBooks = new ArrayList<BookInfo>();
		try {
			db = openDatabase();
			Cursor cursor = db.query("book", new String[] { "isbn", "quantity",
					"description", "publiclevel", "status" }, "", null, null,
					null, null);
			while (cursor.moveToNext()) {
				String isbn = cursor.getString(0);
				BookInfo bookInfo = getBookInfo(isbn, db);
				if (null != bookInfo) {
					ownedBooks.add(bookInfo);
				}
			}
			cursor.close();
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
		return ownedBooks;
	}
	
	private static BookInfo getBookInfo(String isbn, SQLiteDatabase db)
	{
		BookInfo bookInfo = null;
		Cursor cursor = db.query("bookinfo", new String[] { "bookname",
				"publisher", "pagecount", "author", "summary" }, "isbn=?",
				new String[] { isbn }, null, null, null);
		while (cursor.moveToNext()) {
			bookInfo = new BookInfo(isbn);

			String bookName = cursor.getString(0);
			String publisher = cursor.getString(1);
			String pageCount = cursor.getString(2);
			String author = cursor.getString(3);
			String summary = cursor.getString(4);

			bookInfo.setCoverImage(StorageUtil.loadCoverImageFromCache(isbn));
			bookInfo.setBookName(bookName);
			bookInfo.setPublisher(publisher);
			bookInfo.setPageCount(pageCount);
			bookInfo.setAuthor(author);
			bookInfo.setSummary(summary);
			break;
		}
		cursor.close();
		return bookInfo;
	}
	
	public static BookInfo getBookInfo(String isbn)
	{
		BookInfo bookInfo = null;
		SQLiteDatabase db = null;
		try {
			db = openDatabase();
			bookInfo = getBookInfo(isbn, db);
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
		return bookInfo;
	}
}
