import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PurdueLMSClient {
	public static void main(String[] args) {
        boolean condition = true;
	    String hostName;
	    int port;
        //create font for GUIs
        try {
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources/Calicanto-SemiBold.ttf")));
        } catch (IOException|FontFormatException e) {
             e.printStackTrace();
        }
        
        try {
            /*
            hostName = JOptionPane.showInputDialog(null, "What host would you like to connect to?", "Hostname", 3);
            port = Integer.parseInt(JOptionPane.showInputDialog(null, "What port would you like to connect to?",
                    "Port", 3));

             */
            SwingUtilities.invokeLater(new LoginPane());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed.", "Failure", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            condition = false;
            }
    }
}
