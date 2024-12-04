package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

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
	private OutputStream outStream;
	private InputStream inStream;
	private DataOutputStream out;
	private DataInputStream in;
	private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
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
			outStream = socket.getOutputStream();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			objOut = new ObjectOutputStream(socket.getOutputStream());
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
							clientModel = (ClientModel) objIn.readObject();
							doCheckLoginSuccess(clientModel);
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
					case "newMessage":
						doSendRequest("getMessagesList");
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
							JOptionPane.showMessageDialog(null, "Bạn đã là bạn bè với " + cmd[2] +"!",
									"Error!", JOptionPane.ERROR_MESSAGE);
						}
						break;
					case "getClientInfo": 
						ClientModel clientInfo =  (ClientModel) objIn.readObject();
						updateClientInfo(clientInfo);
						break;
					case "newCall":
						int response = JOptionPane.showConfirmDialog(null, "Cuộc gọi từ nhóm " + cmd[1] + "?", 
                                "Cuộc gọi video", JOptionPane.YES_NO_OPTION);
						if (response == JOptionPane.YES_OPTION) {
					        // Gửi thông báo đồng ý đến server để bắt đầu trao đổi video
							doSendRequest("acceptCallVideo", cmd[1]);
					    }
						break;
					case "startCallVideo":
						doSendRequest("startCallVideo", cmd[1]);
						// bắt đầu luồng gọi và nhận
						System.out.println("STart call");
						sendCall();
						receiveCall();
