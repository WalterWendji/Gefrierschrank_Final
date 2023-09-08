package gefrierschrank01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Diese Klasse dient dazu das eigentliche Programm starten zu können.
 * Da Scene Builder für die Erstellung von der grafischen Oberfläche
 * benutzt wurde, wird die von Scene Builder erstelle Datei hierher importiert.
 *
 * @version 0.1
 * @author Philip Heising
 */
public class GefrierschrankMain extends Application {
    public static Stage popupStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.popupStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface.fxml"));//Die von Scene Builder erstellte Datei.
        Scene scene = new Scene(fxmlLoader.load());
        //Lade die CSS-Datei und füge sie zur Szene hinzu.
        String cssFilePath = getClass().getResource("/design.css").toExternalForm();
        scene.getStylesheets().add(cssFilePath);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED); //mach die Standardeinstellung von Windows bezüglich der Ansicht von dem Minimieren, Maximieren, Schließen weg.
        stage.show();
    }

    public static void main(String[] args) {
        DB_Connect.connect(); //verbindet die Datenbank mit Sourcecode
        launch();
    }

}