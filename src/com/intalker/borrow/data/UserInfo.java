package com.intalker.borrow.data;

public class UserInfo {
	private String mNickName = "";
	private String mEmail = "";
	private String mId = "";
	private String mRegTime = "";
	private String mPermission = "";
	
	private static UserInfo mCurLoggedinUser = null;
	
	public static UserInfo getCurLoginUser() {
		return mCurLoggedinUser;
	}

	public static void setCurLoginUser(String nickName, String email,
			String id, String regTime, String permission) {
		mCurLoggedinUser = new UserInfo(nickName, email, id, regTime, permission);
	}
	
	public UserInfo(String nickName, String email,
			String id, String regTime, String permission) {
		mNickName = nickName;
		mEmail = email;
		mId = id;
		mRegTime = regTime;
		mPermission = permission;
	}
	
	public static void clearLoginStatus() {
		mCurLoggedinUser = null;
	}
	
	public String getNickName() {
		return mNickName;
	}

	public String getEmail() {
		return mEmail;
	}

	public String getId() {
		return mId;
	}

	public String getRegTime() {
		return mRegTime;
	}

	public String getPermission() {
		return mPermission;
	}
}
