<?php

/**
 * @author www.intalker.com
 * @copyright 2012
 */
@session_start();
require_once("config.php");

header("Content-Type:text/html;charset=utf-8");
function guid()
{
    mt_srand((double)microtime()*10000);
    $charid = strtolower(md5(uniqid(rand(), true)));
    $hyphen = chr(45);// "-"
    $uuid = //chr(123)// "{"
           //.
           substr($charid, 0, 8).$hyphen
           .substr($charid, 8, 4).$hyphen
           .substr($charid,12, 4).$hyphen
           .substr($charid,16, 4).$hyphen
           .substr($charid,20,12);
           //.chr(125);// "}"
    return $uuid;
}

// Get sessionid
// First try $_GET, then try session and cookie
function getSessionId()
{
    $sessionId = "";
    if(isset($_GET[DB_SESSIONS_ID]))
    {
        $sessionId = $_GET[DB_SESSIONS_ID];
        if(!empty($sessionId))
        {
            return $sessionId;
        }
    }
    if(isset($_POST[DB_SESSIONS_ID]))
    {
        $sessionId = $_POST[DB_SESSIONS_ID];
        if(!empty($sessionId))
        {
            return $sessionId;
        }
    }
    if(isset($_SESSION[SESSION_NAME]))
    {
        $sessionId = $_SESSION[SESSION_NAME];
        if(!empty($sessionId))
        {
            return $sessionId;
        }
    }
    return getSessionFromCookie();
}

function getUserInfoByUserId($uid)
{
    $sql = "select * from " . DB_TABLE_USER . " where " . DB_USER_ID . "=" . wrapStr($uid);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function getUserInfoBySession($sessionId)
{
    $uid = getUserIdBySession($sessionId);
    if(empty($uid))
    {
        return NULL;
    }
    return getUserInfoByUserId($uid);
}

function getNickNameByUserId($uid)
{
    $userInfo = getUserInfoByUserId($uid);
    if(NULL != $userInfo)
    {
        return $userInfo[DB_USER_NICKNAME];
    }
    return NULL;
}

function getValueFromRequest($id)
{
	return getValueFromGet($id);	//Change to getValueFromPOST() later.
}

function getValueFromGet($id)
{
	if(isset($_GET[$id]))
	{
		$val = $_GET[$id];
		return $val;
	}
	return NULL;
}

function getValueFromPOST($id)
{
	if(isset($_POST[$id]))
	{
		$_POST[$id];
		return $val;
	}
	return NULL;
}

function getLocalId($type, $externalid)
{
    $sql = "select * from " . DB_TABLE_OPENID
    . " where " . DB_OPENID_SOURCE . "=" . wrapStr($type)
    . " and " . DB_OPENID_EXTERNALID . "=" . wrapStr($externalid);
    $result = mysql_query($sql);
    $local_id = -1;
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
	        $local_id = $row[DB_OPENID_LOCALID];
        }
    }
    return $local_id;
}

function getBooksByOwner($ownerid)
{
    $sql = "select * from " . DB_TABLE_BOOK . " where " . DB_BOOK_OWNERID . "=" . wrapStr($ownerid);
    $result = mysql_query($sql);
    return $result;
}

function makeUnifiedColName($tableName, $colName)
{
	return $tableName . "." . $colName;
}

function deleteBookByOwnerIdAndISBN($ownerid, $isbn)
{
	$sql = "delete from " . DB_TABLE_BOOK
	. " where " . DB_BOOK_OWNERID . "=" . wrapStr($ownerid)
	. " and " . DB_BOOK_ISBN . "=" . wrapStr($isbn);
    $result = mysql_query($sql);
    return $result;
}

function getFriendsByHost($hostid)
{
//select user.id, user.nickname, user.email, user.registertime, user.permission,
// friend.alias, friend.group, friend.status, friend.connecttime
// from user inner join friend where user.id=friend.friendid and friend.hostid=2;

	$sql = "select " . makeUnifiedColName(DB_TABLE_USER, DB_USER_ID) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_NICKNAME) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_EMAIL) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_REGISTERTIME) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_PERMISSION) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_ALIAS) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_GROUP) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_STATUS) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_CONNECTTIME)
	. " from " . DB_TABLE_USER . " inner join " . DB_TABLE_FRIEND
	. " where " . makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_HOSTID) . "=" . wrapStr($hostid) 
	. " and " . makeUnifiedColName(DB_TABLE_USER, DB_USER_ID) . "=" . makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_FRIENDID);

    $result = mysql_query($sql);
    return $result;
}

function getFollowersByUserId($userId)
{
	$sql = "select " . makeUnifiedColName(DB_TABLE_USER, DB_USER_ID) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_NICKNAME) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_EMAIL) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_REGISTERTIME) . ", "
	. makeUnifiedColName(DB_TABLE_USER, DB_USER_PERMISSION) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_ALIAS) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_GROUP) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_STATUS) . ", "
	. makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_CONNECTTIME)
	. " from " . DB_TABLE_USER . " inner join " . DB_TABLE_FRIEND
	. " where " . makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_FRIENDID) . "=" . wrapStr($userId) 
	. " and " . makeUnifiedColName(DB_TABLE_USER, DB_USER_ID) . "=" . makeUnifiedColName(DB_TABLE_FRIEND, DB_FRIEND_HOSTID);

    $result = mysql_query($sql);
    return $result;
}

function login($email, $local_pwd)
{
    $sql = "select * from " . DB_TABLE_USER . " where "
    . DB_USER_EMAIL . "=" . wrapStr($email) .
    " and " . DB_USER_LOCALPWD . "=" . wrapStr($local_pwd);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            $uid = $row[DB_USER_ID];
            if(NULL != $uid)
            {
            	return createSessionForUser($uid);
            }
        }
    }
    return NULL;
}

function signUp($newUser)
{
    $email = $newUser[DB_USER_EMAIL];
	if(NULL != getUserInfoByEmail($email))
    {
        return USERNAME_OCCUPIED;
    }
    insertRecord(DB_TABLE_USER, $newUser);
    
    return SUCCESSFUL;
}

function createSessionForUser($userId)
{
	if(NULL != $userId && $userId > 0)
	{
		$sid = guid();
	    $sessionRecord[DB_SESSION_ID] = $sid;
        $sessionRecord[DB_SESSION_UID] = $userId;
        insertRecord(DB_TABLE_SESSION, $sessionRecord);
        return $sid;
	}
	return "";
}

function getUserIdBySession($sessionId)
{
	//$sql = "select " . DB_SESSION_UID . " from " . DB_TABLE_SESSION . " where " . DB_SESSION_ID . "=" . wrapStr($sessionId);
	$sql = "select * from " . DB_TABLE_SESSION . " where " . DB_SESSION_ID . "=" . wrapStr($sessionId);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row[DB_SESSION_UID];
        }
    }
    return NULL;
}

