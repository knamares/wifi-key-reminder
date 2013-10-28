package com.kna.wifikeyreminder.helper;

import java.util.List;

import com.kna.wifikeyreminder.listener.LoadDataFromCloudListener;
import com.kna.wifikeyreminder.listener.LogInCallBackListener;
import com.kna.wifikeyreminder.listener.SaveDataToCloudListener;
import com.kna.wifikeyreminder.listener.SignInCallBackListener;
import com.kna.wifikeyreminder.model.Wifi;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class ConnectionHelper {

	public static void signInUser(String email, String pass,
			final SignInCallBackListener signInCallBackListener) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(pass);
		user.setEmail(email);

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					signInCallBackListener.signInCallBackListenerOk();
				} else {
					signInCallBackListener.signInCallBackListenerKo(e);
				}
			}
		});
	}

	public static void logInUser(String email, String pass,
			final LogInCallBackListener logInCallBackListener) {
		ParseUser.logInInBackground(email, pass, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					logInCallBackListener.logInCallBackListenerOk(user);
				} else {
					logInCallBackListener.logInCallBackListenerKo(e);
				}
			}
		});
	}

	public static void saveWifi(Wifi wifi, final SaveDataToCloudListener saveDataToCloudListener) {
		 final ParseObject wifiSave = new ParseObject("Wifi");
		wifiSave.put("bssid", wifi.getBssid());
		wifiSave.put("key", wifi.getKey());
		wifiSave.put("idUser", wifi.getIdUser());
		wifiSave.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if(e == null){
					//OK
					saveDataToCloudListener.savedCallBackOK(wifiSave);
				}else{
					//KO
					saveDataToCloudListener.savedCallBackKO(e);
				}
			}
		});	
	}

	public static void loadWifiData(String idUser, final LoadDataFromCloudListener loadDataFromCloudListener) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Wifi");
		query.whereEqualTo("idUser", idUser);

		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	loadDataFromCloudListener.loadCallBackOK(scoreList);
		        } else {
		        	loadDataFromCloudListener.loadCallBackKO(e);  
		        }
		    }
		});		
	}
}
