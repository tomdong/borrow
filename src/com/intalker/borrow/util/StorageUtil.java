package com.intalker.borrow.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;

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
	
	public static Bitmap loadCoverImageFromCache(String isbn) {
		String imagePath = StorageUtil.CacheImagePath + "/" + isbn + ".png";
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
		ArrayList<BookInfo> cachedBooks = null;
		if (AppConfig.useSQLiteForCache) {
			cachedBooks = DBUtil.loadOwnedBooks();
		} else {
			cachedBooks = XmlUtil.parseCachedBooks();
		}

		AppData appData = AppData.getInstance();
		appData.clearBooks();
		for(BookInfo bookInfo : cachedBooks)
		{
			appData.addBook(bookInfo);
		}
	}
	
	public static void saveCachedBooks() {
		
		if (AppConfig.useSQLiteForCache) {
			DBUtil.clearOwnedBooks();
			ArrayList<BookInfo> ownedBooks = AppData.getInstance().getBooks();
			DBUtil.saveOwnedBooks(ownedBooks);
		} else {
			try {
				File file = new File(CacheBookIndexPath);
				FileWriter fw = new FileWriter(file, false);
				fw.write(XmlUtil.serializeCachedBooks(AppData.getInstance()
						.getBooks()));
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Save image info
		ArrayList<BookInfo> books = AppData.getInstance().getBooks();
		for(BookInfo bookInfo : books)
		{
			String isbn = bookInfo.getISBN();
			Bitmap coverImage = bookInfo.getCoverImage();
			if(isbn.length() > 0 && null != coverImage)
			{
				String path = StorageUtil.CacheImagePath + "/" + isbn + ".png";
				saveImage(path, coverImage);
			}
		}
	}
	
	private static void saveImage(String path, Bitmap image) {
		File file = new File(path);
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