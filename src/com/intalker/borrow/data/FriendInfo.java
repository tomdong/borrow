package com.intalker.borrow.data;

import com.intalker.borrow.util.StringUtil;

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
		if (!StringUtil.isEmpty(mAlias)) {
			return mAlias;
		}
		if (null != mUserInfo) {
			return mUserInfo.getDisplayName();
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
