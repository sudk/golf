package com.jason.golf.dialog;

import com.jason.golf.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;


public class GProgressDialog extends Dialog {
	
	public GProgressDialog(Context context){
		super(context,R.style.GProgressDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
		ProgressBar pb  = (ProgressBar) findViewById(R.id.GProgressBar);
	}
	
}
