package user;

/**
 * @author Thomas
 *
 */
public class RegisterRequest {
	
	private String email;
	private String clearTextPassword;

	public RegisterRequest() {
	}

	public RegisterRequest(String email, String clearTextPassword) {
		super();
		this.email = email;
		this.clearTextPassword = clearTextPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClearTextPassword() {
		return clearTextPassword;
	}

	public void setClearTextPassword(String clearTextPassword) {
		this.clearTextPassword = clearTextPassword;
	}
}
