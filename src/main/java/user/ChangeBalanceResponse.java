package user;

/**
 * @author Thomas
 *
 */
public class ChangeBalanceResponse {
	private boolean successful;
	private double newBalance;
	
	public ChangeBalanceResponse() {
		super();
	}
	
	public ChangeBalanceResponse(boolean successful, double newBalance) {
		super();
		this.successful = successful;
		this.newBalance = newBalance;
	}
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	public double getNewBalance() {
		return newBalance;
	}
	
	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}
}
