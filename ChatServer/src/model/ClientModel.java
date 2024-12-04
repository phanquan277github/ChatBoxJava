package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;

public class ClientModel  implements Serializable {
	private static final long serialVersionUID = 1L; // Đảm bảo tính tương thích khi serial hóa/giải serial hóa
	private int id;
	private String name;
	private int accountId;
	private Boolean gender;
	private String phone;
	private Date birthday;
	private byte[] avata;
	private Timestamp createAt;
	private Timestamp updateAt;
	
	public ClientModel() {
	}
	
	public ClientModel(int id, String name, int accountId, Boolean gender, String phone, Date birthday, byte[] avata,
			Timestamp createAt, Timestamp updateAt) {
		super();
		this.id = id;
		this.name = name;
		this.accountId = accountId;
		this.gender = gender;
		this.phone = phone;
		this.birthday = birthday;
		this.avata = avata;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
	public byte[] getAvata() {
		return avata;
	}

	public void setAvata(byte[] avata) {
		this.avata = avata;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
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
