package runAndTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.ServerThread;

public class Server {
  public static void main(String[] args) {
    ServerSocket serverSocket;
    final int PORT = 7777;
    try {
      serverSocket = new ServerSocket(PORT);
      System.out.println("Server run on " + PORT);
      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("Client " + socket + " is connected!");

        ServerThread thread = new ServerThread(socket);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
