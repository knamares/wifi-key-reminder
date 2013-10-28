package com.kna.wifikeyreminder.listener;

import com.parse.ParseException;
import com.parse.ParseUser;

public interface LogInCallBackListener {

	public void logInCallBackListenerOk(ParseUser parseUser);
	public void logInCallBackListenerKo(ParseException e);
	
}
