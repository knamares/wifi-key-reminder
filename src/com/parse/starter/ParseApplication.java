package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	private final static String APP_ID = "e7v22kkbcLvLm6lOnygY36xnXWJqXL6BayKHv0El";
	private final static String CLIENT_KEY = "V2bSO83DG4FOmrfKgpU6DGHHQrheqHnPE1iRz2BN";
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, APP_ID, CLIENT_KEY);

		
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
