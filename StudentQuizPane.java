import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.awt.event.*;

public class StudentQuizPane implements Runnable {
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
    private Quiz selectedQuiz;
    private ArrayList<String> submission;
    private static JButton quiz;
    private static final Color purdueGrey = new Color(31, 31, 31);
    private static final Color purdueGold = new Color(207, 185, 145);

    public StudentQuizPane(String username, String password, Quiz selectedQuiz, int index, 
        ArrayList<String> submission, int pointsEarned, int totalPoints) {
        this.password = password;
        this.username = username;
        this.selectedQuiz = selectedQuiz;
        this.questions = selectedQuiz.getQuestions();
        this.index = index;
        this.submission = submission;
        this.pointsEarned = pointsEarned;
        this.totalPoints = totalPoints;
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
            ImageIcon image = new ImageIcon("PurdueC.png");
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
        	c.insets = new Insets(10, 10, 10, 10);
            if (questions.get(index) instanceof MultipleChoice) {
                MultipleChoice question = (MultipleChoice) questions.get(index);
                JLabel text = new JLabel(question.getQuestion());
                text.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                text.setForeground(Color.WHITE);
                c.gridy = 2;
                pane.add(text, c);
            	button1 = new JRadioButton(question.getChoices().get(0));
            	button1.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                button1.setForeground(Color.WHITE);
            	button1.addActionListener(actionListener1);
                button2 = new JRadioButton(question.getChoices().get(1));
                button2.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                button2.setForeground(Color.WHITE);
                button2.addActionListener(actionListener2);
                button3 = new JRadioButton(question.getChoices().get(2));
                button3.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                button3.setForeground(Color.WHITE);
                button3.addActionListener(actionListener3);
                button4 = new JRadioButton(question.getChoices().get(3));
                button4.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                button4.setForeground(Color.WHITE);
                button4.addActionListener(actionListener4);
                group = new ButtonGroup();
                group.add(button1);
                group.add(button2);
                group.add(button3);
                group.add(button4);
                c.gridy = 3;
            	pane.add(button1, c);
                c.gridy = 4;
                pane.add(button2, c);
                c.gridy = 5;
                pane.add(button3, c);
                c.gridy = 6;
                pane.add(button4, c);
            } else if (questions.get(index) instanceof Fill) {
                Fill question = (Fill) questions.get(index);
                JLabel fill = new JLabel(question.getQuestion());
                fill.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                fill.setForeground(Color.WHITE);
                c.anchor = GridBagConstraints.CENTER;
                c.gridwidth = GridBagConstraints.REMAINDER;
                pane.add(fill, c);
                c.gridy = 3;
                c.fill = GridBagConstraints.NONE;
                field = new JTextField("Answer Here", 10);
                field.addActionListener(actionListener5);
                pane.add(field, c);
            } else if (questions.get(index) instanceof TrueFalse) {
                TrueFalse question = (TrueFalse) questions.get(index);
                JLabel tf = new JLabel(question.getQuestion());
                tf.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                tf.setForeground(Color.WHITE);
                c.gridwidth = GridBagConstraints.REMAINDER;
                pane.add(tf, c);
                truth = new JButton("True");
                truth.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                truth.addActionListener(actionListener6);
                c.gridwidth = 1;
                c.gridy = 3;
                c.gridx = 0;
                pane.add(truth, c);
                nontruth = new JButton("False");
                nontruth.setFont(new Font("Calicanto-SemiBold", Font.BOLD, 32));
                nontruth.addActionListener(actionListener7);
                c.gridy = 3;
                c.gridx = 1;
                pane.add(nontruth, c);
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
        }
    };

    ActionListener actionListener1 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MultipleChoice question = (MultipleChoice) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals(question.getChoices().get(0))) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), question.getChoices().get(0), question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MultipleChoice question = (MultipleChoice) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals(question.getChoices().get(1))) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), question.getChoices().get(1), question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener3 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MultipleChoice question = (MultipleChoice) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals(question.getChoices().get(2))) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), question.getChoices().get(2), question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener4 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MultipleChoice question = (MultipleChoice) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals(question.getChoices().get(3))) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), question.getChoices().get(3), question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener5 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Fill question = (Fill) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals(field.getText().toLowerCase())) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), field.getText(), question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener6 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TrueFalse question = (TrueFalse) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals("true")) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), "true", question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    ActionListener actionListener7 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TrueFalse question = (TrueFalse) questions.get(index);
            int questionScore = 0;
            if (question.getAnswer().toLowerCase().equals("false")) {
                questionScore = question.getQuestionValue();
                pointsEarned += question.getQuestionValue();
            }
            totalPoints += question.getQuestionValue();
            submission.add(String.format("    Question %s: %d/%d%n    Student Answer:%s%n    "
                + "Correct Answer:%s%n", question.getQuestionNumber(), questionScore, 
                question.getQuestionValue(), "false", question.getAnswer().toLowerCase()));
            if (index < 4) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentQuizPane(username, password, selectedQuiz, index + 1, submission, pointsEarned, totalPoints)).start();
            } else {
                try {
                    submitQuiz();
                    JOptionPane.showMessageDialog(null, "Quiz Completed.", "Success", JOptionPane.WARNING_MESSAGE);
                } catch (Exception d) {
                    d.printStackTrace();
                }   
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new Thread(new StudentLandingPane(username, password)).start();
            }
        }
    };

    public void submitQuiz() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 4242);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        String quizTime = String.format("%s %d, 2022 at %02d:%02d:%02d", date.getMonth(), date.getDayOfMonth(),
            time.getHour(), time.getMinute(), time.getSecond());
        oos.writeObject("submitQuiz");
        oos.flush();
        oos.writeObject(username);
        oos.flush();
        oos.writeObject(Integer.toString(selectedQuiz.getQuizNumber()));
        oos.flush();
        oos.writeObject(Integer.toString(pointsEarned));
        oos.flush();
        oos.writeObject(Integer.toString(totalPoints));
        oos.flush();
        oos.writeObject(quizTime);
        oos.flush();
        oos.writeObject(submission);
        oos.flush();
    }
}
