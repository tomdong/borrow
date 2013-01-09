<?php

/**
 * @author www.intalker.com
 * @copyright 2012
 */
require_once("db_con.php");

// Timezone
date_default_timezone_set('Asia/Brunei');

define("SHARE_IMAGE_URL", "http://share.sketchbook.cn/images/");
define("APIBASEURL", "http://services.sketchbook.cn/sbo-api.php?");

// User
define("DB_TABLE_USER", "user");

define("DB_USER_ID", "id");
define("DB_USER_NICKNAME", "nickname");
define("DB_USER_EMAIL", "email");
define("DB_USER_REGISTERTIME", "registertime");
define("DB_USER_PERMISSION", "permission");
define("DB_USER_LOCALPWD", "localpwd");
define("DB_USER_REMARK", "remark");

// OpenID
define("DB_TABLE_OPENID", "openid");

define("DB_OPENID_LOCALID", "localid");
define("DB_OPENID_SOURCE", "source");
define("DB_OPENID_EXTERNALID", "externalid");

// Book
define("DB_TABLE_BOOK", "book");

define("DB_BOOK_ID", "id");
define("DB_BOOK_ISBN", "isbn");
define("DB_BOOK_OWNERID", "ownerid");
define("DB_BOOK_QUANTITY", "quantity");
define("DB_BOOK_PREVIEW", "preview");
define("DB_BOOK_DESCRIPTION", "description");
define("DB_BOOK_PUBLICLEVEL", "publiclevel");
define("DB_BOOK_STATUS", "status");
define("DB_BOOK_REMARK", "remark");

define("DB_BOOK_PUBLICLEVEL_ALL", "all");
define("DB_BOOK_PUBLICLEVEL_MYFRIENDS", "myfriends");
define("DB_BOOK_PUBLICLEVEL_SPECIFIED", "specified");
define("DB_BOOK_PUBLICLEVEL_ONLYME", "onlyme");

define("DB_TABLE_BOOKINFO", "bookinfo");

define("DB_BOOKINFO_ISBN", "isbn");
define("DB_BOOKINFO_NAME", "name");
define("DB_BOOKINFO_PUBLISHER", "publisher");
define("DB_BOOKINFO_PAGECOUNT", "pagecount");
define("DB_BOOKINFO_LANGUAGE", "language");
define("DB_BOOKINFO_REFLINK", "reflink");
define("DB_BOOKINFO_REMARK", "remark");

// Session
define("DB_TABLE_SESSION", "session");

define("DB_SESSION_ID", "id");
define("DB_SESSION_UID", "uid");
define("DB_SESSION_DATETIME", "datetime");

// GET/POST param keys
define("PARAM_KEY_SESSIONID", "sessionid");

