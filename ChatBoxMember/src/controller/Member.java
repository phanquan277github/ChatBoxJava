package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.MemberModel;
import view.MemberFrame;

public class Member extends javax.swing.JFrame {
  public static void main(String[] args) {
    String hostName = "localhost";
    int portNumber = 7777;
    try {
      Socket socket = new Socket(hostName, portNumber);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

      MemberModel memberModel = new MemberModel();

      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // tùy chỉnh giao diện cho đẹp :)))
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
          | UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
      }
      MemberFrame memberFrame = new MemberFrame(memberModel);
      memberFrame.setVisible(true);

      MemberThread thread = new MemberThread(socket, memberModel);
      thread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
