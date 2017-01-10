package user;

/**
 * @author Thomas
 *
 */
public class User {
	private String email;
	private String hashedPassword;
	private String sessionToken;
	private double balance;

	public User() {
		super();
	}

	public User(String email, String hashedPassword, String sessionToken, int balance) {
		super();
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.sessionToken = sessionToken;
		this.balance = balance;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getSessionToken() {
		return sessionToken;
	}


	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}
}