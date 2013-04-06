package com.intalker.borrow.util;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.cloud.CloudConfig;
import com.intalker.borrow.config.AppConfig;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.data.FriendInfo;
import com.intalker.borrow.data.UserInfo;

public class JSONUtil {
	public static JSONObject makeBookInfoListUploadData() {
		JSONObject jsonData = new JSONObject();
		try {
			JSONArray jsonBookInfoList = new JSONArray();
			ArrayList<BookInfo> bookInfoList = AppData.getInstance().getOwnedBooks();
			for (BookInfo bookInfo : bookInfoList) {
				JSONObject jsonBookInfo = new JSONObject();
				jsonBookInfo.put(CloudConfig.DB_Book_ISBN, bookInfo.getISBN());

				// Hardcode now, will improve later
				jsonBookInfo.put(CloudConfig.DB_Book_Quantity, "1");
				jsonBookInfo.put(CloudConfig.DB_Book_PublicLevel, "all");
				jsonBookInfo.put(CloudConfig.DB_Book_Status, "available");
				// This is not book's official description, it is from owner
				jsonBookInfo.put(CloudConfig.DB_Book_Description, "");
				
				jsonBookInfoList.put(jsonBookInfo);
			}
			jsonData.put(CloudConfig.API_POST_BookInfoList, jsonBookInfoList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonData;
	}

	public static void parseOwnedBooksInfo(String strJSON) {
		try {
			JSONArray jsonBookArray = new JSONArray(strJSON);
			int length = jsonBookArray.length();
			AppData appData = AppData.getInstance();
			ArrayList<BookInfo> curBooks = appData.getOwnedBooks();
			for (int i = 0; i < length; ++i) {
				JSONObject jsonBookItem = (JSONObject) jsonBookArray.get(i);
				if (null != jsonBookItem
						&& jsonBookItem.has(CloudConfig.DB_Book_ISBN)) {
					String isbn = jsonBookItem.getString(CloudConfig.DB_Book_ISBN);
					if (null != isbn && !appData.containsOwnedBook(isbn)) {
						BookInfo bookInfo = null;
						if (AppConfig.useSQLiteForCache) {
							bookInfo = DBUtil.getBookInfo(isbn);
							if (null != bookInfo) {
								bookInfo.setFoundCacheData(true);
							}
						}

						if (null == bookInfo) {
							bookInfo = new BookInfo(isbn);
						}
						
						bookInfo.setInitialized(false);
						curBooks.add(bookInfo);
					}
				}
			}
			//DBUtil.saveBooksOfficialInfo(curBooks);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void parseOthersBooksInfo(String strJSON) {
		try {
			JSONArray jsonBookArray = new JSONArray(strJSON);
			int length = jsonBookArray.length();
			AppData appData = AppData.getInstance();
			ArrayList<BookInfo> othersBooks = appData.getOthersBooks();
			for (int i = 0; i < length; ++i) {
				JSONObject jsonBookItem = (JSONObject) jsonBookArray.get(i);
				if (null != jsonBookItem
						&& jsonBookItem.has(CloudConfig.DB_Book_ISBN)) {
					String isbn = jsonBookItem.getString(CloudConfig.DB_Book_ISBN);
					if (null != isbn && !appData.containsOthersBook(isbn)) {
						BookInfo bookInfo = null;
						if (AppConfig.useSQLiteForCache) {
							bookInfo = DBUtil.getBookInfo(isbn);
							if (null != bookInfo) {
								bookInfo.setFoundCacheData(true);
							}
						}

						if (null == bookInfo) {
							bookInfo = new BookInfo(isbn);
						}
						
						bookInfo.setInitialized(false);
						othersBooks.add(bookInfo);
					}
				}
			}
			//DBUtil.saveBooksOfficialInfo(othersBooks);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static void parseFriendsInfo(String strJSON) {
		try {
			JSONArray jsonFriendArray = new JSONArray(strJSON);
			int length = jsonFriendArray.length();
			AppData appData = AppData.getInstance();
			ArrayList<FriendInfo> curFriends = appData.getFriends();
			for (int i = 0; i < length; ++i) {
				JSONObject jsonFriendItem = (JSONObject) jsonFriendArray.get(i);
				if (null != jsonFriendItem) {
					String id = jsonFriendItem.getString(CloudConfig.DB_User_Id);
					String nickName = jsonFriendItem.getString(CloudConfig.DB_User_NickName);
					String email = jsonFriendItem.getString(CloudConfig.DB_User_Email);
					String regTime = jsonFriendItem.getString(CloudConfig.DB_User_RegTime);
					String permission = jsonFriendItem.getString(CloudConfig.DB_User_Permission);
					
					String alias = jsonFriendItem.getString(CloudConfig.DB_Friend_Alias);
					String group = jsonFriendItem.getString(CloudConfig.DB_Friend_Group);
					String status = jsonFriendItem.getString(CloudConfig.DB_Friend_Status);
					String connectTime = jsonFriendItem.getString(CloudConfig.DB_Friend_ConnectTime);
					
					UserInfo userInfo = new UserInfo(id, nickName, email, regTime, permission);
					FriendInfo friendInfo = new FriendInfo(userInfo, alias, group, status, connectTime);
					
					curFriends.add(friendInfo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void parseAllUsersInfo(String strJSON) {
		try {
			JSONArray jsonFriendArray = new JSONArray(strJSON);
			int length = jsonFriendArray.length();
			AppData appData = AppData.getInstance();
			ArrayList<UserInfo> curAllUsers = appData.getAllUsers();
			for (int i = 0; i < length; ++i) {
				JSONObject jsonFriendItem = (JSONObject) jsonFriendArray.get(i);
				if (null != jsonFriendItem) {
					String id = jsonFriendItem.getString(CloudConfig.DB_User_Id);
					String nickName = jsonFriendItem.getString(CloudConfig.DB_User_NickName);
					String email = jsonFriendItem.getString(CloudConfig.DB_User_Email);
					String regTime = jsonFriendItem.getString(CloudConfig.DB_User_RegTime);
					String permission = jsonFriendItem.getString(CloudConfig.DB_User_Permission);

					UserInfo userInfo = new UserInfo(id, nickName, email, regTime, permission);
					if (appData.isUnfollowed(id)) {
						curAllUsers.add(userInfo);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
