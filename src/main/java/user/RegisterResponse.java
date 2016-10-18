package user;

/**
 * @author Thomas
 *
 */
public class RegisterResponse {
	
	private String sessionToken;
	private int balance;

	public RegisterResponse() {
	}

	public RegisterResponse(String sessionToken, int balance) {
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
}
