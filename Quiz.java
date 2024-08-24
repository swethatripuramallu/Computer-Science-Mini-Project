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

public class Quiz implements Serializable{
    private int quizNumber;
    private int quizOptions;
    private Question[] randomizedQuestions = new Question[10];
    private ArrayList<Question> questions;
    ArrayList<Question> randomized;
    private StringArrayList quizzes;

    //default constructor with initialized quizzes ArrayList is required to call getAvailableQuizzes() and for deleting/editing quizzes
    public Quiz() {
        this.quizzes = new StringArrayList();
        this.questions = null;
        this.randomizedQuestions = null;
        String s;
        try {
            BufferedReader bfr1 = new BufferedReader(new FileReader("Memory/Quizzes.txt"));
            while (true) {
                s = bfr1.readLine();
                if (s == null) {
                    break;
                }
                this.quizzes.add(s);
            }
            bfr1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.quizNumber = -1;
        quizOptions = (int) Math.floor(((double) quizzes.size()) / 63.0);
    }

    //initializes chosen quiz
    public Quiz(String quizNumber) throws FileNotFoundException, IOException {
        this.quizNumber = Integer.parseInt(quizNumber);
        this.quizzes = new StringArrayList();
        this.questions = new ArrayList<Question>();
        int questionNumber;
        int questionValue;
        boolean randomizeQuestions = false;
        boolean randomizeMC = false;
        String question;
        String choice;
        StringArrayList choices;
        IntegerArrayList remainingQuestions = new IntegerArrayList();
        for (int i = 0; i < 10; i++) {
            remainingQuestions.add(i);
        }
        randomized = new ArrayList<Question>();
        int index = 0;
        String answer;
        String s = "";

        // make aggregate quiz content manageable by Java
        BufferedReader bfr1 = new BufferedReader(new FileReader("Memory/Quizzes.txt"));
        while (true) {
            s = bfr1.readLine();
            if (s == null) {
                break;
            }
            this.quizzes.add(s);
        }        
        bfr1.close();

        //define all 10 questions in selected quiz as their respective objects
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                if (quizzes.get(i + 1).equals("true")) {
                    randomizeQuestions = true;
                }
                if (quizzes.get(i + 2).equals("true")) {
                    randomizeMC = true;
                }
                for (int j = i + 3; j <= i + 57; j += 6) {
                    switch (quizzes.get(j)) {
                        case "M":
                            questionNumber = parseNumber(quizzes.get(j + 1));
                            questionValue = parseValue(quizzes.get(j + 2));
                            question = quizzes.get(j + 3);
                            choice = quizzes.get(j + 4);
                            choices = new StringArrayList();
                            while (choice.contains(",")) {
                                choices.add(choice.substring(0,choice.indexOf(",")));
                                choice = choice.substring(choice.indexOf(",") + 1, choice.length());
                                if (!choice.contains(",")) {
                                    choices.add(choice);
                                }
                            }
                            answer = quizzes.get(j + 5);
                            MultipleChoice mc = new MultipleChoice(questionNumber, questionValue, question, choices, answer, randomizeMC);
                            questions.add(mc);
                            break;
                        case "F":
                            questionNumber = parseNumber(quizzes.get(j + 1));
                            questionValue = parseValue(quizzes.get(j + 2));
                            question = quizzes.get(j + 3);
                            answer = quizzes.get(j + 5);
                            Fill f = new Fill(questionNumber, questionValue, question, answer);
                            questions.add(f);
                            break;
                        case "T":
                            questionNumber = parseNumber(quizzes.get(j + 1));
                            questionValue = parseValue(quizzes.get(j + 2));
                            question = quizzes.get(j + 3);
                            answer = quizzes.get(j + 5);
                            TrueFalse t = new TrueFalse(questionNumber, questionValue, question, answer);
                            questions.add(t);
                            break;
                    }
                }
            } 
        }
        if (randomizeQuestions) {
            //create randomized 5 question quiz from 10 question quiz bank
            for (int i = 0; i < 5; i++) {
                while (true) {
                    index = (int) Math.floor(Math.random() * 10);
                    if (remainingQuestions.contains((Integer) index)) {
                        break;
                    }
                }
                remainingQuestions.remove((Integer) index);
                randomizedQuestions[i] = questions.get(index);
            }
            //number of quizzes, necessary for printing available quizzes
            quizOptions = (int) Math.floor(((double) quizzes.size()) / 63.0);
        }
        for (int i = 0; i < 5; i++) {
            randomized.add(randomizedQuestions[i]);
        }
    }

    //return ArrayList of questions for a particular quiz (indicated by constructor; default null)
    public ArrayList<Question> getQuestions() {
        return this.randomized;
    }

    //return number of quizzes that have been created 
    public int getQuizOptions() {
        return quizOptions;
    }

    public void editQuestion(String quizNumber, String questionNumber, String quizQuestion) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                quizzes.set(i + Integer.parseInt(questionNumber) * 6, quizQuestion);
            }
        }
        //save to secondary memory
        for (String s : quizzes) {
            pw.println(s);
        }
        pw.close();
    }

    public void editAnswer(String quizNumber, String questionNumber, String quizAnswer) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                quizzes.set(i + 8 + (Integer.parseInt(questionNumber) - 1) * 6, quizAnswer);
            }
        }
        //save to secondary memory
        for (String s : quizzes) {
            pw.println(s);
        }
        pw.close();
    }

    public void editChoices(String quizNumber, String questionNumber, String quizChoices) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                quizzes.set(i + 7 + (Integer.parseInt(questionNumber) - 1) * 6, quizChoices);
            }
        }
        //save to secondary memory
        for (String s : quizzes) {
            pw.println(s);
        }
        pw.close();
    }

    public void editRandomization(String quizNumber) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                quizzes.set(i + 1, "true");
            }
        }
        //save to secondary memory
        for (String s : quizzes) {
            pw.println(s);
        }
        pw.close();
    }

    public void editMCRandomization(String quizNumber) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).equals(quizNumber)) {
                quizzes.set(i + 2, "true");
            }
        }
        //save to secondary memory
        for (String s : quizzes) {
            pw.println(s);
        }
        pw.close();
    }

    //return quizzes that can be taken
    public IntegerArrayList getAvailableQuizzes(String username, Account account) throws FileNotFoundException, IOException {
        StringArrayList studentSubmissions = account.getStudentSubmissions(username);
        IntegerArrayList quizzes = new IntegerArrayList();
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

        return quizzes;
    }

    //return submissions available to view
    public IntegerArrayList getAvailableSubmissions(String username, Account account) throws FileNotFoundException, IOException {
        StringArrayList studentSubmissions = account.getStudentSubmissions(username);
        IntegerArrayList quizzes = new IntegerArrayList();
        for (String s : studentSubmissions) {
            //locate quiz numbers from Submissions.txt and return them as an Integer ArrayList of quizzes taken.
            if (s.contains("Quiz")){
                quizzes.add(Integer.parseInt(s.substring(s.indexOf("z") + 2, s.indexOf(":"))));
            }
        }
        
        return quizzes;
    }

    public void deleteQuiz(String quizNumber) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("Memory/Quizzes.txt"));
        //arraylists have variable size when augmenting them. 
        //thus it is necessary to create a temp arraylist to 
        //prevent index out of bounds halfway through removal
        StringArrayList temp = new StringArrayList();
        for (String s : quizzes) {
            temp.add(s);
        }
        //edit primary memory
        for (int i = 0; i < quizzes.size(); i+=63) {
            if (quizzes.get(i).equals(quizNumber)) {
                for (int j = i; j < 63; j++) {
                    temp.remove(quizzes.get(j));
                }
            }
        }
        //store changes to secondary memory
        for (String s : temp) {
            pw.println(s);
        }
        pw.close();
    }
    
    //parse question number for formatted Memory/Quizzes.txt
    private int parseNumber(String questionNumber) {
        switch (questionNumber) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            case "VI":
                return 6;
            case "VII":
                return 7;
            case "VIII":
                return 8;
            case "IX":
                return 9;
            case "X":
                return 10;
            default:
                return -1;
        }
    }

    //parse question point value for formatted Memory/Quizzes.txt
    private int parseValue(String questionValue) {
        switch (questionValue) {
            case "a":
                return 1;
            case "b":
                return 2;
            case "c":
                return 3;
            case "d":
                return 4;
            case "e":
                return 5;
            case "f":
                return 6;
            case "g":
                return 7;
            case "h":
                return 8;
            case "i":
                return 9;
            case "j":
                return 10;
            default:
                return -1;
        }
    }

    public int getQuizNumber() {
        return this.quizNumber;
    }

    @Override
    public String toString() {
        return questions.toString();
    }
}

  
