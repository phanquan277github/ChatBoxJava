package model;

public class FileModel {
	private int id;
	private String fileName;

	public FileModel() {
		this.id = 0;
		this.fileName = "";
	}

	public FileModel(int id, String fileName) {
		this.id = id;
		this.fileName = fileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return fileName;
	}
}
