package com.intalker.borrow.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil {

	private static SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(StorageUtil.DatabasePath + "/openshelf.db",
				null);
	}

	public static void initialize() {
		SQLiteDatabase db = null;
		try {
			db = openDatabase();

			// Initialize bookinfo table
			String sql = "CREATE TABLE IF NOT EXISTS `bookinfo` ("
					+ "`id` INTEGER PRIMARY KEY," + "`isbn` TEXT,"
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
		} catch (Exception ex) {
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
		return token;
	}
}
