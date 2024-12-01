package model;

public class GroupModel {
	private int id;
	private String name;
	
	public GroupModel() {
		this.id = 0;
		this.name = "";
	}

	public GroupModel(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	@Override
	public String toString() {
		return this.name; // chỉ render name ra giao diện
	}
}
