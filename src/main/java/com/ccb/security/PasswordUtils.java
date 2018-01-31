package com.ccb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	/**
	 * Generate hash code using BCrypt.
	 *
	 * @param password
	 * @return String
	 */
	public static String generateHashWithBCrypt(String password) {
		if (password == null) {
			return password;
		}
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.encode(password);
	}

	/**
	 * Validate password.
	 *
	 * @param password
	 * @param encodedPassword
	 * @return boolean
	 */
	public static boolean isPasswordValid(String password, String encodedPassword) {
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.matches(password, encodedPassword);
	}
}
