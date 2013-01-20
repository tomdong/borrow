package com.intalker.borrow.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intalker.borrow.cloud.CloudAPI;
import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.BookInfo;
import com.intalker.borrow.data.UserInfo;

public class JSONUtil {
	public static JSONObject makeBookInfoListUploadData() {
		JSONObject jsonData = new JSONObject();
		try {
			UserInfo curUser = UserInfo.getCurLoggedinUser();
			jsonData.put(CloudAPI.DB_Book_OwnerId, curUser.getId());
			JSONArray jsonBookInfoList = new JSONArray();
			ArrayList<BookInfo> bookInfoList = AppData.getInstance().getBooks();
			for(BookInfo bookInfo : bookInfoList)
			{
				JSONObject jsonBookInfo = new JSONObject();
				jsonBookInfo.put(CloudAPI.DB_Book_ISBN, bookInfo.getISBN());
				// This is not book's official description, it is from owner
				jsonBookInfo.put(CloudAPI.DB_Book_Description, bookInfo.getDescription());
				// Hardcode now, will improve later
				jsonBookInfo.put(CloudAPI.DB_Book_Quantity, "1");
				jsonBookInfo.put(CloudAPI.DB_Book_PublicLevel, "all");
				jsonBookInfo.put(CloudAPI.DB_Book_Status, "available");
				jsonBookInfoList.put(jsonBookInfo);
			}
			jsonData.put(CloudAPI.JSON_Book_InfoList, jsonBookInfoList);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return jsonData;
	}
}
