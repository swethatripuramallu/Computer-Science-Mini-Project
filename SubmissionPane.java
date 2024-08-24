import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.awt.event.*;

public class SubmissionPane implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private LocalTime time = LocalTime.now();
    private LocalDate date = LocalDate.now();
    private String hostName;
    private int index;
    private int port;
    private int count;
    private int pointsEarned;
    private int totalPoints;
    private int submissionNumber;
    private String username;
    private String password;
    private JFrame frame;
    private JButton back;
    private JRadioButton button1;
    private JRadioButton button2;
    private JRadioButton button3;
    private JRadioButton button4;
    private JTextField field;
    private ButtonGroup group;
    private JButton truth;
    private JButton nontruth;
    private static ImageIcon scaledImage;
    private ArrayList<JComponent> components;
    private ArrayList<Integer> quizNumbers;
    private ArrayList<JButton> buttons;
    private ArrayList<Question> questions;
    private ArrayList<String> submission;
    private Quiz selectedQuiz;
    private static JButton quiz;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public SubmissionPane(String username, String password, int submissionNumber) {
        this.password = password;
        this.username = username;
        this.submissionNumber = submissionNumber;
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
            c.ipadx = 100;
            img.setOpaque(true);
            img.setBackground(Color.BLACK); 

            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(img, c);
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            panel.setBackground(Color.BLACK);
            c.fill = GridBagConstraints.HORIZONTAL;
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

            c.gridwidth = GridBagConstraints.REMAINDER;
        	c.fill = GridBagConstraints.HORIZONTAL;
        	c.gridy = 2;
        	c.gridx = 0;
        	JLabel label;
        	getSubmission();
        	for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    c.insets = new Insets(10, 10, 10, 10);
                } else if (i == 15) {
                    c.insets = new Insets(0, 0, 10, 0);
                } else {
                    c.insets = new Insets(0, 0, 0, 0);
                }
        		label = new JLabel(submission.get(i));
        		c.gridy = i + 2;
        		label.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
        		label.setForeground(Color.WHITE);
        		pane.add(label, c);
        	}

            //bottom border
            back = new JButton("Return to Dashboard");
            c.insets = new Insets(0,0,0,0);
            c.gridx = 0;
            c.ipady = 0;
            c.gridy = 100;
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
                new Thread(new StudentLandingPane(username, password)).start();
        	}
        }
    };

    public void getSubmission() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        oos.writeObject("getSubmission");
        oos.flush();
        oos.writeObject(username);
        oos.flush();
        oos.writeObject(Integer.toString(submissionNumber));
        oos.flush();
        submission = (StringArrayList) ois.readObject();
    }
}
