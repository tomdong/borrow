package com.intalker.borrow.cloud;

public class CloudConfig {
	public final static String API_BaseURL = "http://services.sketchbook.cn/test/openshelf/api/index.php?op=";

	// API operations
	public final static String API_Login = "Login";
	public final static String API_SignUp = "SignUp";
	public final static String API_GetUserInfo = "GetUserInfo";
	public final static String API_UploadBooks = "UploadBooks";
	public final static String API_GetOwnedBooks = "GetOwnedBooks";
	public final static String API_SynchronizeOwnedBooks = "SynchronizeOwnedBooks";
	public final static String API_GetFollowings = "GetFollowings";
	public final static String API_DeleteBook = "DeleteBook";
	public final static String API_GetAllUsers = "GetAllUsers";
	public final static String API_Follow = "Follow";
	public final static String API_UnFollow = "UnFollow";
	public final static String API_GetBooksByOwner = "GetBooksByOwner";
	public final static String API_GetUsersByISBN = "GetUsersByISBN";
	public final static String API_SendMessage = "SendMessage";
	
	// API params
	public final static String API_Email = "&email=";
	public final static String API_LocalPwd = "&localpwd=";
	public final static String API_NickName = "&nickname=";
	public final static String API_TOKEN = "&sessionid=";
	public final static String API_ISBN = "&isbn=";
	public final static String API_FriendId = "&friendid=";
	public final static String API_OwnerId = "&ownerid=";
	
	public final static String API_POST_BookInfoList = "bookinfolist";
	
	// DB keys
	// Book
	public final static String DB_Book_OwnerId = "ownerid";
	public final static String DB_Book_ISBN = "isbn";
	public final static String DB_Book_Quantity = "quantity";
	public final static String DB_Book_Description = "description";
	public final static String DB_Book_PublicLevel = "publiclevel";
	public final static String DB_Book_Status = "status";
	
	// User
	public final static String DB_User_Id = "id";
	public final static String DB_User_NickName = "nickname";
	public final static String DB_User_Email = "email";
	public final static String DB_User_RegTime = "registertime";
	public final static String DB_User_Permission = "permission";
	
	// Friend
	public final static String DB_Friend_Alias = "alias";
	public final static String DB_Friend_Group = "group";
	public final static String DB_Friend_Status = "status";
	public final static String DB_Friend_ConnectTime = "connecttime";
	
	// Server Return code
	public final static String ServerReturnCode_Successful = "Successful";
	public final static String ServerReturnCode_NoSuchUser = "NoSuchUser";
	public final static String ServerReturnCode_BadSession = "BadSession";
	public final static String ServerReturnCode_WrongUserNameOrPwd = "WrongUserNameOrPwd";
	public final static String ServerReturnCode_UserNameOccupied = "UserNameOccupied";
	public final static String ServerReturnCode_EmptyResult = "EmptyResult";

	// API return code
	public final static int Return_Unset = -1;
	public final static int Return_OK = 0;
	public final static int Return_TimeOut = 1;
	public final static int Return_NoNetworkConnection = 2;
	public final static int Return_NoSuchUser = 3;
	public final static int Return_WrongUserNameOrPassword = 4;
	public final static int Return_UserNameOccupied = 5;
	public final static int Return_BadToken = 6;
	public final static int Return_NetworkError = 7;
	public final static int Return_UnknownError = 100;
}
