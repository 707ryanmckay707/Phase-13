/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    A subclass of Alert. It informs the user when
    there is an error inserting into the database,
    and provides the user with ideas of what to do,
    and exits the program.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.customalerts;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DatabaseInsertError extends Alert
{
    public DatabaseInsertError()
    {
        super(AlertType.ERROR, "The database could be opened, however there was an error inserting into it. "
                + "Please close the application and try starting Phase 13 again. "
                + "Pressing Close will close the application so you can try re-starting it.\n\n"
                + "You could also verify that the database is located in the correct place: "
                + "FinalProject_McKay_Ryan_Phase13\\database\\Phase13.db\n\n"
                + "If neither of these options fix the problem you may need to re-download the application.",
                ButtonType.CLOSE);
        this.setTitle("An error has occurred.");
        this.setHeaderText(null);
        this.showAndWait();
        Platform.exit();
    }
}
