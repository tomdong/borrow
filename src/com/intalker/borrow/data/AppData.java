package com.intalker.borrow.data;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.intalker.borrow.util.DBUtil;

public class AppData {
	private ArrayList<BookInfo> mOwnedBooks = null;
	private ArrayList<BookInfo> mOthersBooks = null;
	private ArrayList<UserInfo> mAllUsersOnServer = null;
	private ArrayList<UserInfo> mTempUsers = null;
	private ArrayList<FriendInfo> mFriends = null;
	private static AppData instance = null;

	public static AppData getInstance() {
		if (null == instance) {
			instance = new AppData();
		}
		return instance;
	}

	public AppData() {
		mOwnedBooks = new ArrayList<BookInfo>();
		mOthersBooks = new ArrayList<BookInfo>();
		mAllUsersOnServer = new ArrayList<UserInfo>();
		mTempUsers = new ArrayList<UserInfo>();
		mFriends = new ArrayList<FriendInfo>();
	}
	
	public void initialize() {
		mOwnedBooks.clear();
		mOthersBooks.clear();
		mAllUsersOnServer.clear();
		mTempUsers.clear();
		mFriends.clear();
		DBUtil.initialize();
	}

	public ArrayList<BookInfo> getOwnedBooks() {
		return mOwnedBooks;
	}
	
	public ArrayList<BookInfo> getOthersBooks() {
		return mOthersBooks;
	}
	
	public boolean isUnfollowed(String userId) {
		if (UserInfo.getCurLoggedinUser().getId().compareTo(userId) == 0) {
			return false;
		}
		for (FriendInfo friendInfo : mFriends) {
			if (friendInfo.getUserInfo().getId().compareTo(userId) == 0) {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<UserInfo> getAllUsers() {
		return mAllUsersOnServer;
	}
	
	public ArrayList<UserInfo> getTempUsers() {
		return mTempUsers;
	}

	public ArrayList<FriendInfo> getFriends() {
		return mFriends;
	}

	public void addOwnedBook(BookInfo bookInfo) {
		mOwnedBooks.add(bookInfo);
	}

	public void removeFriend(FriendInfo friendInfo) {
		if (mFriends.contains(friendInfo))
			mFriends.remove(friendInfo);
	}

	public void removeFriend(String friendId) {
		int indexToRemove = -1;
		int count = mFriends.size();
		for (int i = 0; i < count; ++i) {
			FriendInfo friendInfo = mFriends.get(i);
			if (friendId.compareTo(friendInfo.getUserInfo().getId()) == 0) {
				indexToRemove = i;
				break;
			}
		}

		if (indexToRemove >= 0) {
			mFriends.remove(indexToRemove);
		}
	}

	public void removeOwnedBook(BookInfo bookInfo) {
		if (mOwnedBooks.contains(bookInfo)) {
			mOwnedBooks.remove(bookInfo);
		}
	}

	public void removeOwnedBook(String isbn) {
		int indexToRemove = -1;
		int count = mOwnedBooks.size();
		for (int i = 0; i < count; ++i) {
			BookInfo bookInfo = mOwnedBooks.get(i);
			if (isbn.compareTo(bookInfo.getISBN()) == 0) {
				indexToRemove = i;
				break;
			}
		}

		if (indexToRemove >= 0) {
			mOwnedBooks.remove(indexToRemove);
		}
	}
	
	private void recycleCoverImages(ArrayList<BookInfo> books) {
		int count = books.size();
		for(int i = 0; i < count; ++i)
		{
			BookInfo bookInfo = books.get(i);
			Bitmap image = bookInfo.getCoverImage();
			if (null != image && !image.isRecycled()) {
				bookInfo.getCoverImage().recycle();
			}
		}
	}

	public void clearOwnedBooks() {
		recycleCoverImages(mOwnedBooks);
		mOwnedBooks.clear();
	}
	
	public void clearOthersBooks() {
		recycleCoverImages(mOthersBooks);
		mOthersBooks.clear();
	}
	
	public void clearAllUsers() {
		mAllUsersOnServer.clear();
	}
	
	public void clearTempUsers() {
		mTempUsers.clear();
	}

	public void clearFriends() {
		mFriends.clear();
	}

	// [TODO] Use another implementation to improve the performance later
	public boolean containsOwnedBook(String isbn) {
		for (BookInfo bookInfo : mOwnedBooks) {
			if (bookInfo.getISBN().compareTo(isbn) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsOthersBook(String isbn) {
		for (BookInfo bookInfo : mOthersBooks) {
			if (bookInfo.getISBN().compareTo(isbn) == 0) {
				return true;
			}
		}
		return false;
	}
}