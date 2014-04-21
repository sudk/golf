package com.jason.golf;

import com.jason.golf.classes.GAccount;

import android.app.Application;

public class GolfAppliaction extends Application{
	
	private GAccount _account;
	
	public GAccount getAccount(){
		
		if(null == _account)
			_account = new GAccount();
		
		return _account;
	}

}
