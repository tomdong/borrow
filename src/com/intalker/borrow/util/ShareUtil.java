package com.intalker.borrow.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.intalker.borrow.HomeActivity;
import com.intalker.borrow.R;
import com.intalker.borrow.data.BookInfo;

public class ShareUtil {
	public static void fireShareIntent(BookInfo bookInfo) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File imageFile = new File(StorageUtil.getCoverImagePath(bookInfo
				.getISBN()));
		if (imageFile.exists()) {
			Uri uri = Uri.fromFile(imageFile);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		}
		Context context = HomeActivity.getApp();
		intent.putExtra("subject", context.getString(R.string.share_subject));
		// intent.putExtra("sms_body", "test");
		String bodyMsg = context.getString(R.string.share_msg_prefix)
				+ bookInfo.getBookName()
				+ context.getString(R.string.share_msg_suffix);
		intent.putExtra(Intent.EXTRA_TEXT, bodyMsg);
		intent.setType("image/*");
		// intent.setClassName("com.android.mms",
		// "com.android.mms.ui.ComposeMessageActivity");
		HomeActivity.getApp().startActivity(intent);
	}
}
