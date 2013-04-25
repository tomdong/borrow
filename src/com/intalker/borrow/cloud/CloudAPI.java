package com.intalker.borrow.cloud;

import android.content.Context;
import android.widget.Toast;

import com.intalker.borrow.R;
import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.util.DBUtil;

public class CloudAPI {

	// Token
	public static String CloudToken = "";
	public static boolean IsRunning = false;

	public static void login(Context context, String email, String pwd,
			ICloudAPITaskListener apiListener) {
		String encryptedPwd = CloudUtility.md5(pwd);
		String url = CloudConfig.API_BaseURL + CloudConfig.API_Login + CloudConfig.API_Email + email + CloudConfig.API_LocalPwd
				+ encryptedPwd;

		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url, CloudConfig.API_Login,
				apiListener);
		task.execute();
	}
	
	public static void logout() {
		DBUtil.deleteToken();
		UserInfo.clearLoginStatus();
		CloudToken = "";
	}

	public static void signUp(Context context, String email, String pwd,
			String nickName, ICloudAPITaskListener apiListener) {
		String encryptedPwd = CloudUtility.md5(pwd);
		String url = CloudConfig.API_BaseURL + CloudConfig.API_SignUp + CloudConfig.API_Email + email
				+ CloudConfig.API_LocalPwd + encryptedPwd + CloudConfig.API_NickName + nickName;

		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_SignUp, apiListener);
		task.execute();
	}

	public static void getLoggedInUserInfo(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_GetUserInfo, apiListener);
		task.execute();
	}

	public static void uploadBooks(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_UploadBooks, apiListener);
		task.execute();
	}
	
	public static void getOwnedBooks(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_GetOwnedBooks, apiListener);
		task.execute();
	}
	
	public static void sychronizeOwnedBooks(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_SynchronizeOwnedBooks, apiListener);
		task.execute();
	}
	
	public static void getFollowings(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_GetFollowings, apiListener);
		task.execute();
	}
	
	public static void deleteBook(Context context, String isbn,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_DeleteBook + CloudConfig.API_TOKEN + CloudAPI.CloudToken + CloudConfig.API_ISBN + isbn;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_DeleteBook, apiListener);
		task.execute();
	}
	
	public static void getAllUsers(Context context,
			ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "",
				CloudConfig.API_GetAllUsers, apiListener);
		task.execute();
	}
	
	public static void getUsersByISBN(Context context, String isbn,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetUsersByISBN
				+ CloudConfig.API_ISBN + isbn;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_GetUsersByISBN, apiListener);
		task.execute();
	}
	
	public static void follow(Context context, String friendId,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_Follow + CloudConfig.API_TOKEN + CloudAPI.CloudToken + CloudConfig.API_FriendId + friendId;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_Follow, apiListener);
		task.execute();
	}
	
	public static void unFollow(Context context, String friendId,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_UnFollow + CloudConfig.API_TOKEN + CloudAPI.CloudToken + CloudConfig.API_FriendId + friendId;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_UnFollow, apiListener);
		task.execute();
	}
	
	public static void getBooksByOwner(Context context, String ownerId,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetBooksByOwner + CloudConfig.API_TOKEN + CloudAPI.CloudToken + CloudConfig.API_OwnerId + ownerId;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_GetBooksByOwner, apiListener);
		task.execute();
	}
	
	public static void sendMessage(Context context, String replyId, String friendId, String isbn,
			ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_SendMessage + CloudConfig.API_TOKEN + CloudAPI.CloudToken + CloudConfig.API_ReplyId + replyId + CloudConfig.API_FriendId + friendId + CloudConfig.API_ISBN + isbn;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_SendMessage, apiListener);
		task.execute();
	}
	
	public static void getAllMessages(Context context,boolean showDialog, ICloudAPITaskListener apiListener) {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetAllMessages + CloudConfig.API_TOKEN + CloudAPI.CloudToken;
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url,
				CloudConfig.API_GetAllMessages, apiListener,showDialog);
		task.execute();
	}
	
	public static boolean isSuccessfulWithoutToast(int returnCode) {
		return CloudConfig.Return_OK == returnCode;
	}
	
	public static boolean isSuccessful(Context context, int returnCode) {
		String errorMsg = "";
		switch (returnCode) {
		case CloudConfig.Return_OK:
			return true;
		case CloudConfig.Return_TimeOut:
			errorMsg = context.getString(R.string.api_error_timeout);
			break;
		case CloudConfig.Return_NoNetworkConnection:
			errorMsg = context.getString(R.string.api_error_noconnection);
			break;
		case CloudConfig.Return_NoSuchUser:
			errorMsg = context.getString(R.string.api_error_nosuchuser);
			break;
		case CloudConfig.Return_WrongUserNameOrPassword:
			errorMsg = context.getString(R.string.api_error_wrongusernameorpwd);
			break;
		case CloudConfig.Return_UserNameOccupied:
			errorMsg = context.getString(R.string.api_error_usernameoccupied);
			break;
		case CloudConfig.Return_BadToken:
			errorMsg = context.getString(R.string.api_error_badtoken);
			break;
		case CloudConfig.Return_NetworkError:
			errorMsg = context.getString(R.string.api_error_networkerror);
			break;
		case CloudConfig.Return_UnknownError:
			errorMsg = context.getString(R.string.api_error_unknownerror);
			break;
		default:
			errorMsg = context.getString(R.string.api_error_unknownerror);
			break;
		}
		Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
		return false;
	}
}
