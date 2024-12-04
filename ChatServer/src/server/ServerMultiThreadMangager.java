package server;

import java.util.ArrayList;

public class ServerMultiThreadMangager {
	private ArrayList<ServerThread> serverThreadList;
	
	public ServerMultiThreadMangager() {
		serverThreadList = new ArrayList<>();
	}
	public void remove(ServerThread thread) {
		serverThreadList.remove(thread);
	}
	public void add(ServerThread thread) {
		serverThreadList.add(thread);
	}
	public void clear() {
		serverThreadList.clear();
	}
	
	public void notifyAcceptCall(int groupId) {
		for (ServerThread thread : serverThreadList) {
			if(thread.getClient().getCurentGroupId() == groupId) {
				thread.doSendResponse("startCallVideo", groupId+"");
			}
		}
	}
	
	public void notifyCallVideo(int groupId, int callerId) {
		for (ServerThread thread : serverThreadList) {
			if(thread.getClient().getClientId() != callerId && thread.getClient().getCurentGroupId() == groupId) {
				thread.doSendResponse("newCall", groupId + "");
			}
		}
	}
	
	public void notifyNewMessage(int groupId) {
		for (ServerThread thread : serverThreadList) {
			if(thread.getClient().getCurentGroupId() == groupId) {
				thread.doSendResponse("newMessage");
			}
		}
	}

	public void notifyNewGroup(String userName) {
		for (ServerThread thread : serverThreadList) {
			// kiểm tra thằng nào đang kết nối mà trùng với userName thì thông báo thằng đó mới được thêm vào group
			if(thread.getClient().getUserName().equals(userName)) {
				thread.doSendResponse("newGroup", userName);				
			}
		}
	}
	public void notifyNewFriendRequest(String userName) {
		for (ServerThread thread : serverThreadList) {
			// kiểm tra thằng nào đang kết nối mà trùng với userName thì thông báo thằng đó mới được thêm vào group
			if(thread.getClient().getUserName().equals(userName)) {
				thread.doSendResponse("updateFriendRequestList");				
			}
		}
	}
	public void notifyUpdateFriensList(int clientId) {
		for (ServerThread thread : serverThreadList) {
			// kiểm tra thằng nào đang kết nối mà trùng với userName thì thông báo thằng đó mới được thêm vào group
			if(thread.getClient().getClientId() == clientId) {
				thread.doSendResponse("updateFriendRequestList");				
			}
		}
	}
	
	public ArrayList<ServerThread> getServerThreadList() {
		return serverThreadList;
	}
	public void setServerThreadList(ArrayList<ServerThread> serverThreadList) {
		this.serverThreadList = serverThreadList;
	}
	
	
}
