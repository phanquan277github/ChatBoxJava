package model;

public class ClientModel {
	private int id;
	private String name;
	private int accountId;
	private int currentGroupId;
	
	public ClientModel() {
		this.id = 0;
		this.name = "";
		this.accountId = 0;
		this.currentGroupId = 0;
	}
	public ClientModel(int id, String name, int groupId) {
		super();
		this.id = id;
		this.name = name;
		this.accountId = groupId;
	}
	
	
	public int getCurrentGroupId() {
		return currentGroupId;
	}
	public void setCurrentGroupId(int currentGroupId) {
		this.currentGroupId = currentGroupId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
}
