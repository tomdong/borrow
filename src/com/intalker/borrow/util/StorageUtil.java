package com.intalker.borrow.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.data.CacheData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class StorageUtil {
	public final static String SDRootPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public final static String CompanyPath = SDRootPath + "/intalker";
	public final static String AppPath = CompanyPath + "/openshelf";
	public final static String DataPath = AppPath + "/data";
	public final static String CachePath = DataPath + "/cache";
	public final static String CacheImagePath = CachePath + "/image";
	public final static String CacheXmlPath = CachePath + "/xml";
	public final static String CacheBookIndexPath = CacheXmlPath + "/book.xml";
	public final static String DatabasePath = AppPath + "/db";

	public static void initialize() {
		checkPath(CacheImagePath);
		checkPath(CacheXmlPath);
		checkPath(DatabasePath);
	}

	private static void checkPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static String getCoverImagePath(String isbn) {
		return StorageUtil.CacheImagePath + "/" + isbn + ".png";
	}
	
	public static Bitmap loadCoverImageFromCache(String isbn) {
		String imagePath = getCoverImagePath(isbn);
		return StorageUtil.getLocalImage(imagePath);
	}
	
	public static Bitmap getLocalImage(String path)
	{
		Bitmap image = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			image = BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
		}
        return image;
	}
	
	public static void loadCachedBooks() {
		//DebugUtil.startTimeRecord();
		ArrayList<BookInfo> cachedBooks = null;
		if (AppConfig.useSQLiteForCache) {
			cachedBooks = DBUtil.loadOwnedBooks();
		} else {
			cachedBooks = XmlUtil.parseCachedBooks();
		}
		//DebugUtil.showTimeRecordResult(HomeActivity.getApp());

		AppData appData = AppData.getInstance();
		appData.clearOwnedBooks();
		for(BookInfo bookInfo : cachedBooks)
		{
			appData.addOwnedBook(bookInfo);
		}
	}
	
	public static void saveCachedBooks() {
		AppData appData = AppData.getInstance();
		if (AppConfig.useSQLiteForCache) {
			DBUtil.clearOwnedBooks();
			ArrayList<BookInfo> ownedBooks = appData.getOwnedBooks();
			DBUtil.saveOwnedBooks(ownedBooks);
			//DBUtil.saveBooksOfficialInfo(ownedBooks);
			
			//ArrayList<BookInfo> othersBooks = appData.getOthersBooks();
			//DBUtil.saveBooksOfficialInfo(othersBooks);
			
			Collection<BookInfo> cachedBooks = CacheData.getCachedBooks();
			DBUtil.saveBooksOfficialInfo(cachedBooks);
		} else {
			try {
				File file = new File(CacheBookIndexPath);
				FileWriter fw = new FileWriter(file, false);
				fw.write(XmlUtil.serializeCachedBooks(appData.getOwnedBooks()));
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Save cover images
		//ArrayList<BookInfo> books = appData.getOwnedBooks();
		Collection<BookInfo> books = CacheData.getCachedBooks();
		for(BookInfo bookInfo : books)
		{
			saveCoverImage(bookInfo);
//			String isbn = bookInfo.getISBN();
//			Bitmap coverImage = bookInfo.getCoverImage();
//			if(isbn.length() > 0 && null != coverImage)
//			{
//				String path = StorageUtil.CacheImagePath + "/" + isbn + ".png";
//				saveImage(path, coverImage);
//			}
		}
		
//		books = AppData.getInstance().getOthersBooks();
//		for(BookInfo bookInfo : books)
//		{
//			String isbn = bookInfo.getISBN();
//			Bitmap coverImage = bookInfo.getCoverImage();
//			if(isbn.length() > 0 && null != coverImage)
//			{
//				String path = StorageUtil.CacheImagePath + "/" + isbn + ".png";
//				saveImage(path, coverImage);
//			}
//		}
		CacheData.clearCachedBooks();
		
		appData.clearOwnedBooks();
		appData.clearOthersBooks();
	}
	
	public static void saveCoverImage(BookInfo bookInfo) {
		String isbn = bookInfo.getISBN();
		Bitmap coverImage = bookInfo.getCoverImage();
		if(isbn.length() > 0 && null != coverImage)
		{
			String path = StorageUtil.CacheImagePath + "/" + isbn + ".png";
			saveImage(path, coverImage);
		}
	}
	
	private static void saveImage(String path, Bitmap image) {
		File file = new File(path);
		if (file.exists() && file.length() > 50) {
			return;
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			image.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
}