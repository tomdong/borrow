package com.intalker.borrow.ui.login;

import android.app.Dialog;
import android.content.Context;

public class OBLoginDialog extends Dialog{
	private OBLoginView mLoginView = null;
	
	public OBLoginDialog(Context context) {
		super(context);
		
		mLoginView = new OBLoginViewHorizontal(context);
	}
	

}
