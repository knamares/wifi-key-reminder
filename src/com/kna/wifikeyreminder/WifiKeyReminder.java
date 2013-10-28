package com.kna.wifikeyreminder;

import java.util.List;

import android.app.Application;

import com.kna.wifikeyreminder.model.User;
import com.kna.wifikeyreminder.model.Wifi;
import com.parse.Parse;

public class WifiKeyReminder extends Application {

	private final static String APP_ID = "e7v22kkbcLvLm6lOnygY36xnXWJqXL6BayKHv0El";
	private final static String CLIENT_KEY = "V2bSO83DG4FOmrfKgpU6DGHHQrheqHnPE1iRz2BN";
	
	public static User user;
	public static List<Wifi> wifis;
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, APP_ID, CLIENT_KEY);

//		ParseUser.enableAutomaticUser();
//		ParseACL defaultACL = new ParseACL();
//	    
//		// If you would like all objects to be private by default, remove this line.
//		defaultACL.setPublicReadAccess(true);
//		
//		ParseACL.setDefaultACL(defaultACL, true);
	
	}

}
