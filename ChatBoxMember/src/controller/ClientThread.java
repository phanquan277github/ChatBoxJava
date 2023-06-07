package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.GroupModel;
import model.ClientModel;
import model.FileModel;
import view.Login;
import view.ClientFrame;
import view.ClientFrameOld;
import view.Register;

public class ClientThread extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ClientModel clientModel;
	private GroupModel groupModel;
	private ClientFrame clientFrame;
	private Register register;
	private Login login;
	private HashMap<Integer, String> groupList = new HashMap<>();
	private HashMap<Integer, String> fileList = new HashMap<>();
	private int currentGroup = 0;
	private String transformSendBtn = "";

	public ClientThread(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // tùy chỉnh giao diện cho đẹp
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}
			clientModel = new ClientModel();
			groupModel = new GroupModel();
			login = new Login();
			register = new Register();
			login.setVisible(true);
			clientFrame = new ClientFrame(clientModel, groupModel);
			addEventForAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
				String receive = in.readUTF();
				String debug = receive;
				 if (debug.length() > 60) {
					 debug = debug.substring(0, 60) + "...";
			    }
				String[] cmd = receive.split("\\<\\$\\>");
				switch(cmd[0]) {
					case "checkUserName":
						if(cmd[1].equals("userNameNotExist")) {
							registerSuccess();
						} else if(cmd[1].equals("userNameIsExist")) {
							JOptionPane.showMessageDialog(null, "User name đã tồn tại");
							register.getUsernameTxt().requestFocus();
						}
						break;
					case "createAccount":
						if(cmd[1].equals("true")) {
							register.setVisible(false);
							login.setVisible(true);
						} else if(cmd[1].equals("false")) {
							// bỏ trường hợp false
						}
						break;
					case "checkLogin":
						if(cmd[1].equals("true")) {
							doCheckLoginSuccess(cmd[2]);
						} else if(cmd[1].equals("false")) {
							doCheckLoginFailed();
						}
						break;
					case "getGroupListByMemberId":
						if(!cmd[1].equals("null")) {
							setGroupListToHashMap(cmd[1]);
							renderGroup(groupList);
						}
						break;
					case "createGroup":
						if (cmd[1].equals("false")) {
							JOptionPane.showMessageDialog(null, "Bạn đã tạo group tên " + cmd[2] + "!", "Error!",
									JOptionPane.ERROR_MESSAGE);
						} else if (cmd[1].equals("true")) {
							doSendRequest("getGroupListByMemberId", clientModel.getId()+"");
						}
						break;
					case "checkUserNameAddMemberToGroup": 
						if (cmd[1].equals("userNameNotExist")) {
							JOptionPane.showMessageDialog(null, "Không có user name tương ứng với " + cmd[2] + "!", "Error!",
									JOptionPane.ERROR_MESSAGE);
						} else if (cmd[1].equals("userNameIsExist")) {
							doSendRequest("addMemberToGroup", currentGroup+"", cmd[2], clientModel.getName());
						}
						break;
					case "newGroup":
						doSendRequest("getGroupListByMemberId", clientModel.getId()+"");
						break;
					case "getMessagesListByGroupId":
						if(!cmd[1].equals("null")) {
							String[] path = cmd[1].split("\\<\\?\\>");
							renderChat(path);
						}
						doSendRequest("getFileListByGroupId", currentGroup+"");  // gọi update file của group
						break;
					case "getFileListByGroupId":
//						nhận được danh sachs các file của group và hiển thị file
						if(!cmd[1].equals("null")) {
							setFileListToHashMap(cmd[1]);
							renderFile(fileList);
						}
						break;
					case "newMessage":
						if(currentGroup == Integer.parseInt(cmd[1])) {
							doSendRequest("getMessagesListByGroupId", currentGroup+"");
						}
						break;
					case "newFile":
						if(cmd[1].equals("null")) {
							break;
						}else if(currentGroup == Integer.parseInt(cmd[1])) {
							setFileListToHashMap(cmd[1]);
							renderFile(fileList);
						}
						break;
					case "downloadFile":
						// đọc dữ liệu file
						int fileSize = Integer.parseInt(cmd[1]);
						String fileName = cmd[2];
						byte[] fileData = new byte[fileSize];
						in.readFully(fileData);
						
						// Lưu file vào thư mục được chọn
					    File selectedDirectory = clientFrame.getFileDownChooser().getSelectedFile();
					    File fileDownload = new File(selectedDirectory.getAbsolutePath(), fileName);
					    FileOutputStream fileOut = new FileOutputStream(fileDownload);
					    
					    fileOut.write(fileData);
					    fileOut.close();
					    break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// sau khi đăng nhập sẽ gọi hàm này
	private void runclientFrame() {
		clientFrame.setVisible(true);
		clientFrame.getNickNameLabel().setText(clientModel.getName().trim());
		doSendRequest("getGroupListByMemberId", clientModel.getId()+"");
	}

	private void addEventForAll() {
		sendBtnEvent();
		addToGroupBtnEvent();
		createGroupBtnEvent();
		choseFileBtnEvent();
		showGroupListEvent();
		toRegisterBtnEvent();
		toLoginBtnEvent();
		loginBtnEvent();
		registerBtnEvent();
		showFilesListEvent();
	}

	// String...cont: gọi là varargs (variable arguments) cho phép một phương thức
	// có số lượng đối số linh động và dễ dàng sử dụng.
	private void doSendRequest(String cmd, String... cont) {
		try {
			synchronized (out) {
				String request = cmd + "<$>";
				// varargs sẽ là mảng gồm các đối số được truyền vào. Lặp qua mảng để lấy các đối số
				for (String c : cont) {
					request += c + "<$>";
				}
				out.writeUTF(request);
				out.flush(); // đảm bảo đã gửi dữ liệu đi
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doSendFile(String cmd, int memberId, int groupId, String fileName, byte[] fileData) {
		try {
			synchronized (out) {
				String request = cmd + "<$>" + memberId + "<$>" + groupId + "<$>" + fileName + "<$>" + fileData.length;
				out.writeUTF(request); // gửi yêu cầu có file mới
				out.flush();
				out.write(fileData); // gửi dữ liệu của file
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void renderChat(String[] messagesList) {
		clientFrame.getChatListModel().clear();
		for (String mess : messagesList) {
			clientFrame.getChatListModel().addElement(mess);
		}
		clientFrame.getShowChatList().setModel(clientFrame.getChatListModel());
	}
	
	private void renderFile(HashMap<Integer, String> fileList) {
		clientFrame.getFileListModel().clear();
		for (Map.Entry<Integer, String> entry : fileList.entrySet()) { // lặp qua hashmap grList để lấy thông tin id, name
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getFileListModel().addElement(new FileModel(id, value)); // lấy listModel và thêm elemnt là
																				// GroupModel
		}
	}
	
	private String[] getFileListByMemberId(String fileListString) {
		String[] listReturn = {};
		if (fileListString.contains("<#>")) {
			listReturn = fileListString.split("\\<\\#\\>");
		}
		return listReturn;
	}

	private void setFileListToHashMap(String fileListString) {
		fileList.clear();
		String[] list = getFileListByMemberId(fileListString);
		if (list.length != 0) {
			for (String grlu : list) {
				fileList.put(Integer.parseInt(grlu.split("\\<\\?\\>")[0]), grlu.split("\\<\\?\\>")[1]);
			}
		}
	}

	private void renderGroup(HashMap<Integer, String> grList) {
		clientFrame.getListModel().clear();
		for (Map.Entry<Integer, String> entry : grList.entrySet()) { // lặp qua hashmap grList để lấy thông tin id, name
																		// của group
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getListModel().addElement(new GroupModel(id, value)); // lấy listModel và thêm elemnt là
																				// GroupModel
		}
	}

	private void sendBtnEvent() {
		clientFrame.getSendBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(transformSendBtn.equals("sendFile")) {
					File file = clientFrame.getFileUpChooser().getSelectedFile();
				    // Đọc nội dung của file
				    byte[] fileData;
					try {
						fileData = Files.readAllBytes(file.toPath());
						// gửi nội dung file lên server
						doSendFile("newFile", clientModel.getId(), currentGroup, file.getName(), fileData);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					transformSendBtn = "";
				}else {
					String message = clientFrame.getMessageTxt().getText();
					if (message.equals("")) {
						return;
					} else {
						doSendRequest("newMessage", clientModel.getId()+"", currentGroup+"", message);
					}
				}
			}
		});
	}

	private String[] getGroupListByMemberId(String groupListString) {
		String[] listReturn = {};
		if (groupListString.contains("<#>")) {
			listReturn = groupListString.split("\\<\\#\\>");
		}
		return listReturn;
	}

	private void setGroupListToHashMap(String groupListString) {
		groupList.clear();
		String[] list = getGroupListByMemberId(groupListString);
		if (list.length != 0) {
			for (String grlu : list) {
				groupList.put(Integer.parseInt(grlu.split("\\<\\?\\>")[0]), grlu.split("\\<\\?\\>")[1]);
			}
		}
	}

	private void createGroupBtnEvent() {
		clientFrame.getCreateGroupBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String groupName = (String) JOptionPane.showInputDialog(null, "Nhập nickname: ", "Thông báo!",
						JOptionPane.PLAIN_MESSAGE);
				if (groupName == null)
					return;
				if (groupName.length() > 30) {
					JOptionPane.showMessageDialog(null, "Tên quá dài!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				doSendRequest("createGroup", clientModel.getId()+"", groupName);
			}
		});
	}
	
	private void addToGroupBtnEvent() {
		clientFrame.getAddToGroupBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentGroup == 0) {
					JOptionPane.showMessageDialog(null, "Bạn chưa chọn group!", "Error!",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String userName = (String) JOptionPane.showInputDialog(null, "Nhập userName cần thêm: ",
							"Thông báo!", JOptionPane.PLAIN_MESSAGE);
					if (userName == null)
						return;
					if (userName.length() > 30) {
						JOptionPane.showMessageDialog(null, "UserName không quá 30 ký tự!", "Error!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					doSendRequest("checkUserNameAddMemberToGroup", userName);
				}
			}
		});
	}

	private void choseFileBtnEvent() {
		clientFrame.getChoseFileBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = clientFrame.getFileUpChooser().showOpenDialog(clientFrame.getGroupPanel());
				if (result == JFileChooser.APPROVE_OPTION) {	// kiểm tra đã chọn file hay chưa
				   transformSendBtn = "sendFile"; // chuyển sang sendFile cho sendBtn biết là đang gửi file
				   clientFrame.getMessageTxt().setText(clientFrame.getFileUpChooser().getSelectedFile().getName());
				}
			}
		});
	}

	private void showGroupListEvent() {
		clientFrame.getShowGroupList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					GroupModel selectedItem = clientFrame.getShowGroupList().getSelectedValue();
					if (selectedItem != null) {
						clientFrame.getGroupNameLabel().setText(selectedItem.getName());
						int selectedId = selectedItem.getId();
						currentGroup = selectedId;
						doSendRequest("getMessagesListByGroupId", currentGroup+"");
					}
				}
			}
		});
	}
	
	private void showFilesListEvent() {
		clientFrame.getShowFilesList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					FileModel selectedItem = clientFrame.getShowFilesList().getSelectedValue();
					if (selectedItem != null) {
						
						int userSelection = clientFrame.getFileDownChooser().showOpenDialog(null);
						if (userSelection == JFileChooser.APPROVE_OPTION) {
							doSendRequest("downloadFile", selectedItem.getId()+"", selectedItem.getFileName());
						} else {
						    return;
						}
					}
				}
			}
		});
	}

	private void doCheckLoginSuccess(String memberInfo) {
		String path[] = memberInfo.split("\\<\\?\\>");
		clientModel.setId(Integer.parseInt(path[0]));
		clientModel.setName(path[1]);
		clientModel.setAccountId(Integer.parseInt(path[2]));
		login.setVisible(false);
		runclientFrame();
	}

	private void doCheckLoginFailed() {
		JOptionPane.showMessageDialog(null, "Username hoặc Password không đúng");
		register.getUsernameTxt().requestFocus();
	}

	private void loginBtnEvent() {
		login.getLoginBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = login.getUserNameTxt().getText();
				String password = String.valueOf(login.getPasswordTxt().getPassword());

				if (userName.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Các trường không được trống.");
				} else if (userName.contains(" ") || password.contains(" ")) {
					JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không được chứa khoảng trắng.");
				} else {
					doSendRequest("checkLogin", userName, password);
				}
			}
		});
	}

	private void registerSuccess() {
		String nickName = (String) JOptionPane.showInputDialog(null, "Nhập nickname: ", "Thông báo!", JOptionPane.PLAIN_MESSAGE);
		if (nickName == null)
			return;
		if (nickName.length() > 30) {
			JOptionPane.showMessageDialog(null, "Tên quá dài!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String userName = register.getUsernameTxt().getText();
		String password = String.valueOf(register.getPasswordTxt().getPassword());
		doSendRequest("createAccount", userName, password, nickName);
	}
	
	private void registerBtnEvent() {
		register.getRegisterBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = register.getUsernameTxt().getText();
				String password = String.valueOf(register.getPasswordTxt().getPassword());
				String confirmPassword = String.valueOf(register.getConfirmPasswordTxt().getPassword());
				if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Các trường không được trống.");
				} else if (userName.contains(" ") || password.contains(" ")) {
					JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không được chứa khoảng trắng.");
				} else if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "Mật khẩu nhập lại không khớp.");
				} else {
					doSendRequest("checkUserName", userName, password);
				}
			}
		});
	}

	private void toRegisterBtnEvent() {
		login.getToRegisterBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register.setVisible(true);
				login.setVisible(false);
			}
		});
	}

	private void toLoginBtnEvent() {
		register.getToLoginBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.setVisible(true);
				register.setVisible(false);
			}
		});
	}
}
