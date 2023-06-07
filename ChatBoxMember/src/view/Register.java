package view;

public class Register extends javax.swing.JFrame {

    public Register() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JTextField();
        passwordTxt = new javax.swing.JPasswordField();
        confirmPasswordTxt = new javax.swing.JPasswordField();
        registerBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        toLoginBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Welcome to the");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Chatbot App");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(118, 118, 118))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("REGISTER");
        jLabel2.setText("Username:");
        jLabel3.setText("Password:");
        jLabel4.setText("Confirm Password");

        registerBtn.setBackground(new java.awt.Color(0, 153, 153));
        registerBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        registerBtn.setForeground(new java.awt.Color(255, 255, 255));
        registerBtn.setText("Register");
        
        jLabel5.setText("Do you already have an account?");
        
        toLoginBtn.setBackground(new java.awt.Color(242, 242, 242));
        toLoginBtn.setForeground(new java.awt.Color(0, 153, 153));
        toLoginBtn.setText("Login");
        toLoginBtn.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(confirmPasswordTxt)
                                    .addComponent(usernameTxt)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(149, 149, 149)
                                    .addComponent(registerBtn))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(94, 94, 94)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(toLoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPasswordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toLoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    
    public javax.swing.JPasswordField getConfirmPasswordTxt() {
		return confirmPasswordTxt;
	}

	public void setConfirmPasswordTxt(javax.swing.JPasswordField confirmPasswordTxt) {
		this.confirmPasswordTxt = confirmPasswordTxt;
	}

	public javax.swing.JLabel getjLabel1() {
		return jLabel1;
	}

	public void setjLabel1(javax.swing.JLabel jLabel1) {
		this.jLabel1 = jLabel1;
	}

	public javax.swing.JLabel getjLabel2() {
		return jLabel2;
	}

	public void setjLabel2(javax.swing.JLabel jLabel2) {
		this.jLabel2 = jLabel2;
	}

	public javax.swing.JLabel getjLabel3() {
		return jLabel3;
	}

	public void setjLabel3(javax.swing.JLabel jLabel3) {
		this.jLabel3 = jLabel3;
	}

	public javax.swing.JLabel getjLabel4() {
		return jLabel4;
	}

	public void setjLabel4(javax.swing.JLabel jLabel4) {
		this.jLabel4 = jLabel4;
	}

	public javax.swing.JLabel getjLabel5() {
		return jLabel5;
	}

	public void setjLabel5(javax.swing.JLabel jLabel5) {
		this.jLabel5 = jLabel5;
	}

	public javax.swing.JLabel getjLabel6() {
		return jLabel6;
	}

	public void setjLabel6(javax.swing.JLabel jLabel6) {
		this.jLabel6 = jLabel6;
	}

	public javax.swing.JLabel getjLabel7() {
		return jLabel7;
	}

	public void setjLabel7(javax.swing.JLabel jLabel7) {
		this.jLabel7 = jLabel7;
	}

	public javax.swing.JPanel getjPanel1() {
		return jPanel1;
	}

	public void setjPanel1(javax.swing.JPanel jPanel1) {
		this.jPanel1 = jPanel1;
	}

	public javax.swing.JButton getToLoginBtn() {
		return toLoginBtn;
	}

	public void setToLoginBtn(javax.swing.JButton toLoginBtn) {
		this.toLoginBtn = toLoginBtn;
	}

	public javax.swing.JPasswordField getPasswordTxt() {
		return passwordTxt;
	}

	public void setPasswordTxt(javax.swing.JPasswordField passwordTxt) {
		this.passwordTxt = passwordTxt;
	}

	public javax.swing.JButton getRegisterBtn() {
		return registerBtn;
	}

	public void setRegisterBtn(javax.swing.JButton registerBtn) {
		this.registerBtn = registerBtn;
	}

	public javax.swing.JTextField getUsernameTxt() {
		return usernameTxt;
	}

	public void setUsernameTxt(javax.swing.JTextField usernameTxt) {
		this.usernameTxt = usernameTxt;
	}




	private javax.swing.JPasswordField confirmPasswordTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton toLoginBtn;
    private javax.swing.JPasswordField passwordTxt;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameTxt;
}
