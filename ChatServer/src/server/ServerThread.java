package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import database.Database;
import model.MessageModel;
import view.ServerFrame;

public class ServerThread extends Thread {
	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
    private Database db;
    private ClientManagement client;
    
	public ClientManagement getClient() {
		return client;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setClient(ClientManagement client) {
		this.client = client;
	}

	public ServerThread(Socket socket) {
    	this.socket = socket;

		try {
			this.client = new ClientManagement();
            LocalDateTime connectTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            
			this.client.setTimeIn(connectTime.format(formatter).toString());
			this.db = new Database();
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void doSendObjResponse(Object obj) {
		try {
			synchronized(objOut) {
				objOut.writeObject(obj);
				out.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doSendResponse(String cmd, String...cont) {
		try {
			synchronized(out) {
				String response = cmd + "<$>";
				for (String c : cont) {
					if(c.equals("")) {
						response += "null" + "<$>";
					}else {
						response += c + "<$>";
					}
				}
				String debug = response;
				 if (debug.length() > 60) {
					 debug = debug.substring(0, 60) + "...";
			    }
				out.writeUTF(response);
				out.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void doSendFile(String cmd, String fileName, byte[] fileData) {
		try {
			synchronized (out) {
				String request = cmd + "<$>" + fileData.length + "<$>" + fileName;
				out.writeUTF(request); // gửi yêu cầu có file mới
				out.flush();
				out.write(fileData); // gửi dữ liệu của file
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public void run() {
    	try {
			String message;
			while(!socket.isClosed()){
				message = in.readUTF();
            	String[] handle = message.split("\\<\\$\\>");
            	switch(handle[0]) {
	            	case "checkUserName":
	            		userNameIsExist(handle[1], "createAccount"); // phản hồi true nếu tồn tại userName
	        			break;
	            	case "createAccount": 
	            		db.createAccount(handle[1], handle[2]);
	            		int accountId = db.getAccountIdByUserName(handle[1]);
	            		db.createMember(handle[3], accountId);
	            		doSendResponse("createAccount", "true");
	            		break;
	            	case "checkLogin":
	            		checkLogin(handle[1], handle[2]); // neu account đúng trả về thong tin của client tuong ung
	            		break;
	            	case "getGroupList":
            			String reply2 = "";
            			ArrayList<String> grList = db.getGroupListByMemberId(client.getClientId());
            			if(grList.size() != 0) {
            				for (String gr : grList) {
            					reply2 += gr + "<#>";
            				}
            				doSendResponse("getGroupList", reply2);
            			}
            			break;
            		case "createGroup":
            			String check = db.checkGroupExistence(client.getClientId(), handle[1]); // nếu tồn tại group, memberId thì trả về true
            			if(check.equals("true")) {
            				doSendResponse("createGroup", "false", handle[1]);
            			}else if(check.equals("false")){
            				db.createGroup(client.getClientId(), handle[1]);
            				int newGroupId = db.getGroupIdByMemberId(client.getClientId());
            				String memberName2 = db.getMemberNameByMemberId(client.getClientId());
            				db.addMessage(1, newGroupId, memberName2+" đã tạo nhóm!"); // hệ thống thông báo đã tạo nhóm
            				String reply3 = "";
                			ArrayList<String> grList1 = db.getGroupListByMemberId(client.getClientId());
            				for (String gr : grList1) {
            					reply3 += gr + "<#>";
            				}
            				doSendResponse("getGroupList", reply3);
            			}
            			break;
            		case "addMemberToGroup":
            			db.addMemberToGroup(Integer.parseInt(handle[1]), handle[2]);
            			String memberName2 = db.getMemberNameByUserName(handle[2]);
            			String message2 = client.getClientName()+" đã thêm "+ memberName2 + " vào nhóm!";
            			db.addMessage(1, Integer.parseInt(handle[1]), message2);
            			Server.mThreadManager.notifyNewGroup(handle[2]); // thong bao cho user duoc them vao group
            			Server.mThreadManager.notifyNewMessage(Integer.parseInt(handle[1]));
            			break;
            		case "checkUserNameAddMemberToGroup":
            			userNameIsExist(handle[1], "addMemberToGroup");
	        			break;
            		case "newMessage":
            			db.addMessage(client.getClientId(), client.getCurentGroupId(), handle[1]);
            			Server.mThreadManager.notifyNewMessage(client.getCurentGroupId());
            			break;
            		case "getMessagesList": 
            			ArrayList<MessageModel> messageList = db.getMessagesListByGroupId(client.getCurentGroupId());
            			doSendResponse("getMessagesList");
            			if (messageList.size() != 0) {
            				doSendObjResponse(messageList);
            			}
            			break;
//            		case "getFileList": 
//            			String replyFile = "";
//            			ArrayList<String> fileList = db.getFileListByGroupId(client.getCurentGroupId());
//            			if(fileList.size() == 0) {
//    						replyFile += "empty" + "<$>";
//    					}else {
//    						for (String gr : fileList) {
//    							replyFile += gr + "<#>";
//            				}
//    					}
//        				doSendResponse("getFileList", replyFile);
//            			break;
            		case "newFile":
            			String fileName = handle[1];
            			int fileSize = Integer.parseInt(handle[2]);
            			
            			// đọc dữ liệu file
            			byte[] fileData = new byte[fileSize];
                        in.readFully(fileData);
            			
                        db.addFile(client.getClientId(), client.getCurentGroupId(), fileName, fileData);
            			Server.mThreadManager.notifyNewFile(client.getCurentGroupId());
            			break;
            		case "downloadFile":
            			int fileId = Integer.parseInt(handle[1]);
            			String fileNameDown = handle[2];
            			byte[] fileDataDown = db.getFileDataById(fileId);
            			doSendFile("downloadFile",fileNameDown, fileDataDown);
            			break;
            		case "setCurrentGroupId":
            			client.setCurentGroupId(Integer.parseInt(handle[1]));
            			break;
            		case "updateClientInfo":
            			db.updateClientInfo(Integer.parseInt(handle[1]), handle[2], handle[3], handle[4], handle[5]);
            			String clientInfo = db.getClientInfo(Integer.parseInt(handle[1]));
            			doSendResponse("getClientInfo", clientInfo);
            			break;
            		case "getClientInfo":
            			String clientInfo2 = db.getClientInfo(Integer.parseInt(handle[1]));
            			doSendResponse("getClientInfo", clientInfo2);
            			break;
            		case "getFriendsList":
            			String friendsList = db.getFriendsList(handle[1]);
        				doSendResponse("getFriendsList", friendsList);
            			break;
            		case "getFriendRequestList":
            			String replyGetFrRqList = "";
            			ArrayList<String> frRqList = db.getFriendRequestList(handle[1]);
            			if(frRqList.size() == 0) {
            				replyGetFrRqList += "empty" + "<$>";
    					}else {
    						for (String gr : frRqList) {
    							replyGetFrRqList += gr + "<#>";
            				}
    					}
        				doSendResponse("getFriendRequestList", replyGetFrRqList);
            			break;
            		case "checkUserNameAddFriend":
            			userNameIsExist(handle[1], "addFriend");
            			break;
            		case "addFriend":
						int memId = db.getMemberIdByUserName(handle[2]);
						int rs = db.checkFriendStatus(memId+"", handle[1]);
						if(rs == 1) {
							doSendResponse("addFriend", "unsuccessful", handle[2]);
						} else {
							db.addFriendRequest(memId, Integer.parseInt(handle[1]));
							Server.mThreadManager.notifyNewFriendRequest(handle[2]);
						}
            			break;
            		case "acceptFriend":
            			db.acceptFriend(handle[1], handle[2]);
            			doSendResponse("updateFriendRequestList");
            			Server.mThreadManager.notifyUpdateFriensList(Integer.parseInt(handle[2]));
            			break;
            		case "logOut":
            			System.out.println("debug log out serrver");
//            			Server.mThreadManager.remove(this);
//            			socket.close();
            			break;
            		case "deleteGroup":
            			db.deleteGroup(Integer.parseInt(handle[1]));
            			Server.mThreadManager.notifyNewGroup(client.getUserName());
            			break;
            		}
			}
		} catch (Exception e) {
			if (socket.isClosed()) {
                Server.mThreadManager.remove(this);
                System.out.println("Load");
            }

            Server.mThreadManager.remove(this);
            System.out.println("Load");
            Server.loadClientTable();
			e.printStackTrace();
		}
    }	
    
    private void userNameIsExist(String userName, String situation) throws SQLException {
    	ArrayList<String> accList = db.getAccountList();
			for (String acc : accList) {
				String[] tmp = acc.split("\\<\\?\\>");
				if(tmp[1].equals(userName)) {
					if(situation.equals("createAccount")) {
						doSendResponse("checkUserName", "userNameIsExist", userName);
					}else if(situation.equals("addMemberToGroup")) {
						doSendResponse("checkUserNameAddMemberToGroup", "userNameIsExist", userName);
					}else if(situation.equals("addFriend")) {
						doSendResponse("checkUserNameAddFriend", "userNameIsExist", userName);
					}
					return;
				}
			}
		if(situation.equals("createAccount")) {
			doSendResponse("checkUserName", "userNameNotExist", userName);
		}else if(situation.equals("addMemberToGroup")) {
			doSendResponse("checkUserNameAddMemberToGroup", "userNameNotExist", userName);
		}else if(situation.equals("addFriend")) {
			doSendResponse("checkUserNameAddFriend", "userNameNotExist", userName);
		}
    }
    
    private void checkLogin(String userName, String password) throws SQLException {
   	 	ArrayList<String> accList = db.getAccountList();
		for (String acc : accList) {
			String[] tmp = acc.split("\\<\\?\\>");
			if(tmp[1].equals(userName) && tmp[2].equals(password)) {
				int accountId = db.getAccountIdByUserName(userName);
				String memberInfo = db.getMemberInfoByAccountId(accountId);
				String path[] = memberInfo.split("\\<\\?\\>");
				client.setClientId(Integer.parseInt(path[0]));
				client.setClientName(path[1]);
				client.setAccountId(Integer.parseInt(path[2]));
				client.setUserName(userName);
				String info = db.getClientInfo(Integer.parseInt(path[0]));
				doSendResponse("checkLogin", "true", info);
				return;
			}
		}
		doSendResponse("checkLogin", "false");
    }
}
