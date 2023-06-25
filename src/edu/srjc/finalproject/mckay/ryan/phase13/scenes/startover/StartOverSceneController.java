/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    This is the start over scene controller. This
    controls the scene that lives at both the
    beginning and the end of all of the Phases. Before
    completing Phase 13 it can only be accessed
    to the left of Phase 1, but after completing Phase 13,
    it can also be accessed to the right of the 13th
    Phase. This scene gives the user the option to restart
    the Phase 13 program, and if it is accessed after
    completing Phase 13, it also congratulates the user
    on completing the program.

    This scene is also displayed if the user enters
    in a starting date for Phase 13 that is in the
    future. The user will be placed in the start over
    scene and won't be able to continue on until the
    first start date is reached. This also gives them
    the option to enter in a different starting date
    if they so choose.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.scenes.startover;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.*;

public class StartOverSceneController implements Initializable
{
    @FXML
    Button previousSceneButton;
    @FXML
    Button nextSceneButton;
    @FXML
    Label startOverSceneLabel;

    @FXML
    public void restartPhase13ButtonPressed(ActionEvent actionEvent)
    {
        mainStage.setScene(startScene);
        firstPhaseUnlocked = true;
    }

    @FXML
    public void previousSceneButtonPressed(ActionEvent e)
    {
        mainStage.setScene(phaseScene);
    }

    @FXML
    public void nextSceneButtonPressed(ActionEvent actionEvent)
    {
        LocalDate currentDate = LocalDate.now();
        if (currentPhase == 0 && (currentDate.compareTo(phases.get(currentPhase).getPhaseStartDate()) >= 0))
        {
            firstPhaseUnlocked = true;
            mainStage.setScene(phaseScene);
        }
        else
        {
            Alert nextPhaseUnlockDateAlert = new Alert(Alert.AlertType.INFORMATION,
                    "The next phase will unlock on "
                            + phases.get(currentPhase).getPhaseStartDate().getMonthValue()
                            + "/" + phases.get(currentPhase).getPhaseStartDate().getDayOfMonth()
                            + "/" + phases.get(currentPhase).getPhaseStartDate().getYear() + ".",
                    ButtonType.OK);
            nextPhaseUnlockDateAlert.setTitle("Almost!");
            nextPhaseUnlockDateAlert.setHeaderText(null);
            nextPhaseUnlockDateAlert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if (currentPhase == (NUM_OF_PHASES - 1))
        {
            previousSceneButton.setDisable(false);
            previousSceneButton.setVisible(true);
            nextSceneButton.setDisable(true);
            nextSceneButton.setVisible(false);
        }
        else
        {
            previousSceneButton.setDisable(true);
            previousSceneButton.setVisible(false);
            nextSceneButton.setDisable(false);
            nextSceneButton.setVisible(true);
            if (phase13IsCompleted)
            {
                startOverSceneLabel.setText("Congratulations on completing Phase 13! Would you like to start it over?");
            }
            else if (!firstPhaseUnlocked)
            {
                startOverSceneLabel.setText("The first phase in Phase 13\n has not begun yet.");
            }
            else
            {
                startOverSceneLabel.setText("Would you like to start Phase 13 over?");
            }
        }
    }
}
