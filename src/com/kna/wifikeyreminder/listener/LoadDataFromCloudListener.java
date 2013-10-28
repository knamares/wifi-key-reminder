package com.kna.wifikeyreminder.listener;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;


public interface LoadDataFromCloudListener {

	public void loadCallBackOK(List<ParseObject> list);
	public void loadCallBackKO(ParseException e);
}
