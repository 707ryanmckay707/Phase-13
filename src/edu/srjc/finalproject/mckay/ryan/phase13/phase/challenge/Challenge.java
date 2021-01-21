/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    The base class that all the challenges
    are built off of. It has a GridPane that will
    hold all the challenge's controls. It has a Label
    that will contain the title of the challenge,
    as well as the phaseNum, currentNumCompleted,
    requiredNumCompleted, fields. The currentNumCompleted
    and requiredNumCompleted represent the amount of
    progress the user has made in the challenge, and
    the total amount that represents completion.
    Each challenge also has its own ChallengeProgressBar
    object to display its progress.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_FONT;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_INSETS;

public class Challenge
{
    private GridPane challengeGridpane = new GridPane();

    private Label title = new Label();
    private int phaseNum = 0;
    private int currentNumCompleted = 0;
    private int requiredNumCompleted = 0;

    ChallengeProgressBar challengeProgressBar = null;

    public Challenge(int inPhaseNum, int inCurrentNumCompleted, int inRequiredNumCompleted)
    {
        title.setFont(DEFAULT_FONT);
        title.setWrapText(true);
        challengeGridpane.add(title, 0, 0);
        challengeGridpane.setMargin(title, DEFAULT_INSETS);

        phaseNum = inPhaseNum;
        currentNumCompleted = inCurrentNumCompleted;
        requiredNumCompleted = inRequiredNumCompleted;

        challengeProgressBar = new ChallengeProgressBar(currentNumCompleted, requiredNumCompleted);
    }

    public boolean challengeIsCompleted()
    {
        return (currentNumCompleted >= requiredNumCompleted);
    }

    public Label getTitle()
    {
        return title;
    }

    public String getTitleString()
    {
        return title.getText();
    }

    public void setTitle(String inChallengeTitle)
    {
        this.title.setText(inChallengeTitle);
    }

    public GridPane getChallengeGridpane()
    {
        return challengeGridpane;
    }

    public int getPhaseNum()
    {
        return phaseNum;
    }

    public int getCurrentNumCompleted()
    {
        return currentNumCompleted;
    }

    public void addToCurrentNumCompleted(int input)
    {
        this.currentNumCompleted += input;
        challengeProgressBar.updateProgressBar(currentNumCompleted, requiredNumCompleted);
    }

    public void setCurrentNumCompleted(int currentNumCompleted)
    {
        this.currentNumCompleted = currentNumCompleted;
    }

    public int getRequiredNumCompleted()
    {
        return requiredNumCompleted;
    }

    public ChallengeProgressBar getChallengeProgressBar()
    {
        return challengeProgressBar;
    }
}
