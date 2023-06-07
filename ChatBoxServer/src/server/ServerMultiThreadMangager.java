package server;

import java.util.ArrayList;

public class ServerMultiThreadMangager {
	private ArrayList<ServerThread> serverThreadList;
	
	public ArrayList<ServerThread> getServerThreadList() {
		return serverThreadList;
	}

	public ServerMultiThreadMangager() {
		serverThreadList = new ArrayList<>();
	}
	
	public void add(ServerThread thread) {
		serverThreadList.add(thread);
	}
	
	public void notifyNewMessage(int groupId) {
		for (ServerThread serverThread : Server.mThreadManager.getServerThreadList()) {
			serverThread.writeMuti("newMessage", groupId+"");
		}
	}
	public void notifyNewFile(int groupId) {
		for (ServerThread serverThread : Server.mThreadManager.getServerThreadList()) {
			serverThread.writeMuti("newFile", groupId+"");
		}
	}
	public void notifyNewGroup(String userName) {
		for (ServerThread serverThread : Server.mThreadManager.getServerThreadList()) {
			// kiểm tra thằng nào đang kết nối mà trùng với userName thì thông báo thằng đó mới được thêm vào group
			if(serverThread.getUserName().equals(userName)) {
				serverThread.writeMuti("newGroup", userName);				
			}
		}
	}
}
