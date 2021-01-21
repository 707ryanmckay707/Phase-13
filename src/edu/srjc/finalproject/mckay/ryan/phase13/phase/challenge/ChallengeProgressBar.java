/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    The ChallengeProgressBar class is
    used by all Challenge objects. It displays
    the users progress towards completing each
    challenge with a combination of a ProgressBar
    and Label that are held together by a GridPane.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_FONT;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_INSETS;

public class ChallengeProgressBar
{
    private GridPane progressBarGridPane = new GridPane();

    private ProgressBar progressBar = new ProgressBar();
    private Label progressBarLabel = new Label();

    public ChallengeProgressBar(int currentNumCompleted, int requiredNumCompleted)
    {
        progressBar.setStyle("-fx-accent: lawngreen;");
        progressBar.setPrefWidth(200);
        progressBarGridPane.add(progressBar, 0, 0);
        progressBarGridPane.setMargin(progressBar, DEFAULT_INSETS);
        progressBarGridPane.setHalignment(progressBar, HPos.CENTER);

        progressBarLabel.setFont(DEFAULT_FONT);
        progressBarGridPane.add(progressBarLabel, 0, 0);
        progressBarGridPane.setMargin(progressBarLabel, DEFAULT_INSETS);
        progressBarGridPane.setHalignment(progressBarLabel, HPos.CENTER);

        progressBarGridPane.setAlignment(Pos.CENTER);

        updateProgressBar(currentNumCompleted, requiredNumCompleted);
    }

    public GridPane getProgressBarGridPane()
    {
        return progressBarGridPane;
    }

    public void updateProgressBar(int currentNumCompleted, int requiredNumCompleted)
    {
        if(currentNumCompleted >= requiredNumCompleted)
        {
            progressBar.setProgress(1);
            progressBarLabel.setText("Completed");
        }
        else
        {
            progressBar.setProgress((double) currentNumCompleted / requiredNumCompleted);
            progressBarLabel.setText(currentNumCompleted + "/" + requiredNumCompleted);
        }
    }
}
