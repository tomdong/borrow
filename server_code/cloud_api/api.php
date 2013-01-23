<?php

/**
 * @author www.intalker.com
 * @copyright 2012
 */

require_once("util.php");

set_time_limit(0);

if(!isset($_GET["op"]))
{
	exit("@copyright 2012, www.intalker.com");
}
$op = $_GET["op"];
switch($op)
{
	case "GetUserInfoById":
		$con = connectDB();
		$uid = getValueFromRequest(DB_USER_ID);
		if(NULL != $uid)
		{
			$user_info = getUserInfoByUserId($uid);
			//echo "Hi! " . $user_info[DB_USER_NICKNAME];
			echo encodeUserInfo($user_info);
			//showQueryResult($user_info);
		}
		disconnectDB($con);
		break;
	case "GetUserInfoBySession":
		$con = connectDB();
		$sessionId = getValueFromRequest(PARAM_KEY_SESSIONID);
		if(NULL != $sessionId)
		{
			$uid = getUserIdBySession($sessionId);
			if(NULL != $uid)
			{
				$user_info = getUserInfoByUserId($uid);
				if(NULL != $user_info)
				{
					echo encodeUserInfo($user_info);
				}
				else
				{
					echo NO_SUCH_USER;
				}
			}
			else
			{
				echo BAD_SESSION;
			}
		}
		else
		{
			echo BAD_SESSION;
		}
		disconnectDB($con);
		break;
	case "GetLocalUserID":
		$con = connectDB();
		$type = getValueFromRequest(DB_OPENID_SOURCE);
		$external_id = getValueFromRequest(DB_OPENID_EXTERNALID);
		if(NULL != $type && NULL != $external_id)
		{
			$local_id = getLocalId($type, $external_id);
			echo "Local ID: " . $local_id;
		}
		disconnectDB($con);
		break;
	case "GetAllPublicBooks":
		$con = connectDB();
		$books = getAllPublicBooks();
		//showQueryResult($books);
		echo encodeBooksQueryResult($books);
		disconnectDB($con);
		break;
	case "GetBookOfficialInfo":
		$con = connectDB();
		$isbn = getValueFromRequest(DB_BOOKINFO_ISBN);
		if(NULL != $isbn)
		{
			$bookinfo = getBookOfficialInfo($isbn);
			//showQueryResult($bookinfo);
			if(NULL != $bookinfo)
			{
				echo encodeBookOfficialInfo($bookinfo);
			}
		}
		disconnectDB($con);
		break;
    case "Login":
        $email = getValueFromRequest(DB_USER_EMAIL);
        $local_pwd = getValueFromRequest(DB_USER_LOCALPWD);
        if((NULL != $email) && (NULL != $local_pwd))
        {
            $con = connectDB();
            $sessionid = login($email, $local_pwd);
            if(NULL != $sessionid)
            {
                echo $sessionid;
            }
            else
            {
                echo WRONG_USERNAME_OR_PWD;
            }
            disconnectDB($con);
        }
        else
        {
        	echo WRONG_USERNAME_OR_PWD;
        }
        break;
    case "LoginBySession":
        $sessionId = getValueFromRequest(PARAM_KEY_SESSIONID);
        if(NULL != $sessionId)
        {
            $con = connectDB();
            $uid = getUserIdBySession($sessionId);
            //var_dump($uid);
            if(NULL != $uid)
            {
            	$user_info = getUserInfoById($uid);
            	//var_dump($user_info);
            	if(NULL != $user_info)
            	{
            		echo encodeUserInfo($user_info);
            	}
            	else
            	{
            		echo NO_SUCH_USER;
            	}
            }
            else
            {
                echo BAD_SESSION;
            }
            disconnectDB($con);
        }
        else
        {
        	echo BAD_SESSION;
        }
        break;
        
    case "AddSessionForTest":
            $con = connectDB();
            $uid = 1;
            createSessionForUser($uid);
            disconnectDB($con);
        break;

	case "SignUp":
		$email = getValueFromRequest(DB_USER_EMAIL);
        $local_pwd = getValueFromRequest(DB_USER_LOCALPWD);
        $nickname = getValueFromRequest(DB_USER_NICKNAME);
        if((NULL != $email) && (NULL != $local_pwd) && (NULL!= nickname))
        {
        	$newUserData[DB_USER_EMAIL] = $email;
        	$newUserData[DB_USER_LOCALPWD] = $local_pwd;
        	$newUserData[DB_USER_NICKNAME] = $nickname;
        	
            $con = connectDB();
            $result = signUp($newUserData);
            
            switch($result)
            {
            	case USERNAME_OCCUPIED:
            		echo USERNAME_OCCUPIED;
            	break;
            	case SUCCESSFUL:
	            	$sessionid = login($email, $local_pwd);
		            if(NULL != $sessionid)
		            {
		                echo $sessionid;
		            }
		            else
		            {
		                echo UNKNOWN_ERROR;
		            }
            	break;
            	default:
            		echo UNKNOWN_ERROR;
            	break;
            }
            disconnectDB($con);
        }
		break;
    case "UploadBooks":
    	$sessionId = getValueFromRequest(PARAM_KEY_SESSIONID);
		$bookUploadData = json_decode($GLOBALS["HTTP_RAW_POST_DATA"]);
		$bookInfoList = $bookUploadData->bookinfolist;
		if(empty($bookUploadData) || empty($bookInfoList))
		{
			//Handle this kind of cases later, now, just treat them as successful.
			echo SUCCESSFUL;
			break;
		}
	    $con = connectDB();
	    
	    $ownerId = getUserIdBySession($sessionId);
        if(NULL == $ownerId)
        {
        	echo BAD_SESSION;
        	disconnectDB($con);
			break;
        }
		foreach($bookInfoList as $bookInfo)
		{
			$isbn = $bookInfo->isbn;
			if(NULL != getBookByOwnerAndISBN($ownerId, $isbn))
			{
				continue;
			}
			$bookInfoRecord[DB_BOOK_OWNERID] = $ownerId;
			$bookInfoRecord[DB_BOOK_ISBN] = $isbn;
			$bookInfoRecord[DB_BOOK_QUANTITY] = $bookInfo->quantity;
			$bookInfoRecord[DB_BOOK_DESCRIPTION] = $bookInfo->description;
			$bookInfoRecord[DB_BOOK_STATUS] = $bookInfo->status;
		    insertRecord(DB_TABLE_BOOK, $bookInfoRecord);
		}
		disconnectDB($con);
		echo SUCCESSFUL;
    	break;
    case "GetBooksBySession":
		$sessionId = getValueFromRequest(PARAM_KEY_SESSIONID);
        if(NULL != $sessionId)
        {
            $con = connectDB();
            $owner_id = getUserIdBySession($sessionId);
            if(NULL != $owner_id)
            {
				$books = getBooksByOwner($owner_id);
				echo encodeBooksQueryResult($books);
            }
            else
            {
                echo BAD_SESSION;
            }
            disconnectDB($con);
        }
        else
        {
        	echo BAD_SESSION;
        }
    	break;
	/*	
    case "Signup":
        $con = connectDB();
        $newUserData[DB_USER_NICKNAME] = $_POST[DB_USER_NICKNAME];
        $password = $_POST[PASSWORD];
        $newUserData[DB_USER_PASSWORD_MD5] = md5($password);
        $newUserData[DB_USER_PASSWORD_SHA1] = sha1($password);
        $email = $_POST[DB_USER_EMAIL];
        $career = $_POST[DB_USER_CAREER];
        $newUserData[DB_USER_EMAIL] = $email;
        $newUserData[DB_USER_CAREER] = $career;
        $newUserData[DB_USER_ACCEPTNEWS] = $_POST[DB_USER_ACCEPTNEWS];
        $newUserData[DB_USER_SELFINTRO] = $_POST[DB_USER_SELFINTRO];
        $type = $_POST[DB_USER_TYPE];
        $newUserData[DB_USER_TYPE] = $type;
        $externalid = $_POST[DB_USER_EXTERNALID];
        $newUserData[DB_USER_EXTERNALID] = $externalid;

        $signUpResult = signUp($newUserData);
        if(SIGN_UP_SUCCESSFUL == $signUpResult)
        {
        	$rememberMe = 1;
        	$userInfo = NULL;
        	switch ($type)
        	{
        		case DB_USER_TYPE_NATIVE:
        			$userInfo = getUserInfoByEmail($email);
        			break;
        		case DB_USER_TYPE_WEIBO:
        			$userInfo = getWeiboUserInfo($externalid);
        			break;
        		default:
        			break;
        	}
        	$uid = $userInfo[DB_USER_ID];
            $sessionId = guid();
            $newSessionData[DB_SESSIONS_ID] = $sessionId;
            $newSessionData[DB_SESSIONS_USERID] = $uid;
            $newSessionData[DB_SESSIONS_CREATEDTIME] = date("Y-m-d H:i:s");
            storeSession($newSessionData, $rememberMe);
            createSamplesForUser($uid);
            echo AUTHENTICATION_PASSED . "|" . $sessionId;
            disconnectDB($con);
        }
        else
        {
            echo $signUpResult;
            disconnectDB($con);
        }

    break;
    case "Login":
        $con = connectDB();
        $email = $_POST[DB_USER_EMAIL];
        $password = $_POST[PASSWORD];
        $uid = "";
        $loginResult = checkEmailPwd($email, $password, $uid);
        switch($loginResult)
        {
            case AUTHENTICATION_PASSED:
                $sessionId = guid();
                
                $newSessionData[DB_SESSIONS_ID] = $sessionId;
                $newSessionData[DB_SESSIONS_USERID] = $uid;
                $newSessionData[DB_SESSIONS_CREATEDTIME] = date("Y-m-d H:i:s");
                
                $rememberMe = 1;
                if(isset($_POST[SESSION_REMEMBERME]))
                {
                    $rememberMe = $_POST[SESSION_REMEMBERME];
                }
                storeSession($newSessionData, $rememberMe);
                echo AUTHENTICATION_PASSED . "|" . $sessionId;
            break;
            case NO_SUCH_USER:
                echo NO_SUCH_USER;
            break;
            case WRONG_PWD:
                echo WRONG_PWD;
            break;
            default:
            break;
        }
        disconnectDB($con);
        break;
    break;
    */
    case "Logout":
        $con = connectDB();
        echo logout();
        disconnectDB($con);
    break;
    case "Upload":
        $con = connectDB();
        $sessionId = "";
        $storeid = guid();
        if(!isLoggedin($sessionId))
        {
            echo NEED_LOGIN;
            disconnectDB($con);
            break;
        }
        $hasError = true;
        foreach($_FILES as $key=>$file)
        {
            if(empty($file))
            {
                continue;
            }
            $filepath = "";
            $createThumb = false;
            switch($key)
            {
                case "preview":
                    $filepath = UPLOAD_PATH . $storeid . ".png";
                    $createThumb = true;
                break;
                case "outline":
                    $filepath = UPLOAD_PATH . $storeid . "_outline.png";
                break;
                case "painting":
                    $filepath = UPLOAD_PATH . $storeid . "_painting.png";
                break;
                case "overlay":
                    $filepath = UPLOAD_PATH . $storeid . "_overlay.png";
                break;
                case "h2d":
                    $filepath = UPLOAD_PATH . $storeid . ".sbs";
                break;
                default:
                break;
            }
            if (!move_uploaded_file($file['tmp_name'], $filepath))
            {
                echo FAILED_TO_UPLOAD . $file['name'];
                $hasError = true;
                break;
            }
            else if($createThumb)
            {
				makeThumb($storeid);
            }
            $hasError = false;
        }
        if(!$hasError)
        {
            $works[DB_WORKS_TITLE] = $_POST[DB_WORKS_TITLE];
            $tags = $_POST[DB_TMP_TAGLIST];
            $works[DB_WORKS_AUTHORID] = getUserIdBySession($sessionId);
            $works[DB_WORKS_ISPUBLIC] = $_POST[DB_WORKS_ISPUBLIC];
            $curTime = date("Y-m-d H:i:s");
            $works[DB_WORKS_CREATETIME] = $curTime;
            $works[DB_WORKS_LASTEDITTIME] = $curTime;
            $works[DB_WORKS_STOREID] = $storeid;
            if(isset($_POST[DB_WORKS_ATTRIBUTES]))
            {
            	$works[DB_WORKS_ATTRIBUTES] = $_POST[DB_WORKS_ATTRIBUTES];
            }
            
            $oldid = -1;
            if(isset($_POST["worksid"]))
            {
            	$oldid = $_POST["worksid"];
            	updateWorks($oldid, $works, $tags);
            }
            else
            {
            	addWorks($works, $tags);
            }

            disconnectDB($con);
            echo WORKS_ADDED;
        }
        //echo "<br><a href='upload.php'>" . CONTINUE_TO_UPLOAD . "</a>";
    break;
    case "Image2URL":
        $storeid = guid();
        $fileName = $storeid . ".png";
        $sharePath = SHARE_PATH . $fileName;
        $isOK = false;
        foreach($_FILES as $key=>$file)
        {
            if(empty($file))
            {
                continue;
            }
            if (move_uploaded_file($file['tmp_name'], $sharePath))
            {
            	$isOK = true;
                break;
            }
        }
        if($isOK)
        {
        	echo SHARE_IMAGE_URL . $fileName;
        }
        else
        {
        	echo FAILED_TO_UPLOAD;
        }
    break;
    case "DuplicateWorksByStoreId":
    	if(!isset($_POST[DB_WORKS_STOREID]))
    	{
    		echo ARG_ERROR;
    	}
    	$originalStoreId = $_POST[DB_WORKS_STOREID];
		if(empty($originalStoreId))
		{
			echo ARG_ERROR;
		}
        $con = connectDB();
        $sessionId = "";
        $newStoreId = guid();
        if(!isLoggedin($sessionId))
        {
            echo NEED_LOGIN;
            disconnectDB($con);
            break;
        }
        echo duplicateWorksById($originalStoreId, $newStoreId, $sessionId);
        disconnectDB($con);
    break;
    case "ShowAllPublicWorks":
        $con = connectDB();
        showQueryResult(getAllPublicWorks());
        disconnectDB($con);
    break;
    case "ShowAllWorksForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            showQueryResult(getAllWorksByAuthorId($uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowPublicWorksForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $loginName = $userInfo[DB_USER_LOGINNAME];
            showQueryResult(getPublicWorksByAuthor($loginName));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowPrivateWorksForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $loginName = $userInfo[DB_USER_LOGINNAME];
            showQueryResult(getPrivateWorksByAuthor($loginName));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowAllPublicGallery":
        $con = connectDB();
        showGalleryFromQueryResult(getAllPublicWorks());
        disconnectDB($con);
    break;
    case "ShowAllGalleryForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $loginName = $userInfo[DB_USER_LOGINNAME];
            showGalleryFromQueryResult(getAllWorksByAuthor($loginName));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowAllWorksForCurrentUserInternalUseOnly":
        $con = connectDB();
        $uid = $_GET[DB_USER_ID];
        showGalleryFromQueryResult(getAllWorksByAuthorId($uid));
        disconnectDB($con);
    break;
    case "ShowPublicGalleryForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $loginName = $userInfo[DB_USER_LOGINNAME];
            showGalleryFromQueryResult(getPublicWorksByAuthor($loginName));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowPrivateGalleryForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $loginName = $userInfo[DB_USER_LOGINNAME];
            showGalleryFromQueryResult(getPrivateWorksByAuthor($loginName));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowPrivateWorksWithSingleTag":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $tag = "aaa";
            echo showGalleryFromQueryResult(getPrivateWorksByTag($tag, $uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowPublicWorksWithSingleTag":
        $con = connectDB();
        $tag = "aaa";
        showQueryResult(getPublicWorksByTag($tag));
        disconnectDB($con);
    break;
    case "GetPrivateWorksWithSingleTag":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $tag = $_GET[DB_TAGS_TAG];
            $uid = getUserIdBySession($sessionId);
            echo encodeWorksQueryResult(getPrivateWorksByTag($tag, $uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "GetPublicWorksWithSingleTag":
        $con = connectDB();
        $tag = $_GET[DB_TAGS_TAG];
        echo encodeWorksQueryResult(getPublicWorksByTag($tag));
        disconnectDB($con);
    break;
    case "GetAllWorksWithSingleTagForCurUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $tag = $_POST[DB_TAGS_TAG];
            echo encodeWorksQueryResult(getAllWorksByTagForCurUser($tag, $uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "GetAllWorksByKeywordsForCurUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $keywords = $_POST[KEYWORDS];
            echo encodeWorksQueryResult(getAllWorksByKeywordsForCurUser($keywords, $uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;    
    case "GetAllWorksForCurrentUser":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            echo encodeWorksQueryResultWithoutTag(getAllWorksByAuthorId($uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "ShowFeedbacks":
        $con = connectDB();
        showTable(DB_FEEDBACKS);
        disconnectDB($con);
    break;
    case "GetStream":
        $con = connectDB();
        $storeid = $_GET[DB_WORKS_STOREID];
        echo "preview:<br />";
        echo getPreviewImageStream($storeid);
        echo "outline:<br />";
        echo getOutlineImageStream($storeid);
        echo "painting:<br />";
        echo getPaintingImageStream($storeid);
        echo "overlay:<br />";
        echo getOverlayImageStream($storeid);
        disconnectDB($con);
    break;
    case "GetPreviewImageStream":
        $con = connectDB();
        $storeid = $_GET[DB_WORKS_STOREID];
        echo getPreviewImageStream($storeid);
        disconnectDB($con);
    break;
    case "GetOutlineImageStream":
        $con = connectDB();
        $storeid = $_GET[DB_WORKS_STOREID];
        echo getOutlineImageStream($storeid);
        disconnectDB($con);
    break;
    case "GetPaintingImageStream":
        $con = connectDB();
        $storeid = $_GET[DB_WORKS_STOREID];
        echo getPaintingImageStream($storeid);
        disconnectDB($con);
    break;
    case "GetOverlayImageStream":
        $con = connectDB();
        $storeid = $_GET[DB_WORKS_STOREID];
        echo getOverlayImageStream($storeid);
        disconnectDB($con);
    break;
    case "GetSessionId":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            echo $sessionId;
        }
        else
        {
            echo "";
        }
        disconnectDB($con);
    break;
    case "CanRegister":
        $con = connectDB();
        $email = $_POST[DB_USER_EMAIL];
        if(getUserInfoByEmail($email) == NULL)
        {
            echo RESULT_YES;
        }
        else
        {
            echo RESULT_NO;
        }
        disconnectDB($con);
    break;
    case "GetResetPwdURL":
        $con = connectDB();
        $email = $_GET[DB_USER_EMAIL];
        $userInfo = getUserInfoByEmail($email);
        if($userInfo == NULL)
        {
            echo NO_SUCH_USER;
        }
        else
        {
            $uid = $userInfo[DB_USER_ID];
            $token = guid();
            if(!empty($uid))
            {
                $resetpwdData[DB_RESETPWD_UID] = $userInfo[DB_USER_ID];
                $resetpwdData[DB_RESETPWD_ONETIMETOKEN] = $token;
                $resetpwdData[DB_RESETPWD_REQUESTTIME] = date("Y-m-d H:i:s");
                
                insertRecord(DB_RESETPWD, $resetpwdData);
                echo APIBASEURL . "op=ResetPwd&key=" . $token;
            }
        }
        disconnectDB($con);
    break;
    case "ResetPwd":
        if(isset($_GET["key"]))
        {
            $con = connectDB();
            $token = $_GET["key"];
            displayPwdPage($token);
            disconnectDB($con);
        }
    break;
    
    case "ChangePwd":
    	$con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $oldpwd = $_POST[OLD_PWD];
    		$newpwd = $_POST[NEW_PWD];
            echo changePwd($uid, $oldpwd, $newpwd);
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    
    // For forget pwd reset link.
    case "UpdatePwd":
        if(isset($_POST["pwd"]))
        {
            $pwd = $_POST["pwd"];
            $con = connectDB();
            $token = $_GET["key"];
            resetPwd($token, $pwd);
            disconnectDB($con);
        }
        else
        {
            echo CANNOT_SUBMIT_AGAIN;
        }
    break;
    case "UpdateUserInfo":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $conditions[DB_USER_ID] = $uid;
            $newValues[DB_USER_NICKNAME] = $_POST[DB_USER_NICKNAME];
            $newValues[DB_USER_SELFINTRO] = $_POST[DB_USER_SELFINTRO];
            $newValues[DB_USER_ACCEPTNEWS] = $_POST[DB_USER_ACCEPTNEWS];
            $newValues[DB_USER_CAREER] = $_POST[DB_USER_CAREER];
            updateRecord(DB_USERS, $conditions, $newValues);
            echo USERINFO_UPDATED;
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "UpdateWorksAttributes":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            $worksId = $_POST[DB_WORKS_ID];
            if(canEditThisWorks($worksId, $uid))
            {
                $title = $_POST[DB_WORKS_TITLE];
                $tags = $_POST[DB_WORKS_TAGS];
                $isPublic = $_POST[DB_WORKS_ISPUBLIC];
                changeWorksAttributes($worksId, $title, $tags, $isPublic);
                echo WORKS_ATTRIBUTES_UPDATED;
            }
            else
            {
                echo CANNOT_UPDATED_WORKS_ATTRIBUTES;
            }
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "GetUserInfo":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $userInfo = getUserInfoBySession($sessionId);
            $encodedUserInfo = encodeUserInfo($userInfo);
            echo $encodedUserInfo;
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "DeleteWorks":
        $con = connectDB();
        $storeId = $_GET[DB_WORKS_STOREID];
        echo deleteWorks($storeId);
        disconnectDB($con);
    break;
    //For temp test
    case "PrivateGalleryTest":
        $con = connectDB();
        $sessionId = "";
        if(isLoggedin($sessionId))
        {
            $uid = getUserIdBySession($sessionId);
            echo "All works by current user:<br /><hr><br />Private works:<br />";
            showGalleryFromQueryResult(getPrivateWorksByAuthorId($uid));
            echo "<hr><br />Public works:<br />";
            showGalleryFromQueryResult(getPublicWorksByAuthorId($uid));
        }
        else
        {
            echo NEED_LOGIN;
        }
        disconnectDB($con);
    break;
    case "GetWeiboUserInfo":
    	$con = connectDB();
    	if(isset($_GET[DB_USER_EXTERNALID]))
    	{
    		$externalid = $_GET[DB_USER_EXTERNALID];
    	}
    	$userInfo = getWeiboUserInfo($externalid);
    	if(NULL != $userInfo)
    	{
			$sessionId = guid();
			$newSessionData[DB_SESSIONS_ID] = $sessionId;
			$newSessionData[DB_SESSIONS_USERID] = $userInfo[DB_USER_ID];
			$newSessionData[DB_SESSIONS_CREATEDTIME] = date("Y-m-d H:i:s");
			$rememberMe = 1;
//			if(isset($_POST[SESSION_REMEMBERME]))
//			{
//				$rememberMe = $_POST[SESSION_REMEMBERME];
//			}
			storeSession($newSessionData, $rememberMe);
			echo AUTHENTICATION_PASSED . "|" . $sessionId;
    	}
        disconnectDB($con);
    	break;
    case "GetThumbnail":
    	if(isset($_GET[DB_WORKS_STOREID]))
    	{
    		$storeid = $_GET[DB_WORKS_STOREID];
    		$thumb_filepath = UPLOAD_PATH . $storeid . "_thumb.png";
    		//echo $thumb_filepath;
    		if(!file_exists($thumb_filepath))
    		{
    			makeThumb($storeid);
    		}
    		header("Content-type:image/png");
    		return readfile($thumb_filepath);
    	}
    	break;
    case "GetShareImage":
    	if(isset($_GET[DB_WORKS_STOREID]))
    	{
    		$storeid = $_GET[DB_WORKS_STOREID];
    		$fileName = $storeid . ".png";
	        $sharePath = SHARE_PATH . $fileName;
    		//echo $thumb_filepath;
    		if(!file_exists($sharePath))
    		{
    			makeShareImage($storeid, $sharePath);
    		}
    		header("Content-type:image/png");
    		return readfile($sharePath);
    	}
    	break;
//    case "Test":
//    	$con = connectDB();
//    	createSamplesForUser(1);
//    	disconnectDB($con);
//    	break;
    default:
    break;
}

?>