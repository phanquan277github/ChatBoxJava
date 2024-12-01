package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import server.ClientManagement;
import server.ServerMultiThreadMangager;
import server.ServerThread;
import java.awt.*;
import java.util.ArrayList;

public class ServerFrame extends JFrame {
	private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAddress;
    private javax.swing.JTable tableClientAccess;
    private javax.swing.JTextField txtPort;
    private javax.swing.JLabel lableRunning;
    private DefaultTableModel clientTableModel;

    public ServerFrame() {
        this.setTitle("Server Client Manager");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        initComponents();	
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClientAccess = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnStart = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        labelAddress = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        lableRunning = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        clientTableModel = new DefaultTableModel(new String [] {
                "IP Address", "Thời gian truy cập", "Ngắt kết nối", "Title 4"
            }, 0);
        tableClientAccess.setModel(clientTableModel);

        jScrollPane1.setViewportView(tableClientAccess);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Địa chỉ server:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 204));
        jLabel2.setText("PORT:");

       
        btnStart.setBackground(new java.awt.Color(102, 255, 102));
        btnStart.setText("Start");
       
        btnStop.setBackground(new java.awt.Color(255, 51, 51));
        btnStop.setForeground(new java.awt.Color(255, 255, 255));
        btnStop.setText("Stop");
        

        labelAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        labelAddress.setText("000.000.000.000");

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(51, 0, 204));
        btnRefresh.setText("Refresh");
        btnRefresh.setToolTipText("");
      
        lableRunning.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lableRunning.setText("Server đang đóng");
        
        
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lableRunning, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStop)
                    .addComponent(btnStart))
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labelAddress))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lableRunning)
                        .addGap(2, 2, 2)))
                .addComponent(btnStop))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
        );

        pack();
    }
    
    

	public javax.swing.JLabel getLableRunning() {
		return lableRunning;
	}

	public void setLableRunning(javax.swing.JLabel lableRunning) {
		this.lableRunning = lableRunning;
	}

	public DefaultTableModel getClientTableModel() {
		return clientTableModel;
	}

	public void setClientTableModel(DefaultTableModel clientTableModel) {
		this.clientTableModel = clientTableModel;
	}

	public javax.swing.JButton getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(javax.swing.JButton btnStart) {
		this.btnStart = btnStart;
	}

	public javax.swing.JButton getBtnStop() {
		return btnStop;
	}

	public void setBtnStop(javax.swing.JButton btnStop) {
		this.btnStop = btnStop;
	}

	public javax.swing.JLabel getLabelAddress() {
		return labelAddress;
	}

	public void setLabelAddress(javax.swing.JLabel labelAddress) {
		this.labelAddress = labelAddress;
	}

	public javax.swing.JTable getTableClientAccess() {
		return tableClientAccess;
	}

	public void setTableClientAccess(javax.swing.JTable tableClientAccess) {
		this.tableClientAccess = tableClientAccess;
	}

	public javax.swing.JTextField getTxtPort() {
		return txtPort;
	}

	public void setTxtPort(javax.swing.JTextField txtPort) {
		this.txtPort = txtPort;
	}                                          

    
    
}