function getBookByOwnerAndISBN($ownerid, $isbn)
{
    $sql = "select * from " . DB_TABLE_BOOK . " where " . DB_BOOK_OWNERID . "=" . wrapStr($ownerid)
    . " and " . DB_BOOK_ISBN . "=" . wrapStr($isbn);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function getUserInfoById($uid)
{
    $sql = "select * from " . DB_TABLE_USER . " where " . DB_USER_ID . "=" . wrapStr($uid);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function getAllUsers()
{
    $sql = "select * from " . DB_TABLE_USER . " where 1=1";
    $result = mysql_query($sql);
    return $result;
}

function getUserInfoByEmail($email)
{
    $sql = "select * from " . DB_TABLE_USER . " where " . DB_USER_EMAIL . "=" . wrapStr($email);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function getAllPublicBooks()
{
    $sql = "select * from " . DB_TABLE_BOOK . " where " . DB_BOOK_PUBLICLEVEL . "=" . wrapStr(DB_BOOK_PUBLICLEVEL_ALL);
    $result = mysql_query($sql);
    return $result;
}

function getBookOfficialInfo($isbn)
{
    $sql = "select * from " . DB_TABLE_BOOKINFO . " where " . DB_BOOKINFO_ISBN . "=" . wrapStr($isbn);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function makeFriends($hostId, $friendId)
{
	if(NULL != $hostId && NULL != $friendId)
	{
	    $friendRecord[DB_FRIEND_HOSTID] = $hostId;
        $friendRecord[DB_FRIEND_FRIENDID] = $friendId;
        insertRecord(DB_TABLE_FRIEND, $friendRecord);
        return SUCCESSFUL;
	}
	return UNKNOWN_ERROR;
}

function encodeUserInfo($data)
{
    $userInfo[DB_USER_ID] = $data[DB_USER_ID];
    $userInfo[DB_USER_NICKNAME] = htmlspecialchars_decode($data[DB_USER_NICKNAME], ENT_QUOTES);
    $userInfo[DB_USER_EMAIL] = $data[DB_USER_EMAIL];
    $userInfo[DB_USER_REGISTERTIME] = $data[DB_USER_REGISTERTIME];
    $userInfo[DB_USER_PERMISSION] = $data[DB_USER_PERMISSION];
    return json_encode($userInfo);
}

function encodeBookOfficialInfo($row)
{
    $bookInfo[DB_BOOKINFO_ISBN] = $row[DB_BOOKINFO_ISBN];
    $bookInfo[DB_BOOKINFO_NAME] = htmlspecialchars_decode($row[DB_BOOKINFO_NAME], ENT_QUOTES);
    $bookInfo[DB_BOOKINFO_PUBLISHER] = htmlspecialchars_decode($row[DB_BOOKINFO_PUBLISHER], ENT_QUOTES);
    $bookInfo[DB_BOOKINFO_PAGECOUNT] = $row[DB_BOOKINFO_PAGECOUNT];
    $bookInfo[DB_BOOKINFO_LANGUAGE] = htmlspecialchars_decode($row[DB_BOOKINFO_LANGUAGE], ENT_QUOTES);
    $bookInfo[DB_BOOKINFO_REFLINK] = htmlspecialchars_decode($row[DB_BOOKINFO_REFLINK], ENT_QUOTES);
    $bookInfo[DB_BOOKINFO_REMARK] = $row[DB_BOOKINFO_REMARK];
    $bookInfo[DB_BOOKINFO_ISBN] = $row[DB_BOOKINFO_ISBN];
    $bookInfo[DB_BOOKINFO_ISBN] = $row[DB_BOOKINFO_ISBN];
    return json_encode($bookInfo);
}

function encodeBooksQueryResult($result)
{
    $encodedStr = "";
    if(mysql_num_rows($result) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($result))
        {
            unset($item);
            $item[DB_BOOK_ID] = $row[DB_BOOK_ID];
            $item[DB_BOOK_ISBN] = $row[DB_BOOK_ISBN];
            $item[DB_BOOK_OWNERID] = $row[DB_BOOK_OWNERID];
            $item[DB_BOOK_QUANTITY] = $row[DB_BOOK_QUANTITY];
            $item[DB_BOOK_PREVIEW] = $row[DB_BOOK_PREVIEW];
            $item[DB_BOOK_DESCRIPTION] = $row[DB_BOOK_DESCRIPTION];
            $item[DB_BOOK_PUBLICLEVEL] = $row[DB_BOOK_PUBLICLEVEL];
            $item[DB_BOOK_STATUS] = $row[DB_BOOK_STATUS];
            $item[DB_BOOK_REMARK] = $row[DB_BOOK_REMARK];
            $booksList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($booksList);
    }
    else
    {
        $encodedStr = EMPTY_RESULT;
	}
    return $encodedStr;
}



function encodeFriendsQueryResult($result)
{
    $encodedStr = "";
    if(mysql_num_rows($result) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($result))
        {
            unset($item);
            
			$item[DB_USER_ID] = $row[DB_USER_ID];
			$item[DB_USER_NICKNAME] = htmlspecialchars_decode($row[DB_USER_NICKNAME], ENT_QUOTES);
			$item[DB_USER_EMAIL] = $row[DB_USER_EMAIL];
			$item[DB_USER_REGISTERTIME] = $row[DB_USER_REGISTERTIME];
			$item[DB_USER_PERMISSION] = $row[DB_USER_PERMISSION];
			
			$item[DB_FRIEND_ALIAS] = $row[DB_FRIEND_ALIAS];
			$item[DB_FRIEND_GROUP] = $row[DB_FRIEND_GROUP];
			$item[DB_FRIEND_STATUS] = $row[DB_FRIEND_STATUS];
			$item[DB_FRIEND_CONNECTTIME] = $row[DB_FRIEND_CONNECTTIME];
			
            $friendList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($friendList);
    }
    else
    {
        $encodedStr = EMPTY_RESULT;
	}
    return $encodedStr;
}

function encodeUsersQueryResult($result)
{
    $encodedStr = "";
    if(mysql_num_rows($result) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($result))
        {
            unset($item);
            
			$item[DB_USER_ID] = $row[DB_USER_ID];
			$item[DB_USER_NICKNAME] = htmlspecialchars_decode($row[DB_USER_NICKNAME], ENT_QUOTES);
			$item[DB_USER_EMAIL] = $row[DB_USER_EMAIL];
			$item[DB_USER_REGISTERTIME] = $row[DB_USER_REGISTERTIME];
			$item[DB_USER_PERMISSION] = $row[DB_USER_PERMISSION];
			
            $userList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($userList);
    }
    else
    {
        $encodedStr = EMPTY_RESULT;
	}
    return $encodedStr;
}

//========================================================================
//===========Below code is not necessary for current api script===========
//==========================Intalker @ 2013===============================
//========================================================================









function encodeWorksQueryResult($result)
{
    $encodedStr = "";
    if(mysql_num_rows($result) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($result))
        {
            unset($item);
            $item[DB_WORKS_ID] = $row[DB_WORKS_ID];
            $item[DB_WORKS_TITLE] = htmlspecialchars_decode($row[DB_WORKS_TITLE], ENT_QUOTES);
            $item[DB_WORKS_DESCRIPTION] = htmlspecialchars_decode($row[DB_WORKS_DESCRIPTION], ENT_QUOTES);
            $item[DB_WORKS_STOREID] = $row[DB_WORKS_STOREID];
            $item[DB_WORKS_AUTHORID] = $row[DB_WORKS_AUTHORID];
            $item[DB_WORKS_CREATETIME] = $row[DB_WORKS_CREATETIME];
            $item[DB_WORKS_LASTEDITTIME] = $row[DB_WORKS_LASTEDITTIME];
            $item[DB_TMP_TAGLIST] = @htmlspecialchars_decode($row[DB_TMP_TAGLIST], ENT_QUOTES);
            $item[DB_WORKS_ISPUBLIC] = $row[DB_WORKS_ISPUBLIC];
            $item[DB_WORKS_ATTRIBUTES] = htmlspecialchars_decode($row[DB_WORKS_ATTRIBUTES]);
            $worksList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($worksList);
    }
    return $encodedStr;
}








function addFeedback($newFeedback)
{
    insertRecord(DB_FEEDBACKS, $newFeedback);
    //return FEEDBACK_ADDED_TO_DB;
}

function storeSession($newSessionData, $rememberMe = 1)
{
    // Store session info into DB.
    insertRecord(DB_SESSIONS, $newSessionData);
    
    $sessionId = $newSessionData[DB_SESSIONS_ID];
    $_SESSION[SESSION_NAME] = $sessionId;
    
    if(1 == $rememberMe)
    {
        // Set session info to cookie.
        //setcookie(SESSION_NAME, $newSessionData[DB_SESSIONS_ID], time() + SESSION_EXPIRE_TIME);
        // We control the expire time on server side, so do not need to set the expire time value on client side.
        setcookie(SESSION_NAME, $sessionId, time() + SESSION_EXPIRE_TIME);
    }
    else
    {
        setcookie(SESSION_NAME, $sessionId);
    }
}

function logout()
{
    $sessionId = getSessionId();
    if(NULL != $sessionId)
    {
        $sql = "DELETE from " . DB_SESSIONS . " where " . DB_SESSIONS_ID . "=" . wrapStr($sessionId);
        $result = mysql_query($sql);
        //mysql_free_result($result);
        session_destroy();
        
        if(isset($_COOKIE[SESSION_NAME]))
        {
            setcookie(SESSION_NAME, "", time() - 3600);
        }
    }
    return LOGGED_OUT;
}

// Check if current status is logged in, and try to set current sessionid to the ref param
function isLoggedin(&$sessionId)
{
    $sessionId = getSessionId();
    return checkSession($sessionId);
}

function getSessionFromCookie()
{
    if(isset($_COOKIE[SESSION_NAME]))
    {
        return $_COOKIE[SESSION_NAME];
    }
    return NULL;
}

function checkSession($sessionId)
{
    if(NULL == $sessionId)
    {
        return false;
    }
    $sql = "select * from " . DB_SESSIONS . " where " . DB_SESSIONS_ID . "=" . wrapStr($sessionId);
    $result = mysql_query($sql);
    $isValid = false;
    while($row = mysql_fetch_array($result))
    {
        $createdTime = $row[DB_SESSIONS_CREATEDTIME];
        $now = date("Y-m-d H:i:s");
        $timespan = strtotime($now) - strtotime($createdTime);
        if($timespan < SESSION_EXPIRE_TIME)
        {
            $isValid = true;
            break;
        }
    }
    //mysql_free_result($result);
    return $isValid;
}

//function getLoginNameFromSession($sessionId)
//{
//    $sql = "select * from " . DB_SESSIONS . " where " . DB_SESSIONS_ID . "=" . wrapStr($sessionId);
//    $result = mysql_query($sql);
//    $loginName = "";
//    while($row = mysql_fetch_array($result))
//    {
//        $loginName = $row[DB_SESSIONS_LOGINNAME];
//        break;
//    }
//    return $loginName;
//}

/*
function getUserIdBySession($sessionId)
{
    $sql = "select * from " . DB_SESSIONS . " where " . DB_SESSIONS_ID . "=" . wrapStr($sessionId);
    $result = mysql_query($sql);
    $uid = "";
    while($row = mysql_fetch_array($result))
    {
        $uid = $row[DB_SESSIONS_USERID];
        break;
    }
    //mysql_free_result($result);
    return $uid;
}
*/

//function getUserInfoBySession($sessionId)
//{
//    $loginName = getLoginNameFromSession($sessionId);
//    if(empty($loginName))
//    {
//        return NULL;
//    }
//    return getUserInfo($loginName);
//}


function getAllPublicWorks()
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_ISPUBLIC . "=1";
    $result = mysql_query($sql);
    return $result;
}

function getAllWorksByAuthorId($authorId)
{
	//$sql = test() . " where " . DB_WORKS . "." . DB_WORKS_AUTHORID . "=" . wrapStr($authorId). " order by " . DB_WORKS_LASTEDITTIME . " desc";
	
	
//	$sql = "select " . DB_WORKS . ".*," . DB_TMP_SET . "." . DB_TMP_TAGLIST
//	. " from (" . DB_WORKS . " inner join (select " . DB_TAGS_WORKSID . ", " . DB_TAGS_TAG . ", group_concat(" . DB_TAGS_TAG . " separator '|') as " . DB_TMP_TAGLIST . " from " . DB_TAGS . " group by " . DB_TAGS_WORKSID . ") as " . DB_TMP_SET . " on " . DB_TMP_SET . "." . DB_TAGS_WORKSID . "=" . DB_WORKS . "." . DB_WORKS_ID . ")";
	
	$sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHORID . "=" . wrapStr($authorId). " order by " . DB_WORKS_LASTEDITTIME . " desc";
//	echo $sql;
//	exit();
    //$sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHORID . "=" . wrapStr($authorId). " order by " . DB_WORKS_LASTEDITTIME . " desc";
    $result = mysql_query($sql);
    return $result;
}

function getPrivateWorksByAuthorId($uid)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHORID . "=" . wrapStr($uid) . " and " . DB_WORKS_ISPUBLIC . "=0";
    $result = mysql_query($sql);
    return $result;
}

function getPublicWorksByAuthorId($uid)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHORID . "=" . wrapStr($uid) . " and " . DB_WORKS_ISPUBLIC . "=1";
    $result = mysql_query($sql);
    return $result;
}

function getPrivateWorksByAuthor($author)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHOR . "=" . wrapStr($author) . " and " . DB_WORKS_ISPUBLIC . "=0";
    $result = mysql_query($sql);
    return $result;
}

