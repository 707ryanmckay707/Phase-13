/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    A subclass of Alert. It informs the user when
    there is an error accessing the database,
    provides the user with ideas of what to do,
    and exits the program since this error should
    not occur unless the files have been tampered
    with.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.customalerts;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DatabaseAccessError extends Alert
{
    public DatabaseAccessError()
    {
        super(AlertType.ERROR, "There was an error opening the database. "
                + "Please make sure it is located in the correct place: "
                + "FinalProject_McKay_Ryan_Phase13\\database\\Phase13.db\n\n"
                + "Pressing Close will close the application so you can try re-starting it.\n\n"
                + "If neither of the options fix the problem you may need to re-download the application.",
                ButtonType.CLOSE);
        this.setTitle("An error has occurred.");
        this.setHeaderText(null);
        this.showAndWait();
        Platform.exit();
    }
}
