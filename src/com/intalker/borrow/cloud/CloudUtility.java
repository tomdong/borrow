package com.intalker.borrow.cloud;

import java.security.MessageDigest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Xml.Encoding;

import com.intalker.borrow.data.AppData;
import com.intalker.borrow.data.UserInfo;
import com.intalker.borrow.util.DBUtil;
import com.intalker.borrow.util.JSONUtil;

public class CloudUtility {
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

			String id = jsonObject.getString(CloudConfig.DB_User_Id);
			String nickName = jsonObject
					.getString(CloudConfig.DB_User_NickName);
			String email = jsonObject.getString(CloudConfig.DB_User_Email);
			String regTime = jsonObject.getString(CloudConfig.DB_User_RegTime);
			String permission = jsonObject
					.getString(CloudConfig.DB_User_Permission);

			UserInfo userInfo = new UserInfo(id, nickName, email, regTime,
					permission);

			return userInfo;
		} catch (Exception ex) {
		}
		return null;
	}

	public static int _login(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (setAccessToken(strResult)) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_WrongUserNameOrPwd) == 0) {
					return CloudConfig.Return_WrongUserNameOrPassword;
				} else {
					return CloudConfig.Return_NetworkError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static boolean isLoggedIn() {
		return null != UserInfo.getCurLoggedinUser();
	}

	public static boolean setAccessToken(String token) {
		if (token.length() == 36) {
			if (token.compareTo(CloudAPI.CloudToken) != 0) {
				CloudAPI.CloudToken = token;
				DBUtil.saveToken(token);
			}
			return true;
		}
		return false;
	}

	public static int _signUp(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				if (setAccessToken(strResult)) {
					return CloudConfig.Return_OK;
				} else {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_UserNameOccupied) == 0) {
						return CloudConfig.Return_UserNameOccupied;
					} else {
						return CloudConfig.Return_UnknownError;
					}
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_UnknownError;
		}
	}

	public static int _getLoggedInUserInfo() {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetUserInfo
				+ CloudConfig.API_TOKEN + CloudAPI.CloudToken;
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				UserInfo userInfo = getUserInfoFromJSON(strResult);

				if (null != userInfo) {
					UserInfo.setCurLoginUser(userInfo);
					return CloudConfig.Return_OK;
				} else {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_NoSuchUser) == 0) {
						return CloudConfig.Return_NoSuchUser;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						return CloudConfig.Return_UnknownError;
					}
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _uploadBooks() {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_UploadBooks
				+ CloudConfig.API_TOKEN + CloudAPI.CloudToken;

		String strResult;
		HttpPost httpRequest = new HttpPost(url);
		try {
			JSONObject data = JSONUtil.makeBookInfoListUploadData();
			String dataStr = data.toString();
			StringEntity se = new StringEntity(dataStr);
			httpRequest.setEntity(se);
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				if (strResult
						.compareTo(CloudConfig.ServerReturnCode_Successful) == 0) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
					return CloudConfig.Return_BadToken;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception ex) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _getOwnedBooks() {
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetOwnedBooks
				+ CloudConfig.API_TOKEN + CloudAPI.CloudToken;

		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (null != strResult && strResult.length() > 0) {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_EmptyResult) == 0) {
						return CloudConfig.Return_OK;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						JSONUtil.parseOwnedBooksInfo(strResult);
					}
					return CloudConfig.Return_OK;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _getFriends() {
		AppData.getInstance().clearFriends();
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetFollowings
				+ CloudConfig.API_TOKEN + CloudAPI.CloudToken;
		
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (null != strResult && strResult.length() > 0) {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_EmptyResult) == 0) {
						return CloudConfig.Return_OK;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						JSONUtil.parseFriendsInfo(strResult);
					}
					return CloudConfig.Return_OK;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _deleteBook(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (strResult
						.compareTo(CloudConfig.ServerReturnCode_Successful) == 0) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
					return CloudConfig.Return_BadToken;
				} else {
					return CloudConfig.Return_NetworkError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _getAllUsers() {
		AppData.getInstance().clearAllUsers();
		String url = CloudConfig.API_BaseURL + CloudConfig.API_GetAllUsers
				+ CloudConfig.API_TOKEN + CloudAPI.CloudToken;
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (null != strResult && strResult.length() > 0) {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_EmptyResult) == 0) {
						return CloudConfig.Return_OK;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						JSONUtil.parseAllUsersInfo(strResult);
					}
					return CloudConfig.Return_OK;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}
	
	public static int _getUsersByISBN(String url) {
		AppData.getInstance().clearTempUsers();
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (null != strResult && strResult.length() > 0) {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_EmptyResult) == 0) {
						return CloudConfig.Return_OK;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						JSONUtil.parseTempUsersInfo(strResult);
					}
					return CloudConfig.Return_OK;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _follow(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (strResult
						.compareTo(CloudConfig.ServerReturnCode_Successful) == 0) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
					return CloudConfig.Return_BadToken;
				} else {
					return CloudConfig.Return_NetworkError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _unFollow(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (strResult
						.compareTo(CloudConfig.ServerReturnCode_Successful) == 0) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
					return CloudConfig.Return_BadToken;
				} else {
					return CloudConfig.Return_NetworkError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}

	public static int _getBooksByOwner(String url) {
		HttpGet getReq = new HttpGet(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				if (null != strResult && strResult.length() > 0) {
					if (strResult
							.compareTo(CloudConfig.ServerReturnCode_EmptyResult) == 0) {
						return CloudConfig.Return_OK;
					} else if (strResult
							.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
						return CloudConfig.Return_BadToken;
					} else {
						JSONUtil.parseOthersBooksInfo(strResult);
					}
					return CloudConfig.Return_OK;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception e) {
			return CloudConfig.Return_NetworkError;
		}
	}
	
	public static int _sendMessage(String url) {
		String strResult;
		HttpPost httpRequest = new HttpPost(url);
		try {
			AppData data = AppData.getInstance();
			String msg = data.getMessage();
			StringEntity se = new StringEntity(msg, "UTF-8");
			httpRequest.setEntity(se);
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				if (strResult
						.compareTo(CloudConfig.ServerReturnCode_Successful) == 0) {
					return CloudConfig.Return_OK;
				} else if (strResult
						.compareTo(CloudConfig.ServerReturnCode_BadSession) == 0) {
					return CloudConfig.Return_BadToken;
				} else {
					return CloudConfig.Return_UnknownError;
				}
			} else {
				return CloudConfig.Return_NetworkError;
			}
		} catch (Exception ex) {
			return CloudConfig.Return_NetworkError;
		}
	}
}