function getPublicWorksByAuthor($author)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_AUTHOR . "=" . wrapStr($author) . " and " . DB_WORKS_ISPUBLIC . "=1";
    $result = mysql_query($sql);
    return $result;
}

//function deleteWorks($workId)
//{
//    $sql = "DELETE from " . DB_WORKS . " where " . DB_WORKS_ID . "=" . $workId;
//    $result = mysql_query($sql);
//    No need to remove cascade, because we have changed the type of tables to innoDB.
//    $sql = "DELETE from " . DB_TAGS . " where " . DB_TAGS_WORKSID . "=" . $workId;
//    $result = mysql_query($sql);
//    return $result;
//}

function getWorksIdByStoreId($storeid)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_STOREID . "=" . wrapStr($storeid);
    $result = mysql_query($sql);
    $wid = "";
    while($row = mysql_fetch_array($result))
    {
        $wid = $row[DB_WORKS_ID];
        break;
    }
    //mysql_free_result($result);
    return $wid;
}

function getStoreIdByWorksId($worksid)
{
    $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_ID . "=" . wrapStr($worksid);
    $result = mysql_query($sql);
    $storeid = "";
    while($row = mysql_fetch_array($result))
    {
        $storeid = $row[DB_WORKS_STOREID];
        break;
    }
    //mysql_free_result($result);
    return $storeid;
}

