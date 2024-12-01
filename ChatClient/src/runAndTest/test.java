package runAndTest;

import java.awt.Color;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

public class test extends JFrame {

	private JPanel contentPane;
	private JTextField userText;
	private JTextField passwordText;
	private JTextField emailText;
	private JTextField numberphoneText;
	private JLabel errorLabel;
	private JLabel successLabel;
	private JButton Registerbutton;
	private JButton btnBack;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 996, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("REGISTER");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel.setBounds(380, 26, 227, 49);
		contentPane.add(lblNewLabel);
		
		JLabel usernamelabel = new JLabel("Username:");
		usernamelabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usernamelabel.setBounds(92, 96, 98, 25);
		contentPane.add(usernamelabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(92, 157, 98, 25);
		contentPane.add(lblPassword);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEmail.setBounds(92, 218, 98, 25);
		contentPane.add(lblEmail);
		
		JLabel lblNumberphone = new JLabel("Number Phone");
		lblNumberphone.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNumberphone.setBounds(92, 283, 134, 25);
		contentPane.add(lblNumberphone);
		
		userText = new JTextField();
		userText.setBounds(252, 96, 552, 26);
		userText.setColumns(10);
		contentPane.add(userText);
		
		passwordText = new JTextField();
		passwordText.setColumns(10);
		passwordText.setBounds(252, 161, 552, 26);
		contentPane.add(passwordText);
		
		emailText = new JTextField();
		emailText.setColumns(10);
		emailText.setBounds(252, 217, 552, 26);
		contentPane.add(emailText);
		
		numberphoneText = new JTextField();
		numberphoneText.setColumns(10);
		numberphoneText.setBounds(252, 283, 552, 26);
		contentPane.add(numberphoneText);
		
		Registerbutton = new JButton("Register");
		Registerbutton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		Registerbutton.setBounds(282, 356, 124, 33);
		contentPane.add(Registerbutton);
		
		
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnBack.setBounds(634, 356, 124, 33);
		contentPane.add(btnBack);
		
		// Tạo label hiển thị lỗi
				errorLabel = new JLabel("");
				errorLabel.setForeground(Color.RED);
				errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
				errorLabel.setBounds(252, 394, 552, 25);
				contentPane.add(errorLabel);
				
		// Tạo label hiển thị kết quả		
				successLabel = new JLabel("");
				successLabel.setForeground(Color.GREEN);
				successLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
				successLabel.setBounds(252, 430, 552, 25);
				contentPane.add(successLabel);	
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTextField getUserText() {
		return userText;
	}

	public JTextField getPasswordText() {
		return passwordText;
	}

	public JTextField getEmailText() {
		return emailText;
	}

	public JTextField getNumberphoneText() {
		return numberphoneText;
	}

	public JLabel getErrorLabel() {
		return errorLabel;
	}

	public JLabel getSuccessLabel() {
		return successLabel;
	}

	public JButton getRegisterbutton() {
		return Registerbutton;
	}

	public JButton getBtnBack() {
		return btnBack;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public void setUserText(JTextField userText) {
		this.userText = userText;
	}

	public void setPasswordText(JTextField passwordText) {
		this.passwordText = passwordText;
	}

	public void setEmailText(JTextField emailText) {
		this.emailText = emailText;
	}

	public void setNumberphoneText(JTextField numberphoneText) {
		this.numberphoneText = numberphoneText;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.errorLabel = errorLabel;
	}

	public void setSuccessLabel(JLabel successLabel) {
		this.successLabel = successLabel;
	}

	public void setRegisterbutton(JButton registerbutton) {
		Registerbutton = registerbutton;
	}

	public void setBtnBack(JButton btnBack) {
		this.btnBack = btnBack;
	}


	
	
	
}