package logic;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class CustomAuthenticator extends Authenticator {
	
	public PasswordAuthentication getPasswordAuthentication() {
		String username = "pwon460@cse.unsw.edu.au";
		String password = "abc123";

		return new PasswordAuthentication(username, password.toCharArray());
	}
	
}
