package gefrierschrank01;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Diese Klasse ist verantwortlich für alles, was mit dem Fenster 3 zu tun hat. Diese Klasse dient ausschließlich
 * zu einer kleinen Meldung, die geworfen wird, wenn der Nutzer ein Konto löschen möchte.
 *
 * @author  Walter Dongmepi, Philip Heising, Momcilo Bajic
 * @version 0.1
 */

public class ControllerFenster3 implements Initializable {

    @FXML
    private Button jaButton = new Button();

    @FXML
    private Button neinButton = new Button();

    @FXML
    private Button schliessenButton;

    Stage stage = new Stage();

    private Stage parentStage;

    private static OnChoiceComplete onChoiceComplete;

    interface OnChoiceComplete {
        void choiceComplete(boolean choice);
    }

    public ControllerFenster3 () {
        this.parentStage = GefrierschrankMain.popupStage;

    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        schliessenButton.getStyleClass().add("bloom-button-schliessen");
        jaButton.getStyleClass().add("bloom-button-jaButton");
        neinButton.getStyleClass().add("bloom-button-neinButton");
    }


    /*
     Wenn der geklickte Button der Nein-Button ist, schließ die Meldung,
     ansonsten lösche das Konto und schließe danach die Meldung.
     */
    @FXML
    void jaOderNeinhandeln(ActionEvent event) {
        if(event.getSource() == jaButton) {
            if(ControllerFenster3.onChoiceComplete != null)
                ControllerFenster3.onChoiceComplete.choiceComplete(true);
        } else {
            if (ControllerFenster3.onChoiceComplete != null)
                ControllerFenster3.onChoiceComplete.choiceComplete(false);
        }
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
        parentStage.getScene().getRoot().setDisable(false); //Mach die Interaktion auf dem Hauptfenster wieder möglich, sobald das Popup geschlossen ist.
    }

    //Schließ das Fenster, wenn man auf den KreuzButton klickt.
    @FXML
    void schliesseDasFenster(MouseEvent event) {
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
        parentStage.getScene().getRoot().setDisable(false);  //Mach die Interaktion auf dem Hauptfenster wieder möglich, sobald das Popup geschloßen ist.
    }

    //Zeig die Warnungsmeldung/Popup
    public void warnungsFensterAnzeigenn(OnChoiceComplete onChoiceComplete) throws IOException {
        ControllerFenster3.onChoiceComplete = onChoiceComplete; //ermöglicht das Löschen von dem Node.
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(parentStage); //Das Hauptfenster wird bestimmt.
        stage.initModality(Modality.WINDOW_MODAL); //legt fest, dass die Interaktion auf dem Hauptfenster nicht möglich ist.
        FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface3.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssFilePath = getClass().getResource("/design.css").toExternalForm(); //hole die Stildatei.
        scene.getStylesheets().add(cssFilePath); //Füge den Stil auf dem Fenster hinzu.
        stage.setScene(scene);
        System.out.println("Das Popup wird geworfen.");
        parentStage.getScene().getRoot().setDisable(true); // Macht die Interaktion auf dem Hauptfenster nicht möglich, solange das Popup angezeigt wird.
        stage.show();
    }

}
