package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader stdIn;
    
    public ServerThread(Socket socket) {
    	this.socket = socket;
    }
    
    @Override
    public void run() {
    	try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stdIn = new BufferedReader(new InputStreamReader(System.in));
	        
	        ReceiveMessages rm = new ReceiveMessages();
            SendMessages sm = new SendMessages();
            rm.start();
            sm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
 // Receiving messages from the server
    class ReceiveMessages extends Thread{  
         public void run(){
        	try {
                while (true) {
                    String message = in.readLine();
                	System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    // Sending messages to the server
    class SendMessages extends Thread {
    	public void run() {
    		try {
                while (true) {
                	System.out.println("Enter message: ");
                    out.println(stdIn.readLine());
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
    	}
    }
    
    
}
