package runAndTest;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class test {
    private JFrame frame;
    private JList<String> groupList;

    public test() {
        frame = new JFrame("Chat Application");
        groupList = new JList<>(new String[]{"Group 1", "Group 2", "Group 3"});

        groupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedGroup = groupList.getSelectedValue();
                    // Xử lý hiển thị tin nhắn của group đã chọn
                    System.out.println("Selected Group: " + selectedGroup);
                }
            }
        });

        frame.add(new JScrollPane(groupList));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new test();
            }
        });
	}
    
}
