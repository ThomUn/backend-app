package user;

public class User {
	private String username;
	private String hashedPassword;
	private String userMode;

	public User(String username, String hashedPassword, String userMode) {
		super();
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.userMode = userMode;
	}
	
	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getUserMode() {
		return userMode;
	}

	public void setUserMode(String userMode) {
		this.userMode = userMode;
	}
}