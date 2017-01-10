package user;

/**
 * @author Thomas
 *
 */
public class ChangeBalanceRequest {
	private String sessionToken;
	private double changeValue;
	private String acceptanceCode;
	
	public ChangeBalanceRequest() {
		super();
	}
	
	public ChangeBalanceRequest(String sessionToken, double changeValue, String acceptanceCode) {
		super();
		this.sessionToken = sessionToken;
		this.changeValue = changeValue;
		this.acceptanceCode = acceptanceCode;
	}

	public String getSessionToken() {
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public double getChangeValue() {
		return changeValue;
	}
	
	public void setChangeValue(double changeValue) {
		this.changeValue = changeValue;
	}
	
	public String getAcceptanceCode() {
		return acceptanceCode;
	}

	public void setAcceptanceCode(String acceptanceCode) {
		this.acceptanceCode = acceptanceCode;
	}

}
