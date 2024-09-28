package screens;

import constants.CommonConstants;
import database.Answer;
import database.Category;
import database.JDBC;
import database.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizScreenGui extends JFrame implements ActionListener {
    private JLabel scoreLabel;
    private JTextArea questionTextArea;
    private JButton[] answerButtons;
    private JButton nextButton;

    // current quiz category
    private Category category;

    // question based on category
    private ArrayList<Question> questions;
    private Question currentQuestion;
    private int currentQuestionNumber;
    private int numOfQuestions;
    private int score;
    private boolean firstChoiceMade;

    public QuizScreenGui(Category category, int numOfQuestions) {
        super("Quiz Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make the window full screen
        setUndecorated(false); // Show title bar
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY); // Change background to dark gray

        answerButtons = new JButton[4];
        this.category = category;

        // Load the questions based on category
        questions = JDBC.getQuestions(category);

        // Adjust number of questions to choose the min between the user's input and the total number of
        // questions in the database
        this.numOfQuestions = Math.min(numOfQuestions, questions.size());

        // Load the answers for each question
        for (Question question : questions) {
            ArrayList<Answer> answers = JDBC.getAnswers(question);
            question.setAnswers(answers);
        }

        // Load current question
        currentQuestion = questions.get(currentQuestionNumber);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // Topic label
        JLabel topicLabel = new JLabel("Topic: " + category.getCategoryName());
        topicLabel.setFont(new Font("Arial", Font.BOLD, 28));
        topicLabel.setBounds(15, 15, 350, 30);
        topicLabel.setForeground(Color.YELLOW); // Keeping it bright
        add(topicLabel);

        // Score label
        scoreLabel = new JLabel("Score: " + score + "/" + numOfQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setBounds(500, 15, 250, 30);
        scoreLabel.setForeground(Color.YELLOW); // Keeping it bright
        add(scoreLabel);

        // Question text area
        questionTextArea = new JTextArea(currentQuestion.getQuestionText());
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 28));
        questionTextArea.setBounds(15, 60, 800, 120);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setForeground(Color.BLACK); // Change question text to black
        questionTextArea.setBackground(Color.LIGHT_GRAY); // Light gray background for better contrast
        add(questionTextArea);

        addAnswerComponents();

        // Return to title button
        JButton returnToTitleButton = new JButton("Return to Title");
        returnToTitleButton.setFont(new Font("Arial", Font.BOLD, 18));
        returnToTitleButton.setBounds(15, 420, 150, 35);
        returnToTitleButton.setBackground(Color.YELLOW);
        returnToTitleButton.setForeground(Color.BLACK);
        returnToTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load title screen
                TitleScreenGui titleScreenGui = new TitleScreenGui();
                titleScreenGui.setLocationRelativeTo(QuizScreenGui.this);

                // Dispose of this screen
                QuizScreenGui.this.dispose();

                // Display title screen
                titleScreenGui.setVisible(true);
            }
        });
        add(returnToTitleButton);

        // Next button
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBounds(750, 470, 100, 35);
        nextButton.setBackground(Color.YELLOW);
        nextButton.setForeground(Color.BLACK);
        nextButton.setVisible(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide next button
                nextButton.setVisible(false);

                // Reset first choice flag
                firstChoiceMade = false;

                // Update current question to the next question
                currentQuestion = questions.get(++currentQuestionNumber);
                questionTextArea.setText(currentQuestion.getQuestionText());

                // Reset and update the answer buttons
                for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
                    Answer answer = currentQuestion.getAnswers().get(i);

                    // Reset background color for button
                    answerButtons[i].setBackground(Color.WHITE);

                    // Update answer text
                    answerButtons[i].setText(answer.getAnswerText());
                }
            }
        });
        add(nextButton);
    }

    private void addAnswerComponents() {
        // Apply a 60px vertical space between each answer button
        int verticalSpacing = 60;

        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            Answer answer = currentQuestion.getAnswers().get(i);

            JButton answerButton = new JButton(answer.getAnswerText());
            answerButton.setBounds(60, 180 + (i * verticalSpacing), 262, 45);
            answerButton.setFont(new Font("Arial", Font.BOLD, 18));
            answerButton.setHorizontalAlignment(SwingConstants.LEFT);
            answerButton.setBackground(Color.WHITE);
            answerButton.setForeground(Color.BLUE); // Change option text to blue
            answerButton.addActionListener(this);
            answerButtons[i] = answerButton;
            add(answerButtons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton answerButton = (JButton) e.getSource();

        // Find correct answer
        Answer correctAnswer = null;
        for (Answer answer : currentQuestion.getAnswers()) {
            if (answer.isCorrect()) {
                correctAnswer = answer;
                break;
            }
        }

        if (answerButton.getText().equals(correctAnswer.getAnswerText())) {
            // User chose the right answer

            // Change button to green
            answerButton.setBackground(CommonConstants.LIGHT_GREEN);

            // Increase score only if it was the first choice
            if (!firstChoiceMade) {
                scoreLabel.setText("Score: " + (++score) + "/" + numOfQuestions);
            }

            // Check to see if it was the last question
            if (currentQuestionNumber == numOfQuestions - 1) {
                // Display final results
                JOptionPane.showMessageDialog(QuizScreenGui.this,
                        "Your final score is " + score + "/" + numOfQuestions);
            } else {
                // Make next button visible
                nextButton.setVisible(true);
            }
        } else {
            // Make button red to indicate incorrect choice
            answerButton.setBackground(CommonConstants.LIGHT_RED);
        }

        firstChoiceMade = true;
    }
}
