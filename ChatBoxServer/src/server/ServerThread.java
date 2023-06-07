package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

public class ServerThread extends Thread {
	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Database db;
    private boolean isClosed;
    private String userName;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ServerThread(Socket socket) {
    	this.socket = socket;
    	isClosed = false;
    }
	
	private void doSendResponse(String cmd, String...cont) {
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
    		in = new DataInputStream(socket.getInputStream());
    		out = new DataOutputStream(socket.getOutputStream());
    		
			db = new Database();
			String message;
			while(!isClosed){
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
	            		checkLogin(handle[1], handle[2]); //gọi hàm trả về nếu account đúng trả về memberId của account đó
	            		break;
	            	case "getGroupListByMemberId":
            			String reply2 = "";
            			ArrayList<String> grList = db.getGroupListByMemberId(Integer.parseInt(handle[1]));
        				for (String gr : grList) {
        					reply2 += gr + "<#>";
        				}
        				doSendResponse("getGroupListByMemberId", reply2);
            			break;
            		case "createGroup":
            			String check = db.checkGroupExistence(Integer.parseInt(handle[1]), handle[2]); // nếu tồn tại group, memberId thì trả về true
            			if(check.equals("true")) {
            				doSendResponse("createGroup", "false", handle[2]);
            			}else if(check.equals("false")){
            				db.createGroup(Integer.parseInt(handle[1]), handle[2]);
            				int newGroupId = db.getGroupIdByMemberId(Integer.parseInt(handle[1]));
            				String memberName2 = db.getMemberNameByMemberId(Integer.parseInt(handle[1]));
            				db.addMessage(1, newGroupId, memberName2+" đã tạo nhóm!"); // hệ thống thông báo đã tạo nhóm
            				
            				String reply3 = "";
                			ArrayList<String> grList1 = db.getGroupListByMemberId(Integer.parseInt(handle[1]));
            				for (String gr : grList1) {
            					reply3 += gr + "<#>";
            				}
            				doSendResponse("getGroupListByMemberId", reply3);
            			}
            			break;
            		case "addMemberToGroup":
            			db.addMemberToGroup(Integer.parseInt(handle[1]), handle[2]);
            			String memberName2 = db.getMemberNameByUserName(handle[2]);
            			String message2 = handle[3]+" đã thêm "+ memberName2 + " vào nhóm!";
            			db.addMessage(1, Integer.parseInt(handle[1]), message2);
            			Server.mThreadManager.notifyNewGroup(handle[2]);
            			Server.mThreadManager.notifyNewMessage(Integer.parseInt(handle[1]));
            			break;
            		case "checkUserNameAddMemberToGroup":
            			userNameIsExist(handle[1], "addMemberToGroup");
	        			break;
            		case "newMessage":
            			db.addMessage(Integer.parseInt(handle[1]), Integer.parseInt(handle[2]), handle[3]);
            			Server.mThreadManager.notifyNewMessage(Integer.parseInt(handle[2]));
            			break;
            		case "getMessagesListByGroupId": 
            			String reply4 = "";
            			ArrayList<String> messageList = db.getMessagesListByGroupId(Integer.parseInt(handle[1]));
        				for (String gr : messageList) {
        					reply4 += gr + "<?>";
        				}
        				doSendResponse("getMessagesListByGroupId", reply4);
            			break;
            		case "getFileListByGroupId": 
            			String replyFile = "";
            			ArrayList<String> fileList = db.getFileListByGroupId(Integer.parseInt(handle[1]));
        				for (String gr : fileList) {
        					if(fileList.size() == 0) {
        						replyFile = "null" + "<#>";
        					}else {
        						replyFile = gr + "<#>";
        					}
        				}
        				doSendResponse("getFileListByGroupId", replyFile);
            			break;
            		case "newFile":
            			int memberId = Integer.parseInt(handle[1]);
            			int groupId = Integer.parseInt(handle[2]);
            			String fileName = handle[3];
            			int fileSize = Integer.parseInt(handle[4]);
            			
            			// đọc dữ liệu file
            			byte[] fileData = new byte[fileSize];
                        in.readFully(fileData);
            			
                        db.addFile(memberId, groupId, fileName, fileData);
            			Server.mThreadManager.notifyNewFile(groupId);
            			break;
            		case "downloadFile":
            			int fileId = Integer.parseInt(handle[1]);
            			String fileNameDown = handle[2];
            			byte[] fileDataDown = db.getFileDataById(fileId);
            			doSendFile("downloadFile",fileNameDown, fileDataDown);
            			break;
            		}
			}
		} catch (Exception e) {
			System.err.println("client "+ socket + " is disconnect!!!");
//			e.printStackTrace();
		}
    }	
    
    public void writeMuti(String message, String cont) {
    	doSendResponse(message, cont);
    }
    
    private void userNameIsExist(String userName, String situation) throws SQLException {
    	ArrayList<String> accList = db.getAccountList();
			for (String acc : accList) {
				String[] tmp = acc.split("\\<\\?\\>");
				if(tmp[1].equals(userName)) {
					if(situation.equals("checkUserName")) {
						doSendResponse("checkUserName", "userNameIsExist", userName); // trả về true nếu tồn tại account
					}else if(situation.equals("addMemberToGroup")) {
						doSendResponse("checkUserNameAddMemberToGroup", "userNameIsExist", userName);
					}
					return;
				}
			}
		if(situation.equals("checkUserName")) {
			doSendResponse("checkUserName", "userNameNotExist", userName);// trả về true chưa tồn tại account nào
		}else if(situation.equals("addMemberToGroup")) {
			doSendResponse("checkUserNameAddMemberToGroup", "userNameNotExist", userName);
		}
    }
    
    private void checkLogin(String userName, String password) throws SQLException {
   	 	ArrayList<String> accList = db.getAccountList();
		for (String acc : accList) {
			String[] tmp = acc.split("\\<\\?\\>");
			if(tmp[1].equals(userName) && tmp[2].equals(password)) {
				int accountId = db.getAccountIdByUserName(userName);
				String memberInfo = db.getMemberInfoByAccountId(accountId);
				this.userName = userName;
				doSendResponse("checkLogin", "true", memberInfo);
				return;
			}
		}
		doSendResponse("checkLogin", "false");
    }
}
