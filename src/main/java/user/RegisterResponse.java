package user;

/**
 * @author Thomas
 *
 */
public class RegisterResponse {
	
	private String sessionToken;
	private double balance;

	public RegisterResponse() {
	}

	public RegisterResponse(String sessionToken, double balance) {
		super();
		this.sessionToken = sessionToken;
		this.balance = balance;
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
