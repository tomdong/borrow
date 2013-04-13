package com.intalker.borrow.data;

public class MessageInfo {
	private String mId = "";
	private String mReplyId = "";
	private String mHostId = "";
	private String mHostName = "";
	private String mFriendId = "";
	private String mFriendName = "";
	private String mISBN = "";
	private String mMessage = "";
	private String mStatus = "";
	private String mTime = "";
	
	public MessageInfo(String id, String replyId, String hostId,
			String hostName, String friendId, String friendName, String isbn,
			String message, String status, String time) {
		this.mId = id;
		this.mReplyId = replyId;
		this.mHostId = hostId;
		this.mHostName = hostName;
		this.mFriendId = friendId;
		this.mFriendName = friendName;
		this.mISBN = isbn;
		this.mMessage = message;
		this.mStatus = status;
		this.mTime = time;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getReplyId() {
		return mReplyId;
	}

	public void setReplyId(String replyId) {
		this.mReplyId = replyId;
	}

	public String getHostId() {
		return mHostId;
	}

	public void setHostId(String hostId) {
		this.mHostId = hostId;
	}

	public String getFriendId() {
		return mFriendId;
	}

	public void setFriendId(String friendId) {
		this.mFriendId = friendId;
	}

	public String getISBN() {
		return mISBN;
	}

	public void setISBN(String isbn) {
		this.mISBN = isbn;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		this.mStatus = status;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}

	public String getHostName() {
		return mHostName;
	}

	public void setHostName(String hostName) {
		this.mHostName = hostName;
	}

	public String getFriendName() {
		return mFriendName;
	}

	public void setFriendName(String friendName) {
		this.mFriendName = friendName;
	}

}
