/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    A subclass of Challenge. A single check box
    challenge presents the user with a single
    check box that the user can check to enter that
    they have completed the challenge.
 */

package edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

import java.sql.*;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DATABASE_URL;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_FONT;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_INSETS;

public class SingleCheckBoxChallenge extends Challenge
{
    private final static int PHASE_NUM_LOCATION = 1;
    private final static int TITLE_LOCATION = 2;
    private final static int IS_COMPLETED = 3;

    private CheckBox checkBox = new CheckBox();

    public SingleCheckBoxChallenge(ResultSet resultSet) throws SQLException
    {
        super(resultSet.getInt(PHASE_NUM_LOCATION), resultSet.getInt(IS_COMPLETED), 1);
        setTitle(resultSet.getString(TITLE_LOCATION));

        checkBox.setFont(DEFAULT_FONT);
        checkBox.setOnAction(event ->
        {
            checkBoxPressed(event);
        });
        getChallengeGridpane().add(checkBox, 0, 1);
        getChallengeGridpane().setMargin(checkBox, DEFAULT_INSETS);

        if(challengeIsCompleted())
        {
            checkBox.setSelected(true);
            checkBox.setDisable(true);
            checkBox.setFocusTraversable(false);
        }
    }

    private void checkBoxPressed(ActionEvent event)
    {
        checkBox.setDisable(true);
        checkBox.setFocusTraversable(false);
        setCurrentNumCompleted(1);
        challengeProgressBar.updateProgressBar(getCurrentNumCompleted(), getRequiredNumCompleted());

        try(Connection connection = DriverManager.getConnection(DATABASE_URL))
        {
            Statement databaseStatement = connection.createStatement();
            String sqlStatement = "UPDATE SingleCheckBoxChallengesTable SET challengeIsCompleted = " + getCurrentNumCompleted() + " WHERE phaseNum = "
                    + getPhaseNum() + " AND title = '" + getTitleString() + "'";
            databaseStatement.execute(sqlStatement);
        }
        catch (Exception e)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }
    }
}
