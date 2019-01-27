package com.auto2fa.client;

import java.net.InetAddress;
import java.security.SecureRandom;

public class User {

	private String ip;

	public User(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String toString() {
		return "[" + ip + "]";
	}
}
