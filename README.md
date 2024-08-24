# Computer-Science-Mini-Project
# README
## Instructions
  - compile using: javac *.java
  - run server using: java PurdueLMSServer
  - open new tab
  - run server using: java PurdueLMSClient
## Class Descriptions
  ### PurdueLMSServer
   - Threaded class that allows the client to perform actions on the server-maintained account object
  ### PurdueLMSClient
   - Threaded class that begins the GUI pane sequence
  ### StudentLandingPane.java
   - EDT class that invokes the Account, Quiz, and Submission panes for any Student
  ### StudentAccountPane.java
   - EDT class that allows students to communicate with the server-maintained account object to create, edit, delete their accounts, and modify their passwords and usernames.
  ### StudentQuizLandingPane.java
   - EDT class that allows students to communicate with the server-maintained account object to view available quizzes
  ### StudentQuizPane.java
   - EDT class that allows students to communicate with the server-maintained account object to take quizzes
  ### StudentSubmissionPane.java
   - EDT class that students to communicate with the server-maintained account object to take quizzes
  ### TeacherLandingPane.java
   - EDT class that invokes the Account, Quiz, and Grading panes for any Teacher
  ### TeacherAccountPane.java
   - EDT class that allows teachers to communicate with the server-maintained account object to create, edit, delete their accounts, and modify their passwords and usernames.
  ### TeacherQuizPane.java
   - EDT class that teachers to communicate with the server-maintained account object to create, edit, and delete quizzes.
  ### TeacherGradingPane.java
   - EDT class that allows teachers to communicate with the server-maintained account object to regrade auto-graded quiz submissions.
  ### Account.java
   - Class used to:
     - initialize primary memory from secondary memory text files: StudentList.txt, TeacherList.txt, and Submissions.Txt
     - create, edit and delete both student and teacher accounts by reading and writing to secondary memory
     - add submission to secondary memory upon completion of quiz by student or regrade by teacher.
  ### Quiz.java
   - Class used to:
     - allow students to take randomized quizzes generated from Question and it's implementations
     - allow teachers to create, modify, and edit quizzes (in minor conjunction with Account)
  ### Question.java
   - Class used to organize question types for all quizzes in the PurdueLMS
  ### MultipleChoice.java
   - Class used to catalog multiple choice quiz questions
  ### TrueFalse.java
   - Class used to catalog multiple true/false questions
  ### Fill.java
   - Class used to catalog fill in the blank questions
  ### Quizzes.txt
   - Secondary memory for quizzes
  ### Submissions.txt
   - Secondary memory for submissions
  ### StudentList.txt
   - Secondary memory for student database
  ### TeacherList.txt
   - Secondary memory for teacher database
 ## Testing
   - Test Cases
   - Test 1: User Login
     1) User launches application
     2) Type in username and password
     3) Click Login
   - Expected Result: If username is in the database then the program should take you to the student landing
   - pane. If usernames is not in database then the program will show an error message saying “Incorrect
   - password or username”.
   - Test Status: Passed
   - Test 2: User Deletes Account
     1) User launches application
     2) Type in username and password
     3) Click Login
     4) Click Account Settings
     5) Click “Delete Account”
   - Expected Results: User has successfully logged in and the if you look at the student list text file the
   - account with that username and password will not be there.
   - Test Status: Passed.
   - Test 3: User Changes Account
     1) User launches application
     2) Type in username and password
     3) Click Login
     4) Click Account Settings
     5) Change username and password, or only username, or only password
     6) Click “Submit Changes”
   - Expected Results: Once user successfully logs in and changed password, username or both, then go to
   - the student list text file and the student username and password fill be changed in the exact same spot
   - in the list of usernames and passwords on the student list text file.
   - Test Status: Passed.
   - Test 4: Take Quiz
     1) User launches application
     2) Type in username and password
     3) Click Login
     4) Click the “Take Quiz” Button
     5) Click on any quiz button you would like to take
     6) Answer questions, for any question select answer or type in answer and click enter to move to
   - next question
   - Expected Result: You should reach to pane that says you have finished the quiz.
   - Test Status: Passed.
   - Test 5: View Submissions
     1) User launches application
     2) Type in username and password
     3) Click Login
     4) Click the “Take Quiz” Button
     5) Click on any quiz button you would like to take
     6) Answer questions, for any question select answer or type in answer and click enter to move to next question
     7) Return to Dashboard
     8) Click View Submissions
   - Expected Results: You should be able to view the most recent submission
   - Test Status: Passed
