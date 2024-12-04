package controller;

import java.net.Socket;

import org.opencv.core.Core;

public class Client {
	  public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		  
	    String hostName = "192.168.42.23";
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
