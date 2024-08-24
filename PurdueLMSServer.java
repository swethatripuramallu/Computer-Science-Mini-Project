import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PurdueLMSServer implements Runnable {

    Socket socket; //no modifier is "package private" all classes see field
    private Account account = new Account();
    private Quiz quiz = new Quiz();

    public PurdueLMSServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.printf("connection received from %s\n", socket);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());    
            switch ((String) ois.readObject()) {
                case "login":
                    login(ois, oos);
                    break;
                case "studentSignUp":
                    studentSignUp(ois, oos);
                    break;
                case "teacherSignUp":
                    teacherSignUp(ois, oos);
                    break;
                case "changeStudentUsername":
                    changeStudentUsername(ois, oos);
                    break;
                case "changeTeacherUsername":
                    changeTeacherUsername(ois, oos);
                    break;
                case "changeStudentPassword":
                    changeStudentPassword(ois, oos);
                    break;
                case "changeTeacherPassword":
                    changeTeacherPassword(ois, oos);
                    break;
                case "deleteStudentAccount":
                    deleteStudentAccount(ois, oos);
                    break;
                case "deleteTeacherAccount":
                    deleteTeacherAccount(ois, oos);
                    break;
                case "findAvailableQuizzes":
                    findAvailableQuizzes(ois, oos);
                    break;
                case "takeQuiz":
                    takeQuiz(ois, oos);
                    break;
                case "submitQuiz":
                    submitQuiz(ois, oos);
                    break;
                case "findAvailableSubmissions":
                    findAvailableSubmissions(ois, oos);
                    break;
                case "getSubmission":
                    getSubmission(ois, oos);
                    break;
                case "findAvailableStudents":
                    getStudents(ois, oos);
                    break;
                case "findQuizzes":
                    getQuizzes(ois, oos);
                    break;
                case "deleteQuiz":
                    deleteQuiz(ois, oos);
                    break;
                default:
                    break;
            }
            oos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //note: declaring methods as synchronized blocks the one instance of Account in this program
    //this means that any synchronized method acting on account will block changes to Account
    //no need for gatekeeper in multiple methods (e.g. data modified by studentSignUp while accessed by login)
    //only Account-modifying methods need to be synchronized as login will not be able to access the 
    //signUp-blocked Account object (login doesn't need to be synchronized)
    public void login(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {  
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        oos.writeObject(account.login(username, password));
        oos.flush();
        oos.writeObject(account.hasStudent(username));
        oos.flush();
    }

    public synchronized void studentSignUp(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        oos.writeObject(account.hasUser(username));
        oos.flush();
        if (!Boolean.parseBoolean(account.hasUser(username))) {
            account.addStudent(username, password); 
        }
    }

    public synchronized void teacherSignUp(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        oos.writeObject(account.hasUser(username));
        oos.flush();
        if (!Boolean.parseBoolean(account.hasUser(username))) {
            account.addTeacher(username, password); 
        }
    }

    public synchronized void changeStudentUsername(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {    
        String username = (String) ois.readObject();
        String newUser = (String) ois.readObject();
        oos.writeObject(account.hasUser(newUser));
        oos.flush();
        if (!Boolean.parseBoolean(account.hasUser(newUser))) {
            account.editStudentUsername(username, newUser);
        }
    }

    public synchronized void changeTeacherUsername(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {    
        String username = (String) ois.readObject();
        String newUser = (String) ois.readObject();
        oos.writeObject(account.hasUser(newUser));
        oos.flush();
        if (!Boolean.parseBoolean(account.hasUser(newUser))) {
            account.editTeacherUsername(username, newUser);
        }
    }

    public synchronized void changeStudentPassword(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {    
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        String newPass = (String) ois.readObject();
        account.editStudentPassword(username, password, newPass);
    }

    public synchronized void changeTeacherPassword(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {    
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        String newPass = (String) ois.readObject();
        account.editTeacherPassword(username, password, newPass);
    }

    public synchronized void deleteStudentAccount(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        account.deleteStudentAccount(username);
    }

    public synchronized void deleteTeacherAccount(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        account.deleteTeacherAccount(username);
    }

    public synchronized void findAvailableQuizzes(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        //get submissions for student
        StringArrayList studentSubmissions = account.getStudentSubmissions(username);
        IntegerArrayList quizzes = new IntegerArrayList();
        int quizOptions = this.quiz.getQuizOptions();
        for (int i = 1; i <= quizOptions; i++) {
            quizzes.add(i);
        }
        for (String s : studentSubmissions) {
            if (s.contains("Quiz")) {
                Integer k = Integer.parseInt(s.substring(s.indexOf("z") + 2, s.indexOf(":")));
                if (quizzes.contains(k)) {
                    quizzes.remove(k);
                }
            }
        }
        oos.writeObject(quizzes);
        
    }

    public synchronized void takeQuiz(ObjectInputStream ois, ObjectOutputStream oos) 
        throws FileNotFoundException, IOException, ClassNotFoundException{
        Integer i = (Integer) ois.readObject();
        String s = i.toString();
        Quiz quizz = new Quiz(s);
        oos.writeObject(quizz);
    }

    public synchronized void submitQuiz(ObjectInputStream ois, ObjectOutputStream oos) 
        throws FileNotFoundException, IOException, ClassNotFoundException{
        String username = (String) ois.readObject();
        String quizNumber = (String) ois.readObject();
        String s = (String) ois.readObject();
        int pointsEarned = Integer.parseInt(s);
        String t = (String) ois.readObject();
        int totalPoints = Integer.parseInt(t);
        String quizTime =  (String) ois.readObject();
        StringArrayList quizLog = (StringArrayList) ois.readObject();
        account.addSubmission(username, quizNumber, pointsEarned, totalPoints, quizTime, quizLog);
    }

    public synchronized void findAvailableSubmissions(ObjectInputStream ois, ObjectOutputStream oos) throws FileNotFoundException, IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        oos.writeObject(quiz.getAvailableSubmissions(username, account));
        oos.flush();
    }

    public synchronized void getSubmission(ObjectInputStream ois, ObjectOutputStream oos) throws FileNotFoundException, IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        String submissionNumber = (String) ois.readObject();
        oos.writeObject(account.getStudentSubmission(username, submissionNumber));
        oos.flush();
    }

    public synchronized void getStudents(ObjectInputStream ois, ObjectOutputStream oos) throws FileNotFoundException, IOException, ClassNotFoundException {
        oos.writeObject(account.getStudentList());
        oos.flush();
    } 

    public synchronized void getQuizzes(ObjectInputStream ois, ObjectOutputStream oos) throws FileNotFoundException, IOException, ClassNotFoundException {
        int i = quiz.getQuizOptions();
        String s = String.valueOf(i);
        oos.writeObject(s);
        oos.flush();
    } 

    public synchronized void deleteQuiz(ObjectInputStream ois, ObjectOutputStream oos) throws FileNotFoundException, IOException, ClassNotFoundException {
        String s = (String) ois.readObject();
        quiz.deleteQuiz(s);
    } 

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            System.out.printf("Socket open, waiting for connections on %s\n",
                serverSocket);
            while (true) {
                Socket socket = serverSocket.accept();
                PurdueLMSServer server = new PurdueLMSServer(socket);
                new Thread(server).start();
            }
        } catch (Exception e) {
            System.out.println("Server creation failed.");
            e.printStackTrace();
        }
    }
}
