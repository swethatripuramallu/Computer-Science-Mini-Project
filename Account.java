import java.util.*;
import java.io.*;

/**
 * Learning Database System Quiz Tool - Option 2.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Project 2 -- Option 2 </p>
 *
 * @author Jack Teschner  ( Team Leader) ,Nikunj Maheshwari ( Team Manager ) ,Shreya Senthil , Swetha Tripuramallu ,
 * Aadi Patni
 * @version April 11, 2021
 **/

public class Account {

    //primary memory: txt files are secondary memory
    private static StringArrayList studentList;
    private static StringArrayList teacherList;
    private static StringArrayList submissions;
    private static final int teacherPIN = 320129;

    //initialize primary memory from secondary .txt memory
    public Account() {
        String s = "";
        this.studentList = new StringArrayList();
        this.teacherList = new StringArrayList();
        this.submissions = new StringArrayList();
        try {
            // initialize readable arraylist based on persistent student data
            BufferedReader bfr1 = new BufferedReader(new FileReader("Memory/StudentList.txt"));
            while (true) {
                s = bfr1.readLine();
                if  (s == null) {
                    break;
                }
                this.studentList.add(s);
            }
            bfr1.close();

            // initialize readable arraylist based on persistent teacher data
            BufferedReader bfr2 = new BufferedReader(new FileReader("Memory/TeacherList.txt"));
            while (true) {
                s = bfr2.readLine();
                if (s == null) {
                    break;
                }
                this.teacherList.add(s);
            }
            bfr2.close();

            // initialize readable arraylist based on persistent submission
            BufferedReader bfr3 = new BufferedReader(new FileReader("Memory/Submissions.txt"));
            while (true) {
                s = bfr3.readLine();
                if (s == null) {
                    break;
                }
                this.submissions.add(s);
            }
            bfr3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //checks for duplicate username
    public String hasUser(String username) {
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                return "true";
            }
        }

        for (String s : teacherList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                return "true";
            }
        }

