package com.intalker.borrow.data;

public class UserInfo {
	private String mId = "";
	private String mNickName = "";
	private String mEmail = "";
	private String mRegTime = "";
	private String mPermission = "";

	private static UserInfo mCurLoggedinUser = null;

	public static UserInfo getCurLoggedinUser() {
		return mCurLoggedinUser;
	}

//	public static void setCurLoginUser(String id, String nickName,
//			String email, String regTime, String permission) {
//		mCurLoggedinUser = new UserInfo(id, nickName, email, regTime,
//				permission);
//	}
	
	public static void setCurLoginUser(UserInfo userInfo) {
		mCurLoggedinUser = userInfo;
	}

	public UserInfo(String id, String nickName, String email, String regTime,
			String permission) {
		mId = id;
		mNickName = nickName;
		mEmail = email;
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

	@Override
	public String toString() {
		return "ID         : " + mId.toString()
				+ "\nNickName   : " + mNickName
				+ "\nEmail      : " + mEmail
				+ "\nRegTime    : " + mRegTime
				+ "\nPermission : " + mPermission;
	}
}