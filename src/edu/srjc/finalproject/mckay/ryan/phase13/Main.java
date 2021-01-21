/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    The is the main class of the program that loads all
    .fxml files and determines what scene to start
    the program with.
*/
package edu.srjc.finalproject.mckay.ryan.phase13;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.FxmlLoadingErrorAlert;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.Phase;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.phase.PhaseSceneController;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.startover.StartOverSceneController;
import edu.srjc.finalproject.mckay.ryan.phase13.scenes.start.StartSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main extends Application
{
    public final static String DATABASE_URL = "jdbc:sqlite:database/Phase13.db";

    public final static Insets DEFAULT_INSETS = new Insets(14, 14, 14, 14);
    public final static Font DEFAULT_FONT = new Font(14);

    public final static int NUM_OF_PHASES = 13;

    public static ArrayList<Phase> phases = new ArrayList<>();
    public static int currentPhase = 0;
    public static boolean phase13IsCompleted = false;
    public static boolean firstPhaseUnlocked = true;    //assumes this is true, to reduce the # of occurrences of this var

    public static Stage mainStage = null;

    public static Scene startScene = null;
    public static Scene startOverScene = null;
    public static Scene phaseScene = null;

    public static Parent startParent = null;
    public static Parent startOverParent = null;
    public static Parent phaseParent = null;

    private final static int PHASE_IS_COMPLETED_LOCATION = 1;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainStage = primaryStage;
        mainStage.setTitle("Phase 13");

        try
        {
            startParent = FXMLLoader.load(StartSceneController.class.getResource("StartScene.fxml"));
            startScene = new Scene(startParent);
        }
        catch (Exception exception)
        {
            FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("StartScene.fxml");
        }

        try(Connection connection = DriverManager.getConnection(DATABASE_URL))
        {
            Statement databaseStatement = connection.createStatement();
            String sqlStatement = "SELECT phaseIsCompleted FROM PhaseCommonsTable";
            ResultSet resultSet = databaseStatement.executeQuery(sqlStatement);

            //if Phase 13 has been started
            if(resultSet.next())
            {
                boolean currentPhaseFound = false;
                do
                {
                    if(resultSet.getInt(PHASE_IS_COMPLETED_LOCATION) == 0)
                    {
                        try
                        {
                            phaseParent = FXMLLoader.load(PhaseSceneController.class.getResource("PhaseScene.fxml"));
                            phaseScene = new Scene(phaseParent);
                        }
                        catch (Exception exception)
                        {
                            FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("PhaseScene.fxml");
                        }

                        LocalDate currentDate = LocalDate.now();

                        //compareTo returns:
                        // - 0, if both the dates are equal.
                        // - a positive value, if currentDate is greater than the argument date.
                        // - a negative value if currentDate is less than the argument date.

                        //If the currentPhase is 0 and the current date is less than the start date for Phase 1
                        //Then load the start over scene
                        if (currentPhase == 0 && (currentDate.compareTo(phases.get(currentPhase).getPhaseStartDate()) < 0))
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

                        currentPhaseFound = true;
                    }
                    //if the current Phase is the last phase and if that Phase is completed
                    else if((currentPhase == (NUM_OF_PHASES - 1)) && resultSet.getInt(PHASE_IS_COMPLETED_LOCATION) == 1)
                    {
                        phase13IsCompleted = true;

                        try
                        {
                            phaseParent = FXMLLoader.load(PhaseSceneController.class.getResource("PhaseScene.fxml"));
                            phaseScene = new Scene(phaseParent);
                        }
                        catch (Exception exception)
                        {
                            FxmlLoadingErrorAlert fxmlLoadingErrorAlert = new FxmlLoadingErrorAlert("PhaseScene.fxml");
                        }

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
                        currentPhase++;
                    }
                } while(resultSet.next() && (!currentPhaseFound));
            }
            else
            {
                mainStage.setScene(startScene);
            }
        }
        catch(Exception exception)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }

        mainStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
