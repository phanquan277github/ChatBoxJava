package model;

public class MemberModel {
	private int id;
	private String name;
	private int groupId;
	
	public MemberModel() {
		this.id = 1;
		this.name = "Quan";
		this.groupId = 1;
	}
	public MemberModel(int id, String name, int groupId) {
		super();
		this.id = id;
		this.name = name;
		this.groupId = groupId;
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
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupid) {
		this.groupId = groupid;
	}
}
