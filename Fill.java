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

public class Fill implements Question, Serializable {
    private int questionNumber;
    private int questionValue;
    private String question;
    private String answer;

    public Fill(int questionNumber, int questionValue, String question, String answer) {
        this.questionNumber = questionNumber;
        this.questionValue = questionValue;
        this.question = question;
        this.answer = answer;
    }

    public String toString() {
        return String.format("Fill in the blank:%n%s%n", question);
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

    public String getQuestion() {
        return question;
    }
}