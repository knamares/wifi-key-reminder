package com.kna.wifikeyreminder.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1272945223288425659L;

	private String id;
	private String username;
	private String password;
	private String device_token;

	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

}
