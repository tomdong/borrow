package com.intalker.borrow.cloud;

import java.security.MessageDigest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;

import com.intalker.borrow.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.borrow.data.UserInfo;

public class CloudApi {
	public final static String API_BaseURL = "http://services.sketchbook.cn/openlib/service_test/api.php?op=";

	// API keys
	public final static String API_Login = "Login";
	public final static String API_SignUp = "SignUp";
	public final static String API_GetUserInfo = "GetUserInfoBySession";
	public final static String API_Email = "&email=";
	public final static String API_LocalPwd = "&localpwd=";
	public final static String API_NickName = "&nickname=";
	public final static String API_TOKEN = "&sessionid=";

	// Parse keys
	public final static String Parse_User_Id = "id";
	public final static String Parse_User_NickName = "nickname";
	public final static String Parse_User_Email = "email";
	public final static String Parse_User_RegTime = "registertime";
	public final static String Parse_User_Permission = "permission";

	// Return code
	public final static String ReturnCode_Successful = "SUCCESSFUL";

	public static String CloudToken = "";

	public static String md5(String val) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(val.getBytes("UTF-8"));
		} catch (Exception e) {
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static UserInfo getUserInfoFromJSON(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);

			String id = jsonObject.getString(Parse_User_Id);
			String nickName = jsonObject.getString(Parse_User_NickName);
			String email = jsonObject.getString(Parse_User_Email);
			String regTime = jsonObject.getString(Parse_User_RegTime);
			String permission = jsonObject.getString(Parse_User_Permission);

			UserInfo userInfo = new UserInfo(id, nickName, email, regTime,
					permission);

			return userInfo;
		} catch (Exception ex) {
		}
		return null;
	}

	public static boolean _login(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return setAccessToken(strResult);
			} else {
				// Network error.
			}
		} catch (Exception e) {
			// Check result string for more info.
		}
		return false;
	}

	public static boolean isLoggedIn() {
		return null != UserInfo.getCurLoginUser();
	}

	public static boolean setAccessToken(String token) {
		if (token.length() == 36) {
			CloudToken = token;
			return true;
		}
		return false;
	}

	public static boolean _signUp(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				return setAccessToken(strResult);
			} else {
				// Network error.
			}
		} catch (Exception e) {
			// Check result string for more info.
		}
		return false;
	}
	
	public static boolean _updateLoggedInUserInfo() {
		String url = CloudApi.API_BaseURL + CloudApi.API_GetUserInfo + CloudApi.API_TOKEN + CloudApi.CloudToken;
        HttpGet getReq = new HttpGet(url);
        try {
                        HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                        String strResult = EntityUtils.toString(httpResponse
                                                                        .getEntity());

                                        UserInfo userInfo = getUserInfoFromJSON(strResult);

                                        if (null != userInfo) {
                                                        UserInfo.setCurLoginUser(userInfo);
                                                        return true;
                                        }
                        } else {
                                        // Network error.
                        }
        } catch (Exception e) {
                        // Check result string for more info.
        }
        return false;
}
	//Client should not call any of the methods above.
	public static void login(Context context, String email, String pwd, ICloudAPITaskListener apiListener) {
		String encryptedPwd = md5(pwd);
		String url = API_BaseURL + API_Login + API_Email + email + API_LocalPwd
				+ encryptedPwd;

		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url, API_Login, apiListener);
		task.execute();
	}
	
	public static void signUp(Context context, String email, String pwd, String nickName, ICloudAPITaskListener apiListener) {
		String encryptedPwd = md5(pwd);
		String url = API_BaseURL + API_SignUp + API_Email + email
				+ API_LocalPwd + encryptedPwd + API_NickName + nickName;
		
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, url, API_SignUp, apiListener);
		task.execute();
	}
	
	public static void updateLoggedInUserInfo(Context context, ICloudAPITaskListener apiListener) {
		CloudAPIAsyncTask task = new CloudAPIAsyncTask(context, "", API_GetUserInfo, apiListener);
		task.execute();
	}

}
