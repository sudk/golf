package com.jason.golf.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

public class ProgressDialog extends DialogFragment {
	
	Context _context;
	
	CharSequence _title;
	CharSequence _message;
	
	private static ProgressDialog _dialog;
	
	public static ProgressDialog newInstance(Context ctx){
		_dialog = new ProgressDialog(ctx);
		return _dialog;
	}
	
	public static void dissmiss(Context ctx){
		if(_dialog != null)
			_dialog.dismiss();
		_dialog = null;
	}
	
	public ProgressDialog(Context ctx) {
		// TODO Auto-generated constructor stub
		_context = ctx;
	}
	
	public ProgressDialog setTitle(String title){
		this._title = title;
		return this;
	}
	
	public ProgressDialog setMessage(String msg){
		this._message = msg;
		return this;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
	    android.app.ProgressDialog dialog = new android.app.ProgressDialog (getActivity());
	    dialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
	    if(!TextUtils.isEmpty(_title)){
	    	dialog.setTitle(_title);
	    }
	    if(!TextUtils.isEmpty(_message)){
	    	dialog.setMessage(_message);
	    }
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setCancelable(false);
	    return dialog;
	}

}
