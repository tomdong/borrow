package com.intalker.borrow.util;

import java.util.List;

import com.intalker.borrow.config.ResultCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class ScanUtil {
	private static final String BS_PACKAGE = "com.google.zxing.client.android";
	private static Activity activity = null;
	
	private static boolean checkBSInstall(Intent intent) {
		PackageManager pm = activity.getPackageManager();
		List<ResolveInfo> availableApps = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (availableApps != null) {
			for (ResolveInfo availableApp : availableApps) {
				String packageName = availableApp.activityInfo.packageName;
				if (packageName.toLowerCase().compareTo(BS_PACKAGE) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static AlertDialog showDownloadDialog() {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
		downloadDialog.setTitle("Install BarCode Scanner");
		downloadDialog
				.setMessage("Install BarCode scanner before scanning. Do you want to install from market now?");
		downloadDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Uri uri = Uri
								.parse("market://details?id=" + BS_PACKAGE);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						try {
							activity.startActivity(intent);
						} catch (ActivityNotFoundException anfe) {
							// Hmm, market is not installed
							// Log.w(TAG,
							// "Android Market is not installed; cannot install Barcode Scanner");
						}
					}
				});
		downloadDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				});
		return downloadDialog.show();
	}
	
	public static void scanBarCode(Activity app)
	{
		activity = app;
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		if(checkBSInstall(intent))
		{
			activity.startActivityForResult(intent, ResultCode.SCAN_RESULT_CODE);
		}
		else
		{
			showDownloadDialog();
		}
	}
}
