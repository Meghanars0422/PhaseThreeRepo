package net.codejava;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//for user password is codejava
		//for admin password is adminjava
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawpassword = "yashu1397;";
		String encodedPassword = encoder.encode(rawpassword);
		System.out.println(encodedPassword);
	}

}
