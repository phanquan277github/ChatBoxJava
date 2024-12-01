package model;

import java.util.Date;

public class ClientModel {
	private int id;
	private String name;
	private int accountId;
	private String gender;
	private String phone;
	private Date birthday;
	
	public ClientModel() {
		this.id = 0;
		this.name = "";
		this.accountId = 0;
		this.gender = "";
		this.phone = "";
		this.birthday = null;
	}
	
	public ClientModel(int id, String name, int accountId, String gender, String phone, Date birthday) {
		this.id = id;
		this.name = name;
		this.accountId = accountId;
		this.gender = gender;
		this.phone = phone;
		this.birthday = birthday;
	}

	
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
