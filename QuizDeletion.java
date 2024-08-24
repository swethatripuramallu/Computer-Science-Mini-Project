import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuizDeletion implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String hostName;
    private int port;
    private int count;
    private String quizzes;
    private String username;
    private String password;
    private JFrame frame;
    private JButton back;
    private static ImageIcon scaledImage;
    private ArrayList<Integer> quizNumbers;
    private ArrayList<JButton> buttons;
    private Quiz selectedQuiz;
    private static JButton quiz;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public QuizDeletion(String username, String password) {
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
            quizzes = findQuizzes();
            quizNumbers = new IntegerArrayList();
            for (int i = 1; i <= Integer.parseInt(quizzes); i++) {
            	quizNumbers.add(i);
            }
            c.gridwidth = 1;
            String s;
            for (int i = 0; i < quizNumbers.size(); i++) {
            	c.fill = GridBagConstraints.HORIZONTAL;
            	c.gridy = (int) ((double) i / (double) 3) + 2; 
            	c.gridx = i % 3;
            	c.insets = new Insets(10, 10, 10, 10);
            	quiz = new JButton("Quiz " + (quizNumbers.get(i)));
            	quiz.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
            	quiz.addActionListener(actionListener);
            	buttons.add(quiz);
            	pane.add(quiz, c);
            	count++;
            }
            if (quizNumbers.size() == 0) {
            	c.gridwidth = GridBagConstraints.REMAINDER;
            	c.fill = GridBagConstraints.NONE;
            	c.gridy = 2;
            	c.gridx = 0;
            	c.insets = new Insets(10, 10, 10, 10);
            	JLabel label = new JLabel("No quizzes available");
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
                new Thread(new StudentLandingPane(username, password)).start();
        	}

        	for (int j = 0; j < count; j++) {
        		if (e.getSource() == buttons.get(j)) {
        			try {
        				deleteQuiz(quizNumbers.get(j));
		                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		                new Thread(new TeacherLandingPane(username, password)).start();
        			} catch (Exception ex) {
        				ex.printStackTrace();
        			}
        		}
        	}
	        
        }
    };

    public void deleteQuiz(int quizNumber) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        oos.writeObject("deleteQuiz");
        oos.flush();
        String s = String.valueOf(quizNumber);
        oos.writeObject(s);
        oos.flush();
        JOptionPane.showMessageDialog(null, String.format("Quiz %d has been deleted", quizNumber), 
        	"Success", JOptionPane.WARNING_MESSAGE);
    }

    public String findQuizzes() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        oos.writeObject("findQuizzes");
        oos.flush();
        return (String) ois.readObject();
    }
}