function test()
{
	//select works.*, tmp.taglist from (works INNER JOIN (select worksid, group_concat(tag separator '|') as taglist from tags group by worksid) as tmp on tmp.worksid = works.id) where id=345;
	$sql = "select " . DB_WORKS . ".*," . DB_TMP_SET . "." . DB_TMP_TAGLIST
	. " from (" . DB_WORKS . " inner join (select " . DB_TAGS_WORKSID . ", " . DB_TAGS_TAG . ", group_concat(" . DB_TAGS_TAG . " separator '|') as " . DB_TMP_TAGLIST . " from " . DB_TAGS . " group by " . DB_TAGS_WORKSID . ") as " . DB_TMP_SET . " on " . DB_TMP_SET . "." . DB_TAGS_WORKSID . "=" . DB_WORKS . "." . DB_WORKS_ID . ")";
	//. " where " . DB_WORKS_AUTHORID . "=" . $authorid;
	return $sql;
}

function addWorks($newWorks, $tags)
{
    insertRecord(DB_WORKS, $newWorks);
//    $taglist = explode("|", $tags);
//    $storeid = $newWorks[DB_WORKS_STOREID];
//    $worksid = getWorksIdByStoreId($storeid);
//    //if(!empty($tag))
//    {
//	    foreach($taglist as $tag)
//	    {
//	        $tagRecord[DB_TAGS_TAG] = $tag;
//	        $tagRecord[DB_TAGS_WORKSID] = $worksid;
//	        insertRecord(DB_TAGS, $tagRecord);
//	    }
//    }
    return WORKS_ADDED;
}

function updateWorks($oldid, $newWorks, $tags)
{
	$oldstoreid = getStoreIdByWorksId($oldid);
	
	$conditions[DB_WORKS_ID] = $oldid;
    $newValues[DB_WORKS_STOREID] = $newWorks[DB_WORKS_STOREID];
    $newValues[DB_WORKS_LASTEDITTIME] = $newWorks[DB_WORKS_LASTEDITTIME];
    if(isset($newWorks[DB_WORKS_ATTRIBUTES]))
    {
    	$newValues[DB_WORKS_ATTRIBUTES] = $newWorks[DB_WORKS_ATTRIBUTES];
    }
    updateRecord(DB_WORKS, $conditions, $newValues);
    
    deleteTagsOfWorks($oldid);
    
    $taglist = explode("|", $tags);
	foreach($taglist as $tag)
    {
        $tagRecord[DB_TAGS_TAG] = $tag;
        $tagRecord[DB_TAGS_WORKSID] = $oldid;
        insertRecord(DB_TAGS, $tagRecord);
    }

    deleteImagesOfWorks($oldstoreid);

    return WORKS_ADDED;
}

/*
function signUp($newUser)
{
    $email = $newUser[DB_USER_EMAIL];
    if(DB_USER_TYPE_NATIVE == $newUser[DB_USER_TYPE])
    {
	    if(NULL != getUserInfoByEmail($email))
	    {
	        return USERNAME_OCCUPIED;
	    }
    }
    insertRecord(DB_USERS, $newUser);
    return SIGN_UP_SUCCESSFUL;
}
*/

function deleteUser($loginname)
{
    $sql = "DELETE from " . DB_USERS . " where " . DB_USER_LOGINNAME . "=" . wrapStr($loginname);
    $result = mysql_query($sql);
    //mysql_free_result($result);
}

function checkUserPwd($loginname, $password, $encryptMode = MD5)
{
    $userInfo = getUserInfo($loginname);
    if(NULL == $userInfo)
    {
        return NO_SUCH_USER;
    }
    $encryptedPwdInDB = "";
    $encryptedPwd = "";
    switch($encryptMode)
    {
        case MD5:
            $encryptedPwdInDB = $userInfo[DB_USER_PASSWORD_MD5];
            $encryptedPwd = md5($password);
        break;
        case SHA1:
            $encryptedPwdInDB = $userInfo[DB_USER_PASSWORD_SHA1];
            $encryptedPwd = sha1($password);
        break;
        default:
        break;
    }
    if($encryptedPwd == $encryptedPwdInDB)
    {
        return AUTHENTICATION_PASSED;
    }
    else
    {
        return WRONG_PWD;
    }
}

function checkEmailPwd($email, $password, &$uid, $encryptMode = MD5)
{
    $userInfo = getUserInfoByEmail($email);
    if(NULL == $userInfo)
    {
        return NO_SUCH_USER;
    }
    $encryptedPwdInDB = "";
    $encryptedPwd = "";
    switch($encryptMode)
    {
        case MD5:
            $encryptedPwdInDB = $userInfo[DB_USER_PASSWORD_MD5];
            $encryptedPwd = md5($password);
        break;
        case SHA1:
            $encryptedPwdInDB = $userInfo[DB_USER_PASSWORD_SHA1];
            $encryptedPwd = sha1($password);
        break;
        default:
        break;
    }
    if($encryptedPwd == $encryptedPwdInDB)
    {
        $uid = $userInfo[DB_USER_ID];
        return AUTHENTICATION_PASSED;
    }
    else
    {
        return WRONG_PWD;
    }
}

//function getLoginNameByEmail($email)
//{
//    $userInfo = getUserInfoByEmail($email);
//    if(NULL != $userInfo)
//    {
//        return $userInfo[DB_USER_LOGINNAME];
//    }
//    return NULL;
//}

/*
function getUserInfoByEmail($email)
{
    $sql = "select * from " . DB_USERS . " where " . DB_USER_EMAIL . "=" . wrapStr($email);
//    echo $sql;
//    exit();
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}
*/

function getNickName($loginname)
{
    $userInfo = getUserInfo($loginname);
    if(NULL != $userInfo)
    {
        return $userInfo[DB_USER_NICKNAME];
    }
    return NULL;
}