/*
define("DB_MAILS", "mails");
define("DB_SESSIONS", "sessions");
define("DB_FEEDBACKS", "feedbacks");
define("DB_RESETPWD", "resetpwd");


define("DB_USERS", "users");
define("DB_WORKS", "works");
define("DB_TAGS", "tags");
define("DB_MAILS", "mails");
define("DB_SESSIONS", "sessions");
define("DB_FEEDBACKS", "feedbacks");
define("DB_RESETPWD", "resetpwd");

define("DB_USER_ID","id");
define("DB_USER_TYPE","type");
define("DB_USER_TYPE_NATIVE","Native");
define("DB_USER_TYPE_WEIBO","Weibo");
define("DB_USER_EXTERNALID","externalid");
define("DB_USER_LOGINNAME","loginname");
define("DB_USER_NICKNAME","nickname");
define("DB_USER_PASSWORD_MD5","pwd_md5");
define("DB_USER_PASSWORD_SHA1","pwd_sha1");
define("DB_USER_EMAIL","email");
define("DB_USER_CAREER","career");
define("DB_USER_ACCEPTNEWS","acceptnews");
define("DB_USER_SELFINTRO","selfintro");
define("DB_USER_REMARK","remark");

define("OLD_PWD", "oldpwd");
define("NEW_PWD", "newpwd");

define("DB_WORKS_ID","id");
define("DB_WORKS_TITLE","title");
//define("DB_WORKS_TAGS","tags");	//Discard this column
define("DB_WORKS_AUTHORID","authorid");
define("DB_WORKS_ISPUBLIC","ispublic");
define("DB_WORKS_CREATETIME","createtime");
define("DB_WORKS_LASTEDITTIME","lastedittime");
define("DB_WORKS_DESCRIPTION","description");
define("DB_WORKS_STOREID","storeid");
define("DB_WORKS_ATTRIBUTES","attributes");
define("DB_WORKS_REMARK","remark");

define("DB_TAGS_ID","id");
define("DB_TAGS_TAG","tag");
define("DB_TAGS_WORKSID","worksid");

define("DB_TMP_SET","tmp");
define("DB_TMP_TAGLIST","taglist");

define("DB_MAILS_ID", "id");
define("DB_MAILS_FROM", "from");
define("DB_MAILS_TO", "to");
define("DB_MAILS_CC", "cc");
define("DB_MAILS_SUBJECT", "subject");
define("DB_MAILS_CONTENT", "content");
define("DB_MAILS_DATETIME", "datetime");
define("DB_MAILS_TRIEDTIME", "triedtime");
define("DB_MAILS_STATUS", "status");

define("DB_SESSIONS_ID","sessionid");
//define("DB_SESSIONS_LOGINNAME","loginname");
//define("DB_SESSIONS_EMAIL","email");
define("DB_SESSIONS_USERID","uid");
define("DB_SESSIONS_CREATEDTIME","createdtime");

define("DB_FEEDBACKS_ID","id");
define("DB_FEEDBACKS_CATEGORY","category");
define("DB_FEEDBACKS_DATETIME","datetime");
define("DB_FEEDBACKS_TITLE","subject");
define("DB_FEEDBACKS_DESCRIPTION","description");
define("DB_FEEDBACKS_EMAIL","email");

define("DB_RESETPWD_ID", "id");
define("DB_RESETPWD_ONETIMETOKEN", "onetimetoken");
define("DB_RESETPWD_UID", "uid");
define("DB_RESETPWD_REQUESTTIME", "requesttime");
define("DB_RESETPWD_STATUS", "status");

// Session
define("SESSION_EXPIRE_TIME", 315360000);  // 10 years
define("SESSION_NAME", "sbo_sid");
define("SESSION_REMEMBERME","rememberme");

// Password
define("PASSWORD","password");
define("MD5","md5");
define("SHA1","sha1");

define("USERNAME_OCCUPIED","UserNameOccupied");
define("SUCCESSFUL","Successful");

// Message string
define("AUTHENTICATION_PASSED","Authentication passed.");
define("NO_SUCH_USER","No such user.");
define("WRONG_PWD","Wrong password.");

define("SIGN_UP_SUCCESSFUL","SignUpsuccessful.");
define("LOGGED_OUT","Logged Out.");
define("WORKS_ADDED", "New works has been added successfully.");
define("WORKS_CANNOT_DELETE", "Works cannot be deleted.");
define("WORKS_DELETED", "Works has been deleted.");
define("FAILED_TO_UPLOAD", "Failed to upload ");
define("CONTINUE_TO_UPLOAD", "Continue to upload.");
define("NEED_LOGIN", "Please login.");
define("FEEDBACK_MAIL_SENT", "The feedback has been sent via mail.");
define("FEEDBACK_ADDED_TO_DB", "The feedback has been store to DB.");
define("INVALID_RESETPWD_TOKEN" , "Invalid token to reset password.");
define("PASSWORD_CHANGED" , "Password has been changed.");

define("USERINFO_UPDATED" , "User info has been updated.");
define("WORKS_ATTRIBUTES_UPDATED" , "Attributes have been updated.");
define("CANNOT_UPDATED_WORKS_ATTRIBUTES" , "You are not the author of this works.");
define("CANNOT_SUBMIT_AGAIN" , "You cannot submitted the request again.");
define("DUPLICATE_ERROR", "Duplicate error.");
define("ARG_ERROR", "Arguments error.");

// Feedback mail
define("FEEDBACK_MAIL_TITLE", "[.cn feedback]");
define("SBO_CN_USER", ".cn user");
define("MAIL_ADDRESS", "sketchbook.online.support@autodesk.com");
define("QUESTION", "question");
define("IDEA", "idea");
define("LOVE", "love");
define("NOTHING_IN_FEEDBACK", "Please input something.");

// Store path
define("UPLOAD_PATH","uploadedfiles/");
define("GALLERYSAMPLE_PATH","gallery_samples/");
define("SHARE_PATH","../share/images/");

// Gallery
define("GALLERY_COL_COUNT", 5);
define("GALLERY_EMPTY", "Empty.");
define("THUMBNAIL_WIDTH", 220);
define("SHARE_IMAGE_PROPER_WIDTH", 640);
define("SHARE_IMAGE_PROPER_HEIGHT", 480);
define("SHARE_IMAGE_MAX_SIZE", 1048576);	//1M

//
define("RESULT_YES", "Yes.");
define("RESULT_NO", "No.");
define("KEYWORDS", "keywords");
*/
?>
