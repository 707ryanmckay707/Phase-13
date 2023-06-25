/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    This is the Phase scene controller. It controllers
    the scene that user will spend the majority of
    their time. Here they can view their current Phase's
    challenges, as well as view and enter in their progress.
    They can also return to previous phases and view what they
    have completed. The user can only continue to the
    next phase after completing the current Phase and
    waiting until the next start date. All information
    the user enters is stored into a database, and all
    this data is retrieved on startup so the user can
    continue where they left off.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.scenes.phase;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.FxmlLoadingErrorAlert;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge.SingleCheckBoxChallenge;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge.TextFieldChallenge;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.Phase;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.startover.StartOverSceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.*;

public class PhaseSceneController implements Initializable
{
    @FXML
    Label phaseNumLabel;
    @FXML
    GridPane mainGridPane;

    @FXML
    public void previousSceneButtonPressed(ActionEvent e)
    {
        if (currentPhase == 0)
        {
            try
            {
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
            clearMainGridPane();
            currentPhase -= 1;
            updateMainGridPane();
        }
    }


    @FXML
    public void nextSceneButtonPressed(ActionEvent actionEvent)
    {
        if (phases.get(currentPhase).phaseIsCompleted())
        {
            nextScene();
            return;
        }

        // A user completes a phase after hitting next,
        // as long as all of the challenges for that phase are completed
        // and if that phase's week has ended

        // Get the number of challenges completed in the current phase
        int numOfChallengesCompleted = 0;
        for (int challengeNumber = 0; challengeNumber < phases.get(currentPhase).getPhaseChallenges().size(); challengeNumber++)
        {
            if (phases.get(currentPhase).getPhaseChallenges().get(challengeNumber).challengeIsCompleted())
            {
                numOfChallengesCompleted++;
            }
        }

        // If all of the challenges in the current phase are completed
        if (numOfChallengesCompleted == phases.get(currentPhase).getPhaseChallenges().size())
        {
            LocalDate currentDate = LocalDate.now();
            // And, if we are past or at the next phase start date (Or if we just finished the final phase)
            if (currentPhase == 12 || (currentDate.compareTo(phases.get(currentPhase + 1).getPhaseStartDate()) >= 0))
            {
                phases.get(currentPhase).setPhaseIsCompleted(true);
                try (Connection connection = DriverManager.getConnection(DATABASE_URL))
                {
                    Statement databaseStatement = connection.createStatement();
                    String sqlStatement = "UPDATE PhaseCommonsTable SET phaseIsCompleted = 1 WHERE phaseNum = "
                            + (currentPhase + 1);
                    databaseStatement.execute(sqlStatement);
                }
                catch (Exception exception)
                {
                    DatabaseAccessError databaseAccessError = new DatabaseAccessError();
                }

                nextScene();
            }
            else
            {
                Alert nextPhaseUnlockDateAlert = new Alert(Alert.AlertType.INFORMATION,
                        "The next phase will unlock on "
                                + phases.get(currentPhase + 1).getPhaseStartDate().getMonthValue()
                                + "/" + phases.get(currentPhase + 1).getPhaseStartDate().getDayOfMonth()
                                + "/" + phases.get(currentPhase + 1).getPhaseStartDate().getYear() + ".",
                        ButtonType.OK);
                nextPhaseUnlockDateAlert.setTitle("Almost!");
                nextPhaseUnlockDateAlert.setHeaderText(null);
                nextPhaseUnlockDateAlert.showAndWait();
            }
        }
        else
        {
            Alert phaseNotCompletedAlert = new Alert(Alert.AlertType.WARNING,
                    "Please finish the current phase before continuing on.", ButtonType.OK);
            phaseNotCompletedAlert.setTitle("Not Yet!");
            phaseNotCompletedAlert.setHeaderText(null);
            phaseNotCompletedAlert.showAndWait();
        }
    }

    private void nextScene()
    {
        if (currentPhase < (NUM_OF_PHASES - 1))
        {
            clearMainGridPane();
            currentPhase += 1;
            updateMainGridPane();
        }
        else if (currentPhase == (NUM_OF_PHASES - 1))
        {
            phase13IsCompleted = true;
            try
            {
                startOverParent = FXMLLoader.load(StartOverSceneController.class.getResource("StartOverScene.fxml"));
                startOverScene = new Scene(startOverParent);
                mainStage.setScene(startOverScene);
            }
            catch (Exception exception)
            {
                FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("StartOverScene.fxml");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        retrievePhaseProgress();
        updateMainGridPane();
    }

    public void retrievePhaseProgress()
    {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL))
        {
            Statement databaseStatement = connection.createStatement();
            String sqlStatement = null;
            ResultSet resultSet = null;

            sqlStatement = "SELECT * FROM PhaseCommonsTable";
            resultSet = databaseStatement.executeQuery(sqlStatement);
            while (resultSet.next())
            {
                phases.add(new Phase(resultSet));
            }

            sqlStatement = "SELECT * FROM TextFieldChallengesTable";
            resultSet = databaseStatement.executeQuery(sqlStatement);
            int phaseNum = 0;
            while (resultSet.next())
            {
                phaseNum = resultSet.getInt(1);
                phases.get(phaseNum - 1).getPhaseChallenges().add(new TextFieldChallenge(resultSet));
            }

            sqlStatement = "SELECT * FROM SingleCheckBoxChallengesTable";
            resultSet = databaseStatement.executeQuery(sqlStatement);
            while (resultSet.next())
            {
                phaseNum = resultSet.getInt(1);
                phases.get(phaseNum - 1).getPhaseChallenges().add(new SingleCheckBoxChallenge(resultSet));
            }
        }
        catch (Exception e)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }

    }

    public void updateMainGridPane()
    {
        for (int challengeNumber = 0; challengeNumber < phases.get(currentPhase).getPhaseChallenges().size(); challengeNumber++)
        {
            mainGridPane.add(phases.get(currentPhase).getPhaseChallenges().get(challengeNumber)
                    .getChallengeGridpane(), 0, (challengeNumber + 1));
            mainGridPane.add(phases.get(currentPhase).getPhaseChallenges().get(challengeNumber).getChallengeProgressBar()
                    .getProgressBarGridPane(), 1, (challengeNumber + 1));
        }

        phaseNumLabel.setText("Phase " + (currentPhase + 1));
    }

    public void clearMainGridPane()
    {
        for (int challengeNumber = 0; challengeNumber < phases.get(currentPhase).getPhaseChallenges().size(); challengeNumber++)
        {
            mainGridPane.getChildren().remove(phases.get(currentPhase).getPhaseChallenges().get(challengeNumber)
                    .getChallengeGridpane());
            mainGridPane.getChildren().remove(phases.get(currentPhase).getPhaseChallenges().get(challengeNumber).getChallengeProgressBar()
                    .getProgressBarGridPane());
        }
    }
}
