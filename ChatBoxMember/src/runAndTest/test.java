package runAndTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class test {
	private static void doSendRequest(String cmd, String... cont) {
		try {
				String request = cmd + "<?>";
				// varargs sẽ là mảng gồm các đối số được truyền vào. Lặp qua mảng để lấy các đối số
				for (String c : cont) {
					request += c + "<?>";
				}
				System.out.println(request);
		} catch (Exception e) {

		}
	}
	
    public static void main(String[] args) {
    	 int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn tiếp tục?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
         
         if (option == JOptionPane.OK_OPTION) {
             // Người dùng đã chọn "OK"
             System.out.println("Đã xác nhận");
         } else if (option == JOptionPane.CANCEL_OPTION) {
             // Người dùng đã chọn "Cancel"
             System.out.println("Đã hủy bỏ");
         } else if (option == JOptionPane.CLOSED_OPTION) {
             // Người dùng đã đóng hộp thoại
             System.out.println("Đã đóng hộp thoại");
         }
    	
//    	doSendRequest("check", "helo", "toi la quan");
    	
    }
}
