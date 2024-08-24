import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentLandingPane implements Runnable {
    private String hostName;
    private int port;
    private String username;
    private String password;
    private JFrame frame;
    private static ImageIcon scaledImage;
    private JButton account;
    private JButton quizzes;
    private JButton submissions;
    private JButton logOut;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);
    
    public StudentLandingPane(String username, String password) {
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

            //create jpanel for top row
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints e = new GridBagConstraints();

            ImageIcon image = new ImageIcon("Resources/PurdueC.png");
            JLabel img = new JLabel(image);
            e.insets = new Insets(0, 0, 0, 0);
            e.weightx = 0.5;
            e.gridx = 1;
            e.gridy = 0;
            e.gridwidth = GridBagConstraints.REMAINDER;
            e.ipady = 20;
            e.ipadx = 20;
            img.setOpaque(true);
            img.setBackground(Color.BLACK);
            panel.add(img, e);

            logOut = new JButton("Log Out");
            logOut.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 16));
            e.insets = new Insets(10, 10, 10, 10);
            e.gridx = 2;
            e.anchor = GridBagConstraints.EAST;
            logOut.addActionListener(actionListener);
            panel.add(logOut, e);

            //add jpanel to jframe container
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 3;
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 0;
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

            //account button
            account = new JButton("Account Settings");
            account.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(10, 10, 10, 10);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 2;
            c.ipady = 100;
            c.ipadx = 100;
            account.addActionListener(actionListener);
            pane.add(account, c);

            //welcome message
            JLabel welcome = new JLabel("Welcome to");
            c.fill = GridBagConstraints.REMAINDER;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 2;
            c.ipady = 0;
            c.ipadx = 0;
            welcome.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 128));
            welcome.setOpaque(true);
            welcome.setBackground(purdueGrey);
            welcome.setForeground(Color.WHITE);
            pane.add(welcome, c);

            //quiz button
            quizzes = new JButton("Take Quiz");
            quizzes.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 3;
            c.ipady = 100;
            c.ipadx = 100;
            quizzes.addActionListener(actionListener);
            pane.add(quizzes, c);
  
            //welcome message          
            JLabel purdueLMS = new JLabel("Purdue LMS,");
            c.fill = GridBagConstraints.REMAINDER;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 3;
            c.ipady = 0;
            c.ipadx = 0;
            purdueLMS.setOpaque(true);
            purdueLMS.setBackground(purdueGrey);
            purdueLMS.setForeground(Color.WHITE);
            purdueLMS.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 128));
            pane.add(purdueLMS, c);

            //submission button
            submissions = new JButton("View Submissions");
            submissions.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 4;
            c.ipady = 100;
            c.ipadx = 100;
            submissions.addActionListener(actionListener);
            pane.add(submissions, c);

            //welcome message
            JLabel name = new JLabel(username);
            if (name.getText().equals("username")) 
                name.setText("debug");
            c.fill = GridBagConstraints.REMAINDER;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = 4;
            c.ipady = 0;
            c.ipadx = 0;
            name.setOpaque(true);
            name.setBackground(purdueGrey);
            name.setForeground(Color.WHITE);
            name.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 128));
            pane.add(name, c);

            //bottom border
            JLabel border = new JLabel(" ");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 5;
            c.ipady = 0;
            c.ipadx = 20;
            c.insets = new Insets(0, 0, 0, 0);
            border.setOpaque(true);
            border.setBackground(purdueGold);
            pane.add(border, c);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logOut) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new LoginPane()).start();
            }
            if (e.getSource() == account) {
                account();
            }
            if (e.getSource() == quizzes) {
                quizzes();
            }
            if (e.getSource() == submissions) {
                submissions();
            }
        }
    };

    public void account() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        new Thread(new StudentAccountPane(username, password)).start();
    }

    public void quizzes() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        new Thread(new StudentQuizLandingPane(username, password)).start();
    }

    public void submissions() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        new Thread(new StudentSubmissionPane(username, password)).start();
    }
    /*
    *JLabel welcome = new JLabel("<html>" +
    *    "<body>" +
    *    "<pre>" +
    *    "                             ,,                                                       <br>"
    *    + "`7MMF'     A     `7MF'     `7MM                                                  mm            <br>"
    *    + "  `MA     ,MA     ,V         MM                                                  MM            <br>"
    *    + "   VM:   ,VVM:   ,V .gP\"Ya   MM  ,p6\"bo   ,pW\"Wq.`7MMpMMMb.pMMMb.  .gP\"Ya      mmMMmm ,pW\"Wq.  <br>"
    *    + "    MM.  M' MM.  M',M'   Yb  MM 6M'  OO  6W'   `Wb MM    MM    MM ,M'   Yb       MM  6W'   `Wb <br>"
    *    + "    `MM A'  `MM A' 8M\"\"\"\"\"\"  MM 8M       8M     M8 MM    MM    MM 8M\"\"\"\"\"\"       MM  8M     M8 <br>"
    *    + "     :MM;    :MM;  YM.    ,  MM YM.    , YA.   ,A9 MM    MM    MM YM.    ,       MM  YA.   ,A9 <br>"
    *    + "      VF      VF    `Mbmmd'.JMML.YMbmd'   `Ybmd9'.JMML  JMML  JMML.`Mbmmd'       `Mbmo`Ybmd9'  <br>"
    *    +"</pre>"
    *    +"</body>"
    *    +"</html>");
    *    
    *    
    *JLabel purdueLMS = new JLabel("<html>"
    *    +"<body>"
    *    +"<pre>"
    *    +"                                          ,,<br>"
    *    +"    `7MM\"\"\"Mq.                          `7MM                         `7MMF'      `7MMM.     ,MMF' .M\"\"\"bgd<br>"
    *    +"      MM   `MM.                           MM                           MM          MMMb    dPMM  ,MI    \"Y<br>"
    *    +"      MM   ,M9 `7MM  `7MM  `7Mb,od8  ,M\"\"bMM `7MM  `7MM  .gP\"Ya        MM          M YM   ,M MM  `MMb.<br>"
    *    +"      MMmmdM9    MM    MM    MM' \"',AP    MM   MM    MM ,M'   Yb       MM          M  Mb  M' MM    `YMMNq.<br>"
    *    +"      MM         MM    MM    MM    8MI    MM   MM    MM 8M\"\"\"\"\"\"       MM      ,   M  YM.P'  MM  .     `MM<br>"
    *    +"      MM         MM    MM    MM    `Mb    MM   MM    MM YM.    ,       MM     ,M   M  `YM'   MM  Mb     dM<br>"
    *    +"    .JMML.       `Mbod\"YML..JMML.   `Wbmd\"MML. `Mbod\"YML.`Mbmmd'     .JMMmmmmMMM .JML. `'  .JMML.P\"Ybmmd\""
    *    +"</pre>"
    *    +"</body>"
    *    +"</html>");
*/
}
