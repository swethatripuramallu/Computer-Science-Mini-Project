import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPane implements Runnable {
    private String hostName;
    private int port;
    private JFrame frame;
    private static ImageIcon scaledImage;
    private JTextField userField;
    private JTextField passField;
    private String username;
    private String password;
    private static JButton login;
    private static JButton signUp;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public LoginPane(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public LoginPane(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginPane() {

    }

    public void run() {
        try {

            //create frame and layout
            frame = new JFrame("Purdue LMS");
            frame.setResizable(false);
            GridBagLayout grid = new GridBagLayout();

            //create container and layouts + constraints
            Container pane = frame.getContentPane();
            pane.setLayout(grid);
            pane.setBackground(purdueGrey);
            GridBagConstraints c = new GridBagConstraints();

            ImageIcon image = new ImageIcon("Resources/PurdueC.png");
            JLabel img = new JLabel(image);
            c.insets = new Insets(10, 10, 10, 10);
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            c.ipady = 0;
            c.ipadx = 20;
            c.fill = GridBagConstraints.HORIZONTAL;
            img.setOpaque(true);
            img.setBackground(Color.BLACK); 

            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(img, c);
            c.fill = GridBagConstraints.NONE;
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            panel.setBackground(Color.BLACK);
            pane.add(panel, c);


            JLabel topBorder = new JLabel(" ");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridy = 1;
            c.ipady = 0;
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 10, 0);
            topBorder.setOpaque(true);
            topBorder.setBackground(purdueGold);
            pane.add(topBorder, c);

            userField = new JTextField("username", 10);
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(0,0,0,0);
            c.gridy = 2;
            c.gridwidth = 1;
            userField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    userField.setText("");
                }
            });
            userField.addActionListener(actionListener);
            pane.add(userField, c);

            passField = new JTextField("password", 10);
            c.gridy = 3;
            c.insets = new Insets(0, 0, 0, 0);
            passField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    passField.setText("");
                }
            });
            passField.addActionListener(actionListener);
            pane.add(passField,c);

            login = new JButton ("Login");
            login.addActionListener(actionListener);
            c.gridy = 4;
            c.ipady = 20;
            c.ipadx = 50;
            c.insets = new Insets(0, 0, 0, 0);
            login.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 18));
            pane.add(login, c);

            signUp = new JButton ("Don't have an account? Sign up");
            c.insets = new Insets(10,0,0,0);
            c.ipady = 0;
            c.gridy = 5;
            c.fill = 2;
            signUp.setOpaque(true);
            signUp.setBackground(purdueGold);
            signUp.addActionListener(actionListener);
            signUp.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            pane.add(signUp, c);

            frame.setVisible(true);
            frame.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login || e.getSource() == userField ||e.getSource() == passField) {
                login();
            }
            if (e.getSource() == signUp) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new SignUpPane()).start();
            }
        }
    };

    public void login() {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            oos.writeObject("login");
            oos.flush();
            oos.writeObject(userField.getText());
            oos.flush();
            oos.writeObject(passField.getText());
            oos.flush();
            String boole = (String) ois.readObject();
            if (!Boolean.parseBoolean(boole)) {
                ois.readObject(); //scrap 
                JOptionPane.showMessageDialog(null, "Incorrect password/username", "Error", 0);
                userField.setText("username");
                passField.setText("password");
            } else {
                if (Boolean.parseBoolean((String) ois.readObject())) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    new Thread(new StudentLandingPane(userField.getText(), passField.getText())).start();
                } else {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    new Thread(new TeacherLandingPane(userField.getText(), passField.getText())).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
