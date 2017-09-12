package com.example.encry;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String disguiseMD5(String message) {

		if (null == message) {

			return null;
		}

		return disguiseMD5(message, "UTF-8");
	}
	
	public static String disguiseMD5(String message, String encoding) {

		if (null == message || null == encoding) {

			return null;
		}

		message = message.trim();
		byte value[];
		try {
			value = message.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			value = message.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return toHex(md.digest(value));
	}
	
	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}
	
}
