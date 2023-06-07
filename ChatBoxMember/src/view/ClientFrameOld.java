package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import model.GroupModel;
import model.ClientModel;

public class ClientFrameOld extends JFrame {
	private ClientModel memberModel;
	private GroupModel groupModel;
	private DefaultListModel<GroupModel> groupListModel;
	private DefaultListModel<String> chatListModel = new DefaultListModel<>();
	
    public ClientFrameOld(ClientModel memberModel, GroupModel groupModel) {
    	this.memberModel = memberModel;
    	this.groupModel = groupModel;
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    private void initComponents() {
    	groupListModel = new DefaultListModel<>();
        showChatScrollPane = new javax.swing.JScrollPane();
        showChatList = new javax.swing.JList<>(chatListModel);
        
        messageTxt = new javax.swing.JTextField();
        choseFileBtn = new javax.swing.JButton();
        sendBtn = new javax.swing.JButton();
        groupPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        groupListDisplay = new javax.swing.JList<>(groupListModel);
        groupListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chỉ cho phép click 1 item trong jlist
        addFriendBtn = new javax.swing.JButton();
        createGroupBtn = new javax.swing.JButton();
        groupNameLabel = new javax.swing.JLabel();
        addToGroupBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        showChatScrollPane.setViewportView(showChatList);
        choseFileBtn.setText("File");
        sendBtn.setText("Send");
        groupPanel.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane2.setViewportView(groupListDisplay);

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        addFriendBtn.setText("Kết bạn");
        createGroupBtn.setText("Tạo nhóm");
        groupNameLabel.setBackground(new java.awt.Color(0, 255, 255));
        groupNameLabel.setForeground(Color.BLACK);
        groupNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        groupNameLabel.setText("..............................");
        addToGroupBtn.setText("Thêm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addFriendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showChatScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(groupNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(groupNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addToGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addComponent(showChatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(choseFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addFriendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>                                   

    
    
	public GroupModel getGroupModel() {
		return groupModel;
	}

	public void setGroupModel(GroupModel groupModel) {
		this.groupModel = groupModel;
	}

	public DefaultListModel<GroupModel> getListModel() {
		return groupListModel;
	}
	

	public DefaultListModel<String> getChatListModel() {
		return chatListModel;
	}

	public void setChatListModel(DefaultListModel<String> chatListModel) {
		this.chatListModel = chatListModel;
	}

	public void setListModel(DefaultListModel<GroupModel> listModel) {
		this.groupListModel = listModel;
	}

	public ClientModel getMemberModel() {
		return memberModel;
	}

	
	public void setMemberModel(ClientModel memberModel) {
		this.memberModel = memberModel;
	}

	public javax.swing.JButton getAddFriendBtn() {
		return addFriendBtn;
	}

	public void setAddFriendBtn(javax.swing.JButton addFriendBtn) {
		this.addFriendBtn = addFriendBtn;
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

	public javax.swing.JList<GroupModel> getGroupListDisplay() {
		return groupListDisplay;
	}

	public void setGroupListDisplay(javax.swing.JList<GroupModel> groupListDisplay) {
		this.groupListDisplay = groupListDisplay;
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

	// Variables declaration - do not modify                     
    private javax.swing.JButton addFriendBtn;
    private javax.swing.JButton addToGroupBtn;
    private javax.swing.JButton choseFileBtn;
    private javax.swing.JButton createGroupBtn;
    private javax.swing.JList<GroupModel> groupListDisplay;
    private javax.swing.JLabel groupNameLabel;
    private javax.swing.JPanel groupPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField messageTxt;
    private javax.swing.JButton sendBtn;
    private javax.swing.JList<String> showChatList;
    private javax.swing.JScrollPane showChatScrollPane;
    // End of variables declaration                   
}
