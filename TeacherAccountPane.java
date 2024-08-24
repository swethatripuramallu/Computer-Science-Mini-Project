import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherAccountPane implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String hostName;
    private int port;
    private String username;
    private String password;
    private JTextField newUserField;
    private JTextField newPassField;
    private JFrame frame;
    private static ImageIcon scaledImage;
    private JButton submit;
    private JButton delete;
    private JButton back;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public TeacherAccountPane(String username, String password) {
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

            ImageIcon image = new ImageIcon("Resources/PurdueC.png");
            JLabel img = new JLabel(image);
            c.insets = new Insets(10, 10, 10, 10);
            c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.ipady = 0;
            c.ipadx = 20;
            img.setOpaque(true);
            img.setBackground(Color.BLACK); 

            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(img, c);
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            panel.setBackground(Color.BLACK);
            pane.add(panel, c);

            JLabel topBorder = new JLabel(" ");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridx = 0;
            c.gridy = 1;
            c.ipady = 0;
            c.ipadx = 0;
            c.insets = new Insets(0, 0, 0, 0);
            topBorder.setOpaque(true);
            topBorder.setBackground(purdueGold);
            pane.add(topBorder, c);

            JLabel accountHeading = new JLabel("Account Settings");
            accountHeading.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 18));
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = 1;
            c.insets = new Insets(10, 10, 10, 10);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 2;
            c.ipady = 0;
            c.ipadx = 0;
            accountHeading.setForeground(Color.WHITE);
            pane.add(accountHeading, c);

            JLabel userHeading = new JLabel("Username:");
            userHeading.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 3;
            c.ipady = 0;
            c.ipadx = 0;
            userHeading.setForeground(Color.WHITE);
            pane.add(userHeading, c);

            newUserField = new JTextField(username, 10);
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 1;
            c.gridy = 3;
            c.ipady = 0;
            c.ipadx = 0;
            newUserField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    newUserField.setText("");
                }
            });
            newUserField.addActionListener(actionListener);
            pane.add(newUserField, c);

            JLabel passHeading = new JLabel("Password:");
            passHeading.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 4;
            c.ipady = 0;
            c.ipadx = 0;
            passHeading.setForeground(Color.WHITE);
            pane.add(passHeading, c);

            String s = "";
            for (int i = 0; i < password.length(); i++) {
                s += "*";
            }
            newPassField = new JTextField(repeat(), 10);
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 1;
            c.gridy = 4;
            c.ipady = 0;
            c.ipadx = 0;
            newPassField.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    newPassField.setText("");
                }
            });
            newPassField.addActionListener(actionListener);
            pane.add(newPassField, c);


            submit = new JButton("Submit Changes");
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 5;
            c.ipady = 0;
            c.ipadx = 0;
            submit.addActionListener(actionListener);
            submit.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            pane.add(submit, c);

            delete = new JButton("Delete Account");
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridheight = 1;
            c.insets = new Insets(0, 0, 10, 0);
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridx = 0;
            c.gridy = 6;
            c.ipady = 0;
            c.ipadx = 0;
            delete.addActionListener(actionListener);
            delete.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 14));
            pane.add(delete, c);


            back = new JButton("Return to Dashboard");
            c.insets = new Insets(0,0,0,0);
            c.ipady = 0;
            c.gridy = 10;
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
            if (e.getSource() == submit || e.getSource() == newUserField || e.getSource() == newPassField) {
                //verifies username meets all conditions except duplicate (handled by server) 
                //username change expected if submit button is clicked with no changes to password or username
                boolean noChange = false;
                if (!newUserField.getText().equals(username) && !newUserField.getText().contains(",") && newUserField.getText().length() > 3) {
                    changeUser(newUserField.getText());
                } else if (newUserField.getText().contains(",")) {
                    JOptionPane.showMessageDialog(null, "New username cannot contain commas.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (newUserField.getText().length() <= 3) {
                    JOptionPane.showMessageDialog(null, "New username must be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (newUserField.getText().equals(username)) {
                    JOptionPane.showMessageDialog(null, "New username must be different.", "Error", JOptionPane.ERROR_MESSAGE);
                } 

                //verifies password meets all conditions
                if (!newPassField.getText().equals(password) && !newPassField.getText().contains(",") && newPassField.getText().length() > 3 && !newPassField.getText().equals(repeat())) {
                    changePass(newPassField.getText());
                } else if (newPassField.getText().equals(password)) {
                    JOptionPane.showMessageDialog(null, "New password must be different.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (newPassField.getText().contains(",")) {
                    JOptionPane.showMessageDialog(null, "New password cannot contain commas.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (newPassField.getText().length() <= 3) {
                    JOptionPane.showMessageDialog(null, "New password must be longer than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == back) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new TeacherLandingPane(username, password)).start();
            }

            if (e.getSource() == delete) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?") == 0) {
                    delete();
                    JOptionPane.showMessageDialog(null, "Account successfully deleted. Returning to login..."); 
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    new Thread(new LoginPane()).start();
                }
            }
        }
    };

    public void changeUser(String username) {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            oos.writeObject("changeTeacherUsername");
            oos.flush();
            oos.writeObject(this.username);
            oos.flush();
            oos.writeObject(newUserField.getText());
            oos.flush();
            //check for username duplicate
            String boole = (String) ois.readObject();
            if (!Boolean.parseBoolean(boole)) {
                this.username = newUserField.getText();
                JOptionPane.showMessageDialog(null, "Username changed successfully.", "Success", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePass(String password) {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            oos.writeObject("changeTeacherPassword");
            oos.flush();
            oos.writeObject(this.username);
            oos.flush();
            oos.writeObject(this.password);
            oos.flush();
            oos.writeObject(newPassField.getText());
            oos.flush();
            //password duplicates are allowed
            this.password = newPassField.getText();
            JOptionPane.showMessageDialog(null, "Password changed successfully.", 
                "Success", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            Socket socket = new Socket("localhost", 4242);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            oos.writeObject("deleteTeacherAccount");
            oos.flush();
            oos.writeObject(this.username);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String repeat() {
        String s = "";
        for (int i = 0; i < password.length(); i++){
            s += "*";
        }
        return s;
    }
}
