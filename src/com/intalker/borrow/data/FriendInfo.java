package com.intalker.borrow.data;

public class FriendInfo {
	private UserInfo mUserInfo = null;
	private String mAlias = null;
	private String mGroup = null;
	private String mStatus = null;
	private String mConnectTime = null;

	public FriendInfo(UserInfo userInfo, String alias, String group,
			String status, String connectTime) {
		mUserInfo = userInfo;
		mAlias = alias;
		mGroup = group;
		mStatus = status;
		mConnectTime = connectTime;
	}
	
	public String getDisplayName() {
		if (null != mAlias && mAlias.length() > 0 && mAlias.compareTo("null") != 0) {
			return mAlias;
		}
		if (null != mUserInfo) {
			String nickName = mUserInfo.getNickName();
			if (null != nickName && nickName.compareTo("null") != 0) {
				return nickName;
			} else {
				return mUserInfo.getEmail();
			}
		}
		return "?"; // [TODO] Move to resource file later
	}

	public UserInfo getUserInfo() {
		return mUserInfo;
	}

	public String getAlias() {
		return mAlias;
	}

	public String getGroup() {
		return mGroup;
	}

	public String getStatus() {
		return mStatus;
	}

	public String getConnectTime() {
		return mConnectTime;
	}
}
