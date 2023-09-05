import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Random;
/**
 * A class that constructs a game named Jordle.
 * @author Alexander Snapp
 * @version 1.0
 */
public class Jordle extends Application {
    private int numberOfLine = 0;
    private char[] eachValueInGrid = {'1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                                      '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                                      '1', '1', '1', '1', '1', '1', '1', '1', '1'};
    private Random rand = new Random();
    private int numForWord = rand.nextInt(Words.list.size());
    private String answer = new String(Words.list.get(numForWord));
    private boolean finished = false;
    private int positionInTheLine = 0;
    private int streak = 0;
    private int longestStreak = 0;
    private int timesPlayed = 0;
    private int totalWins = 0;
    private int totalGuesses = 0;
    /**
     * The main method that is used to launch the Jordle game.
     * @param args the String[] arguments of the main method
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The start method which is the main component in creating the Jordle game.
     * @param window the stage which the Jordle game is put on to
     */
    public void start(Stage window) {
        // Sets title on top of window
        window.setTitle("JORDLE");
        BorderPane bp = new BorderPane();
        VBox initial = gridHelper();
        HBox forName = new HBox();
        // Creates large "JORDLE" on top of page in black
        Text name = new Text();
        name.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
        name.setFill(Color.BLACK);
        name.setStroke(Color.BLACK);
        name.setStrokeWidth(1);
        name.setText("                JORDLE");
        forName.getChildren().add(name);
        forName.setPadding(new Insets(15));
        HBox left = new HBox();
        Text leftTxt = new Text("                         ");
        left.getChildren().add(leftTxt);
        bp.setLeft(left);
        bp.setTop(forName);
        bp.setCenter(initial);

        Button instructions = new Button("Instructions");
        instructions.setOnAction(e -> showInstructions()); //Create a method to display the instructions

        Button reset = new Button("reset");

        Button stats = new Button("Your Stats");
        stats.setOnAction(e -> showStats());

        StackPane forBottom = new StackPane();
        HBox bottomH = new HBox();
        VBox instrAndReset = new VBox();
        instrAndReset.setSpacing(10);
        instrAndReset.setPadding(new Insets(10));
        instrAndReset.getChildren().addAll(instructions, reset);

        Text bottomText = new Text();
        bottomText.setText("Try guessing a word!\n\nStreak: " + streak + "\nLongest Streak: " + longestStreak);
        bottomH.getChildren().addAll(instrAndReset, bottomText, stats);
        bottomH.setSpacing(43);
        forBottom.getChildren().addAll(bottomH);
        bp.setBottom(forBottom);
        stats.setAlignment(Pos.BOTTOM_CENTER);
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timesPlayed += 1;
                totalGuesses += numberOfLine - 1;
                numberOfLine = 0;
                positionInTheLine = 0;
                finished = false;
                for (int i = 0; i < eachValueInGrid.length; i++) {
                    eachValueInGrid[i] = '1';
                }
                numForWord = rand.nextInt(Words.list.size());
                answer = new String(Words.list.get(numForWord));
                VBox resetBoxes = gridHelper();
                BorderPane bp = new BorderPane();
                bp.setTop(forName);
                bp.setLeft(left);
                bp.setCenter(resetBoxes);
                bottomText.setText("Try guessing a word!\n\nStreak: " + streak + "\nLongest Streak: " + longestStreak);
                bp.setBottom(forBottom);
                Scene scene = new Scene(bp, 400, 500, Color.WHITE);
                window.setScene(scene);
                window.setResizable(false);
                window.show();

            }
        });

        Scene scene = new Scene(bp, 400, 500);
        window.setResizable(false);
        window.setScene(scene);
        window.show();

        EventHandler<KeyEvent> typeHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if (!finished) {
                    if (e.getCode() == KeyCode.ENTER) {
                        int position = numberOfLine * 5;
                        if ((eachValueInGrid[position] != '1') && (eachValueInGrid[position + 1] != '1')
                             && (eachValueInGrid[position + 2] != '1') && (eachValueInGrid[position + 3] != '1')
                             && (eachValueInGrid[position + 4] != '1')) {
                            String charAt1 = String.valueOf(eachValueInGrid[position]);
                            String charAt2 = String.valueOf(eachValueInGrid[position + 1]);
                            String charAt3 = String.valueOf(eachValueInGrid[position + 2]);
                            String charAt4 = String.valueOf(eachValueInGrid[position + 3]);
                            String charAt5 = String.valueOf(eachValueInGrid[position + 4]);
                            String totalGuess = new String(charAt1 + charAt2 + charAt3 + charAt4 + charAt5);
                            if (totalGuess.equals(answer)) {
                                numberOfLine += 1;
                                VBox newRender = gridHelper();
                                BorderPane bp = new BorderPane();
                                bp.setLeft(left);
                                bp.setTop(forName);
                                bp.setCenter(newRender);
                                streak += 1;
                                totalWins += 1;
                                bottomText.setText("Congrats!\nYou won in " + numberOfLine
                                                   + " guesses!\nStreak: " + streak + "\nLongest Streak: "
                                                   + longestStreak);
                                bp.setBottom(forBottom);

                                Scene scene = new Scene(bp, 400, 500);
                                window.setScene(scene);
                                window.setResizable(false);
                                window.show();
                                finished = true;  //end of code initializer
                            }
                            numberOfLine += 1;
                            positionInTheLine = 0;
                            if (6 <= numberOfLine && !finished) {
                                VBox newRender = gridHelper();
                                if (streak > longestStreak) {
                                    longestStreak = streak;
                                }
                                bottomText.setText("That is wrong!\nThe word was '" + answer
                                                   + "'.\nStreak: " + streak + "\nLongest Streak: " + longestStreak);
                                streak = 0;
                                numberOfLine += 1;
                                StackPane bottom = new StackPane();

                                BorderPane bp = new BorderPane();
                                bp.setLeft(left);
                                bp.setTop(forName);
                                bp.setCenter(newRender);
                                bp.setBottom(forBottom);

                                Scene scene = new Scene(bp, 400, 500);
                                window.setScene(scene);
                                window.setResizable(false);
                                window.show();
                                finished = true;  //end of code initializer
                            }
                            if (!finished) {
                                VBox newRender = gridHelper();

                                BorderPane bp = new BorderPane();
                                bp.setLeft(left);
                                bp.setTop(forName);
                                bp.setCenter(newRender);
                                bp.setBottom(forBottom);

                                Scene scene = new Scene(bp, 400, 500);
                                window.setScene(scene);
                            }
                        } else {
                            System.out.println("The word must be 5 characters long");
                            errorMessage();

                            position = numberOfLine * 5;
                            eachValueInGrid[position] = '1';
                            eachValueInGrid[position + 1] = '1';
                            eachValueInGrid[position + 2] = '1';
                            eachValueInGrid[position + 3] = '1';
                            eachValueInGrid[position + 4] = '1';

                            positionInTheLine = 0;

                            VBox newRender = gridHelper();
                            BorderPane bp = new BorderPane();
                            bp.setLeft(left);
                            bp.setTop(forName);
                            bp.setCenter(newRender);
                            bp.setBottom(forBottom);

                            Scene scene = new Scene(bp, 400, 500);
                            window.setScene(scene);
                            window.setResizable(false);
                            window.show();
                        }
                    } else if (e.getCode() == KeyCode.BACK_SPACE) {
                        if (positionInTheLine != 0) {
                            int toRemove = numberOfLine * 5 + positionInTheLine - 1;
                            eachValueInGrid[toRemove] = '1';
                            positionInTheLine -= 1;
                        }
                        VBox newRender = gridHelper();
                        BorderPane bp = new BorderPane();
                        bp.setLeft(left);
                        bp.setTop(forName);
                        bp.setCenter(newRender);
                        bp.setBottom(forBottom);

                        Scene scene = new Scene(bp, 400, 500);
                        window.setScene(scene);
                        window.setResizable(false);
                        window.show();
                    } else {
                        if (positionInTheLine <= 4) {
                            KeyCode typed = e.getCode();
                            String theChar = typed.getChar().toLowerCase();
                            char letter = theChar.charAt(0);
                            String alphabet = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
                            if (alphabet.contains(String.valueOf(letter))) {
                                int toMove = numberOfLine * 5 + positionInTheLine;
                                eachValueInGrid[toMove] = letter;
                                positionInTheLine += 1;
                            }
                        }
                        VBox newRender = gridHelper();
                        BorderPane bp = new BorderPane();
                        bp.setLeft(left);
                        bp.setTop(forName);
                        bp.setCenter(newRender);
                        bp.setBottom(forBottom);

                        Scene scene = new Scene(bp, 400, 500);
                        window.setScene(scene);
                        window.setResizable(false);
                        window.show();
                    }
                }
            }
        };
        window.addEventFilter(KeyEvent.KEY_PRESSED, typeHandler);
        initial.requestFocus();
    }
    /**
     * A method that launches a new window that contains the instructions for
     * the Jordle game on it.
     */
    public void showInstructions() {
        Stage instructionsWindow = new Stage();
        instructionsWindow.setTitle("Instructions");
        Text instructionsTXT = new Text();
        instructionsTXT.setText("Hello and welcome to JORDLE! In this game a word has been randomly chosen "
                             + "and it is up to find out what it is! To play, type in a five letter word "
                             + "of your choosing and press enter. If the letter in the your guess is in the same "
                             + "location as that of the letter in the random word, the box will turn green. If "
                             + "your letter is in the word but not in the right spot the box will turn yellow, "
                             + "and if the letter is not in the word at all the box will turn gray. You only "
                             + "have six tries to get the word so be strategic! \n\nBest of luck, \nAlexander Snapp");
        instructionsTXT.setWrappingWidth(200);
        StackPane instrPane = new StackPane();
        instrPane.getChildren().add(instructionsTXT);
        Scene forInstructions = new Scene(instrPane, 250, 270);
        instructionsWindow.setScene(forInstructions);
        instructionsWindow.setResizable(false);
        instructionsWindow.show();
    }
    /**
     * A method that launches a new window which displays the stats of the
     * person playing the game.
     */
    public void showStats() {
        Stage statsWindow = new Stage();
        statsWindow.setTitle("Your Stats");
        Text statsTXT = new Text();
        double avgGuesses;
        if (timesPlayed >= 1) {
            avgGuesses = totalGuesses / timesPlayed;
        } else {
            avgGuesses = totalGuesses;
        }
        statsTXT.setText("Your Stats:\n\nLongest Streak: " + longestStreak
                         + "\nTimes Played: " + timesPlayed + "\nTotal Wins: "
                         + totalWins + "\nAverage Guesses Per Attempt: "
                         + String.format("%.2f", avgGuesses));
        statsTXT.setWrappingWidth(200);
        StackPane statsPane = new StackPane();
        statsPane.getChildren().add(statsTXT);
        Scene forStats = new Scene(statsPane, 250, 270);
        statsWindow.setScene(forStats);
        statsWindow.setResizable(false);
        statsWindow.show();
    }
    /**
     * A helper method for the gridHelper() method which re-renders the white rectangles.
     * @param letter the letter passed in to the box
     * @param i the index of the letter
     * @return a StackPane containing a rectangle object
     */
    public StackPane blankBox(char letter, int i) {
        StackPane sPforBlank = new StackPane();
        Rectangle r1 = new Rectangle();
        int recSize = 40;
        r1.setWidth(recSize);
        r1.setHeight(recSize);
        r1.setFill(Color.WHITE);
        r1.setStroke(Color.BLACK);
        Text blankText = new Text();
        if (letter != '1') {
            blankText.setText(String.valueOf(letter).toUpperCase());
        } else {
            blankText.setText(" ");
        }
        sPforBlank.getChildren().addAll(r1, blankText);
        return sPforBlank;
    }
    /**
     * A helper method for the gridHelper() method that sets boxes with colors.
     * @param theAnswer the answer the guess is being compared to.
     * @param letter the passed in letter
     * @param i the index i of the letter
     * @return a StackPane with a correctly colored box
     */
    public StackPane checkedCorrect(String theAnswer, char letter, int i) {
        StackPane sPlocal1 = new StackPane();
        int recSize = 40;
        Rectangle r1 = new Rectangle();
        r1.setHeight(recSize);
        r1.setWidth(recSize);
        r1.setFill(Color.WHITE);
        r1.setStroke(Color.BLACK);
        if (letter == '1') {
            r1.setFill(Color.GHOSTWHITE);
        } else if (theAnswer.charAt(i) == letter) {
            r1.setFill(Color.LIGHTGREEN);
        } else if (theAnswer.contains(String.valueOf(letter))) {
            r1.setFill(Color.YELLOW);
        } else {
            r1.setFill(Color.GREY);
        }
        Text letterDisplay = new Text();
        if (letter != '1') {
            letterDisplay.setText(String.valueOf(letter).toUpperCase());
        } else {
            letterDisplay.setText(" ");
        }
        sPlocal1.getChildren().addAll(r1, letterDisplay);
        return sPlocal1;
    }
    /**
     * Creates the grid of rectangles in which the first number of each spotxx variable
     * represents the row and the second number represents the column.
     * @return a vbox that contains hboxes which make up the Jordle grid
     */
    public VBox gridHelper() {
        //Constructs the first row in the grid
        StackPane spot11, spot12, spot13, spot14, spot15;
        HBox hb1 = new HBox();
        if (numberOfLine != 0) {
            spot11 = checkedCorrect(answer, eachValueInGrid[0], 0);
            spot12 = checkedCorrect(answer, eachValueInGrid[1], 1);
            spot13 = checkedCorrect(answer, eachValueInGrid[2], 2);
            spot14 = checkedCorrect(answer, eachValueInGrid[3], 3);
            spot15 = checkedCorrect(answer, eachValueInGrid[4], 4);
        } else {
            spot11 = blankBox(eachValueInGrid[0], 0);
            spot12 = blankBox(eachValueInGrid[1], 1);
            spot13 = blankBox(eachValueInGrid[2], 2);
            spot14 = blankBox(eachValueInGrid[3], 3);
            spot15 = blankBox(eachValueInGrid[4], 4);
        }
        hb1.getChildren().addAll(spot11, spot12, spot13, spot14, spot15);
        hb1.setSpacing(15);
        //Constructs the second row
        StackPane spot21, spot22, spot23, spot24, spot25;
        HBox hb2 = new HBox();
        if (numberOfLine > 1) {
            spot21 = checkedCorrect(answer, eachValueInGrid[5], 0);
            spot22 = checkedCorrect(answer, eachValueInGrid[6], 1);
            spot23 = checkedCorrect(answer, eachValueInGrid[7], 2);
            spot24 = checkedCorrect(answer, eachValueInGrid[8], 3);
            spot25 = checkedCorrect(answer, eachValueInGrid[9], 4);
        } else {
            spot21 = blankBox(eachValueInGrid[5], 0);
            spot22 = blankBox(eachValueInGrid[6], 1);
            spot23 = blankBox(eachValueInGrid[7], 2);
            spot24 = blankBox(eachValueInGrid[8], 3);
            spot25 = blankBox(eachValueInGrid[9], 4);
        }
        hb2.getChildren().addAll(spot21, spot22, spot23, spot24, spot25);
        hb2.setSpacing(15);
        //Creates the third row
        StackPane spot31, spot32, spot33, spot34, spot35;
        HBox hb3 = new HBox();
        if (numberOfLine > 2) {
            spot31 = checkedCorrect(answer, eachValueInGrid[10], 0);
            spot32 = checkedCorrect(answer, eachValueInGrid[11], 1);
            spot33 = checkedCorrect(answer, eachValueInGrid[12], 2);
            spot34 = checkedCorrect(answer, eachValueInGrid[13], 3);
            spot35 = checkedCorrect(answer, eachValueInGrid[14], 4);
        } else {
            spot31 = blankBox(eachValueInGrid[10], 0);
            spot32 = blankBox(eachValueInGrid[11], 1);
            spot33 = blankBox(eachValueInGrid[12], 2);
            spot34 = blankBox(eachValueInGrid[13], 3);
            spot35 = blankBox(eachValueInGrid[14], 4);
        }
        hb3.getChildren().addAll(spot31, spot32, spot33, spot34, spot35);
        hb3.setSpacing(15);
        //Creates the fourth row
        StackPane spot41, spot42, spot43, spot44, spot45;
        HBox hb4 = new HBox();
        if (numberOfLine > 3) {
            spot41 = checkedCorrect(answer, eachValueInGrid[15], 0);
            spot42 = checkedCorrect(answer, eachValueInGrid[16], 1);
            spot43 = checkedCorrect(answer, eachValueInGrid[17], 2);
            spot44 = checkedCorrect(answer, eachValueInGrid[18], 3);
            spot45 = checkedCorrect(answer, eachValueInGrid[19], 4);
        } else {
            spot41 = blankBox(eachValueInGrid[15], 0);
            spot42 = blankBox(eachValueInGrid[16], 1);
            spot43 = blankBox(eachValueInGrid[17], 2);
            spot44 = blankBox(eachValueInGrid[18], 3);
            spot45 = blankBox(eachValueInGrid[19], 4);
        }
        hb4.getChildren().addAll(spot41, spot42, spot43, spot44, spot45);
        hb4.setSpacing(15);
        //Creates fifth row
        StackPane spot51, spot52, spot53, spot54, spot55;
        HBox hb5 = new HBox();
        if (numberOfLine > 4) {
            spot51 = checkedCorrect(answer, eachValueInGrid[20], 0);
            spot52 = checkedCorrect(answer, eachValueInGrid[21], 1);
            spot53 = checkedCorrect(answer, eachValueInGrid[22], 2);
            spot54 = checkedCorrect(answer, eachValueInGrid[23], 3);
            spot55 = checkedCorrect(answer, eachValueInGrid[24], 4);
        } else {
            spot51 = blankBox(eachValueInGrid[20], 0);
            spot52 = blankBox(eachValueInGrid[21], 1);
            spot53 = blankBox(eachValueInGrid[22], 2);
            spot54 = blankBox(eachValueInGrid[23], 3);
            spot55 = blankBox(eachValueInGrid[24], 4);
        }
        hb5.getChildren().addAll(spot51, spot52, spot53, spot54, spot55);
        hb5.setSpacing(15);
        //Creates sixth row
        StackPane spot61, spot62, spot63, spot64, spot65;
        HBox hb6 = new HBox();
        if (numberOfLine > 5) {
            spot61 = checkedCorrect(answer, eachValueInGrid[25], 0);
            spot62 = checkedCorrect(answer, eachValueInGrid[26], 1);
            spot63 = checkedCorrect(answer, eachValueInGrid[27], 2);
            spot64 = checkedCorrect(answer, eachValueInGrid[28], 3);
            spot65 = checkedCorrect(answer, eachValueInGrid[29], 4);
        } else {
            spot61 = blankBox(eachValueInGrid[25], 0);
            spot62 = blankBox(eachValueInGrid[26], 1);
            spot63 = blankBox(eachValueInGrid[27], 2);
            spot64 = blankBox(eachValueInGrid[28], 3);
            spot65 = blankBox(eachValueInGrid[29], 4);
        }
        hb6.getChildren().addAll(spot61, spot62, spot63, spot64, spot65);
        hb6.setSpacing(15);

        VBox putTogether = new VBox();
        putTogether.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6);
        putTogether.setSpacing(15);
        return putTogether;
    }
    /**
     * Displays a new alert that shows an error when the given word is less
     * than 5 characters.
     */
    public void errorMessage() {
        Alert newAlert = new Alert(Alert.AlertType.ERROR, "Must enter a word with 5 letters");
        newAlert.show();
    }
}