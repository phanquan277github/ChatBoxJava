package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import database.Database;
import model.ClientModel;
import model.MessageModel;
import view.ServerFrame;

public class ServerThread extends Thread {
	private Socket socket;
	private InputStream inStream;
	private OutputStream outStream;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
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
			this.outStream = socket.getOutputStream();
			this.inStream = socket.getInputStream();
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.objOut = new ObjectOutputStream(socket.getOutputStream());
			this.objIn = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void doSendObjResponse(Object obj) {
		try {
			synchronized(objOut) {
				objOut.writeObject(obj);
				objOut.flush();
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
			ClientModel clientModel;
			while(!socket.isClosed()){
				message = in.readUTF();
            	String[] handle = message.split("\\<\\$\\>");
            	switch(handle[0]) {
	            	case "checkLogin":
	            		checkLogin(handle[1], handle[2]); // neu account đúng trả về thong tin của client tuong ung
	            		break;
	            	case "checkUserName":
	            		userNameIsExist(handle[1], "createAccount"); // phản hồi true nếu tồn tại userName
	        			break;
	            	case "createAccount": 
	            		int accountId = db.createAccount(handle[1], handle[2]);
	            		db.createMember(handle[3], accountId);
	            		doSendResponse("createAccount", "true");
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
            		case "newFile":
            			String fileName = handle[1];
            			int fileSize = Integer.parseInt(handle[2]);
            			byte[] fileData = new byte[fileSize];
                        in.readFully(fileData);
                        db.addFile(client.getClientId(), client.getCurentGroupId(), fileName, fileData);
            			Server.mThreadManager.notifyNewMessage(client.getCurentGroupId());
            			break;
            		case "updateAvata":
            			String avatata = handle[1];
            			int avataSize = Integer.parseInt(handle[2]);
            			byte[] avataData = new byte[avataSize];
                        in.readFully(avataData);
                        db.updateAvata(client.getClientId(), avataData);
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
            			clientModel = (ClientModel) objIn.readObject();
            			db.updateClientInfo(clientModel);
            			ClientModel clientInfo = db.getClientInfo(clientModel.getId(), "byClientId");
            			doSendResponse("getClientInfo");
            			doSendObjResponse(clientInfo);
            			break;
            		case "getClientInfo":
            			ClientModel clientInfo2 = db.getClientInfo(Integer.parseInt(handle[1]), "byClientId");
            			doSendResponse("getClientInfo");
            			doSendObjResponse(clientInfo2);
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
            		case "deleteGroup":
            			db.deleteGroup(Integer.parseInt(handle[1]));
            			Server.mThreadManager.notifyNewGroup(client.getUserName());
            			break;
            		case "callVideo":
            			// chuyển tiếp thông báo có call tới client thuộc group đang call
            			this.client.setCurentGroupId(Integer.parseInt(handle[1]));
            			Server.mThreadManager.notifyCallVideo(this.client.getCurentGroupId(), this.client.getClientId());
            			break;
					case "acceptCallVideo":
		    			// thông báo với tất cả client là sẵn sàng call
//		    			Server.mThreadManager.startingCallVideo(this.client.getCurentGroupId(), this.client.getClientId());
						Server.mThreadManager.notifyAcceptCall(Integer.parseInt(handle[1]));
		    			break;
					case "startCallVideo":
			            System.out.println("startCallVideo:" + client.getClientName());
						for (ServerThread thread : Server.mThreadManager.getServerThreadList()) {
							if(thread.getClient().getClientId() != this.client.getClientId() && thread.getClient().getCurentGroupId() == Integer.parseInt(handle[1])) {
								System.out.println("chuyển tiếp: "+ thread.getClient().getClientName());
								while(true) {
									int dataType = in.readInt(); // Loại dữ liệu
					                if (dataType == 0) { // Dữ liệu từ client
					                    int imageSize = in.readInt();
					                    byte[] imageData = new byte[imageSize];
					                    in.readFully(imageData);

					                    // Gửi dữ liệu đến client khác
					                    out.writeInt(1);                  // Loại dữ liệu: Server gửi
					                    out.writeInt(imageSize);          // Kích thước dữ liệu
					                    out.write(imageData);             // Dữ liệu
					                    out.flush();
					                }
								}
							}
						}
		    			break;
		    		}
			}
		} catch (Exception e) {
			if (socket.isClosed()) {
                Server.mThreadManager.remove(this);
                System.out.println("end");
            }
            Server.mThreadManager.remove(this);
            System.out.println("End");
            Server.loadClientTable();
			e.printStackTrace();
		}
    }	
       
    private void userNameIsExist(String userName, String situation) throws SQLException {
    	int accountId = db.checkUsername(userName);
    	if (accountId == -1) {
    		if(situation.equals("createAccount")) {
    			doSendResponse("checkUserName", "userNameNotExist", userName);
    		}else if(situation.equals("addMemberToGroup")) {
    			doSendResponse("checkUserNameAddMemberToGroup", "userNameNotExist", userName);
    		}else if(situation.equals("addFriend")) {
    			doSendResponse("checkUserNameAddFriend", "userNameNotExist", userName);
    		}
    	} else {
    		if(situation.equals("createAccount")) {
				doSendResponse("checkUserName", "userNameIsExist", userName);
			}else if(situation.equals("addMemberToGroup")) {
				doSendResponse("checkUserNameAddMemberToGroup", "userNameIsExist", userName);
			}else if(situation.equals("addFriend")) {
				doSendResponse("checkUserNameAddFriend", "userNameIsExist", userName);
			}
    	}
    }
    
    private void checkLogin(String userName, String password) throws SQLException {
    	int accountId = db.checkLogin(userName, password);
    	if (accountId == -1) {
    		doSendResponse("checkLogin", "false");
    	} else {
			ClientModel clientInfo = db.getClientInfo(accountId, "byAccId");
    		client.setClientId(clientInfo.getId());
			client.setClientName(clientInfo.getName());
			client.setAccountId(clientInfo.getAccountId());
			client.setUserName(userName);
			doSendResponse("checkLogin", "true");
			doSendObjResponse(clientInfo);
    	}
    }
}
