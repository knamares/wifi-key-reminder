package com.kna.wifikeyreminder.listener;

import com.parse.ParseException;

public interface SignInCallBackListener {

	public void signInCallBackListenerOk();
	public void signInCallBackListenerKo(ParseException e);
	
}