        return "false";
    }

    //part of hasUser, used for differentiation at login
    public String hasStudent(String username) {
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                return "true";
            }
        }
        return "false";
    }

    //part of hasUser, used for differentiation at login
    public String hasTeacher(String username) {
        for (String s : teacherList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                return "true";
            }
        }
        return "false";
    }

    //adds student to StudentList.txt and uses checkID (101-999) to check for duplicate PIN prior to generation 
    //Format: username,password,PIN
    public void addStudent(String username, String password) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/StudentList.txt", true));
        int id = (int) Math.floor(Math.random() * 1000);
        while (checkID(id) || id <= 100) {
            id = (int) Math.floor(Math.random() * 1000);
        }
        pw.printf("%s,%s,%d%n", username, password, id);
        pw.close();
    }

    //adds teacher to Memory/TeacherList.txt and uses checkID (1-100) to check for duplicate PIN prior to generation
    public void addTeacher(String username, String password) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/TeacherList.txt", true));
        int id = (int) Math.floor(Math.random() * 100 + 1);
        while (checkID(id)) {
            id = (int) Math.floor(Math.random() * 100 + 1);
        }
        pw.printf("%s,%s,%d%n", username, password, id);
        pw.close();

    }

    //adds 16-line submission with Format:
    //PIN, Quiz #: scored/total Time: MONTH DAY, 2022 at hh:mm:ss
    //    Question #: Question?
    //    Student answer: answer
    //    Correct answer: answer

    public void addSubmission(String username, String quizNumber, int quizScore,
                              int totalPoints, String quizTime, StringArrayList quizLog) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Submissions.txt", true));
        int pin = -1;
        boolean duplicate = false;
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                pin = Integer.parseInt(s.substring(s.indexOf(",", s.indexOf(",") + 1) + 1, s.length()));
            }
        }
        pw.printf("%d, Quiz %s: %d/%d Time: %s%n", pin, quizNumber, quizScore, totalPoints, quizTime);
        for (String s : quizLog) {
            pw.print(s);
        }
        pw.close();
    }

    //returns all submissions for a particular student, after locating PIN number from username
    public StringArrayList getStudentSubmissions(String studentUsername) {
        int pin = -1;
        StringArrayList submissions = new StringArrayList();
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(studentUsername)) {
                pin = Integer.parseInt(s.substring(s.indexOf(",", s.indexOf(",") + 1) + 1, s.length()));
            }
        }
        for (int i = 0; i < this.submissions.size(); i += 16) {
            if (this.submissions.get(i).contains(Integer.toString(pin))) {
                for (int j = i; j < i + 16; j++) {
                    submissions.add(this.submissions.get(j));
                }
            }
        }
        return submissions;
    }

    //returns the submission for a given quiz by a particular student located by PIN number
    public StringArrayList getStudentSubmission(String studentUsername, String quizNumber) {
        int pin = -1;
        StringArrayList submission = new StringArrayList();
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(studentUsername)) {
                pin = Integer.parseInt(s.substring(s.indexOf(",", s.indexOf(",") + 1) + 1, s.length()));
            }
        }
        for (int i = 0; i < submissions.size(); i += 16) {
            if (submissions.get(i).contains(Integer.toString(pin)) && submissions.get(i).contains("Quiz " + quizNumber)) {
                for (int j = i; j < i + 16; j++) {
                    submission.add(submissions.get(j));
                }
            }
        }
        return submission;

    }

    //returns manageable ArrayList containing all submissions by all students
    public StringArrayList getSubmissions() {
        return submissions;
    }

    //doesn't cause race conditions only one submission allowed per student
    public void regradeSubmission(String username, String quizNumber, String questionNumber
            , String regrade) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Submissions.txt"));
        String beg = "";
        String end = "";
        int pin = -1;
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                pin = Integer.parseInt(s.substring(s.indexOf(",", s.indexOf(",") + 1) + 1, s.length()));
            }
        }
        //edit primary memory
        for (int i = 0; i < submissions.size(); i += 16) {
            if (submissions.get(i).contains(Integer.toString(pin))) {

                String s = submissions.get(i + 1 + (Integer.parseInt(questionNumber) - 1) * 3); //locate question line

                beg = s.substring(0, s.indexOf(":") + 2);

                end = s.substring(s.indexOf("/"), s.length());

                submissions.set(i + 1 + (Integer.parseInt(questionNumber) - 1) * 3, beg + regrade + end);
            }
        }
        //store changes to secondary memory
        for (String s : submissions) {
            pw.println(s);
        }
        pw.close();
    }


    //checks ID to avoid duplicates during generation
    public boolean checkID(int id) {

        for (String s : teacherList) {
            s = s.substring(s.indexOf(",") + 1, s.length());
            s = s.substring(s.indexOf(",") + 1, s.length());
            if (id == Integer.parseInt(s)) {
                return true;
            }
        }

        for (String s : studentList) {
            s = s.substring(s.indexOf(",") + 1, s.length());
            s = s.substring(s.indexOf(",") + 1, s.length());
            if (id == Integer.parseInt(s)) {
                return true;
            }
        }

        return false;
    }

    //checks validity of passed information to grant user access to their account
    public String login(String username, String password) {
        for (String s : studentList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                s = s.substring(s.indexOf(",") + 1, s.length());
                s = s.substring(0, s.indexOf(","));
                if (s.equals(password)) {
                    return "true";
                }
            }
        }
        for (String s : teacherList) {
            if (s.substring(0, s.indexOf(",")).equals(username)) {
                s = s.substring(s.indexOf(",") + 1, s.length());
                s = s.substring(0, s.indexOf(","));
                if (s.equals(password)) {
                    return "true";
                }
            }
        }

        return "false";
    }

    //allows student users to edit their usernames (duplication is prevent in main control flow)
    //note: PIN is static, unique, randomly generated, and hidden from all users
    public void editStudentUsername(String olduser, String newuser) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/StudentList.txt"));
        String remainder = "";
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).substring(0, studentList.get(i).indexOf(",")).equals(olduser)) {
                remainder = studentList.get(i).substring(studentList.get(i).indexOf(","), studentList.get(i).length());
                studentList.set(i, newuser + remainder);
            }
        }
        for (String s : studentList) {
            pw.println(s);
        }
        pw.close();
    }
    
    public void editStudentPassword(String username, String oldpass, String newpass) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/StudentList.txt"));
        String beginning = "";
        String end = "";
        String s = "";
        for (int i = 0; i < studentList.size(); i++) {
            s = studentList.get(i).substring(studentList.get(i).indexOf(",") + 1, studentList.get(i).length());
            if (s.substring(0, s.indexOf(",")).equals(oldpass)
                    && studentList.get(i).substring(0, studentList.get(i).indexOf(",")).equals(username)) {
                beginning = studentList.get(i).substring(0, studentList.get(i).indexOf(",") + 1);
                end = s.substring(s.indexOf(","), s.length());
                studentList.set(i, beginning + newpass + end);
            }
        }
        for (String t : studentList) {
            pw.println(t);
        }
        pw.close();
    }

    //removes student account data from Memory/StudentList.txt
    public void deleteStudentAccount(String username) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/StudentList.txt"));
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).substring(0, studentList.get(i).indexOf(",")).equals(username)) {
                studentList.remove(i);
            }
        }
        for (String t : studentList) {
            pw.println(t);
        }
        pw.close();
    }

    //allows teaching staff to edit their usernames (duplication is prevented in main control flow)
    //note: PIN is static, unique, randomly generated, and hidden from all users
    public void editTeacherUsername(String olduser, String newuser) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/TeacherList.txt"));
        String remainder = "";
        for (int i = 0; i < teacherList.size(); i++) {
            if (teacherList.get(i).substring(0, teacherList.get(i).indexOf(",")).equals(olduser)) {
                remainder = teacherList.get(i).substring(teacherList.get(i).indexOf(","), teacherList.get(i).length());
                teacherList.set(i, newuser + remainder);
            }
        }
        for (String s : teacherList) {
            pw.println(s);
        }
        pw.close();
    }

    //allows teachers to edit their passwords (duplicated are allowed)
    public void editTeacherPassword(String username, String oldpass, String newpass) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/TeacherList.txt"));
        String beginning = "";
        String end = "";
        String s = "";
        for (int i = 0; i < teacherList.size(); i++) {
            s = teacherList.get(i).substring(teacherList.get(i).indexOf(",") + 1, teacherList.get(i).length());
            if (s.substring(0, s.indexOf(",")).equals(oldpass)
                    && teacherList.get(i).substring(0, teacherList.get(i).indexOf(",")).equals(username)) {
                beginning = teacherList.get(i).substring(0, teacherList.get(i).indexOf(",") + 1);
                end = s.substring(s.indexOf(","), s.length());
                teacherList.set(i, beginning + newpass + end);
            }
        }
        for (String t : teacherList) {
            pw.println(t);
        }
        pw.close();
    }

    //removes teacher account data from Memory/TeacherList.txt
    public void deleteTeacherAccount(String username) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/TeacherList.txt"));
        for (int i = 0; i < teacherList.size(); i++) {
            if (teacherList.get(i).substring(0, teacherList.get(i).indexOf(",")).equals(username)) {
                teacherList.remove(i);
            }
        }
        for (String t : teacherList) {
            pw.println(t);
        }
        pw.close();
    }

    public StringArrayList getStudentList() {
        return this.studentList;
    }

    //prevents students from creating user accounts
    public boolean checkPIN(int teacherPIN) {
        return teacherPIN == this.teacherPIN;
    }
}
