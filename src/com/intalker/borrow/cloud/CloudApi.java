package com.intalker.borrow.cloud;

import java.security.MessageDigest;

import com.intalker.borrow.data.UserInfo;

public class CloudApi {
	public final static String API_BaseURL = "http://services.sketchbook.cn/openlib/service_test/api.php?";
	
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
	
	public static String login(String email, String pwd)
	{
		return "";
	}
	
	public static boolean isLoggedIn()
	{
		return null != UserInfo.getCurLoginUser();
	}
}
