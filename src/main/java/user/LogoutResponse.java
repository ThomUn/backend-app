package user;

/**
 * @author Thomas
 *
 */
public class LogoutResponse {
	private boolean isLoggedOut;
	
	public LogoutResponse() {
	}

	public LogoutResponse(boolean isLoggedOut) {
		super();
		this.isLoggedOut = isLoggedOut;
	}

	public boolean isLoggedOut() {
		return isLoggedOut;
	}

	public void setLoggedOut(boolean isLoggedOut) {
		this.isLoggedOut = isLoggedOut;
	}
}
