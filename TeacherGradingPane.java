import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherGradingPane implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String hostName;
    private int port;
    private int count;
    private String student;
    private String username;
    private String password;
    private JFrame frame;
    private JButton back;
    private static ImageIcon scaledImage;
    private ArrayList<Integer> submissionNumbers;
    private ArrayList<JButton> buttons;
    private Quiz selectedQuiz;
    private static JButton quiz;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public TeacherGradingPane(String username, String password, String student) {
        this.password = password;
        this.username = username;
        this.student = student;
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
            JButton sub;
            c.gridwidth = 1;
            findAvailableSubmissions();
            for (int i = 0; i < submissionNumbers.size(); i++) {
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridy = (int) ((double) i / (double) 3) + 2; 
                c.gridx = i % 3;
                c.insets = new Insets(10, 10, 10, 10);
                sub = new JButton("Quiz " + submissionNumbers.get(i));
                sub.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                sub.addActionListener(actionListener);
                buttons.add(sub);
                pane.add(sub, c);
                count++;
            }
            if (submissionNumbers.size() == 0) {
                c.gridwidth = GridBagConstraints.REMAINDER;
                c.fill = GridBagConstraints.NONE;
                c.gridy = 2;
                c.gridx = 0;
                c.insets = new Insets(10, 10, 10, 10);
                JLabel label = new JLabel("No submissions available");
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
                new Thread(new TeacherGradingLandingPane(username, password)).start();
            }

            for (int j = 0; j < count; j++) {
                if (e.getSource() == buttons.get(j)) {
                    try {
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        new Thread(new TeacherSubmissionPane(student, password, student, submissionNumbers.get(j))).start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
        }
    };

    public void findAvailableSubmissions() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        oos.writeObject("findAvailableSubmissions");
        oos.flush();
        oos.writeObject(this.student);
        oos.flush();
        submissionNumbers = (IntegerArrayList) ois.readObject();
    }
}
