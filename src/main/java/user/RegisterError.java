package user;

/**
 * @author Thomas
 *
 */
public class RegisterError {
	
	private String errorMessage;

	/**
	 * 
	 */
	public RegisterError() {
		// TODO Auto-generated constructor stub
	}
	
	public RegisterError(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
