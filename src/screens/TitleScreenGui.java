package screens;

import constants.CommonConstants;
import database.Category;
import database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TitleScreenGui extends JFrame {
    private JComboBox<String> categoriesMenu;
    private JTextField numOfQuestionsTextField;

    public TitleScreenGui() {
        // Call the constructor of the superclass with the title of "Title Screen"
        super("Title Screen");

        // Set the size of the JFrame to 400 pixels wide and 565 pixels tall
        setSize(400, 565);

        // Set the layout manager of the frame to null, allowing manual positioning of the components
        setLayout(null);

        // Set the frame to be centered on the screen when displayed
        setLocationRelativeTo(null);

        // Disable resizing of the frame by the user
        setResizable(false);

        // Set the default close operation of the frame to exit after the application has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Change the background color to dark grey
        getContentPane().setBackground(Color.DARK_GRAY);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // Title label
        JLabel titleLabel = new JLabel("Quiz Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0, 20, 390, 43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK); // Change text color to black
        add(titleLabel);

        // Choose category label
        JLabel chooseCategoryLabel = new JLabel("Choose a Category");
        chooseCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCategoryLabel.setBounds(0, 90, 400, 20);
        chooseCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCategoryLabel.setForeground(Color.BLACK); // Change text color to black
        add(chooseCategoryLabel);

        // Category drop-down menu
        ArrayList<String> categoryList = JDBC.getCategories();
        categoriesMenu = new JComboBox<>(categoryList.toArray(new String[0]));
        categoriesMenu.setBounds(20, 120, 337, 45);
        categoriesMenu.setForeground(Color.BLACK); // Change text color to black
        categoriesMenu.setBackground(Color.LIGHT_GRAY); // Set a lighter background for contrast
        add(categoriesMenu);

        // Number of questions label
        JLabel numOfQuestionsLabel = new JLabel("Number of Questions: ");
        numOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsLabel.setBounds(20, 190, 172, 20);
        numOfQuestionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionsLabel.setForeground(Color.BLACK); // Change text color to black
        add(numOfQuestionsLabel);

        // Number of questions text input field
        numOfQuestionsTextField = new JTextField("10");
        numOfQuestionsTextField.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsTextField.setBounds(200, 190, 148, 26);
        numOfQuestionsTextField.setForeground(Color.BLACK); // Change text color to black
        numOfQuestionsTextField.setBackground(Color.LIGHT_GRAY); // Set a lighter background for contrast
        add(numOfQuestionsTextField);

        // Start button
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(65, 290, 262, 45);
        startButton.setBackground(Color.YELLOW);
        startButton.setForeground(Color.BLACK); // Change text color to black
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    // Retrieve category
                    Category category = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());

                    // Invalid category
                    if (category == null) return;

                    int numOfQuestions = Integer.parseInt(numOfQuestionsTextField.getText());

                    // Load quiz screen
                    QuizScreenGui quizScreenGui = new QuizScreenGui(category, numOfQuestions);
                    quizScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                    // Dispose of this screen
                    TitleScreenGui.this.dispose();

                    // Display quiz screen
                    quizScreenGui.setVisible(true);
                }
            }
        });
        add(startButton);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBounds(65, 350, 262, 45);
        exitButton.setBackground(Color.YELLOW);
        exitButton.setForeground(Color.BLACK); // Change text color to black
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose of this screen
                TitleScreenGui.this.dispose();
            }
        });
        add(exitButton);

        // Create question button
        JButton createAQuestionButton = new JButton("Create a Question");
        createAQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAQuestionButton.setBounds(65, 420, 262, 45);
        createAQuestionButton.setBackground(Color.YELLOW);
        createAQuestionButton.setForeground(Color.BLACK); // Change text color to black
        createAQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create question screen GUI
                CreateQuestionScreenGui createQuestionScreenGui = new CreateQuestionScreenGui();
                createQuestionScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                // Dispose of this title screen
                TitleScreenGui.this.dispose();

                // Display create a question screen GUI
                createQuestionScreenGui.setVisible(true);
            }
        });
        add(createAQuestionButton);
    }

    // True - valid input; false - invalid input
    private boolean validateInput() {
        // Number of questions field must not be empty
        if (numOfQuestionsTextField.getText().replaceAll(" ", "").length() <= 0) return false;

        // No category is chosen
        if (categoriesMenu.getSelectedItem() == null) return false;

        return true;
    }
}
