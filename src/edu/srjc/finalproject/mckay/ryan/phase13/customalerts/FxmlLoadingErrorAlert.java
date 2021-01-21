/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    A subclass of Alert. It informs the user when
    there is an error loading an .fxml file,
    provides the user with ideas of what to do,
    and closes the program since this error should
    not occur unless the files of the program have
    been tampered with.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.customalerts;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class FxmlLoadingErrorAlert extends Alert
{
    public FxmlLoadingErrorAlert(String fileName)
    {
        super(AlertType.ERROR,
                "There was an error loading " + fileName + "\n\n"
                        + "You can try starting the program again or checking that the file is at this location: "
                        + "FinalProject_McKay_Ryan_Phase13\\src\\edu\\srjc\\FinalProject\\mckay\\Ryan\\phase13\\"
                        + fileName.substring(0, fileName.lastIndexOf('.')) + "\\fileName\n\n"
                        + "If none neither these options fix the problem you may need to re-download the application.\n\n"
                        + "Pressing Close will close the application so you can try re-starting it.", ButtonType.CLOSE);
        this.setTitle("An error has occurred.");
        this.setHeaderText(null);
        this.showAndWait();
        Platform.exit();
    }
}