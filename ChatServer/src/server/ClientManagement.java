package server;

public class ClientManagement {
	private String userName;
	private int accountId;
	private int clientId;
	private String clientName;
	private int curentGroupId;
	private String timeIn;
	
	public ClientManagement() {
		this.userName = "";
		this.accountId = 0;
		this.clientId = 0;
		this.clientName = "";
		this.curentGroupId = 0;
		this.timeIn = "";
	}

	public ClientManagement(String userName, int clientId, int curentGroupId) {
		this.userName = userName;
		this.clientId = clientId;
		this.curentGroupId = curentGroupId;
	}

	public String getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getCurentGroupId() {
		return curentGroupId;
	}

	public void setCurentGroupId(int curentGroupId) {
		this.curentGroupId = curentGroupId;
	}
}
