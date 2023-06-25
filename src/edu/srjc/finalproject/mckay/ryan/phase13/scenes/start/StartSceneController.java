/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    The is the starting scene controller. The scene
    this controller is displayed either on a fresh
    start of the program, or when the restart Phase 13
    button is pressed. On this scene the user
    enters the date that they would like to begin
    the Phase 13 program on. Any date may be chosen,
    today's date to see how the program would function
    in a normal use. Far in the past so all challenges
    can be accessed for further testing. Or in to the future
    as well. Entering dates at any of the times provide
    versatile usage for a normal user, and allow more in
    depth testing abilities.
*/
package edu.srjc.finalproject.mckay.ryan.phase13.scenes.start;

import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.FxmlLoadingErrorAlert;
import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseInsertError;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.phase.PhaseSceneController;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge.TextFieldChallenge;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.startover.StartOverSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.*;

public class StartSceneController implements Initializable
{
    private final static int FIRST_PHASE_START_DATE_IDX = 0;

    private final static int numOfDaysPerMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private final static int pushupsChallenges[][] = {{1, 50}, {3, 75}, {5, 100}, {7, 125}, {9, 150}, {11, 200}, {13, 300}};
    private final static int squatsChallenges[][] = {{2, 50}, {4, 75}, {6, 100}, {8, 125}, {10, 125}, {12, 200}, {13, 300}};
    private final static int prayerChallenges[][] = {{1, 30}, {2, 35}, {3, 40}, {4, 45}, {5, 45}, {6, 45}, {7, 45}, {8, 60},
            {9, 60}, {10, 60}, {11, 60}, {12, 60}, {13, 60}};
    private final static String singleCheckBoxChallenges[][] = {{"1", "Give someone a compliment."}, {"3", "Run a mile."},
            {"4", "Delete all the games off your phone."}, {"5", "Ask an adult not in your family what their testimony is."},
            {"8", "Ascend a 1000ft hill."}, {"10", "Share your testimony with someone outside of our church and your family."},
            {"12", "Ascend a 2000ft hill."}};
    private final static String otherChallenges[][] = {{"1", "Memorize John 3:16-18."}, {"2", "Drink only water for a week."},
            {"2", "Read the book of James."}, {"3", "Memorize Ephesians 4:32."}, {"4", "Memorize Luke 16:13."},
            {"6", "One week without added sugar."}, {"6", "Read the gospel of John."}, {"7", "Fast from social media for the week."},
            {"7", "Memorize Matthew 6:9-13."}, {"8", "Memorize Isaiah 40:28."}, {"9", "Memorize Romans 8:37-39."},
            {"11", "Memorize Ephesians chapter 6."}, {"12", "Memorize Revelation 3:20."}, {"13", "Memorize Romans chapter 12"}};

    private ArrayList<LocalDate> phaseStartDates = null;
    private int inYear = 0;
    private int inMonth = 0;
    private int inDay = 0;

    @FXML
    TextField monthInputTextField;
    @FXML
    TextField dayInputTextField;
    @FXML
    TextField yearInputTextField;
    @FXML
    Button enterDateButton;

