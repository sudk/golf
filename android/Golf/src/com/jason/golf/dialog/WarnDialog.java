package com.jason.golf.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

public class WarnDialog extends DialogFragment{
	
	public static WarnDialog newInstance(Context context) {
		WarnDialog f = new WarnDialog(context);
		f.setStyle(STYLE_NO_FRAME, 0);
		return f;
	}

	private Context _context;
	
	private String _title;
	private String _messgae;
	
	private String _positiveBtnText;
	private String _negativeBtnText;
	
	private DialogInterface.OnClickListener _positiveBtnClickListener;
	private DialogInterface.OnClickListener _negativeBtnClickListener;
	
	public WarnDialog(Context context) {
		this._context = context;
	}
	
	public WarnDialog setTitle(String title){
		this._title = title;
		return this;
	}
	
	public WarnDialog setTitle(int resid){
		this._title = this._context.getString(resid);
		return this;
	}
	
	public WarnDialog setMessage(String msg){
		this._messgae = msg;
		return this;
	}
	
	public WarnDialog setMessage(int resid){
		this._messgae = this._context.getString(resid);
		return this;
	}
	
	public WarnDialog setPositiveBtn(String text, DialogInterface.OnClickListener warnDialogInterface){
		this._positiveBtnText = text;
		this._positiveBtnClickListener = warnDialogInterface;
		return this;
	}
	
	public WarnDialog setPositiveBtn(int resid, DialogInterface.OnClickListener warnDialogInterface){
		this._positiveBtnText = this._context.getString(resid);
		this._positiveBtnClickListener = warnDialogInterface;
		return this;
	}
	
	public WarnDialog setNegativeBtn(String text, DialogInterface.OnClickListener listener){
		this._negativeBtnText = text;
		this._negativeBtnClickListener = listener;
		return this;
	}
	
	public WarnDialog setNegativeBtn(int resid, DialogInterface.OnClickListener listener){
		this._negativeBtnText = this._context.getString(resid);
		this._negativeBtnClickListener = listener;
		return this;
	}
	
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		if(!TextUtils.isEmpty(_title))
			builder.setTitle(_title);
		
		if(!TextUtils.isEmpty(_messgae))
			builder.setMessage(_messgae);
		
		if(!TextUtils.isEmpty(_positiveBtnText)){
			builder.setPositiveButton(_positiveBtnText, _positiveBtnClickListener);
		}
		
		if(!TextUtils.isEmpty(_negativeBtnText)){
			builder.setNegativeButton(_negativeBtnText, _negativeBtnClickListener);
		}
		
		return builder.create();
	}

	
}
