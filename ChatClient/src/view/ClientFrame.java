package view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.StyledDocument;
import model.ClientModel;
import model.FileModel;
import model.FriendRequestModel;
import model.GroupModel;

public class ClientFrame extends javax.swing.JFrame {
	private ClientModel memberModel;
	private GroupModel groupModel;
	private DefaultListModel<GroupModel> groupListModel;
	private DefaultListModel<FileModel> fileListModel;
	private DefaultListModel<FriendRequestModel> friendRequestListModel;
	private DefaultListModel<String> chatListModel;
	private DefaultListModel<String> friendsListModel;
	private JFileChooser fileUpChooser;
	private JFileChooser fileDownChooser;
	
	private JTextPane chatPane;
	private StyledDocument doc;
	private JButton recordBtn;
	private JButton callBtn;
	
	
    public ClientFrame(ClientModel memberModel, GroupModel groupModel) {
    	this.memberModel = memberModel;
    	this.groupModel = groupModel;
        initComponents();
//        this.setSize(850, 550);
        this.setLocationRelativeTo(null);
        this.setTitle("Chat application");
        this.setResizable(false);
    }
    
    
    private void initComponents() {
    	 ImageIcon micIcon = new ImageIcon(getClass().getResource("/img/mic.png"));
         Image scaledImage = micIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
         ImageIcon scaledMicIcon = new ImageIcon(scaledImage);
        recordBtn = new JButton(scaledMicIcon);
        recordBtn.setToolTipText("Ghi âm"); // Tooltip khi hover chuột
        recordBtn.setPreferredSize(new Dimension(50, 50)); // Kích thước nút
        recordBtn.setFocusPainted(false); // Loại bỏ khung focus
      
        ImageIcon cameraIcon = new ImageIcon(getClass().getResource("/img/camera.png"));
        Image scaledCamera = cameraIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledCameraIcon = new ImageIcon(scaledCamera);
	       callBtn = new JButton(scaledCameraIcon);
	       callBtn.setToolTipText("Call"); // Tooltip khi hover chuột
	       callBtn.setPreferredSize(new Dimension(50, 50)); // Kích thước nút
	       callBtn.setFocusPainted(false); // Loại bỏ khung focus
        
    	chatPane = new JTextPane();
    	chatPane.setEditable(false); 
    	chatPane.setText("");
    	doc = chatPane.getStyledDocument();
    	
    	groupListModel = new DefaultListModel<>();
    	chatListModel = new DefaultListModel<>();
    	fileListModel = new DefaultListModel<>();
    	friendsListModel = new DefaultListModel<>();
    	friendRequestListModel = new DefaultListModel<>();
    	
    	fileUpChooser = new JFileChooser();
    	fileUpChooser.setDialogTitle("Chọn file muốn gửi!");
    	fileUpChooser.setSize(700, 500);
    	
    	fileDownChooser = new JFileChooser();
    	fileDownChooser.setDialogTitle("Chọn thư mục lưu file!");
    	fileDownChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	fileDownChooser.setSize(700, 500);
    	
        mainPanel = new javax.swing.JPanel();
        groupPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showGroupList = new javax.swing.JList<>(groupListModel); //
        showGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chỉ cho phép click 1 item trong jlist
        nickNameLabel = new javax.swing.JLabel();
        createGroupBtn = new javax.swing.JButton();
        addToGroupBtn = new javax.swing.JButton();
        groupNameLabel = new javax.swing.JLabel();
        showChatScrollPane = new javax.swing.JScrollPane();
        filesLabel = new javax.swing.JLabel();
        choseFileBtn = new javax.swing.JButton();
        messageTxt = new javax.swing.JTextField();
        sendBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setPreferredSize(new Dimension(205, 361)); // ngan jList tu dong thay doi kich thuoc
        showFilesList = new javax.swing.JList<>(fileListModel);	//
        userPanel = new javax.swing.JPanel();
        avataLabel = new javax.swing.JLabel();
        updateAvataBtn = new javax.swing.JButton();
        infoPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        genderLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        birdayLabel = new javax.swing.JLabel();
        nickNameTxt = new javax.swing.JTextField();
        genderTxt = new javax.swing.JTextField();
        birthdayTxt = new javax.swing.JTextField();
        phoneNumTxt = new javax.swing.JTextField();
        updateInfoBtn = new javax.swing.JButton();
        friendPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        friendList = new javax.swing.JList<>(friendsListModel); //
        addFriendBtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        friendRequestList = new javax.swing.JList<>(friendRequestListModel);	//
        jLabel2 = new javax.swing.JLabel();
        addFriendTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        settingMenu = new javax.swing.JMenu();
        infoMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        friendMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        logOutMenuItem = new javax.swing.JMenuItem();
        mainMenuItem = new JMenuItem(); //
        jSeparator3 = new javax.swing.JPopupMenu.Separator(); //
        maleRadioButton = new javax.swing.JRadioButton(); //
        femaleRadioButton = new javax.swing.JRadioButton(); //
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        groupPanel.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane2.setViewportView(showGroupList);

        javax.swing.GroupLayout groupPanelLayout = new javax.swing.GroupLayout(groupPanel);
        groupPanel.setLayout(groupPanelLayout);
        groupPanelLayout.setHorizontalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        groupPanelLayout.setVerticalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        nickNameLabel.setBackground(new java.awt.Color(153, 153, 153));
        nickNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        nickNameLabel.setForeground(new java.awt.Color(0, 102, 51));
        nickNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nickNameLabel.setText(".............................");

        createGroupBtn.setText("Tạo nhóm");

        addToGroupBtn.setText("Thêm vào nhóm");

        groupNameLabel.setBackground(new java.awt.Color(0, 255, 255));
        groupNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        groupNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        groupNameLabel.setText("..........................................................................................");

        showChatScrollPane.setViewportView(chatPane);

        filesLabel.setBackground(new java.awt.Color(224, 224, 224));
        filesLabel.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        filesLabel.setForeground(new java.awt.Color(0, 102, 0));
        filesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        filesLabel.setText("Files");
        filesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filesLabel.setName(""); // NOI18N
        filesLabel.setOpaque(true);

        choseFileBtn.setText("File");
        sendBtn.setText("Send");
        jScrollPane1.setViewportView(showFilesList);
        
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        
     // Định nghĩa layout cho mainPanel
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(nickNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(groupPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(2, 2, 2)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(groupNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(showChatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(recordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10) // Khoảng cách giữa recordBtn và callBtn
                                    .addComponent(callBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))) // Thêm callBtn
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addGap(18, 18, 18)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(sendBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                .addComponent(filesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addComponent(addToGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                            .addContainerGap())))
        );

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addComponent(nickNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(groupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(groupNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addToGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addComponent(filesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(recordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(callBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)) // Đặt callBtn vào hàng với recordBtn
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addComponent(showChatScrollPane)
                            .addGap(18, 18, 18)
                            .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18))))
        );


        getContentPane().add(mainPanel, "mainPanel");

        userPanel.setBackground(new java.awt.Color(255, 255, 255));
        userPanel.setForeground(new java.awt.Color(51, 51, 51));

        avataLabel.setBackground(new java.awt.Color(204, 204, 204));
        avataLabel.setForeground(new java.awt.Color(0, 51, 51));
        avataLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avataLabel.setText("Ảnh đại diện");
        avataLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        avataLabel.setOpaque(true);

        updateAvataBtn.setText("Cập nhật ảnh đại diện");

        nameLabel.setBackground(new java.awt.Color(255, 255, 255));
        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        nameLabel.setForeground(new java.awt.Color(0, 102, 51));
        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nameLabel.setText("Tên người dùng");

        genderLabel.setBackground(new java.awt.Color(255, 255, 255));
        genderLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        genderLabel.setForeground(new java.awt.Color(0, 102, 51));
        genderLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        genderLabel.setText("Giới tính");

        phoneLabel.setBackground(new java.awt.Color(255, 255, 255));
        phoneLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        phoneLabel.setForeground(new java.awt.Color(0, 102, 51));
        phoneLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        phoneLabel.setText("Số điện thoại");

        birdayLabel.setBackground(new java.awt.Color(255, 255, 255));
        birdayLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        birdayLabel.setForeground(new java.awt.Color(0, 102, 51));
        birdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        birdayLabel.setText("Ngày sinh (yyyy-mm-dd)");

        updateInfoBtn.setText("Cập nhật thông tin");

        maleRadioButton.setText("Nam");

        femaleRadioButton.setText("Nữ");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(nickNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(birdayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phoneNumTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdayTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(maleRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(femaleRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(41, 41, 41))
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(updateInfoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nickNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maleRadioButton)
                    .addComponent(femaleRadioButton))
                .addGap(30, 30, 30)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(birthdayTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(birdayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(updateInfoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateAvataBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avataLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(avataLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(updateAvataBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        getContentPane().add(userPanel, "userPanel");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 152, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Danh sách bạn bè");
        friendList.setDragEnabled(true);
        jScrollPane3.setViewportView(friendList);

        addFriendBtn.setText("Thêm bạn");
        
        jScrollPane4.setViewportView(friendRequestList);

        jLabel2.setBackground(new java.awt.Color(231, 231, 231));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 152, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Lời mời kết bạn");
        jLabel2.setOpaque(true);

        jLabel3.setText("Nhập userName cần kết bạn");

        javax.swing.GroupLayout friendPanelLayout = new javax.swing.GroupLayout(friendPanel);
        friendPanel.setLayout(friendPanelLayout);
        friendPanelLayout.setHorizontalGroup(
            friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addGap(23, 23, 23)
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(friendPanelLayout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addFriendTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(addFriendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane4))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        friendPanelLayout.setVerticalGroup(
            friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendPanelLayout.createSequentialGroup()
                .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(friendPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(friendPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(friendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addFriendTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addFriendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        getContentPane().add(friendPanel, "friendPanel");

        settingMenu.setText("Danh mục");

        mainMenuItem.setText("Trang chủ");
        settingMenu.add(mainMenuItem);
        settingMenu.add(jSeparator3);
        
        infoMenuItem.setText("Thông tin tài khoản");
        settingMenu.add(infoMenuItem);
        settingMenu.add(jSeparator2);

        friendMenuItem.setText("Bạn bè");
        settingMenu.add(friendMenuItem);
        settingMenu.add(jSeparator1);

        logOutMenuItem.setText("Đăng xuất");
        settingMenu.add(logOutMenuItem);

        jMenuBar.add(settingMenu);

        setJMenuBar(jMenuBar);

        pack();
    }// </editor-fold>                        

    
    public JButton getRecordBtn() {
		return recordBtn;
	}
	public void setRecordBtn(JButton recordBtn) {
		this.recordBtn = recordBtn;
	}
	public DefaultListModel<FriendRequestModel> getFriendRequestListModel() {
		return friendRequestListModel;
	}

	public void setFriendRequestListModel(DefaultListModel<FriendRequestModel> friendRequestListModel) {
		this.friendRequestListModel = friendRequestListModel;
	}

	public DefaultListModel<String> getFriendsListModel() {
		return friendsListModel;
	}

	public void setFriendsListModel(DefaultListModel<String> friendsListModel) {
		this.friendsListModel = friendsListModel;
	}

	public javax.swing.JRadioButton getMaleRadioButton() {
		return maleRadioButton;
	}
	

	public JTextPane getChatPane() {
		return chatPane;
	}


	public void setChatPane(JTextPane chatPane) {
		this.chatPane = chatPane;
	}


	public StyledDocument getDoc() {
		return doc;
	}


	public void setDoc(StyledDocument doc) {
		this.doc = doc;
	}


	public void setMaleRadioButton(javax.swing.JRadioButton maleRadioButton) {
		this.maleRadioButton = maleRadioButton;
	}

	public javax.swing.JRadioButton getFemaleRadioButton() {
		return femaleRadioButton;
	}

	public void setFemaleRadioButton(javax.swing.JRadioButton femaleRadioButton) {
		this.femaleRadioButton = femaleRadioButton;
	}

	public ClientModel getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(ClientModel memberModel) {
		this.memberModel = memberModel;
	}

	public GroupModel getGroupModel() {
		return groupModel;
	}

	public void setGroupModel(GroupModel groupModel) {
		this.groupModel = groupModel;
	}

	public DefaultListModel<GroupModel> getGroupListModel() {
		return groupListModel;
	}

	public void setGroupListModel(DefaultListModel<GroupModel> groupListModel) {
		this.groupListModel = groupListModel;
	}

	public DefaultListModel<String> getChatListModel() {
		return chatListModel;
	}

	public void setChatListModel(DefaultListModel<String> chatListModel) {
		this.chatListModel = chatListModel;
	}

	public DefaultListModel<FileModel> getFileListModel() {
		return fileListModel;
	}

	public void setFileListModel(DefaultListModel<FileModel> fileListModel) {
		this.fileListModel = fileListModel;
	}

	public JFileChooser getFileUpChooser() {
		return fileUpChooser;
	}

	public void setFileUpChooser(JFileChooser fileUpChooser) {
		this.fileUpChooser = fileUpChooser;
	}

	public JFileChooser getFileDownChooser() {
		return fileDownChooser;
	}

	public void setFileDownChooser(JFileChooser fileDownChooser) {
		this.fileDownChooser = fileDownChooser;
	}

	public javax.swing.JButton getAddFriendBtn() {
		return addFriendBtn;
	}

	public void setAddFriendBtn(javax.swing.JButton addFriendBtn) {
		this.addFriendBtn = addFriendBtn;
	}

	public javax.swing.JTextField getAddFriendTxt() {
		return addFriendTxt;
	}

	public void setAddFriendTxt(javax.swing.JTextField addFriendTxt) {
		this.addFriendTxt = addFriendTxt;
	}

	public javax.swing.JButton getAddToGroupBtn() {
		return addToGroupBtn;
	}

	public void setAddToGroupBtn(javax.swing.JButton addToGroupBtn) {
		this.addToGroupBtn = addToGroupBtn;
	}

	public javax.swing.JLabel getAvataLabel() {
		return avataLabel;
	}

	public void setAvataLabel(javax.swing.JLabel avataLabel) {
		this.avataLabel = avataLabel;
	}

	public javax.swing.JTextField getBirthdayTxt() {
		return birthdayTxt;
	}

	public void setBirthdayTxt(javax.swing.JTextField birthdayTxt) {
		this.birthdayTxt = birthdayTxt;
	}

	public javax.swing.JButton getChoseFileBtn() {
		return choseFileBtn;
	}

	public void setChoseFileBtn(javax.swing.JButton choseFileBtn) {
		this.choseFileBtn = choseFileBtn;
	}

	public javax.swing.JButton getCreateGroupBtn() {
		return createGroupBtn;
	}

	public void setCreateGroupBtn(javax.swing.JButton createGroupBtn) {
		this.createGroupBtn = createGroupBtn;
	}

	public javax.swing.JLabel getFilesLabel() {
		return filesLabel;
	}

	public void setFilesLabel(javax.swing.JLabel filesLabel) {
		this.filesLabel = filesLabel;
	}

	public javax.swing.JList<String> getFriendList() {
		return friendList;
	}

	public void setFriendList(javax.swing.JList<String> friendList) {
		this.friendList = friendList;
	}

	public javax.swing.JMenu getsettingMenu() {
		return settingMenu;
	}

	public void setsettingMenu(javax.swing.JMenu settingMenu) {
		this.settingMenu = settingMenu;
	}

	public javax.swing.JPanel getFriendPanel() {
		return friendPanel;
	}

	public void setFriendPanel(javax.swing.JPanel friendPanel) {
		this.friendPanel = friendPanel;
	}

	public javax.swing.JList<FriendRequestModel> getFriendRequestList() {
		return friendRequestList;
	}

	public void setFriendRequestList(javax.swing.JList<FriendRequestModel> friendRequestList) {
		this.friendRequestList = friendRequestList;
	}

	public javax.swing.JTextField getGenderTxt() {
		return genderTxt;
	}

	public void setGenderTxt(javax.swing.JTextField genderTxt) {
		this.genderTxt = genderTxt;
	}

	public javax.swing.JList<GroupModel> getShowGroupList() {
		return showGroupList;
	}

	public void setShowGroupList(javax.swing.JList<GroupModel> showGroupList) {
		this.showGroupList = showGroupList;
	}

	public javax.swing.JLabel getGroupNameLabel() {
		return groupNameLabel;
	}

	public void setGroupNameLabel(javax.swing.JLabel groupNameLabel) {
		this.groupNameLabel = groupNameLabel;
	}

	public javax.swing.JPanel getGroupPanel() {
		return groupPanel;
	}

	public void setGroupPanel(javax.swing.JPanel groupPanel) {
		this.groupPanel = groupPanel;
	}

	public javax.swing.JMenuItem getinfoMenuItem() {
		return infoMenuItem;
	}

	public void setinfoMenuItem(javax.swing.JMenuItem infoMenuItem) {
		this.infoMenuItem = infoMenuItem;
	}

	public javax.swing.JPanel getInfoPanel() {
		return infoPanel;
	}

	public void setInfoPanel(javax.swing.JPanel infoPanel) {
		this.infoPanel = infoPanel;
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

	public javax.swing.JMenuBar getjMenuBar() {
		return jMenuBar;
	}

	public void setjMenuBar(javax.swing.JMenuBar jMenuBar) {
		this.jMenuBar = jMenuBar;
	}

	public javax.swing.JMenuItem getfriendMenuItem() {
		return friendMenuItem;
	}

	public void setfriendMenuItem(javax.swing.JMenuItem friendMenuItem) {
		this.friendMenuItem = friendMenuItem;
	}

	public javax.swing.JScrollPane getjScrollPane1() {
		return jScrollPane1;
	}

	public void setjScrollPane1(javax.swing.JScrollPane jScrollPane1) {
		this.jScrollPane1 = jScrollPane1;
	}

	public javax.swing.JScrollPane getjScrollPane2() {
		return jScrollPane2;
	}

	public void setjScrollPane2(javax.swing.JScrollPane jScrollPane2) {
		this.jScrollPane2 = jScrollPane2;
	}

	public javax.swing.JScrollPane getjScrollPane3() {
		return jScrollPane3;
	}

	public void setjScrollPane3(javax.swing.JScrollPane jScrollPane3) {
		this.jScrollPane3 = jScrollPane3;
	}

	public javax.swing.JScrollPane getjScrollPane4() {
		return jScrollPane4;
	}

	public void setjScrollPane4(javax.swing.JScrollPane jScrollPane4) {
		this.jScrollPane4 = jScrollPane4;
	}

	public javax.swing.JPopupMenu.Separator getjSeparator1() {
		return jSeparator1;
	}

	public void setjSeparator1(javax.swing.JPopupMenu.Separator jSeparator1) {
		this.jSeparator1 = jSeparator1;
	}

	public javax.swing.JPopupMenu.Separator getjSeparator2() {
		return jSeparator2;
	}

	public void setjSeparator2(javax.swing.JPopupMenu.Separator jSeparator2) {
		this.jSeparator2 = jSeparator2;
	}

	public javax.swing.JMenuItem getLogOutMenuItem() {
		return logOutMenuItem;
	}

	public void setLogOutMenuItem(javax.swing.JMenuItem logOutMenuItem) {
		this.logOutMenuItem = logOutMenuItem;
	}

	public javax.swing.JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(javax.swing.JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public javax.swing.JTextField getMessageTxt() {
		return messageTxt;
	}

	public void setMessageTxt(javax.swing.JTextField messageTxt) {
		this.messageTxt = messageTxt;
	}
	public javax.swing.JLabel getNameLabel() {
		return nameLabel;
	}
	public void setNameLabel(javax.swing.JLabel nameLabel) {
		this.nameLabel = nameLabel;
	}
	public javax.swing.JLabel getNameLabel1() {
		return genderLabel;
	}
	public void setNameLabel1(javax.swing.JLabel genderLabel) {
		this.genderLabel = genderLabel;
	}
	public javax.swing.JLabel getNameLabel2() {
		return birdayLabel;
	}
	public void setNameLabel2(javax.swing.JLabel birdayLabel) {
		this.birdayLabel = birdayLabel;
	}
	public javax.swing.JLabel getNameLabel3() {
		return phoneLabel;
	}
	public void setNameLabel3(javax.swing.JLabel phoneLabel) {
		this.phoneLabel = phoneLabel;
	}
	public javax.swing.JLabel getNickNameLabel() {
		return nickNameLabel;
	}
	public void setNickNameLabel(javax.swing.JLabel nickNameLabel) {
		this.nickNameLabel = nickNameLabel;
	}
	public javax.swing.JTextField getNickNameTxt() {
		return nickNameTxt;
	}
	public void setNickNameTxt(javax.swing.JTextField nickNameTxt) {
		this.nickNameTxt = nickNameTxt;
	}
	public javax.swing.JTextField getPhoneNumTxt() {
		return phoneNumTxt;
	}
	public void setPhoneNumTxt(javax.swing.JTextField phoneNumTxt) {
		this.phoneNumTxt = phoneNumTxt;
	}
	public javax.swing.JButton getSendBtn() {
		return sendBtn;
	}
	public void setSendBtn(javax.swing.JButton sendBtn) {
		this.sendBtn = sendBtn;
	}
	public javax.swing.JList<String> getShowChatList() {
		return showChatList;
	}
	public void setShowChatList(javax.swing.JList<String> showChatList) {
		this.showChatList = showChatList;
	}
	public javax.swing.JScrollPane getShowChatScrollPane() {
		return showChatScrollPane;
	}
	public void setShowChatScrollPane(javax.swing.JScrollPane showChatScrollPane) {
		this.showChatScrollPane = showChatScrollPane;
	}
	public javax.swing.JList<FileModel> getShowFilesList() {
		return showFilesList;
	}
	public void setShowFilesList(javax.swing.JList<FileModel> showFilesList) {
		this.showFilesList = showFilesList;
	}
	public javax.swing.JButton getUpdateAvataBtn() {
		return updateAvataBtn;
	}
	public void setUpdateAvataBtn(javax.swing.JButton updateAvataBtn) {
		this.updateAvataBtn = updateAvataBtn;
	}
	public javax.swing.JButton getUpdateInfoBtn() {
		return updateInfoBtn;
	}
	public void setUpdateInfoBtn(javax.swing.JButton updateInfoBtn) {
		this.updateInfoBtn = updateInfoBtn;
	}
	public javax.swing.JPanel getUserPanel() {
		return userPanel;
	}
	public void setUserPanel(javax.swing.JPanel userPanel) {
		this.userPanel = userPanel;
	}
	public CardLayout getCardLayout() {
		return cardLayout;
	}
	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}
    public javax.swing.JMenuItem getMainMenuItem() {
		return mainMenuItem;
	}
	public void setMainMenuItem(javax.swing.JMenuItem mainMenuItem) {
		this.mainMenuItem = mainMenuItem;
	}
	public javax.swing.JMenu getSettingMenu() {
		return settingMenu;
	}
	public void setSettingMenu(javax.swing.JMenu settingMenu) {
		this.settingMenu = settingMenu;
	}
	public javax.swing.JMenuItem getInfoMenuItem() {
		return infoMenuItem;
	}
	public void setInfoMenuItem(javax.swing.JMenuItem infoMenuItem) {
		this.infoMenuItem = infoMenuItem;
	}
	public javax.swing.JMenuItem getFriendMenuItem() {
		return friendMenuItem;
	}
	public void setFriendMenuItem(javax.swing.JMenuItem friendMenuItem) {
		this.friendMenuItem = friendMenuItem;
	}
	public javax.swing.JPopupMenu.Separator getjSeparator3() {
		return jSeparator3;
	}
	public void setjSeparator3(javax.swing.JPopupMenu.Separator jSeparator3) {
		this.jSeparator3 = jSeparator3;
	}

	public JButton getCallBtn() {
		return callBtn;
	}


	public void setCallBtn(JButton callBtn) {
		this.callBtn = callBtn;
	}

	private javax.swing.JMenuItem mainMenuItem;
	private CardLayout cardLayout;
    private javax.swing.JRadioButton maleRadioButton;
    private javax.swing.JRadioButton femaleRadioButton;
	// Variables declaration - do not modify                     
    private javax.swing.JButton addFriendBtn;
    private javax.swing.JTextField addFriendTxt;
    private javax.swing.JButton addToGroupBtn;
    private javax.swing.JLabel avataLabel;
    private javax.swing.JTextField birthdayTxt;
    private javax.swing.JButton choseFileBtn;
    private javax.swing.JButton createGroupBtn;
    private javax.swing.JLabel filesLabel;
    private javax.swing.JList<String> friendList;
    private javax.swing.JMenu settingMenu;
    private javax.swing.JPanel friendPanel;
    private javax.swing.JList<FriendRequestModel> friendRequestList;
    private javax.swing.JTextField genderTxt;
    private javax.swing.JList<GroupModel> showGroupList;
    private javax.swing.JLabel groupNameLabel;
    private javax.swing.JPanel groupPanel;
    private javax.swing.JMenuItem infoMenuItem;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem friendMenuItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem logOutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField messageTxt;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel genderLabel;  
    private javax.swing.JLabel birdayLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel nickNameLabel;
    private javax.swing.JTextField nickNameTxt;
    private javax.swing.JTextField phoneNumTxt;
    private javax.swing.JButton sendBtn;
    private javax.swing.JList<String> showChatList;
    private javax.swing.JScrollPane showChatScrollPane;
    private javax.swing.JList<FileModel> showFilesList;
    private javax.swing.JButton updateAvataBtn;
    private javax.swing.JButton updateInfoBtn;
    private javax.swing.JPanel userPanel;
    // End of variables declaration                   
}