    @FXML
    private void enterDateButtonPressed(ActionEvent event)
    {
        phaseStartDates = new ArrayList<>();

        if(dateInputIsValid())
        {
            Alert confirmDataAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Start Phase 13 on " + inMonth + "/" + inDay + "/" + inYear + "?",
                    ButtonType.YES, ButtonType.NO);
            confirmDataAlert.setTitle("Just checking!");
            confirmDataAlert.setHeaderText(null);
            confirmDataAlert.showAndWait();
            if (confirmDataAlert.getResult() == ButtonType.YES)
            {
                currentPhase = 0;
                phase13IsCompleted = false;
                phaseStartDates.clear();
                phases.clear();

                createPhaseStartDates();
                clearDatabase();
                insertStartingDatabaseData();
                loadAndSetProperScene();

                monthInputTextField.clear();
                dayInputTextField.clear();
                yearInputTextField.clear();
            }
        }
    }





    private boolean dateInputIsValid()
    {
        boolean isValid = false;

        try
        {
            inYear = Integer.parseInt(yearInputTextField.getText());
            inMonth = Integer.parseInt(monthInputTextField.getText());
            inDay = Integer.parseInt(dayInputTextField.getText());
            if(inMonth > 0 && inMonth <= 12)
            {
                if(inYear >= 0)
                {
                    if (inDay <= numOfDaysPerMonth[inMonth - 1]) {
                        isValid = true;
                    } else {
                        if (inMonth == 2) {
                            if (((inYear % 4 == 0) && (inYear % 100 != 0)) ||
                                    ((inYear % 4 == 0) && (inYear % 100 == 0) && (inYear % 400 == 0))) {
                                if (inDay == 29) {
                                    isValid = true;
                                }
                            }
                        }
                    }
                }
            }

            if(isValid == false)
            {
                throw new InvalidParameterException();
            }
        }
        catch(Exception e)
        {
            Alert invalidDateAlert = new Alert(Alert.AlertType.WARNING, "Please enter a valid date.", ButtonType.OK);
            invalidDateAlert.setTitle("Whoops!");
            invalidDateAlert.setHeaderText(null);
            invalidDateAlert.showAndWait();
        }

        return isValid;
    }

    private void createPhaseStartDates()
    {
        for(int index = 0; index < NUM_OF_PHASES; index++)
        {
            if(index == 0)
            {
                phaseStartDates.add(LocalDate.of(inYear, inMonth, inDay));
            }
            else
            {
                phaseStartDates.add(phaseStartDates.get(index - 1).plusWeeks(1));
            }
        }
    }

    private void clearDatabase()
    {
        try(Connection connection = DriverManager.getConnection(DATABASE_URL))
        {
            Statement databaseStatement = connection.createStatement();
            String sqlStatement = "DELETE FROM PhaseCommonsTable";
            databaseStatement.execute(sqlStatement);
            sqlStatement = "DELETE FROM TextFieldChallengesTable";
            databaseStatement.execute(sqlStatement);
            sqlStatement = "DELETE FROM SingleCheckBoxChallengesTable";
            databaseStatement.execute(sqlStatement);
        }
        catch (Exception exception)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }
    }

    private void insertStartingDatabaseData()
    {
        try(Connection connection = DriverManager.getConnection(DATABASE_URL))
        {
            Statement databaseStatement = connection.createStatement();
            connection.setAutoCommit(false);

            String sqlStatement = null;

            for(int index = 0; index < NUM_OF_PHASES; index++)
            {
                sqlStatement = "INSERT INTO PhaseCommonsTable VALUES ('"
                        + (index + 1) + "', '" + phaseStartDates.get(index).toString() + "', '0')";
                databaseStatement.addBatch(sqlStatement);
            }

            for(int index = 0; index < pushupsChallenges.length; index ++)
            {
                sqlStatement = "INSERT INTO TextFieldChallengesTable VALUES ('"
                        + pushupsChallenges[index][0] + "', '" + TextFieldChallenge.TextFieldChallengeTypes.PUSHUPS
                        + "', '0', '" + pushupsChallenges[index][1] + "')";
                databaseStatement.addBatch(sqlStatement);
            }

            for(int index = 0; index < squatsChallenges.length; index ++)
            {
                sqlStatement = "INSERT INTO TextFieldChallengesTable VALUES ('"
                        + squatsChallenges[index][0] + "', '" + TextFieldChallenge.TextFieldChallengeTypes.SQUATS
                        + "', '0', '" + squatsChallenges[index][1] + "')";
                databaseStatement.addBatch(sqlStatement);
            }

            for(int index = 0; index < prayerChallenges.length; index ++)
            {
                sqlStatement = "INSERT INTO TextFieldChallengesTable VALUES ('"
                        + prayerChallenges[index][0] + "', '" + TextFieldChallenge.TextFieldChallengeTypes.PRAYER
                        + "', '0', '" + prayerChallenges[index][1] + "')";
                databaseStatement.addBatch(sqlStatement);
            }

            for(int index = 0; index < singleCheckBoxChallenges.length; index++)
            {
                sqlStatement = "INSERT INTO SingleCheckBoxChallengesTable VALUES ('"
                        + singleCheckBoxChallenges[index][0] + "', '" + singleCheckBoxChallenges[index][1]
                        + "', '0')";
                databaseStatement.addBatch(sqlStatement);
            }

            for(int index = 0; index < otherChallenges.length; index++)
            {
                sqlStatement = "INSERT INTO SingleCheckBoxChallengesTable VALUES ('"
                        + otherChallenges[index][0] + "', '" + otherChallenges[index][1]
                        + "', '0')";
                databaseStatement.addBatch(sqlStatement);
            }

            databaseStatement.executeBatch();
            try
            {
                connection.commit();
                connection.close();
            }
            catch (Exception exception)
            {
                connection.rollback();
                DatabaseInsertError databaseInsertError = new DatabaseInsertError();
            }
        }
        catch (Exception exception)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }
    }

    private void loadAndSetProperScene()
    {
        LocalDate currentDate = LocalDate.now();

        try
        {
            phaseParent = FXMLLoader.load(PhaseSceneController.class.getResource("PhaseScene.fxml"));
            phaseScene = new Scene(phaseParent);
        }
        catch (Exception e)
        {
            FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("PhaseScene.fxml");
        }

        if (currentDate.compareTo(phaseStartDates.get(FIRST_PHASE_START_DATE_IDX)) < 0)
        {
            try
            {
                firstPhaseUnlocked = false;
                startOverParent = FXMLLoader.load(StartOverSceneController.class.getResource("StartOverScene.fxml"));
                startOverScene = new Scene(startOverParent);
                mainStage.setScene(startOverScene);
            }
            catch (Exception exception)
            {
                FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("StartOverScene.fxml");
            }
        }
        else
        {
            mainStage.setScene(phaseScene);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }
}
