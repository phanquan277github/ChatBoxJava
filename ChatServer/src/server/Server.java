package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.ServerFrame;

public class Server {
	public static final String REX_L2 = "\\@\\#\\@";
	public static volatile ServerMultiThreadMangager mThreadManager;
	/* từ khóa volatile được sử dụng để đánh dấu một biến:
		+ Đảm bảo sự đồng bộ giữa các luồng (thread-safety). mọi thay đổi của nó sẽ được hiển thị ngay lập tức cho tất cả các luồng khác. 
		+ Ngăn cản việc lưu trữ cache của CPU: gg tìm thêm thông tin.
	*/
    private static ServerFrame serverFrame;
    private static int PORT;
    private static ServerSocket serverSocket = null;
    private static Thread serverThread;
  
	public static void main(String[] args) {
		mThreadManager = new ServerMultiThreadMangager();
		try {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // tùy chỉnh giao diện cho đẹp
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					serverFrame = new ServerFrame();
		            try {
//						String ipAddress = InetAddress.getLocalHost().getHostAddress();
//						serverFrame.getLabelAddress().setText(ipAddress);
						serverFrame.getTxtPort().setText("7777");
					} catch (Exception e) {
						e.printStackTrace();
					}
					StartServerEvent();
					StopServerEvent();
					disconectClientEvent();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadClientTable() {
		 serverFrame.getClientTableModel().setRowCount(0); // Clear the table
		 
		 for (ServerThread thread : mThreadManager.getServerThreadList()) {
	            String ipAddress = thread.getSocket().getInetAddress().getHostAddress();
	            String timeIn = thread.getClient().getTimeIn();
	            int port = thread.getSocket().getPort();
	            serverFrame.getClientTableModel().addRow(new Object[]{
	                ipAddress,
	                timeIn,
	                port,
	                "Ngắt kết nối"
	            });
	        }
	}

	private static void disconectClientEvent() {
		serverFrame.getTableClientAccess().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = serverFrame.getTableClientAccess().rowAtPoint(evt.getPoint()); // Lấy hàng được click
		        int col = serverFrame.getTableClientAccess().columnAtPoint(evt.getPoint()); // Lấy cột được click

		        // Kiểm tra nếu cột là "Ngắt kết nối"
		        if (col == 3) {
		            int port = Integer.parseInt(serverFrame.getTableClientAccess().getValueAt(row, 2).toString()); // Lấy IP Address từ hàng

		            // Hiển thị hộp thoại xác nhận
		            int result = JOptionPane.showConfirmDialog(serverFrame, "Bạn có chắc chắn muốn ngắt kết nối client " + port + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
		            if (result == JOptionPane.YES_OPTION) {
		                disconnectClient(port); // Gọi hàm ngắt kết nối
		            }
		        }
		    }
		});
	}
	
	private static void disconnectClient(int port) {
	    for (ServerThread thread : Server.mThreadManager.getServerThreadList()) {
	        if (thread.getSocket().getPort() == port) {
	            try {
	                thread.getSocket().close(); // Đóng socket
	                Server.mThreadManager.remove(thread); // Xóa khỏi danh sách quản lý
	                loadClientTable(); // Cập nhật lại danh sách client
	                JOptionPane.showMessageDialog(serverFrame, "Ngắt kết nối client " + port + " thành công!");
	                return;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	private static void StartServerEvent() {
		serverFrame.getBtnStart().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PORT = Integer.parseInt(serverFrame.getTxtPort().getText());
				serverThread = new Thread(() -> {
					try {
						serverSocket = new ServerSocket(PORT);
						InetAddress serverAddress = serverSocket.getInetAddress();
//						String ipAddress = serverAddress.getByName("192.168.42.23");
						serverFrame.getLabelAddress().setText("192.168.42.23");
	    				serverFrame.getLableRunning().setText("Server đang mở!");
	    				
						while (!serverSocket.isClosed()) {
		                    Socket clientSocket = serverSocket.accept();
		    				System.out.println("Client : " + clientSocket + " is connected!");
		    				ServerThread thread = new ServerThread(clientSocket);
		    				mThreadManager.add(thread);
		    				thread.start();
		    				loadClientTable();
			            }
						
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
				});
				serverThread.start();
				
			}
		});
	}
	
	private static void StopServerEvent() {
		serverFrame.getBtnStop().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (serverSocket != null && !serverSocket.isClosed()) {
	                    serverSocket.close();
	                    System.out.println("Server is stopping...");
	                    
	                    // Dừng tất cả các thread client
	                    for (ServerThread thread : mThreadManager.getServerThreadList()) {
	                        thread.interrupt();  // Ngừng các thread client đang hoạt động
	                        try {
	                            thread.getSocket().close();  // Đóng socket client
	                        } catch (IOException ex) {
	                            ex.printStackTrace();
	                        }
	                    }
	                    mThreadManager.clear();  // Xóa danh sách các thread đã được quản lý
	                    loadClientTable();  
	    				serverFrame.getLableRunning().setText("Server đang đóng!");
	                }
		        } catch (IOException e2) {
		        	e2.printStackTrace();
		        }
			}
		});
	}
}
