package com.ccb.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);
	
	public PasswordUtils() {
	}
	
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
		log.info("Gerando hash com Bcrypt.");
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
