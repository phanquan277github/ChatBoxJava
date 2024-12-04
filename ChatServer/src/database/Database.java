package database;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mysql.cj.xdevapi.Client;

import model.ClientModel;
import model.MessageModel;
import server.Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	private static String URL = "jdbc:mysql://localhost:3306/chatbox";
    private static String USERNAME = "root";
    private static String PASSWORD = "phanquan277";
    
    public int checkLogin(String username, String password) {
    	Connection conn = null;
		try {
			String sql = "SELECT * FROM TA_ACC_Account WHERE T_userName = ? AND T_password = ?";
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	return resultSet.getInt("I_id");
            };
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	// ngắt kết nối sau khi hoàn thành truy vấn
		}
    	return -1;
    }
    
    public int checkUsername(String username) {
    	Connection conn = null;
		try {
			String sql = "SELECT * FROM TA_ACC_Account WHERE T_userName = ?";
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getInt("I_id");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	// ngắt kết nối sau khi hoàn thành truy vấn
		}
    	return -1;
    }
    
    public int createAccount(String userName, String password) {
    	Connection conn = null;
    	int generatedId = -1; 
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "insert into ta_acc_account(T_userName, T_password) values (?, ?)";
	    	PreparedStatement preSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    	preSt.setString(1, userName);
	    	preSt.setString(2, password);
	    	 int rowsAffected = preSt.executeUpdate();
	         if (rowsAffected > 0) {
	             ResultSet generatedKeys = preSt.getGeneratedKeys();
	             if (generatedKeys.next()) {
	                 generatedId = generatedKeys.getInt(1);
	                 System.out.println("ID: "+generatedId);
	             }
	         }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return generatedId;
    }   
    
    public ClientModel getClientInfo(int id, String type) {
    	Connection conn = null;
    	ClientModel clientModel = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			String query = null;
			if (type.equals("byAccId")) {
				query = "select * from ta_mbr_member where I_account_id = ?"; 
			} else if (type.equals("byClientId")) {
				query = "select * from ta_mbr_member where I_id = ?"; 
			}
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.setInt(1, id);
	    	
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				int clientId = rs.getInt("I_id");
				String name = rs.getString("T_name");
				int accId = rs.getInt("I_account_id");
				String phone = rs.getString("T_phone_number");
				Boolean gender = rs.getBoolean("T_gender");
				Date birthday = rs.getDate("D_birthday");
				byte[] avata = rs.getBytes("B_avata");
				Timestamp createdAt = rs.getTimestamp("created_at");
				Timestamp updateAt = rs.getTimestamp("updated_at");
				
				clientModel = new ClientModel(clientId, name, accId, gender, phone, birthday, avata, createdAt, updateAt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return clientModel;
    }
    
    public void createMember(String memberName, int accountId) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "insert into ta_mbr_member(T_name, I_account_id) values (?, ?)";
	    	PreparedStatement st = conn.prepareStatement(query);
	    	st.setString(1, memberName);
	    	st.setInt(2, accountId);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public String checkGroupExistence(int memberId, String groupName) {
    	Connection conn = null;
    	String rep = "false";
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "SELECT * FROM ta_grp_group WHERE T_name = ? and I_member_id = ?";
	    	PreparedStatement st = conn.prepareStatement(query);
	    	st.setString(1, groupName);
	    	st.setInt(2, memberId);
	    	
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				rep = rs.getString("T_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rep.equals("false") ? "false" : "true";
    }
   
    public int getGroupIdByGroupNameAndMemberId(String groupName, int memberId) {
    	Connection conn = null;
    	int rep = 0;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "SELECT I_id FROM ta_grp_group WHERE T_name = ? AND I_member_id = ? ";
	    	PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, groupName);
			st.setInt(2, memberId);
			
	    	ResultSet rs = st.executeQuery();
			while(rs.next()) {
				rep = rs.getInt("I_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rep;
    }
     
    public void createGroup(int memberId, String groupName) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			PreparedStatement preSt;
	    	
			String createGrQuery = "insert into ta_grp_group (T_name, I_member_id) values (?, ?)";
	    	preSt = conn.prepareStatement(createGrQuery);
	    	preSt.setString(1, groupName);
	    	preSt.setInt(2, memberId);
	    	preSt.executeUpdate();	  

			int groupId = getGroupIdByGroupNameAndMemberId(groupName, memberId);
	    	String insertMemQuery = "insert into ta_grm_groupmembers(I_group_id, I_member_id) values (?, ?)";
	    	preSt = conn.prepareStatement(insertMemQuery);
	    	preSt.setInt(1, groupId);
	    	preSt.setInt(2, memberId);
	    	preSt.executeUpdate();
			
			String insertSystemQuery = "insert into ta_grm_groupmembers(I_group_id, I_member_id) values (?, ?)";
			preSt = conn.prepareStatement(insertSystemQuery);
			preSt.setInt(1, groupId);
			preSt.setInt(2, 1);
			preSt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public int getMemberIdByUserName(String userName) {
    	Connection conn = null;
    	int memberId = 0;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select mb.I_id from (ta_acc_account as acc inner join ta_mbr_member as mb on acc.I_id = mb.I_account_id ) where acc.T_userName = '"+ userName + "'");
			
			while(rs.next()) {
				memberId = rs.getInt("I_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberId;
    }
    
    public ArrayList<String> getGroupListByMemberId(int memberId) {
    	Connection conn = null;
		String row = null;
		ArrayList<String> storage = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select grp.I_id, grp.T_name from (ta_grp_group as grp inner join ta_grm_groupmembers as grm on grp.I_id = grm.I_group_id) where grm.I_member_id = "+ memberId);
			
			while(rs.next()) {
				int groupId = rs.getInt("I_id");
				String groupName = rs.getString("T_name");
				row = groupId + "<?>" + groupName;
				storage.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return storage;
    }
    
    public void addMemberToGroup(int groupId, String userName) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
	    	int memberId = getMemberIdByUserName(userName);
	    	
	    	String value = "(" + groupId + ", " + memberId + ")";
			st.executeUpdate("insert into ta_grm_groupmembers(I_group_id, I_member_id) values " + value);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void addMessage(int memberId, int groupId, String content) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	
			String command = "insert into ta_msg_message(I_group_id, I_member_id, T_content) values (?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(command);
			st.setInt(1, groupId);
			st.setInt(2, memberId);
			st.setString(3, content);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public String getMemberNameByUserName(String userName) {
    	Connection conn = null;
    	String memberName = "";
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select mb.T_name from (ta_acc_account as acc inner join ta_mbr_member as mb on acc.I_id = mb.I_account_id ) where acc.T_userName = '"+ userName + "'");
			
			while(rs.next()) {
				memberName = rs.getString("T_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberName;
    }
    
    public String getMemberNameByMemberId(int memberId) {
    	Connection conn = null;
    	String memberName = "";
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select T_name from ta_mbr_member where I_id = "+ memberId);
			
			while(rs.next()) {
				memberName = rs.getString("T_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return memberName;
    }
    
    public int getGroupIdByMemberId(int memberId) {
    	Connection conn = null;
    	int groupId = 0;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select I_id from ta_grp_group WHERE I_member_id = " + memberId + " ORDER BY I_id DESC LIMIT 1;");
			
			while(rs.next()) {
				groupId = rs.getInt("I_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return groupId;
    }
    
    public ArrayList<MessageModel> getMessagesListByGroupId(int groupId) {
    	Connection conn = null;
		ArrayList<MessageModel> storage = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select msg.*, mb.T_name from (ta_msg_message as msg inner join ta_mbr_member as mb on msg.I_member_id = mb.I_id ) where msg.I_group_id = "+ groupId + " ORDER BY msg.I_id");
			
			while(rs.next()) {
				int msgId = rs.getInt("I_id");
				int grpId = rs.getInt("I_group_id");
				int memberId = rs.getInt("I_member_id");
				String nickName = rs.getString("T_name");
				String content = rs.getString("T_content");
				String typeMsg = rs.getString("typeMsg");
				String fileName = rs.getString("T_file_name");
				byte[] fileData = rs.getBytes("B_file_data");
				Timestamp createdAt = rs.getTimestamp("created_at");
				Timestamp updateAt = rs.getTimestamp("updated_at");
				
				if (typeMsg.equals("isTxt")) {
					MessageModel msg = new MessageModel();
					msg.setMessageId(msgId);
					msg.setGroupId(grpId);
					msg.setClientId(memberId);
					msg.setNickname(nickName);
					msg.setContent(content);
					msg.setTypeMsg(typeMsg);
					msg.setCreateAt(createdAt);
					msg.setUpdateAt(updateAt);
					storage.add(msg);
				} else {
					MessageModel msg = new MessageModel();
					msg.setMessageId(msgId);
					msg.setGroupId(grpId);
					msg.setClientId(memberId);
					msg.setNickname(nickName);
					msg.setTypeMsg(typeMsg);
					msg.setFileName(fileName);
					msg.setFileData(fileData);
					msg.setCreateAt(createdAt);
					msg.setUpdateAt(updateAt);
					storage.add(msg);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return storage;
    }
    
    public static boolean isImageFile(String fileName) {
		String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String fileNameLower = fileName.toLowerCase(); // Chuyển về chữ thường để so sánh
        for (String ext : IMAGE_EXTENSIONS) {
            if (fileNameLower.endsWith(ext)) {
                return true; // Tên file có đuôi là file ảnh
            }
        }
        return false; // Không phải file ảnh
    }
    
    public void addFile(int memberId, int groupId, String fileName, byte[] fileData) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "insert into ta_msg_message(I_member_id,  I_group_id, T_file_name, B_file_data, typeMsg) values (?, ?, ?, ?, ?)";
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.setInt(1, memberId);
	    	preparedStatement.setInt(2, groupId);
	    	preparedStatement.setString(3, fileName);
	    	preparedStatement.setBytes(4, fileData);
	    	preparedStatement.setString(5, "isFile");
	    	preparedStatement.executeUpdate();	    	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void updateAvata(int memberId, byte[] fileData) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "update ta_mbr_member SET B_avata=? where I_id=?";
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.setInt(2, memberId);
	    	preparedStatement.setBytes(1, fileData);
	    	preparedStatement.executeUpdate();	    	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public byte[] getFileDataById(int fileId) {
    	Connection conn = null;
    	byte[] fileData = null ;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "SELECT B_file_data FROM ta_msg_message WHERE I_id = "+fileId; 
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	ResultSet rs = preparedStatement.executeQuery();
	    	
	    	 if (rs.next()) {
	    		 fileData = rs.getBytes("B_file_data");
	    	 }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fileData;
    }

    public void updateClientInfo(ClientModel clientModel) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "UPDATE ta_mbr_member SET T_name = ?, T_gender= ?, T_phone_number = ?, D_birthday = ? where I_id = "+clientModel.getId(); 
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.setString(1, clientModel.getName());
	    	preparedStatement.setBoolean(2, clientModel.getGender());
	    	preparedStatement.setString(3, clientModel.getPhone());
	    	preparedStatement.setDate(4, clientModel.getBirthday());
	    	preparedStatement.executeUpdate();	    	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public String getFriendsList(String ownerId) {
    	Connection conn = null;
		String row = "";
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select mb.T_name from (TA_MBR_Friends as fr inner join ta_mbr_member as mb on fr.I_friend_id = mb.I_id)"
					+ " where I_owner_id = "+ownerId+" and TI_accept = 1");
			
			while(rs.next()) {
				String name = rs.getString("T_name");
				row += name + "<?>";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return row;
    }
    
    public ArrayList<String> getFriendRequestList(String ownerId) {
    	Connection conn = null;
		String row = null;
		ArrayList<String> storage = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select fr.I_friend_id, mb.T_name from (TA_MBR_Friends as fr inner join ta_mbr_member as mb on fr.I_friend_id = mb.I_id)"
					+ " where fr.I_owner_id = "+ownerId+" and fr.TI_accept = 0;");
			
			while(rs.next()) {
				int fileId = rs.getInt("I_friend_id");
				String fileName = rs.getString("T_name");
				row = fileId + "<?>" + fileName;
				storage.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return storage;
    }
    
    public int checkFriendStatus(String ownerId, String friendId) {
    	Connection conn = null;
    	int id = 0;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select TI_accept from TA_MBR_Friends "
					+ "where ((I_owner_id = "+ownerId+" and I_friend_id = "+friendId+") or (I_owner_id = "+friendId+" and I_friend_id = "+ownerId+")) and TI_accept = 1");
			
			while(rs.next()) {
				id = rs.getInt("TI_accept");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	return id;
    }
    
    public void addFriendRequest(int ownerId, int requestId) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "insert into TA_MBR_Friends(I_owner_id,  I_friend_id, TI_accept) values (?, ?, ?)"; 
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.setInt(1, ownerId);
	    	preparedStatement.setInt(2, requestId);
	    	preparedStatement.setInt(3, 0);
	    	preparedStatement.executeUpdate();	    	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void acceptFriend(String ownerId, String friendId) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    	String query = "UPDATE TA_MBR_Friends SET TI_accept = 1 where I_owner_id = "+ownerId+" and I_friend_id = "+friendId; 
	    	String query2 = "insert into TA_MBR_Friends (I_owner_id, I_friend_id, TI_accept) values (?, ?, ?)";
	    	PreparedStatement preparedStatement = conn.prepareStatement(query);
	    	preparedStatement.executeUpdate();	  
	    	PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
	    	preparedStatement2.setInt(1, Integer.parseInt(friendId));
	    	preparedStatement2.setInt(2, Integer.parseInt(ownerId));
	    	preparedStatement2.setInt(3, 1);
	    	preparedStatement2.executeUpdate();	     
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void deleteGroup(int groupId) {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			String queryDeleteFiles = "DELETE FROM ta_grp_files WHERE I_group_id = ?"; 
			String queryDeleteMessage = "DELETE FROM ta_msg_message WHERE I_group_id = ?"; 
			String queryDeleteGroupMembers = "DELETE FROM ta_grm_groupmembers WHERE I_group_id = ?"; 
	    	String queryDeleteGroup = "DELETE FROM ta_grp_group WHERE I_id = ?"; 
	    	PreparedStatement preparedStatement;
	    	
	        preparedStatement = conn.prepareStatement(queryDeleteFiles);
	    	preparedStatement.setInt(1, groupId);
	        preparedStatement.executeUpdate();
	        
	        preparedStatement = conn.prepareStatement(queryDeleteMessage);
	        preparedStatement.setInt(1, groupId);
	        preparedStatement.executeUpdate();
	        
	        preparedStatement = conn.prepareStatement(queryDeleteGroupMembers);
	        preparedStatement.setInt(1, groupId);
	        preparedStatement.executeUpdate();
	    	
	        preparedStatement = conn.prepareStatement(queryDeleteGroup);
	    	preparedStatement.setInt(1, groupId);
	        preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }
}
