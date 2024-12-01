package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.GroupModel;
import model.MessageModel;
import model.ClientModel;
import model.FileModel;
import model.FriendRequestModel;
import view.Login;
import view.ClientFrame;
import view.Register;

public class ClientThread extends Thread {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ObjectInputStream objIn;
	private ClientModel clientModel;
	private GroupModel groupModel;
	private ClientFrame clientFrame;
	private Register register;
	private Login login;
	private int currentGroup = 0;
	private String transformSendBtn = "";

	public ClientThread(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // tùy chỉnh giao diện cho đẹp
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}

			clientModel = new ClientModel();
			groupModel = new GroupModel();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					login = new Login();
					register = new Register();
					login.setVisible(true);
					clientFrame = new ClientFrame(clientModel, groupModel);
					addEventForAll();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!socket.isClosed()) {
				String receive = in.readUTF();
				String[] cmd = receive.split("\\<\\$\\>");
				switch (cmd[0]) {
					case "checkUserName":
						if (cmd[1].equals("userNameNotExist")) {
							registerSuccess();
						} else if (cmd[1].equals("userNameIsExist")) {
							JOptionPane.showMessageDialog(null, "User name đã tồn tại");
							register.getUsernameTxt().requestFocus();
						}
						break;
					case "createAccount":
						if (cmd[1].equals("true")) {
							register.setVisible(false);
							login.setVisible(true);
						} else if (cmd[1].equals("false")) {
							// bỏ trường hợp false
						}
						break;
					case "checkLogin":
						if (cmd[1].equals("true")) {
							doCheckLoginSuccess(cmd[2]);
						} else if (cmd[1].equals("false")) {
							doCheckLoginFailed();
						}
						break;
					case "getGroupList":
						renderGroup(cmd[1]);
						break;
					case "createGroup":
						if (cmd[1].equals("false")) {
							JOptionPane.showMessageDialog(null, "Bạn đã tạo group tên " + cmd[2] + "!", "Error!",
									JOptionPane.ERROR_MESSAGE);
						} else if (cmd[1].equals("true")) {
							doSendRequest("getGroupList");
						}
						break;
					case "checkUserNameAddMemberToGroup":
						if (cmd[1].equals("userNameNotExist")) {
							JOptionPane.showMessageDialog(null, "Không có user name tương ứng với " + cmd[2] + "!",
									"Error!", JOptionPane.ERROR_MESSAGE);
						} else if (cmd[1].equals("userNameIsExist")) {
							doSendRequest("addMemberToGroup", currentGroup + "", cmd[2], clientModel.getName());
						}
						break;
					case "newGroup":
						doSendRequest("getGroupList");
						break;
					case "getMessagesList":
						List<MessageModel> messages = (List<MessageModel>) objIn.readObject();
						displayMessage(messages, clientModel.getId());
						doSendRequest("getFileList"); // gọi update file của group
						break;
					case "getFileList":
						// nhận được danh sachs các file của group và hiển thị file
						renderFile(cmd[1]);
						break;
					case "newMessage":
						doSendRequest("getMessagesList");
						break;
					case "newFile":
						doSendRequest("getFileList");
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
					case "getFriendsList":
						if(!cmd[1].equals("null")) {
							renderFiends(cmd[1]);
						}
						break;
					case "getFriendRequestList":
						renderFriendsRequest(cmd[1]);
						break;
					case "updateFriendRequestList":
						doSendRequest("getFriendRequestList", clientModel.getId()+"");
						doSendRequest("getFriendsList", clientModel.getId()+"");
						break;
					case "checkUserNameAddFriend":
						if (cmd[1].equals("userNameNotExist")) {
							JOptionPane.showMessageDialog(null, "Không có user name tương ứng với " + cmd[2] + "!",
									"Error!", JOptionPane.ERROR_MESSAGE);
						} else if (cmd[1].equals("userNameIsExist")) {
							doSendRequest("addFriend", clientModel.getId()+"", cmd[2]);
						}
						break;
					case "addFriend":
						if(cmd[1].equals("unsuccessful")) {
							JOptionPane.showMessageDialog(null, "Bạn đã kết bạn với " + cmd[2] + " từ trước!",
									"Error!", JOptionPane.ERROR_MESSAGE);
						}
						break;
					case "getClientInfo": 
						updateClientInfo(cmd[1]);
					
						break;
					}
				}
		} catch (Exception e) {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
					JOptionPane.showMessageDialog(clientFrame, "Server đã đóng kết nối");
			        System.exit(0);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
        }
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
		toUserPanelEvent();
		toFriendPanelEvent();
		toMainPanelEvent();
		undateInfoBtnEvent();
		logOutBtnEvent();
		radioBtnEvent();
		addFriendBtnEvent();
		acceptFriendEvent();
	}
	
	public void displayMessage(List<MessageModel> messages, int currentClientId) {
		if (messages.isEmpty()) return;
		clientFrame.getChatPane().setText("");

		  for (MessageModel message : messages) {
		        SimpleAttributeSet userStyle = new SimpleAttributeSet();
		        StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
		        SimpleAttributeSet guestStyle = new SimpleAttributeSet();
		        StyleConstants.setAlignment(guestStyle, StyleConstants.ALIGN_LEFT);

		        try {
		            SimpleAttributeSet style = (message.getClientId() == currentClientId) ? userStyle : guestStyle;
		            
		            if (message.getTypeMsg().equals("isTxt")) {
				        String messageContent = message.getNickname() + ": " + message.getContent() + "\n";
			            clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
			            int messageLength = messageContent.length();
			            clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageLength, messageLength, style, false);
			        } else if (isImageFile(message.getFileName())) {
		                // Tin nhắn chứa file ảnh
		                String messageContent = message.getNickname() + ": [Ảnh: " + message.getFileName() + "]\n";
		                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
		                ImageIcon imageIcon = new ImageIcon(message.getFileData()); // `getFileData` trả về byte[] của file
		                JLabel imageLabel = new JLabel(imageIcon);
		                clientFrame.getChatPane().insertComponent(imageLabel);
			        } else {
		                // Tin nhắn chứa file thường
		                String messageContent = message.getNickname() + ": [File: " + message.getFileName() + "]\n";
		                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
		                int messageLength = messageContent.length();
		                clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageLength, messageLength, style, false);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
    }
	
	public static boolean isImageFile(String fileName) {
		String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String fileNameLower = fileName.toLowerCase(); // Chuyển về chữ thường để so sánh
        for (String ext : IMAGE_EXTENSIONS) {
            if (fileNameLower.endsWith(ext)) {
                return true; // Tên file có đuôi là file ảnh
            }
        }
        return false; // Không phải file ảnh
    }
	
	// String...cont: gọi là varargs (variable arguments) cho phép một phương thức
	// có số lượng đối số linh động và dễ dàng sử dụng.
	private void doSendRequest(String cmd, String... cont) {
		try {
			synchronized (out) {
				String request = cmd + "<$>";
				// varargs sẽ là mảng gồm các đối số được truyền vào. Lặp qua mảng để lấy các
				// đối số
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

	private void doSendFile(String cmd, String fileName, byte[] fileData) {
		try {
			synchronized (out) {
				String request = cmd + "<$>" + fileName + "<$>" + fileData.length;
				out.writeUTF(request); // gửi yêu cầu có file mới
				out.flush();
				out.write(fileData); // gửi dữ liệu của file
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addFriendBtnEvent() {
		clientFrame.getAddFriendBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = clientFrame.getAddFriendTxt().getText().trim();
				if(userName.equals("") || userName.equals(" ")) {
					JOptionPane.showMessageDialog(null, "Tên người dùng không được trống", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					doSendRequest("checkUserNameAddFriend", userName);
				}
			}
		});
	}
	
	private void logOutBtnEvent() {
		clientFrame.getLogOutMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				doSendRequest("logOut");
				clientFrame.setVisible(false);
				login.getUserNameTxt().setText("");
				login.getPasswordTxt().setText("");
				login.setVisible(true);
			}
		});
	}
	
	private void radioBtnEvent() {
		clientFrame.getMaleRadioButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getFemaleRadioButton().setSelected(false);
				clientModel.setGender("Nam");
			}
		});
		clientFrame.getFemaleRadioButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getMaleRadioButton().setSelected(false);
				clientModel.setGender("Nữ");
			}
		});
	}
	
	private void undateInfoBtnEvent() {
		clientFrame.getUpdateInfoBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nickName = clientFrame.getNickNameTxt().getText().trim();
				String phoneNumber = clientFrame.getPhoneNumTxt().getText().trim();
				String birthday = clientFrame.getBirthdayTxt().getText().trim();

				if (nickName.equals("") || !phoneNumber.matches("^\\d{10}$") || !birthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
					JOptionPane.showMessageDialog(null, "Thông tin không hợp lệ!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					doSendRequest("updateClientInfo", clientModel.getId() + "", nickName, clientModel.getGender(), phoneNumber, birthday);
				}
			}
		});
	}

	private void toMainPanelEvent() {
		clientFrame.getMainMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getCardLayout().show(clientFrame.getContentPane(), "mainPanel");
				clientFrame.getNickNameLabel().setText(clientModel.getName());
			}
		});
	}

	private void toFriendPanelEvent() {
		clientFrame.getfriendMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getCardLayout().show(clientFrame.getContentPane(), "friendPanel");
				doSendRequest("getFriendsList", clientModel.getId()+"");
				doSendRequest("getFriendRequestList", clientModel.getId()+"");
			}
		});
	}

	private void toUserPanelEvent() {
		clientFrame.getinfoMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getCardLayout().show(clientFrame.getContentPane(), "userPanel");
				doSendRequest("getClientInfo", clientModel.getId()+"");
			}
		});
	}
	
	private void renderFiends(String listStringToRender) {
		String[] list = listStringToRender.split("\\<\\?\\>");
		clientFrame.getFriendsListModel().clear();
		int listLength = list.length;
		for (int i = 0; i < listLength; i++) {
			clientFrame.getFriendsListModel().addElement(list[i]);
		}
		clientFrame.getFriendList().setModel(clientFrame.getFriendsListModel());
	}

	private void renderFriendsRequest(String listStringToRender) {
		HashMap<Integer, String> hashMap = new HashMap<>();
		String[] list = null;
		if (!listStringToRender.equals("empty")) {
			list = listStringToRender.split("\\<\\#\\>");
			for (String item : list) {
				hashMap.put(Integer.parseInt(item.split("\\<\\?\\>")[0]), item.split("\\<\\?\\>")[1]);
			}
		}

		clientFrame.getFriendRequestListModel().clear();
		for (Map.Entry<Integer, String> entry : hashMap.entrySet()) { // lặp qua hashmap grList để lấy thông tin id, name
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getFriendRequestListModel().addElement(new FriendRequestModel(id, value)); 
		}
	}
	
	private void acceptFriendEvent() {
		clientFrame.getFriendRequestList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					FriendRequestModel selectedItem = clientFrame.getFriendRequestList().getSelectedValue();
					if(selectedItem != null) {
						int selectedId = selectedItem.getSenderId();
						int rs = JOptionPane.showConfirmDialog(null, "Chấp nhận kết bạn với "+selectedItem.getSenderName() + " ?");
						if(rs == JOptionPane.YES_OPTION) {
							doSendRequest("acceptFriend", clientModel.getId()+"", selectedId+"");
						} else {
							return;
						}
					}
				}
			}
		});
	}
	
	private void renderFile(String fileListString) {
		HashMap<Integer, String> fileList = new HashMap<>();
		String[] list = null;
		if (!fileListString.equals("empty")) {
			list = fileListString.split("\\<\\#\\>");
			for (String grlu : list) {
				fileList.put(Integer.parseInt(grlu.split("\\<\\?\\>")[0]), grlu.split("\\<\\?\\>")[1]);
			}
		}

		clientFrame.getFileListModel().clear();
		for (Map.Entry<Integer, String> entry : fileList.entrySet()) { // lặp qua hashmap grList để lấy thông tin id,
																		// name
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getFileListModel().addElement(new FileModel(id, value)); // lấy listModel và thêm elemnt là
																					// GroupModel
		}
	}

	private void renderGroup(String groupListString) {
		HashMap<Integer, String> groupList = new HashMap<>();
		String[] grList = null;
		grList = groupListString.split("\\<\\#\\>");

		for (String grlu : grList) {
			groupList.put(Integer.parseInt(grlu.split("\\<\\?\\>")[0]), grlu.split("\\<\\?\\>")[1]);
		}

		clientFrame.getGroupListModel().clear();
		for (Map.Entry<Integer, String> entry : groupList.entrySet()) { // lặp qua hashmap grList để lấy thông tin id,
																		// name
																		// của group
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getGroupListModel().addElement(new GroupModel(id, value)); // lấy listModel và thêm elemnt là
																					// GroupModel
		}
	}

	private void sendBtnEvent() {
		clientFrame.getSendBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if (transformSendBtn.equals("sendFile")) {
					File file = clientFrame.getFileUpChooser().getSelectedFile();
					// Đọc nội dung của file
					byte[] fileData;
					try {
						fileData = Files.readAllBytes(file.toPath());
						// gửi nội dung file lên server
						doSendFile("newFile", file.getName(), fileData);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					clientFrame.getMessageTxt().setText("");
					transformSendBtn = "";
				} else {
					String message = clientFrame.getMessageTxt().getText();
					if (message.equals("")) {
						return;
					} else {
						doSendRequest("newMessage", message);
						clientFrame.getMessageTxt().setText("");
					}
				}
			}
		});
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
				doSendRequest("createGroup", groupName);
			}
		});
	}

	private void addToGroupBtnEvent() {
		clientFrame.getAddToGroupBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentGroup == 0) {
					JOptionPane.showMessageDialog(null, "Bạn chưa chọn group!", "Error!", JOptionPane.ERROR_MESSAGE);
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
				if (result == JFileChooser.APPROVE_OPTION) { // kiểm tra đã chọn file hay chưa
					transformSendBtn = "sendFile"; // chuyển sang sendFile cho sendBtn biết là đang gửi file
					clientFrame.getMessageTxt().setText(clientFrame.getFileUpChooser().getSelectedFile().getName());
				}
			}
		});
	}

	private void showGroupListEvent() {
		clientFrame.getShowGroupList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					GroupModel selectedItem = clientFrame.getShowGroupList().getSelectedValue();
					clientFrame.getGroupNameLabel().setText(selectedItem.getName());
					int selectedId = selectedItem.getId();
					currentGroup = selectedId;
					int rs = JOptionPane.showConfirmDialog(null, "Xác nhận xóa group này.");
					if(rs == JOptionPane.YES_OPTION) {
						doSendRequest("deleteGroup", selectedId+"");
					}
                } else if (SwingUtilities.isLeftMouseButton(e)) { // chuot trai
                	GroupModel selectedItem = clientFrame.getShowGroupList().getSelectedValue();
					clientFrame.getGroupNameLabel().setText(selectedItem.getName());
					int selectedId = selectedItem.getId();
					currentGroup = selectedId;
					doSendRequest("setCurrentGroupId", currentGroup + "");
					doSendRequest("getMessagesList");
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
							doSendRequest("downloadFile", selectedItem.getId() + "", selectedItem.getFileName());
						} else {
							return;
						}
					}
				}
			}
		});
	}

	private void updateClientInfo(String memberInfo) {
		String path[] = memberInfo.split("\\<\\?\\>");
		clientModel.setId(Integer.parseInt(path[0]));
		clientModel.setName(path[1]);
		clientFrame.getNickNameTxt().setText(path[1]);
		clientModel.setAccountId(Integer.parseInt(path[2]));
		if(path[3].equals("null")) {
			// khong render
		} else {
			clientModel.setGender(path[3]); 
			if(path[3].equals("Nam")) {
				clientFrame.getMaleRadioButton().setSelected(true);
			} else {
				clientFrame.getFemaleRadioButton().setSelected(true);
			}
		}
		if(!path[4].equals("null")) {
			clientModel.setPhone(path[4]);
			clientFrame.getPhoneNumTxt().setText(path[4]);
		}
		if(!path[5].equals("null")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
			    java.util.Date utilDate = dateFormat.parse(path[5]);
			    clientModel.setBirthday(utilDate);
				clientFrame.getBirthdayTxt().setText(path[5]);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
	}
	
	private void doCheckLoginSuccess(String memberInfo) {
		updateClientInfo(memberInfo);
		login.setVisible(false);

		clientFrame.setVisible(true);
		clientFrame.getNickNameLabel().setText(clientModel.getName().trim());
		doSendRequest("getGroupList");
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
		String nickName = (String) JOptionPane.showInputDialog(null, "Nhập nickname: ", "Thông báo!",
				JOptionPane.PLAIN_MESSAGE);
		if (nickName == null)
			return;
		if (nickName.length() > 30) {
			JOptionPane.showMessageDialog(null, "Tên quá dài! (<=30 kí tự)", "Error!", JOptionPane.ERROR_MESSAGE);
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
