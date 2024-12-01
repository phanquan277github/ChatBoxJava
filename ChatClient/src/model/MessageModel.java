package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageModel implements Serializable {
	private static final long serialVersionUID = 1L; // Đảm bảo tính tương thích khi serial hóa/giải serial hóa

	private int messageId;
	private int clientId;
	private int groupId;
	private String nickname;
	private String content;
	private String typeMsg;
	private String fileName; 
	private byte[] fileData;
	private Timestamp createAt;
	private Timestamp updateAt;
	
	public MessageModel() {
		
	}
	
	public MessageModel(int messageId, int groupId, int clientId, String nickname, String content) {
		super();
		this.messageId = messageId;
		this.clientId = clientId;
		this.groupId = groupId;
		this.nickname = nickname;
		this.content = content;
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

	public String getTypeMsg() {
		return typeMsg;
	}

	public void setTypeMsg(String typeMsg) {
		this.typeMsg = typeMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
