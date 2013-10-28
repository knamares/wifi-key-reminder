package com.kna.wifikeyreminder.helper;

import java.util.ArrayList;
import java.util.List;

import com.kna.wifikeyreminder.model.Wifi;
import com.parse.ParseObject;

public class WifiParserHelper {

	public static List<Wifi> getListWifi(List<ParseObject> parseWifis) {
		List<Wifi> wifis = new ArrayList<Wifi>();

		Wifi wifi;
		for (ParseObject parseObject : parseWifis) {
			wifi = new Wifi();
			wifi.setBssid(parseObject.getString("bssid"));
			wifi.setKey(parseObject.getString("key"));
			wifi.setId(parseObject.getObjectId());
			wifi.setIdUser(parseObject.getString("idUser"));

			wifis.add(wifi);

		}

		return wifis;
	}

	public static Wifi getWifi(ParseObject parseObject) {
		Wifi wifi = new Wifi();
		wifi.setBssid(parseObject.getString("bssid"));
		wifi.setKey(parseObject.getString("key"));
		wifi.setId(parseObject.getObjectId());
		wifi.setIdUser(parseObject.getString("idUser"));

		return wifi;
	}
}
