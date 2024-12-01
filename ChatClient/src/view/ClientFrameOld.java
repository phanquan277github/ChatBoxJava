package view;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;

import model.ClientModel;
import model.FileModel;
import model.GroupModel;

public class ClientFrameOld extends javax.swing.JFrame {
	private ClientModel memberModel;
	private GroupModel groupModel;
	private DefaultListModel<GroupModel> listModel;
	private DefaultListModel<String> chatListModel;
	private DefaultListModel<FileModel> fileListModel;
	private JFileChooser fileUpChooser;
	private JFileChooser fileDownChooser;
	
	 public ClientFrameOld(ClientModel memberModel, GroupModel groupModel) {
	    	this.memberModel = memberModel;
	    	this.groupModel = groupModel;
	        initComponents();
	        this.setSize(850, 550);
	        this.setLocationRelativeTo(null);
	        this.setTitle("Chat application");
	        this.setResizable(false);
	    }
    
    private void initComponents() {
    	listModel = new DefaultListModel<>();
    	chatListModel = new DefaultListModel<>();
    	fileListModel = new DefaultListModel<>();
    	
    	fileUpChooser = new JFileChooser();
    	fileUpChooser.setDialogTitle("Chọn file muốn gửi!");
    	fileUpChooser.setSize(700, 500);
    	
    	fileDownChooser = new JFileChooser();
    	fileDownChooser.setDialogTitle("Chọn thư mục lưu file!");
    	fileDownChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	fileDownChooser.setSize(700, 500);
    	
        groupPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showGroupList = new javax.swing.JList<>(listModel);
        showGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chỉ cho phép click 1 item trong jlist
        nickNameLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        groupNameLabel = new javax.swing.JLabel();
        addToGroupBtn = new javax.swing.JButton();
        showChatScrollPane = new javax.swing.JScrollPane();
        showChatList = new javax.swing.JList<>(chatListModel);
        jScrollPane1 = new javax.swing.JScrollPane();
        showFilesList = new javax.swing.JList<>(fileListModel);
        showGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        createGroupBtn = new javax.swing.JButton();
        messageTxt = new javax.swing.JTextField();
        choseFileBtn = new javax.swing.JButton();
        sendBtn = new javax.swing.JButton();
        filesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        groupPanel.setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane2.setViewportView(showGroupList);

        javax.swing.GroupLayout groupPanelLayout = new javax.swing.GroupLayout(groupPanel);
        groupPanel.setLayout(groupPanelLayout);
        groupPanelLayout.setHorizontalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        groupPanelLayout.setVerticalGroup(
            groupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        nickNameLabel.setBackground(new java.awt.Color(153, 153, 153));
        nickNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nickNameLabel.setForeground(new java.awt.Color(0, 102, 51));
        nickNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nickNameLabel.setText("................");

        groupNameLabel.setBackground(new java.awt.Color(0, 255, 255));
        groupNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        groupNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        groupNameLabel.setText("..................................");

        addToGroupBtn.setText("Thêm");

        showChatScrollPane.setViewportView(showChatList);

        jScrollPane1.setViewportView(showFilesList);
        
        createGroupBtn.setText("Tạo nhóm");
        choseFileBtn.setText("File");
        sendBtn.setText("Send");

        filesLabel.setBackground(new java.awt.Color(153, 153, 153));
        filesLabel.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        filesLabel.setForeground(new java.awt.Color(0, 102, 0));
        filesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        filesLabel.setText("Files");
        filesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filesLabel.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(messageTxt))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(groupNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(showChatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(addToGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(filesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sendBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(filesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showChatScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(groupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nickNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(nickNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>                        

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

	public DefaultListModel<FileModel> getFileListModel() {
		return fileListModel;
	}

	public void setFileListModel(DefaultListModel<FileModel> fileListModel) {
		this.fileListModel = fileListModel;
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

	public DefaultListModel<GroupModel> getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel<GroupModel> listModel) {
		this.listModel = listModel;
	}

	public javax.swing.JPanel getGroupPanel() {
		return groupPanel;
	}

	public void setGroupPanel(javax.swing.JPanel groupPanel) {
		this.groupPanel = groupPanel;
	}

	public javax.swing.JPanel getjPanel1() {
		return jPanel1;
	}

	public void setjPanel1(javax.swing.JPanel jPanel1) {
		this.jPanel1 = jPanel1;
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

	public javax.swing.JTextField getMessageTxt() {
		return messageTxt;
	}

	public void setMessageTxt(javax.swing.JTextField messageTxt) {
		this.messageTxt = messageTxt;
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

	public DefaultListModel<String> getChatListModel() {
		return chatListModel;
	}

	public void setChatListModel(DefaultListModel<String> chatListModel) {
		this.chatListModel = chatListModel;
	}

	public javax.swing.JButton getAddToGroupBtn() {
		return addToGroupBtn;
	}

	public void setAddToGroupBtn(javax.swing.JButton addToGroupBtn) {
		this.addToGroupBtn = addToGroupBtn;
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

	public javax.swing.JLabel getNickNameLabel() {
		return nickNameLabel;
	}

	public void setNickNameLabel(javax.swing.JLabel nickNameLabel) {
		this.nickNameLabel = nickNameLabel;
	}

	public javax.swing.JButton getSendBtn() {
		return sendBtn;
	}

	public void setSendBtn(javax.swing.JButton sendBtn) {
		this.sendBtn = sendBtn;
	}

	public javax.swing.JList<FileModel> getShowFilesList() {
		return showFilesList;
	}

	public void setShowFilesList(javax.swing.JList<FileModel> showFilesList) {
		this.showFilesList = showFilesList;
	}



	// Variables declaration - do not modify                     
    private javax.swing.JButton addToGroupBtn;
    private javax.swing.JButton choseFileBtn;
    private javax.swing.JButton createGroupBtn;
    private javax.swing.JLabel filesLabel;
    private javax.swing.JList<GroupModel> showGroupList;
    private javax.swing.JLabel groupNameLabel;
    private javax.swing.JPanel groupPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField messageTxt;
    private javax.swing.JLabel nickNameLabel;
    private javax.swing.JButton sendBtn;
    private javax.swing.JList<String> showChatList;
    private javax.swing.JScrollPane showChatScrollPane;
    private javax.swing.JList<FileModel> showFilesList;
    // End of variables declaration                   
}
