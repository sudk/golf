package com.jason.golf.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

@SuppressLint("ValidFragment")
public class ProgressDialog extends DialogFragment {
	
	private DialogInterface.OnCancelListener _cancelListener;
	private DialogInterface.OnDismissListener _dismissListener;
	
	Context _context;
	
	CharSequence _title;
	CharSequence _message;
	
	android.app.ProgressDialog dialog;
	
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
	
	public ProgressDialog(){
		super();
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
	
	public ProgressDialog setOnCancelListener(DialogInterface.OnCancelListener listener){
		_cancelListener = listener;
		return this;
	}
	
	public ProgressDialog setOnDismissListener(DialogInterface.OnDismissListener listener){
		_dismissListener = listener;
		return this;
	}
	
	public boolean isShow(){
		return dialog.isShowing();
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
	    
	    if(_cancelListener != null){
	    	dialog.setOnCancelListener(_cancelListener);
	    }
	    
	    if(_dismissListener != null){
	    	dialog.setOnDismissListener(_dismissListener);
	    }
	    
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setCancelable(false);
	    
	    return dialog;
	}

}
