package com.kna.wifikeyreminder.listener;

import com.parse.ParseException;
import com.parse.ParseObject;


public interface SaveDataToCloudListener {

	public void save(String bssid, String key);
	public void savedCallBackOK(ParseObject wifi);
	public void savedCallBackKO(ParseException e);
}
