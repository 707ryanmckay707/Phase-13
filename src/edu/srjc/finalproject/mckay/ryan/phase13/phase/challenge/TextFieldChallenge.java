/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    A subclass of Challenge. A text field
    challenge presents the user with a TextField
    to enter their progress towards a numeric
    based challenge. The three types that exist
    in Phase 13 are the Pushups, Squats, and Prayer
    challenges.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.security.InvalidParameterException;
import java.sql.*;

import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DATABASE_URL;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_FONT;
import static edu.srjc.finalproject.mckay.ryan.phase13.Main.DEFAULT_INSETS;

public class TextFieldChallenge extends Challenge
{
    private final static int PHASE_NUM_LOCATION = 1;
    private final static int CHALLENGE_TYPE_LOCATION = 2;
    private final static int CURRENT_NUM_COMPLETED_LOCATION = 3;
    private final static int REQUIRED_NUM_COMLETED_LOCATION = 4;

    private TextFieldChallengeTypes challengeType = null;

    private TextField userInputTextField = new TextField();
    private Button enterButton = new Button();

    public enum TextFieldChallengeTypes
    {
        PUSHUPS,
        SQUATS,
        PRAYER
    }

    public TextFieldChallenge(ResultSet resultSet) throws SQLException
    {
        super(resultSet.getInt(PHASE_NUM_LOCATION), resultSet.getInt(CURRENT_NUM_COMPLETED_LOCATION), resultSet.getInt(REQUIRED_NUM_COMLETED_LOCATION));
        try
        {
            if(resultSet.getString(CHALLENGE_TYPE_LOCATION).equals(TextFieldChallengeTypes.PUSHUPS.toString()))
            {
                challengeType = TextFieldChallengeTypes.PUSHUPS;
            }
            else if(resultSet.getString(CHALLENGE_TYPE_LOCATION).equals(TextFieldChallengeTypes.SQUATS.toString()))
            {
                challengeType = TextFieldChallengeTypes.SQUATS;
            }
            else if(resultSet.getString(CHALLENGE_TYPE_LOCATION).equals(TextFieldChallengeTypes.PRAYER.toString()))
            {
                challengeType = TextFieldChallengeTypes.PRAYER;
            }
        }
        catch (Exception e)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }

        userInputTextField.setFont(DEFAULT_FONT);
        getChallengeGridpane().add(userInputTextField, 0, 1);
        getChallengeGridpane().setMargin(userInputTextField, DEFAULT_INSETS);

        enterButton.setFont(DEFAULT_FONT);
        enterButton.setText("Enter");
        enterButton.setOnAction(event ->
        {
           enterButtonPressed(event);
        });
        getChallengeGridpane().add(enterButton, 1, 1);
        getChallengeGridpane().setMargin(enterButton, DEFAULT_INSETS);

        switch (challengeType)
        {
            case PUSHUPS:
            {
                setTitle(getRequiredNumCompleted() + " Pushups");
                break;
            }
            case SQUATS:
            {
                setTitle(getRequiredNumCompleted() + " Squats");
                break;
            }
            case PRAYER:
            {
                setTitle("Spend " + getRequiredNumCompleted() + " minutes in prayer.");
                break;
            }
        }
    }

    private void enterButtonPressed(ActionEvent actionEvent)
    {
        if(!(userInputTextField.getText().isEmpty()))
        {
            if(getCurrentNumCompleted() < getRequiredNumCompleted())
            {
                try
                {
                    int userInput = Integer.parseInt(userInputTextField.getText());
                    if(userInput < 0)
                    {
                        throw new InvalidParameterException();
                    }

                    addToCurrentNumCompleted(userInput);

                    if(challengeIsCompleted())
                    {
                        userInputTextField.setEditable(false);
                        userInputTextField.setFocusTraversable(false);
                    }

                    try(Connection connection = DriverManager.getConnection(DATABASE_URL))
                    {
                        Statement databaseStatement = connection.createStatement();
                        String sqlStatement = "UPDATE TextFieldChallengesTable SET currentNumCompleted = " + getCurrentNumCompleted() + " WHERE phaseNum = "
                                + getPhaseNum() + " AND challengeType = ";
                        switch (challengeType)
                        {
                            case PUSHUPS:
                            {
                                sqlStatement += "'" + TextFieldChallengeTypes.PUSHUPS + "'";
                                break;
                            }
                            case SQUATS:
                            {
                                sqlStatement += "'" + TextFieldChallengeTypes.SQUATS + "'";
                                break;
                            }
                            case PRAYER:
                            {
                                sqlStatement += "'" + TextFieldChallengeTypes.PRAYER + "'";
                                break;
                            }
                        }
                        databaseStatement.execute(sqlStatement);
                    }
                    catch(Exception e)
                    {
                        DatabaseAccessError databaseAccessError = new DatabaseAccessError();
                    }
                }
                catch(Exception e)
                {
                    Alert invalidDateAlert = null;
                    switch(challengeType)
                    {
                        case PUSHUPS:
                        {
                            invalidDateAlert = new Alert(Alert.AlertType.WARNING,
                                    "Please enter a valid number of pushups.", ButtonType.OK);
                            break;
                        }
                        case SQUATS:
                        {
                            invalidDateAlert = new Alert(Alert.AlertType.WARNING,
                                    "Please enter a valid number of squats.", ButtonType.OK);
                            break;
                        }
                        case PRAYER:
                        {
                            invalidDateAlert = new Alert(Alert.AlertType.WARNING,
                                    "Please enter a valid number of minutes in prayer.", ButtonType.OK);
                            break;
                        }
                    }
                    invalidDateAlert.setTitle("Whoops!");
                    invalidDateAlert.setHeaderText(null);
                    invalidDateAlert.showAndWait();
                }
            }
            userInputTextField.clear();
        }
    }
}