function getUserInfo($loginname)
{
    $sql = "select * from " . DB_USERS . " where " . DB_USER_LOGINNAME . "=" . wrapStr($loginname);
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function updateRecord($tableName, $conditions, $newValues)
{
    $condition = "";
    $newValue = "";
    foreach($conditions as $key=>$val)
    {
        if(strlen($condition) == 0)
        {
            $condition = $key . "=" . wrapStr($val);
        }
        else
        {
            $condition .= " and " . $key . "=" . wrapStr($val);
        }
    }
    foreach($newValues as $key=>$val)
    {
        if(strlen($newValue) == 0)
        {
            $newValue = $key . "=" . wrapStr($val);
        }
        else
        {
            $newValue .= ", " . $key . "=" . wrapStr($val);
        }
    }
    $sql = "update " . $tableName . " set " . $newValue . " where " . $condition;
    $result = mysql_query($sql);
    return $result;
}

function insertRecord($tableName, $record)
{
    $keys = "";
    $vals = "";
    foreach($record as $key=>$val)
    {
        if(strlen($keys) == 0)
        {
            $keys = wrapCol($key);
            $vals = wrapStr($val);
        }
        else
        {
            $keys .= "," . wrapCol($key);
            $vals .= "," . wrapStr($val);
        }
    }
    $sql = "insert into " . $tableName . "(" . $keys . ") values(" . $vals . ")";
//    echo $sql;exit();
    $result = mysql_query($sql);
    return $result;
}

function deleteTagsOfWorks($worksId)
{
	$sql = "delete from " . DB_TAGS . " where " . DB_TAGS_WORKSID . "=" . wrapStr($worksId);
	$result = mysql_query($sql);
}

function deleteWorks($storeId)
{
    $sessionId = "";
    if(isLoggedin($sessionId))
    {
        $uid = getUserIdBySession($sessionId);
        $sql = "select * from " . DB_WORKS . " where " . DB_WORKS_STOREID . "=" . wrapStr($storeId) . " and " . DB_WORKS_AUTHORID . "=" . wrapStr($uid);
        
        $result = mysql_query($sql);
        if(mysql_num_rows($result) > 0)
        {
            unset($sql);
            unset($result);
            $sql = "delete from " . DB_WORKS . " where " . DB_WORKS_STOREID . "=" . wrapStr($storeId);
            //echo $sql;exit();
            $result = mysql_query($sql);
            
            unset($sql);
            $worksId = getWorksIdByStoreId($storeId);
            deleteTagsOfWorks($worksId);
            
			deleteImagesOfWorks($storeId);
            
            return WORKS_DELETED;
        }
        else
        {
            return WORKS_CANNOT_DELETE;
        }
    }
    return NEED_LOGIN;
}

function deleteImagesOfWorks($storeId)
{
	$pathPrefix = UPLOAD_PATH . $storeId;
	@unlink($pathPrefix . ".png");
	@unlink($pathPrefix . "_outline.png");
	@unlink($pathPrefix . "_painting.png");
	@unlink($pathPrefix . "_overlay.png");
	@unlink($pathPrefix . "_thumb.png");
}

function wrapStr($str)
{
    return "'" . htmlspecialchars($str, ENT_QUOTES) . "'";
    //return "'" . $str . "'";
}

function wrapCol($str)
{
    return "`" . $str . "`";
}

function getFileStream($storeid)
{
    return file_get_contents(UPLOAD_PATH . $storeid . ".h2d");
}

function getPreviewImageStream($storeid)
{
    return file_get_contents(UPLOAD_PATH . $storeid . ".png");
}

function getOutlineImageStream($storeid)
{
    return file_get_contents(UPLOAD_PATH . $storeid . "_outline.png");
}

function getPaintingImageStream($storeid)
{
    return file_get_contents(UPLOAD_PATH . $storeid . "_painting.png");
}

function getOverlayImageStream($storeid)
{
    return file_get_contents(UPLOAD_PATH . $storeid . "_overlay.png");
}

function showTable($tableName)
{
    $sql = "select * from " . $tableName;
    $result = mysql_query($sql);
    showQueryResult($result);
}

/*
function encodeUserInfo($data)
{
    $userInfo[DB_USER_ID] = $data[DB_USER_ID];
    $userInfo[DB_USER_NICKNAME] = htmlspecialchars_decode($data[DB_USER_NICKNAME], ENT_QUOTES);
    $userInfo[DB_USER_EMAIL] = $data[DB_USER_EMAIL];
    $userInfo[DB_USER_TYPE] = $data[DB_USER_TYPE];
    $userInfo[DB_USER_CAREER] = $data[DB_USER_CAREER];
    $userInfo[DB_USER_ACCEPTNEWS] = $data[DB_USER_ACCEPTNEWS];
    $userInfo[DB_USER_SELFINTRO] = htmlspecialchars_decode($data[DB_USER_SELFINTRO], ENT_QUOTES);
    return json_encode($userInfo);
}
*/

function encodeWorksQueryResultWithoutTag($result)
{
    $encodedStr = "";
    if(mysql_num_rows($result) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($result))
        {
            unset($item);
            $item[DB_WORKS_ID] = $row[DB_WORKS_ID];
            $item[DB_WORKS_TITLE] = htmlspecialchars_decode($row[DB_WORKS_TITLE], ENT_QUOTES);
            $item[DB_WORKS_DESCRIPTION] = htmlspecialchars_decode($row[DB_WORKS_DESCRIPTION], ENT_QUOTES);
            $item[DB_WORKS_STOREID] = $row[DB_WORKS_STOREID];
            $item[DB_WORKS_AUTHORID] = $row[DB_WORKS_AUTHORID];
            $item[DB_WORKS_CREATETIME] = $row[DB_WORKS_CREATETIME];
            $item[DB_WORKS_LASTEDITTIME] = $row[DB_WORKS_LASTEDITTIME];
            $item[DB_TMP_TAGLIST] = "";//@htmlspecialchars_decode($row[DB_TMP_TAGLIST], ENT_QUOTES);
            $item[DB_WORKS_ISPUBLIC] = $row[DB_WORKS_ISPUBLIC];
            $item[DB_WORKS_ATTRIBUTES] = htmlspecialchars_decode($row[DB_WORKS_ATTRIBUTES]);
            $worksList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($worksList);
    }
    return $encodedStr;
}

function duplicateWorksById($originalStoreId, $newStoreId, $sessionId)
{
	$prefixOriginal = UPLOAD_PATH . $originalStoreId;
	$prefixNew = UPLOAD_PATH . $newStoreId;
	$originalPath = $prefixOriginal . ".png";
	$newPath = $prefixNew . ".png";

	if(copy($originalPath, $newPath))
	{
		@copy($prefixOriginal . "_outline.png", $prefixNew . "_outline.png");
		@copy($prefixOriginal . "_painting.png", $prefixNew . "_painting.png");
		@copy($prefixOriginal . "_overlay.png", $prefixNew . "_overlay.png");
		@copy($prefixOriginal . ".sbs", $prefixNew . ".sbs");
		
		$works[DB_WORKS_TITLE] = "duplicated works";
		$works[DB_WORKS_AUTHORID] = getUserIdBySession($sessionId);
		$works[DB_WORKS_ISPUBLIC] = 0;
		$curTime = date("Y-m-d H:i:s");
		$works[DB_WORKS_CREATETIME] = $curTime;
		$works[DB_WORKS_LASTEDITTIME] = $curTime;
		$works[DB_WORKS_STOREID] = $newStoreId;
		addWorks($works, "tags");
		
		$sql = "select * from " . DB_WORKS . " where " . DB_WORKS_STOREID . " = " . wrapStr($newStoreId);
		$result = mysql_query($sql);
		echo encodeWorksQueryResult($result);
	}
	else
	{
		echo DUPLICATE_ERROR;
	}
}

function showGalleryFromQueryResult($result)
{
    if(mysql_num_rows($result) < 1)
    {
        echo GALLERY_EMPTY;
        return;
    }
    $isEmpty = true;
    //echo "<table border='1'>";
    $maxCol = GALLERY_COL_COUNT;
    $colNo = 0;
    while($row = mysql_fetch_array($result))
    {
    	$title = $row[DB_WORKS_TITLE];
    	if($title == "sample works")
    	{
    		continue;
    	}
    	if($isEmpty)
    	{
    		$isEmpty = false;
    		echo "<table border='1'>";
    	}
        if($colNo % $maxCol == 0)
        {
            echo "<tr>";
        }
        
        $description = $row[DB_WORKS_DESCRIPTION];
        $storeid = $row[DB_WORKS_STOREID];
        $authorid = $row[DB_WORKS_AUTHORID];
        $dateTime = $row[DB_WORKS_CREATETIME];
        $imagePath = "../uploadedfiles/" . $storeid . "_thumb.png";
        $imageFullSizePath = "../uploadedfiles/" . $storeid . ".png";
        if(!file_exists($imagePath))
        {
        	$imagePath = $imageFullSizePath;
        }
        echo "<td><a href = \"" . $imageFullSizePath . "\" target = \"_blank\"><img width=220 height=165 src=\"" . $imagePath
        . "\" title=\"" . $title
        . " by " . getNickNameByUserId($authorid)
        . "\"></a></td>";
        
        $colNo++;
        if($colNo >= $maxCol)
        {
            $colNo = 0;
            echo "</tr>";
        }
    }
    if($isEmpty)
    {
    	echo GALLERY_EMPTY;
    }
    else
    {
    	echo "</table>";
    }
}

function showQueryResult($result)
{
    $num = mysql_num_fields($result);
    echo "<table border='1'>";
    echo "<tr>";
    for ($i = 0; $i<$num; $i++)
    {
        echo "<th>" . mysql_field_name($result, $i) . "</th>";
    }
    echo "</tr>";
    while($row = mysql_fetch_row($result))
    {
        echo "<tr>";
        for ($i = 0; $i<$num; $i++)
        {
            $val = $row[$i];
            if(NULL == $val)
            {
                echo "<td>&nbsp;</td>";
            }
            else
            {
                echo "<td>" . processStrForDisplay($val) . "</td>";
            }
        }
        echo "</tr>";
    }
    echo "</table>";
}

function processStrForDisplay($str)
{
    $newStr = htmlspecialchars($str, ENT_QUOTES);
    return str_replace("&lt;br&gt;", "<br>", $newStr);
}

function getPrivateWorksByTag($tag, $uid = "")
{
    if(empty($uid))
    {
        $sessionid = "";
        if(!isLoggedIn($sessionid))
        {
            echo NEED_LOGIN;
            return;
        }
        $uid = getUserIdBySession($sessionid);
    }
    $sql =
    "select " . DB_TAGS . "." . DB_TAGS_WORKSID . " from " . DB_TAGS . "," . DB_WORKS
    . " where " . DB_WORKS . "." . DB_WORKS_ID . "=" . DB_TAGS . "." . DB_TAGS_WORKSID
    . " and " . DB_WORKS . "." . DB_WORKS_AUTHORID . "=" . wrapStr($uid)
    . " and " . DB_WORKS . "." . DB_WORKS_ISPUBLIC . "=0"
    . " and " . DB_TAGS_TAG . "=" . wrapStr($tag)
    . " order by " . DB_WORKS_LASTEDITTIME . " desc";
    $result = mysql_query($sql);
    return $result;
}

function getPublicWorksByTag($tag)
{
    $sql =
    "select " . DB_WORKS . ".* from " . DB_TAGS . "," . DB_WORKS
    . " where " . DB_WORKS . "." . DB_WORKS_ID . "=" . DB_TAGS . "." . DB_TAGS_WORKSID
    . " and " . DB_WORKS . "." . DB_WORKS_ISPUBLIC . "=1"
    . " and " . DB_TAGS_TAG . "=" . wrapStr($tag)
    . " order by " . DB_WORKS_LASTEDITTIME . " desc";
    $result = mysql_query($sql);
    return $result;
}

function getAllWorksByTagForCurUser($tag, $uid = "")
{
    if(empty($uid))
    {
        $sessionid = "";
        if(!isLoggedIn($sessionid))
        {
            echo NEED_LOGIN;
            return;
        }
        $uid = getUserIdBySession($sessionid);
    }
    $nestedQuerySQL = "((select * from " . DB_TAGS . " where " . DB_TAGS_WORKSID . " in (select distinct " . DB_TAGS_WORKSID . " from " . DB_TAGS . " where " . DB_TAGS_TAG . "=" . wrapStr($tag) . ")) tmpTable)";
    $sql = "select " . DB_WORKS . ".*," . DB_TMP_SET . "." . DB_TMP_TAGLIST
	. " from (" . DB_WORKS . " inner join (select " . DB_TAGS_WORKSID . ", " . DB_TAGS_TAG . ", group_concat(" . DB_TAGS_TAG . " separator '|') as " . DB_TMP_TAGLIST . " from " . $nestedQuerySQL . " group by " . DB_TAGS_WORKSID . ") as " . DB_TMP_SET . " on " . DB_TMP_SET . "." . DB_TAGS_WORKSID . "=" . DB_WORKS . "." . DB_WORKS_ID . ")"
	. " order by " . DB_WORKS_LASTEDITTIME;
//    $sql =
//    //"select " . DB_WORKS . ".* from " . DB_TAGS . "," . DB_WORKS
//    test()
//    . " where " . DB_TMP_SET . "." . DB_TAGS_TAG . "=" . wrapStr($tag)
//    . " and " . DB_WORKS . "." . DB_WORKS_AUTHORID . "=" . wrapStr($uid)
//    . " order by " . DB_WORKS_LASTEDITTIME . " desc";
//    echo $sql;
//    exit();
    $result = mysql_query($sql);
    return $result;
}

function getAllWorksByKeywordsForCurUser_backup($keywords, $uid = "")
{
    if(empty($uid))
    {
        $sessionid = "";
        if(!isLoggedIn($sessionid))
        {
            echo NEED_LOGIN;
            return;
        }
        $uid = getUserIdBySession($sessionid);
    }
    
    $keys = explode("|", $keywords);
    $condition = "";
    foreach($keys as $key)
    {
        if(empty($key))
        {
            continue;
        }
        if(strlen($condition) == 0)
        {
            $condition .= DB_WORKS_TITLE . " like " . wrapStr("%" . $key . "%");
        }
        else
        {
            $condition .= " and " . DB_WORKS_TITLE . " like " . wrapStr("%" . $key . "%");
        }
    }
    $result = "";
    if(strlen($condition) > 0)
    {
        $sql =
        "select * from " . DB_WORKS . " where " . $condition
        . " and " . DB_WORKS_AUTHORID . "=" . wrapStr($uid);
        $result = mysql_query($sql);
    }
    return $result;
}

function getAllWorksByKeywordsForCurUser($keywords, $uid = "")
{
    if(empty($uid))
    {
        $sessionid = "";
        if(!isLoggedIn($sessionid))
        {
            echo NEED_LOGIN;
            return;
        }
        $uid = getUserIdBySession($sessionid);
    }
    
    $keys = explode("|", $keywords);
    $condition = "";
    foreach($keys as $key)
    {
        if(empty($key))
        {
            continue;
        }
        if(strlen($condition) == 0)
        {
            $condition .= "(" . DB_WORKS_TITLE . " like " . wrapStr("%" . $key . "%") . " or " . DB_TMP_SET . "." . DB_TMP_TAGLIST . " like " . wrapStr("%" . $key . "%") . ")";
        }
        else
        {
            //$condition .= " and " . DB_WORKS_TITLE . " like " . wrapStr("%" . $key . "%");
            $condition .= " and " . "(" . DB_WORKS_TITLE . " like " . wrapStr("%" . $key . "%") . " or " . DB_TMP_SET . "." . DB_TMP_TAGLIST . " like " . wrapStr("%" . $key . "%") . ")";
        }
    }
    $result = "";
    if(strlen($condition) > 0)
    {
        $sql =
        test() . " where " . $condition
        . " and " . DB_WORKS_AUTHORID . "=" . wrapStr($uid)
        . " order by " . DB_WORKS_LASTEDITTIME . " desc";
//        echo $sql;
//        exit();
        $result = mysql_query($sql);
    }
    return $result;
}

function changePassword($uid, $newPwd)
{
    $conditions[DB_USER_ID] = $uid;
    $newValues[DB_USER_PASSWORD_MD5] = md5($newPwd);
    $newValues[DB_USER_PASSWORD_SHA1] = sha1($newPwd);
    updateRecord(DB_USERS, $conditions, $newValues);
}

function changeProfile($email, $nickName, $selfIntro)
{
    $conditions[DB_USER_EMAIL] = $email;
    $newValues[DB_USER_NICKNAME] = $nickName;
    $newValues[DB_USER_SELFINTRO] = $selfIntro;
    updateRecord(DB_USERS, $conditions, $newValues);
}

function changeWorksAttributes($worksId, $title, $tags, $isPublic)
{
    $conditions[DB_WORKS_ID] = $worksId;
    $newValues[DB_WORKS_TITLE] = $title;
    $newValues[DB_WORKS_TAGS] = $tags;
    $newValues[DB_WORKS_ISPUBLIC] = $isPublic;
    updateRecord(DB_WORKS, $conditions, $newValues);

    $sql = "delete from " . DB_TAGS . " where " . DB_TAGS_WORKSID . "=" . wrapStr($worksId);
    mysql_query($sql);
    $tagArray = explode("|", $tags);
    foreach($tagArray as $tag)
    {
        $tagRecord[DB_TAGS_TAG] = $tag;
        $tagRecord[DB_TAGS_WORKSID] = $worksId;
        insertRecord(DB_TAGS, $tagRecord);
    }
}

function canEditThisWorks($worksId, $uid)
{
    $sql = "select " . DB_WORKS_AUTHORID . " from " . DB_WORKS . " where " . DB_WORKS_ID . "=" . wrapStr($worksId);
    $result = mysql_query($sql);
    $authorId = "-1";
    if(mysql_num_rows($result) > 0)
    {
        while($row = mysql_fetch_array($result))
        {
            $authorId = $row[0];
            break;
        }
    }
    return $authorId == $uid;
}

function encodeSendMailTaskData($data)
{
    $encodedStr = "";
    if(mysql_num_rows($data) > 0)
    {
        $index = 0;
        while($row = mysql_fetch_array($data))
        {
            unset($item);
            $item[DB_MAILS_ID] = $row[DB_MAILS_ID];
            $item[DB_MAILS_FROM] = $row[DB_MAILS_FROM];
            $item[DB_MAILS_TO] = $row[DB_MAILS_TO];
            $item[DB_MAILS_CC] = $row[DB_MAILS_CC];
            $item[DB_MAILS_SUBJECT] = $row[DB_MAILS_SUBJECT];
            $item[DB_MAILS_CONTENT] = $row[DB_MAILS_CONTENT];
            $taskList[$index] = $item;
            ++$index;
        }
        $encodedStr = json_encode($taskList);
    }
    return $encodedStr;
}

function getSendMailTaskData()
{
    $sql = "select * from " . DB_MAILS . " where " . DB_MAILS_STATUS . "=" . wrapStr("initial") . " and " . DB_MAILS_TRIEDTIME . "<5";
    $result = mysql_query($sql);
    $encodedTaskData = encodeSendMailTaskData($result);
    mysql_free_result($result);
    return $encodedTaskData;
}

function getTriedTime($mailId)
{
    $sql = "select " . DB_MAILS_TRIEDTIME . " from " . DB_MAILS . " where " . DB_MAILS_ID . "=" . wrapStr($mailId);
    $result = mysql_query($sql);
    $triedTime = 0;
    if(mysql_num_rows($result) > 0)
    {
        while($row = mysql_fetch_array($result))
        {
            $triedTime = $row[0];
            break;
        }
    }
    return $triedTime;
}

function verifyResetPwdToken($token)
{
    $sql = "select * from " . DB_RESETPWD . " where " . DB_RESETPWD_ONETIMETOKEN . "=" . wrapStr($token) . " and " . DB_RESETPWD_STATUS . "=" . wrapStr("valid");
    $result = mysql_query($sql);
    return mysql_num_rows($result) > 0;
}

function displayPwdPage($token)
{
    if(verifyResetPwdToken($token))
    {
        echo 
        "<html>
            <head>
                <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />
                <title>Change password</title>
                <script type=\"text/javascript\" src=\"validateResetPwd.js\">
                </script>
            </head>
            <body>
                <center>
                <br /><br />
                <h3>test</h3>
                <br />
                <form id=\"resetpwd_form\" action=\"sbo-api.php?op=UpdatePwd&key=" . $token . "\" enctype=\"multipart/form-data\" onsubmit=\"return check_resetpwd_input();\" method=\"post\">
                    <table border=\"none\">
                        <tr>
                            <td>pwd:</td>
                            <td><input type=\"password\" name=\"pwd\"/></td>
                        </tr>
                        <tr>
                            <td>repeat:</td>
                            <td><input type=\"password\" name=\"pwd_repeat\"/></td>
                        </tr>
                    </table>
                    <input type=\"submit\" value=\"submit\" />
                </form>
                </center>
            </body>
        </html>";
    }
    else
    {
        echo INVALID_RESETPWD_TOKEN;
    }
}

function resetPwd($token, $pwd)
{
    if(verifyResetPwdToken($token))
    {
        $sql = "select " . DB_RESETPWD_UID . " from " . DB_RESETPWD . " where " . DB_RESETPWD_ONETIMETOKEN . "=" . wrapStr($token);
        $result = mysql_query($sql);
        $uid = "";
        if(mysql_num_rows($result) > 0)
        {
            while($row = mysql_fetch_array($result))
            {
                if(!empty($row))
                {
                    $uid = $row[0];
                    break;
                }
            }
        }
        changePassword($uid, $pwd);
        
        // Invalid the one-time-token
        $conditions[DB_RESETPWD_ONETIMETOKEN] = $token;
        $newValues[DB_RESETPWD_STATUS] = "invalid";
        updateRecord(DB_RESETPWD, $conditions, $newValues);
        echo PASSWORD_CHANGED;
    }
    else
    {
        echo INVALID_RESETPWD_TOKEN;
    }
}
function getUserIdByResetPwdToken($token)
{
    $sql = "select " . DB_RESETPWD_UID . " from " . DB_RESETPWD . " where " . DB_RESETPWD_ONETIMETOKEN . "=" . wrapStr($token);
    $result = mysql_query($sql);
    $uid = "";
    if(mysql_num_rows($result) > 0)
    {
        while($row = mysql_fetch_array($result))
        {
            if(!empty($row))
            {
                $uid = $row[0];
                break;
            }
        }
    }
    return $uid;
}

function getWeiboUserInfo($externalId)
{
    $sql = "select * from " . DB_USERS . " where " . DB_USER_TYPE . "=" . wrapStr(DB_USER_TYPE_WEIBO) . " and " . DB_USER_EXTERNALID . "=" . wrapStr($externalId);
    //echo $sql;exit();
    $result = mysql_query($sql);
    while($row = mysql_fetch_array($result))
    {
        if(NULL != $row)
        {
            return $row;
        }
    }
    return NULL;
}

function changePwd($uid, $oldpwd, $newpwd)
{
	$sql = "select * from " . DB_USERS . " where " . DB_USER_ID . "=" . wrapStr($uid) . " and " . DB_USER_PASSWORD_MD5 . "=" . wrapStr(md5($oldpwd));
	$result = mysql_query($sql);
	if(mysql_num_rows($result) > 0)
    {
    	changePassword($uid, $newpwd);
    	return PASSWORD_CHANGED;
    }
    return WRONG_PWD;
}

function makeThumb($storeid)
{
	$thumb_filepath = UPLOAD_PATH . $storeid . "_thumb.png";
	$fullsize_filepath = UPLOAD_PATH . $storeid . ".png";
	$src_image = ImageCreateFromPNG($fullsize_filepath);
	$srcW = ImageSX($src_image);
	$srcH = ImageSY($src_image);
	$scale = 1;
	if($srcW > THUMBNAIL_WIDTH)
	{
		$scale = THUMBNAIL_WIDTH / $srcW;
	}
	$dstW = $srcW * $scale;
	$dstH = $srcH * $scale;
	$dst_image = ImageCreateTrueColor($dstW, $dstH);
	imagecopyresampled($dst_image, $src_image, 0, 0, 0, 0, $dstW, $dstH, $srcW, $srcH);
	imagepng($dst_image, $thumb_filepath, 9);
}

function makeShareImage($storeid, $sharePath)
{
	$original_filepath = UPLOAD_PATH . $storeid . ".png";
	$src_image = ImageCreateFromPNG($original_filepath);
	$originalFileSize = FileSize($original_filepath);
	
	$scale = 1;
	if(SHARE_IMAGE_MAX_SIZE < $originalFileSize)
	{
		$srcW = ImageSX($src_image);
		$srcH = ImageSY($src_image);
		if($srcW / $srcH > SHARE_IMAGE_PROPER_WIDTH / SHARE_IMAGE_PROPER_HEIGHT)
		{
			$scale = SHARE_IMAGE_PROPER_WIDTH / $srcW;
		}
		else
		{
			$scale = SHARE_IMAGE_PROPER_HEIGHT / $srcH;
		}
	}
	$dstW = $srcW * $scale;
	$dstH = $srcH * $scale;
	$dst_image = ImageCreateTrueColor($dstW, $dstH);
	imagecopyresampled($dst_image, $src_image, 0, 0, 0, 0, $dstW, $dstH, $srcW, $srcH);
	imagepng($dst_image, $sharePath, 9);
}

function createSamplesForUser($uid)
{
	$dir = GALLERYSAMPLE_PATH;
	$files = read_files($dir);
	$sampleIdList = array();
	foreach ($files as $file)
	{
		$ext = pathinfo($file, PATHINFO_EXTENSION);
		if($ext == "sbs")
		{
			$fileName = pathinfo($file, PATHINFO_BASENAME);
			$sampleIdList[] = substr($fileName, 0, strlen($fileName) - 4);
		}
	}
	sort($sampleIdList);
	foreach ($sampleIdList as $id)
	{
		$newStoreId = guid();
		
		$prefixOriginal = GALLERYSAMPLE_PATH . $id;
		$prefixNew = UPLOAD_PATH . $newStoreId;
		$originalPath = $prefixOriginal . ".png";
		$newPath = $prefixNew . ".png";
	
		if(copy($originalPath, $newPath))
		{
			@copy($prefixOriginal . "_outline.png", $prefixNew . "_outline.png");
			@copy($prefixOriginal . "_painting.png", $prefixNew . "_painting.png");
			@copy($prefixOriginal . "_overlay.png", $prefixNew . "_overlay.png");
			@copy($prefixOriginal . ".sbs", $prefixNew . ".sbs");
			
			$works[DB_WORKS_TITLE] = "sample works";
			$works[DB_WORKS_AUTHORID] = $uid;
			$works[DB_WORKS_ISPUBLIC] = 0;
			$curTime = date("Y-m-d H:i:s");
			$works[DB_WORKS_CREATETIME] = $curTime;
			$works[DB_WORKS_LASTEDITTIME] = $curTime;
			$works[DB_WORKS_STOREID] = $newStoreId;
			addWorks($works, "tags");
		}
	}
}

function read_files($dir)
{
	$ret = array();
	if ($handle = opendir($dir))
	{
		while (false !== ($file = readdir($handle)))
		{
			if($file != '.' && $file !== '..')
			{
				$cur_path = $dir . DIRECTORY_SEPARATOR . $file;
				if(is_file($cur_path))
				{
					$ret[] = $cur_path;
				}
			}
		}
		closedir($handle);
	}
	return $ret;
}
?>