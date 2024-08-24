import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherGradingLandingPane implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String hostName;
    private int port;
    private int count;
    private String username;
    private String password;
    private JFrame frame;
    private JButton back;
    private static ImageIcon scaledImage;
    private StringArrayList studentList;
    private ArrayList<JButton> buttons;
    private Quiz selectedQuiz;
    private static JButton student;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public TeacherGradingLandingPane(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public void run() {
        try {
            //create frame and layout
            frame = new JFrame("Purdue LMS");
            frame.setResizable(false);
            GridBagLayout grid = new GridBagLayout();

            //create container and layouts + cs
            Container pane = frame.getContentPane();
            pane.setLayout(grid);
            pane.setBackground(purdueGrey);
            GridBagConstraints c = new GridBagConstraints();

            //purdue icon 
            ImageIcon image = new ImageIcon("Resources/PurdueC.png");
            JLabel img = new JLabel(image);
            c.insets = new Insets(10, 10, 10, 10);
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.ipady = 0;
            c.ipadx = 1000;
            img.setOpaque(true);
            img.setBackground(Color.BLACK); 

            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(img, c);
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            panel.setBackground(Color.BLACK);
            pane.add(panel, c);

            //top border
            JLabel topBorder = new JLabel(" ");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridy = 1;
            c.ipady = 0;
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            topBorder.setOpaque(true);
            topBorder.setBackground(purdueGold);
            pane.add(topBorder, c);

            //construction label
            buttons = new ArrayList<JButton>();
            studentList = findAvailableStudents();
            c.gridwidth = 1;
            for (int i = 0; i < studentList.size(); i++) {
            	c.fill = GridBagConstraints.HORIZONTAL;
            	c.gridy = (int) ((double) i / (double) 3) + 2; 
            	c.gridx = i % 3;
            	c.insets = new Insets(10, 10, 10, 10);
            	student = new JButton(studentList.get(i).substring(0, studentList.get(i).indexOf(",")));
            	student.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            	student.addActionListener(actionListener);
            	buttons.add(student);
            	pane.add(student, c);
            	count++;
            }
            if (studentList.size() == 0) {
            	c.gridwidth = GridBagConstraints.REMAINDER;
            	c.fill = GridBagConstraints.NONE;
            	c.gridy = 2;
            	c.gridx = 0;
            	c.insets = new Insets(10, 10, 10, 10);
            	JLabel label = new JLabel("No students enrolled");
            	label.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            	label.setForeground(Color.WHITE);
            	pane.add(label, c);
            }

            //bottom border
            back = new JButton("Return to Dashboard");
            c.insets = new Insets(0,0,0,0);
            c.gridx = 0;
            c.ipady = 0;
            c.gridy = 10;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            back.setOpaque(true);
            back.setBackground(purdueGold);
            back.addActionListener(actionListener);
            back.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            pane.add(back, c);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if (e.getSource() == back) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new TeacherLandingPane(username, password)).start();
        	}

        	for (int j = 0; j < count; j++) {
        		if (e.getSource() == buttons.get(j)) {
        			try {
		                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		                new Thread(new TeacherGradingPane(username, password, studentList.get(j).substring(0, studentList.get(j).indexOf(",")))).start();
        			} catch (Exception ex) {
        				ex.printStackTrace();
        			}
        		}
        	}
	        
        }
    };

    public StringArrayList findAvailableStudents() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        StringArrayList studentList = new StringArrayList();
        oos.writeObject("findAvailableStudents");
        oos.flush();
        studentList = (StringArrayList) ois.readObject();
        return studentList;
    }

    public void takeQuiz(int quizNumber) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        oos.writeObject("takeQuiz");
        oos.flush();
        oos.writeObject(quizNumber);
        oos.flush();
        selectedQuiz = (Quiz) ois.readObject();
    }
}
