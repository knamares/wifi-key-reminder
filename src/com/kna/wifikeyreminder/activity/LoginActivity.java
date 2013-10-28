package com.kna.wifikeyreminder.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kna.wifikeyreminder.R;
import com.kna.wifikeyreminder.WifiKeyReminder;
import com.kna.wifikeyreminder.helper.ConnectionHelper;
import com.kna.wifikeyreminder.helper.WifiParserHelper;
import com.kna.wifikeyreminder.listener.LoadDataFromCloudListener;
import com.kna.wifikeyreminder.listener.LogInCallBackListener;
import com.kna.wifikeyreminder.listener.SignInCallBackListener;
import com.kna.wifikeyreminder.model.User;
import com.kna.wifikeyreminder.model.Wifi;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends Activity implements SignInCallBackListener,
		LogInCallBackListener, LoadDataFromCloudListener {

	private EditText editTextEmail;
	private EditText editTextPass;
	private Button buttonLogin;
	private RelativeLayout relativeLayoutLoading;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		sharedPreferences = getSharedPreferences("preferences",
				Context.MODE_PRIVATE);

		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPass = (EditText) findViewById(R.id.editTextPass);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		relativeLayoutLoading = (RelativeLayout) findViewById(R.id.relativeLayoutLoading);

		relativeLayoutLoading.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				return;
			}
		});

		editTextEmail.setText("kanamares@gmail.com");
		editTextPass.setText("1234");

		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showLoading();
				ConnectionHelper.signInUser(editTextEmail.getText().toString(),
						editTextPass.getText().toString(), LoginActivity.this);
			}
		});

		User user = new Gson().fromJson(
				sharedPreferences.getString("user", null), User.class);

		if (user != null) {
			showLoading();
			logIn(user.getUsername(), user.getPassword());
		}

	}

	private void showLoading() {
		if (!relativeLayoutLoading.getViewTreeObserver().equals(View.VISIBLE)) {
			relativeLayoutLoading.setVisibility(View.VISIBLE);
		}
	}

	private void hideLoading() {
		if (!relativeLayoutLoading.getViewTreeObserver().equals(View.GONE)) {
			relativeLayoutLoading.setVisibility(View.GONE);
		}
	}

	private void access() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void logIn(String user, String pass) {
		ConnectionHelper.logInUser(user, pass, LoginActivity.this);
	}

	@Override
	public void signInCallBackListenerOk() {
		ConnectionHelper.logInUser(editTextEmail.getText().toString(),
				editTextPass.getText().toString(), LoginActivity.this);
	}

	@Override
	public void logInCallBackListenerOk(ParseUser parseUser) {
		User user = new User();
		user.setUsername(parseUser.getUsername());
		user.setPassword(editTextPass.getText().toString());
		user.setId(parseUser.getObjectId());

		sharedPreferences.edit().putString("user", new Gson().toJson(user)).commit();

		WifiKeyReminder.user = user;
		
		loadWifiData(user.getId());
	}

	private void loadWifiData(String idUser) {
		ConnectionHelper.loadWifiData(idUser, this);
	}

	@Override
	public void logInCallBackListenerKo(ParseException e) {
		hideLoading();
		Toast.makeText(LoginActivity.this, "Error LogIn: " + e.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void signInCallBackListenerKo(ParseException e) {
		if (e.getCode() == 202) {
			// try to login
			logIn(editTextEmail.getText().toString(), editTextPass.getText()
					.toString());
		} else {
			hideLoading();
			Toast.makeText(LoginActivity.this,
					"Error SignIn: " + e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void loadCallBackOK(List<ParseObject> list) {
		//Persist data
		persisteData(list);
		
		access();
	}

	private void persisteData(List<ParseObject> list) {
		List<Wifi> listWifi = WifiParserHelper.getListWifi(list);
		
		WifiKeyReminder.wifis = listWifi;
		
		sharedPreferences.edit().putString("wifis", new Gson().toJson(listWifi)).commit();
	}

	@Override
	public void loadCallBackKO(ParseException e) {
		hideLoading();
		Toast.makeText(LoginActivity.this, "Error LogIn: " + e.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

}
