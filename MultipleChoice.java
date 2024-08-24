import java.util.*;
import java.io.Serializable;

/**
 * Learning Database System Quiz Tool - Option 2.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Project 2 -- Option 2 </p>
 *
 * @author Jack Teschner  ( Team Leader) ,Nikunj Maheshwari ( Team Manager ) ,Shreya Senthil , Swetha Tripuramallu ,
 * Aadi Patni
 * @version April 11, 2021
 **/

public class MultipleChoice implements Question, Serializable {
    private int questionNumber;
    private int questionValue;
    private String question;
    private ArrayList<String> choices;
    private String answer;
    private boolean shuffle;

    public MultipleChoice(int questionNumber, int questionValue, String question, ArrayList<String> choices, String answer, boolean shuffle) {
        this.questionNumber = questionNumber;
        this.questionValue = questionValue;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.shuffle = shuffle;
    }

    public String toString() {
        if (shuffle) {
            Collections.shuffle(choices);
            return String.format("Multiple Choice:%n%s%n"
                    + "Choices: %s%n", question, choices);
        } else {
            return String.format("Multiple Choice:%n%s%n"
                    + "Choices: %s%n", question, choices);
        }
    }

    public int getQuestionNumber() {
        return this.questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public int getQuestionValue() {
        return questionValue;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public String getQuestion() {
        return question;
    }
}