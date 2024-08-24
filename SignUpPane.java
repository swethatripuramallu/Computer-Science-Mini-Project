import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignUpPane implements Runnable {
    private String hostName;
    private int port;
    private JFrame frame;
    private static ImageIcon scaledImage;
    private JTextField userField;
    private JTextField passField;
    private JTextField pinField;
    private String username;
    private String password;
    private static JButton signUp;
    private static JButton back;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);
    private boolean isTeacher;

    public SignUpPane(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public SignUpPane() {

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
            c.gridwidth = 2;
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
            passField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    passField.setText("");
                }
            });
            passField.addActionListener(actionListener);
            pane.add(passField,c);

            signUp = new JButton ("Sign Up");
            c.gridy = 4;
            c.ipady = 20;
            c.ipadx = 50;
            signUp.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 18));
            signUp.addActionListener(actionListener);
            pane.add(signUp, c);

            JLabel faculty = new JLabel("Faculty:  ");
            c.gridwidth = 1;
            c.ipady = 0;
            c.ipadx = 0;
            c.gridy = 5;
            c.gridx = 0;
            c.insets = new Insets(10, 0, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            faculty.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 18));
            faculty.setForeground(Color.WHITE);
            pane.add(faculty, c);

            pinField = new JTextField("pin", 10);
            c.gridy = 5;
            c.gridx = 1;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(10, 0, 0, 0);
            pinField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    pinField.setText("");
                }
            });
            pinField.addActionListener(actionListener);
            pane.add(pinField,c);

            back = new JButton ("Return to Login");
            c.insets = new Insets(10,0,0,0);
            c.gridx = 0;
            c.gridy = 10;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.ipady = 0;
            c.gridy = 10;
            c.fill = 2;
            back.setOpaque(true);
            back.setBackground(purdueGold);
            back.addActionListener(actionListener);
            back.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            pane.add(back, c);

            frame.setVisible(true);
            frame.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signUp || e.getSource() == userField ||e.getSource() == passField || e.getSource() == pinField) {
                if (pinField.getText().equals("pin") || pinField.getText().equals("")) {
                    //verifies student username and password meet all conditions except user 
                    //duplicate (handled by server)
                    if (!userField.getText().contains(",") && userField.getText().length() > 3 
                        && !passField.getText().contains(",") && passField.getText().length() > 3) {
                        signUp(false);
                    } else if (userField.getText().contains(",") || userField.getText().length() <= 3) {
                        JOptionPane.showMessageDialog(null, "New username cannot contain commas and must"
                            + " be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    } 
                    //report password problems
                    if (passField.getText().contains(",") || passField.getText().length() <= 3) {
                        JOptionPane.showMessageDialog(null, "New password cannot contain commas and must"
                            + " be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You are attempting to create a faculty account. "
                        + "Please leave the pin field as-is or blank if you are a student", "Warning", 
                        JOptionPane.WARNING_MESSAGE);
                    if (pinField.getText().equals("320129")) {
                        //verifies teacher username and password meet all conditions except user 
                        //duplicate (handled by server)
                        if (!userField.getText().contains(",") && userField.getText().length() > 3 
                            && !passField.getText().contains(",") && passField.getText().length() > 3) {
                            signUp(true);
                        } else if (userField.getText().contains(",") || userField.getText().length() <= 3) {
                            JOptionPane.showMessageDialog(null, "New username cannot contain commas and must"
                                + " be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                        } 
                        //report password problems
                        if (passField.getText().contains(",") || passField.getText().length() <= 3) {
                            JOptionPane.showMessageDialog(null, "New password cannot contain commas and must"
                                + " be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid PIN. This incident has been reported", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
            if (e.getSource() == back) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new LoginPane()).start();
            }
        }
    };

    public void signUp(boolean isTeacher) {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            if (!isTeacher) {
                oos.writeObject("studentSignUp");
                oos.flush();
                oos.writeObject(userField.getText());
                oos.flush();
                oos.writeObject(passField.getText());
                oos.flush();
                String boole = (String) ois.readObject();
                if (Boolean.parseBoolean(boole)) { //scrap server print
                    JOptionPane.showMessageDialog(null, "Username is taken.", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Student account created successfully.", 
                        "Success", JOptionPane.WARNING_MESSAGE);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    new Thread(new LoginPane()).start();
                }
            } else {
                oos.writeObject("teacherSignUp");
                oos.flush();
                oos.writeObject(userField.getText());
                oos.flush();
                oos.writeObject(passField.getText());
                oos.flush();
                String boole = (String) ois.readObject();
                if (Boolean.parseBoolean(boole)) {
                    JOptionPane.showMessageDialog(null, "Username is taken.", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Faculty account created successfully.", 
                        "Success", JOptionPane.WARNING_MESSAGE);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    new Thread(new LoginPane()).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
