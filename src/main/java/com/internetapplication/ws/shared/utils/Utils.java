package com.internetapplication.ws.shared.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
	private final Random RANDOM = new SecureRandom();
	private final String ALPHAPET = "123456789abcdefghijklmnopqrstupwxyzABCDEFGHIJKLMNOPQRSTUPWXYZ";

	public String generateEntityId() {
		return generateRandomString(30);
	}

	public String generateRandomString(int length) {
		StringBuilder returnedValue = new StringBuilder(length);
		
		for(int i=0; i<length; i++) {
			returnedValue.append(ALPHAPET.charAt(RANDOM.nextInt(length)));
		}
		
		return new String(returnedValue);

	}
}
