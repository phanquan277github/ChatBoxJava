package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static volatile ServerMultiThreadMangager mThreadManager;
	/* từ khóa volatile được sử dụng để đánh dấu một biến:
		+ Đảm bảo sự đồng bộ giữa các luồng (thread-safety). mọi thay đổi của nó sẽ được hiển thị ngay lập tức cho tất cả các luồng khác. 
		+ Ngăn cản việc lưu trữ cache của CPU: gg tìm thêm thông tin.
	*/
	
	public static void main(String[] args) {
		ServerSocket serverSocket;
		mThreadManager = new ServerMultiThreadMangager();
		Socket socket = null;
		int num = 0;
		final int PORT = 7777;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server run on " + PORT);
			while (true) {
				socket = serverSocket.accept();
				System.out.println("Client "+ num +" : " + socket + " is connected!");
				ServerThread thread = new ServerThread(socket);
				mThreadManager.add(thread);
				num++;
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("Client "+ num +" : " + socket + " is disconnected!");
		}

  }
}
