package controller;

import java.net.Socket;

public class Client {
  public static void main(String[] args) {
    String hostName = "localhost";
    int portNumber = 7777;
    try {
      Socket socket = new Socket(hostName, portNumber);
      ClientThread thread = new ClientThread(socket);
      thread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
