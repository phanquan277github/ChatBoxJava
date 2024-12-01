package model;

public class FriendRequestModel {
	private int senderId;
	private String senderName;

	public FriendRequestModel() {
		this.senderId = 0;
		this.senderName = "";
	}

	public FriendRequestModel(int senderId, String senderName) {
		super();
		this.senderId = senderId;
		this.senderName = senderName;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@Override
	public String toString() {
		return senderName;
	}
	
}
