package user;

/**
 * @author Thomas
 *
 */
public class LogoutRequest {
	private String username;
	private String sessionToken;

	public LogoutRequest() {
		super();
	}
	
	public LogoutRequest(String username, String sessionToken) {
		super();
		this.username = username;
		this.sessionToken = sessionToken;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