//						callVideo();
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
		recortBtnEvent();
		updateAvataEvent();
		callVideoEvent();
	}
	
	public void callVideoEvent() {
	    clientFrame.getCallBtn().addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            doSendRequest("callVideo", currentGroup+"");
	        }
	    });
	}
	
	private void callVideo() {
		System.out.println("CALL VIDEO");
		Mat frame = new Mat(); // Lưu từng khung hình từ camera
		Mat smallFrame = new Mat();
		  
        new Thread(() -> {
            VideoCapture camera = new VideoCapture(0);
            
            byte[] buffer;
            System.out.println("Đang mở camera... Nhấn ESC để thoát.");
            while (true) {
                if (camera.read(frame)) {
                	 synchronized (smallFrame) {
                         if (!smallFrame.empty()) {
                             // Tạo khung hình nhỏ hơn từ `smallFrame`
                             Size smallSize = new Size(frame.cols() / 4.0, frame.rows() / 4.0);
                             Mat resizedSmallFrame = new Mat();
                             Imgproc.resize(smallFrame, resizedSmallFrame, smallSize);

                             // Xác định vị trí chèn vào `frame`
                             Rect roi = new Rect(frame.cols() - resizedSmallFrame.cols(), 0, resizedSmallFrame.cols(), resizedSmallFrame.rows());
                             Mat regionOfInterest = frame.submat(roi);
                             resizedSmallFrame.copyTo(regionOfInterest);
                         }
                     }
                	 
                    try {
                        synchronized (outStream) {
                            buffer = matToBytes(frame);
                            outStream.write(buffer);
                            outStream.flush();
                        }
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                    // Hiển thị khung hình với khung nhỏ đã được chèn
                    HighGui.imshow("Camera Feed", frame);
                    if (HighGui.waitKey(1) == 27) { // Nhấn ESC để thoát
                        break;
                    }
                } else {
                    System.out.println("Không thể đọc khung hình từ camera.");
                }
            }
            camera.release();
            HighGui.destroyAllWindows();
        }).start();
        
        // nhận cuộc gọi
        new Thread(() -> {
        	while (true) {
    	        try {
    	        	int imageSize = in.readInt();
	                byte[] imageData = new byte[imageSize];
	                in.readFully(imageData);

	                Mat decodedFrame = Imgcodecs.imdecode(new MatOfByte(imageData), Imgcodecs.IMREAD_COLOR);
	                synchronized (smallFrame) {
	                    decodedFrame.copyTo(smallFrame);
	                }
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	            break;
    	        }
    	    }
        }).start();
	}
	private BlockingQueue<Mat> frameQueue = new LinkedBlockingQueue<>();

	private void sendCall() {
	    System.out.println("CALL VIDEO");
	    new Thread(() -> {
	        VideoCapture camera = new VideoCapture(0);
	        System.out.println("Đang mở camera...");
	        try {
	            Mat frame = new Mat();
	            while (true) {
	                if (camera.read(frame)) {
	                	 // Mã hóa frame thành JPEG
	                    MatOfByte buffer = new MatOfByte();
	                    Imgcodecs.imencode(".jpg", frame, buffer);
	                    byte[] imageBytes = buffer.toArray();

	                    // Gửi loại dữ liệu, kích thước và nội dung
		                    out.writeInt(0);                  // Loại dữ liệu: Client gửi
		                    out.writeInt(imageBytes.length); // Kích thước
		                    out.write(imageBytes);           // Dữ liệu
		                    out.flush();
		                    System.out.println("Gửi frame: " + imageBytes.length + " bytes.");
	                } else {
	                    System.out.println("Không thể đọc khung hình từ camera.");
	                }
	                Thread.sleep(20);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            camera.release();
	            HighGui.destroyAllWindows();
	        }
	    }).start();
	}
	
	private void receiveCall() {
//		new Thread(() -> {
        	try {
        		while (true) {
        			synchronized (socket) {
        				int dataType = in.readInt(); // 0 = từ client, 1 = từ server
                        if (dataType == 1) { // Dữ liệu từ server
                            int imageSize = in.readInt();
                            byte[] imageData = new byte[imageSize];
                            in.readFully(imageData);
                            // Giải mã hình ảnh
                            Mat decodedFrame = Imgcodecs.imdecode(new MatOfByte(imageData), Imgcodecs.IMREAD_COLOR);
                            if (decodedFrame.empty()) {
                                System.out.println("Frame nhận được không hợp lệ, kích thước: " + imageSize);
                                continue; // Bỏ qua frame này
                            }
                            if (!decodedFrame.empty()) {
                                HighGui.imshow("Received Feed", decodedFrame);
                                if (HighGui.waitKey(1) == 27) break; // ESC để thoát
                            } else {
                                System.out.println("Frame nhận được không hợp lệ.");
                            }
                        }
					}
        		}
   	        } catch (IOException e) {
   	            e.printStackTrace();
//   	            break;
   	        }
            HighGui.destroyAllWindows();
//		}).start();
	}
	
	
	
	private static Mat bytesToMat(byte[] data) {
	    return Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_COLOR);
	}
	private static byte[] matToBytes(Mat mat) {
	    MatOfByte matOfByte = new MatOfByte();
	    Imgcodecs.imencode(".jpg", mat, matOfByte);  // Lưu dưới dạng định dạng ảnh (ví dụ: JPEG)
	    return matOfByte.toArray();
	}
	
	public void doSendObj(Object obj) {
		try {
			synchronized(objOut) {
				objOut.writeObject(obj);
				out.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public void recortBtnEvent() {
		clientFrame.getRecordBtn().addActionListener(e -> {
			recordAudio();
		});
	}
	
	public void recordAudio() {
	    AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true, true);
	    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

	    try {
	        // Lấy dòng dữ liệu từ microphone
	        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
	        microphone.open(audioFormat);
	        microphone.start();

	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];

	        Thread thread = new Thread(() -> {
	            try {
	                while (true) {
	                    int bytesRead = microphone.read(buffer, 0, buffer.length);
	                    if (bytesRead > 0) {
	                        outputStream.write(buffer, 0, bytesRead);
	                    }
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        thread.start(); // bắt đầu ghi âm
	        int userChoice = JOptionPane.showConfirmDialog(clientFrame, "Đang trong quá trình ghi âm ...", "Ghi âm", JOptionPane.OK_CANCEL_OPTION);

	        thread.interrupt();
	        microphone.stop();
	        microphone.close();

	        if (userChoice == JOptionPane.OK_OPTION) {
	            byte[] audioBytes = outputStream.toByteArray();
		        doSendFile("newFile", "ghiam.wav", audioBytes);
	        } else {
	            System.out.println("Hủy quá trình ghi âm.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void playAudio(byte[] audioBytes) {
	    AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true, true);
	    try {
	        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
	        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioBytes.length / audioFormat.getFrameSize());

	        // Lấy dữ liệu từ AudioInputStream và phát lại âm thanh
	        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
	        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        sourceDataLine.open(audioFormat);
	        sourceDataLine.start();
	        
	        // Bộ đệm để đọc dữ liệu và phát lại âm thanh
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
	            sourceDataLine.write(buffer, 0, bytesRead);
	        }
	        
	        // Đảm bảo âm thanh được phát hết
	        sourceDataLine.drain();
	        sourceDataLine.stop();
	        sourceDataLine.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi khi phát âm thanh.");
	    }
	}
	public void displayMessage(List<MessageModel> messages, int currentClientId) {
		if (messages.isEmpty()) return;
		clientFrame.getChatPane().setText("");
    	clientFrame.getFileListModel().clear();
    	
	  for (MessageModel message : messages) {
	        SimpleAttributeSet userStyle = new SimpleAttributeSet();
	        StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_RIGHT);
	        SimpleAttributeSet guestStyle = new SimpleAttributeSet();
	        StyleConstants.setAlignment(guestStyle, StyleConstants.ALIGN_LEFT);

	        try {
	            SimpleAttributeSet style = (message.getClientId() == clientModel.getId()) ? userStyle : guestStyle;
	            if (message.getTypeMsg().equals("isTxt")) {
			        String messageContent = message.getNickname() + ": " + message.getContent() + "\n";
		            clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
		            int messageLength = messageContent.length();
		            clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageLength, messageLength, style, false);
		        } else if (isImageFile(message.getFileName())) {
		        	 // Thêm placeholder cho ảnh
	                String messageContent = "";
	                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
	                int placeholderPosition = clientFrame.getDoc().getLength();
		            clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageContent.length(), messageContent.length(), style, false);
	                
	                // Load và scale ảnh
	                ImageIcon originalIcon = new ImageIcon(message.getFileData()); // `getFileData` trả về byte[] của file
	                Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	                ImageIcon scaledIcon = new ImageIcon(scaledImage);
	                
	                JLabel imageLabel = new JLabel(scaledIcon);
	                clientFrame.getChatPane().setCaretPosition(placeholderPosition);
	                clientFrame.getChatPane().insertComponent(imageLabel);
	                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), "\n", style);
	                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), message.getFileName() + "\n", style);
		        } else if (isAudioFile(message.getFileName())) { // file âm thanh
		        	 String messageContent = "\n";
	                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), messageContent, style);
	                int placeholderPosition = clientFrame.getDoc().getLength();
		            clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageContent.length(), messageContent.length(), style, false);

	                // Tạo nút phát âm thanh với icon mic
	                ImageIcon originalIcon = new ImageIcon("src/img/audioWave.jpeg"); // `getFileData` trả về byte[] của file
	                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 35, Image.SCALE_SMOOTH);
	                ImageIcon scaledIcon = new ImageIcon(scaledImage);
	                JLabel audioLabel = new JLabel(scaledIcon);
	                
	                audioLabel.addMouseListener(new MouseAdapter() {
	                    @Override
	                    public void mouseClicked(MouseEvent e) {
	                        playAudio(message.getFileData());
	                    }
	                });
	                clientFrame.getChatPane().setCaretPosition(placeholderPosition);
	                clientFrame.getChatPane().insertComponent(audioLabel);
	                clientFrame.getDoc().insertString(clientFrame.getDoc().getLength(), "\n", style);
	                clientFrame.getDoc().setParagraphAttributes(clientFrame.getDoc().getLength() - messageContent.length(), messageContent.length(), style, false);
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
	        
            if (!message.getTypeMsg().equals("isTxt")) {
    			clientFrame.getFileListModel().addElement(new FileModel(message.getMessageId(), message.getFileName())); // lấy listModel và thêm elemnt là
            }
	    }
    }
	
	public static boolean isImageFile(String fileName) {
		String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
        if (fileName == null || fileName.isEmpty()) return false;
        String fileNameLower = fileName.toLowerCase(); // Chuyển về chữ thường để so sánh
        for (String ext : IMAGE_EXTENSIONS) {
            if (fileNameLower.endsWith(ext)) return true; // Tên file có đuôi là file ảnh
        }
        return false; // Không phải file ảnh
    }
	
	private boolean isAudioFile(String fileName) {
	    String[] audioExtensions = {".mp3", ".wav", ".ogg", ".flac", ".m4a"};
	    for (String ext : audioExtensions) {
	        if (fileName.toLowerCase().endsWith(ext)) return true;
	    }
	    return false;
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
	
	private void updateAvataEvent() {
		clientFrame.getUpdateAvataBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 JFileChooser fileChooser = new JFileChooser();
				 fileChooser.setDialogTitle("Chọn hình ảnh để cập nhật Avatar");
				 fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "bmp"));
				    
				 int result = fileChooser.showOpenDialog(null);
			    if (result == JFileChooser.APPROVE_OPTION) {
			        File selectedFile = fileChooser.getSelectedFile();
			        ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
				    Image scaledImage = imageIcon.getImage().getScaledInstance(clientFrame.getAvataLabel().getWidth(), clientFrame.getAvataLabel().getHeight(), Image.SCALE_SMOOTH);
				    clientFrame.getAvataLabel().setText("");
				    clientFrame.getAvataLabel().setIcon(new ImageIcon(scaledImage));
				    // Đọc nội dung của file
					byte[] fileData;
					try {
						fileData = Files.readAllBytes(selectedFile.toPath());
						doSendFile("updateAvata", selectedFile.getName(), fileData);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
			    }
			}
		});
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
				clientModel.setGender(true);
			}
		});
		clientFrame.getFemaleRadioButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientFrame.getMaleRadioButton().setSelected(false);
				clientModel.setGender(false);
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
					clientModel.setName(nickName);
					clientModel.setPhone(phoneNumber);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date utilDate;
					try {
						utilDate = dateFormat.parse(birthday);
						java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); 
						clientModel.setBirthday(sqlDate);
						doSendRequest("updateClientInfo");
						doSendObj(clientModel);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
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
	
	private void renderGroup(String groupListString) {
		HashMap<Integer, String> groupList = new HashMap<>();
		String[] grList = null;
		grList = groupListString.split("\\<\\#\\>");

		for (String grlu : grList) {
			groupList.put(Integer.parseInt(grlu.split("\\<\\?\\>")[0]), grlu.split("\\<\\?\\>")[1]);
		}

		clientFrame.getGroupListModel().clear();
		for (Map.Entry<Integer, String> entry : groupList.entrySet()) { // lặp qua hashmap grList để lấy thông tin id,
			int id = entry.getKey();
			String value = entry.getValue();
			clientFrame.getGroupListModel().addElement(new GroupModel(id, value)); // lấy listModel và thêm elemnt là
																					// GroupModel
		}
	}

	private void sendBtnEvent() {
		clientFrame.getMessageTxt().addActionListener(new ActionListener() {
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
				String groupName = (String) JOptionPane.showInputDialog(null, "Nhập tên nhóm: ", "Thông báo!",
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
						JOptionPane.showMessageDialog(null, "UserName không quá 30 ký tự!", "Error!", JOptionPane.ERROR_MESSAGE);
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
	    clientFrame.getShowFilesList().addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            // Kiểm tra xem click là chuột trái và click đôi (double-click)
	            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
	                JList<FileModel> fileList = clientFrame.getShowFilesList();
	                int index = fileList.locationToIndex(e.getPoint()); // Lấy vị trí mục được click
	                if (index >= 0) {
	                    FileModel selectedItem = fileList.getModel().getElementAt(index); // Lấy giá trị mục
	                    if (selectedItem != null) {
	                        int userSelection = clientFrame.getFileDownChooser().showOpenDialog(null);
	                        if (userSelection == JFileChooser.APPROVE_OPTION) {
	                            doSendRequest("downloadFile", selectedItem.getId() + "", selectedItem.getFileName());
	                        }
	                    }
	                }
	            }
	        }
	    });
	}

	private void updateClientInfo(ClientModel clientInfo) {
		clientFrame.getNickNameTxt().setText(clientModel.getName());
		
		if(clientInfo.getGender()) { //true name / false nữ
			clientFrame.getMaleRadioButton().setSelected(true);
		} else {
			clientFrame.getFemaleRadioButton().setSelected(true);
		}
		if (clientInfo.getPhone() != null) clientFrame.getPhoneNumTxt().setText(clientInfo.getPhone());
		if (clientInfo.getBirthday() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        String formattedDate = dateFormat.format(clientInfo.getBirthday());
			clientFrame.getBirthdayTxt().setText(formattedDate);
		}
		if (clientInfo.getAvata() != null) {
			try {
				ByteArrayInputStream byteInputStream = new ByteArrayInputStream(clientInfo.getAvata());
			    BufferedImage bufferedImage = ImageIO.read(byteInputStream);
			    Image scaledImage = bufferedImage.getScaledInstance(clientFrame.getAvataLabel().getWidth(), clientFrame.getAvataLabel().getHeight(), Image.SCALE_SMOOTH);
			    clientFrame.getAvataLabel().setText("");
			    clientFrame.getAvataLabel().setIcon(new ImageIcon(scaledImage));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private void doCheckLoginSuccess(ClientModel memberInfo) {
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
