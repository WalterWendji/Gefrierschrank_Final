package gefrierschrank01;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;


/**
 * Alle hier eingetippten Befehle betreffen hauptsächlich das zweite Fenster, nämlich "Formular ausfüllen"
 * Diese Klasse ist dafür verantwortlich die eingetragenen Informationen
 * über die Lebensmittel aufzunehmen. Danach werden diese Informationen
 * an die Datenbank übertragen und da abgespeichert. Diese Klasse ist logischerweise
 * mit der Datenbank verbunden.
 *
 * @author Walter Dongmepi, Philip Heising, Momcilo Bajic
 * */

public class ControllerFenster2 implements Initializable {

    @FXML
    private DatePicker ablaufdatum;

    @FXML
    private Button absenden;

    @FXML
    private ToggleGroup auswahl;

    @FXML
    private Label autodatum;

    @FXML
    private DatePicker einfrierdatum;

    @FXML
    private TextField fach;

    @FXML
    private ChoiceBox<String> einheit;

    @FXML
    private TextField lebensmittel;

    @FXML
    private TextField menge;

    @FXML
    private Button zurueckbutton;

    @FXML
    private Button minimieren;

    @FXML
    private Button schliessen;

    @FXML
    private TextField einheit1;

    double x, y = 0;
    private boolean update;


    //führe diese Anweisungen aus, sobald dieses Fenster geladen wird.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            einheitsAuswahl();
        });
        buttonHintergrundfarbe();
        farbeVonBeschrifftungAendern();
    }

    /*
       mach das schon automatisch generierte Datum unsichtbar und
       gib die Möglichkeit das Datum manuell einzutragen.
    */
    @FXML
    void aufManuelKlick(MouseEvent event) {
        autodatum.setVisible(false);
        ablaufdatum.setVisible(true);
    }
    /*
        mach das schon manuell eingetragene Datum unsichtbar und
        gib das Ablaufdatum automatisch an.
     */
    @FXML
    void aufAutomatischKlick(MouseEvent event) {
        ablaufdatum.setVisible(false);
        autodatum.setVisible(true);
    }

    /*
        speichert die eingegebenen Informationen ab,
        überträgt sie in die Datenbank
        und kommt zurück auf das erste Fenster.
     */
    @FXML
    void SubmitAndQuit(MouseEvent event) throws IOException {
        // MySQL connection set up
        String url = "jdbc:mysql://localhost:3306/DB_Gefrierschrank";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Vorbereitung für den INSERT
            String sql = "INSERT INTO Item(Item_Name, Quantity, M_Unit, Freezer_Shelf, Freeze_Date, Exp_Date)  VALUES (?, ?, ?, ?, ?, ?)";

            // Holt die Werte aus dem Formular
            String lebensmittelValue = lebensmittel.getText();
            String mengeValue = menge.getText();
            String einheitValue;
            if (einheit.getSelectionModel().getSelectedItem().equals("selbst bestimmen")) {
                einheitValue = einheit1.getText();
            }
            else {
                einheitValue = einheit.getValue().toString();
            }
            String fachValue = fach.getText();
            String einfrierdatumValue = einfrierdatum.getValue().toString();
            String ablaufdatumValue = ablaufdatum.getValue().toString();

            //Execute des SQL statements
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lebensmittelValue);
            statement.setString(2, mengeValue);
            statement.setString(3, einheitValue);
            statement.setString(4, fachValue);
            statement.setString(5, einfrierdatumValue);
            statement.setString(6, ablaufdatumValue);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Daten erfolgreich eingefügt!");
            } else {
                System.out.println("Insert fehlgeschlagen!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        zurueckgehen(event);
    }

    /*
    geht auf das vorherige Fenster zurück.
    Sorgt anschließend dafür, dass man dieses Fenster bewegen kann.
     */
    @FXML
    void zurueckgehen(MouseEvent event) throws IOException {
        Stage stage1 = (Stage)(event.getSource() instanceof  Button && ((Button) event.getSource()).getId().equals(absenden)
                ? absenden.getScene().getWindow() : zurueckbutton.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssFilePath = getClass().getResource("/design.css").toExternalForm();
        scene.getStylesheets().add(cssFilePath);
        stage1.setScene(scene);
        stage1.show();
    }

    /*
    schließt das Fenster
    */
    @FXML
    void closeWindows(ActionEvent event) {
        Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage1.close();
    }

    //Minimiert das Fenster
    @FXML
    void minimizeWindows(ActionEvent event) {
        Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage1.setIconified(true);

    }

    //Sorgt dafür, dass die Hintergrundfarbe transparent bleibt.
    public void buttonHintergrundfarbe() {
        schliessen.setBackground(Background.fill(Color.TRANSPARENT));
        minimieren.setBackground(Background.fill(Color.TRANSPARENT));
        absenden.setBackground(Background.fill(Color.TRANSPARENT));
        zurueckbutton.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //ändert die Farbe, wenn man den Mauszeiger auf dem Button bewegt.
    @FXML
    void onMouseEnteredChangeColorMinimizeButton(MouseEvent event) {
        minimieren.setBackground(Background.fill(Color.DARKBLUE));
    }
    //ändert wieder die Farbe, wenn dieser Mauszeiger sich nicht mehr auf dem Button befindet.
    @FXML
    void onMouseExitedChangeColorMinimizeButton(MouseEvent event) {
        minimieren.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //ändert die Farbe, wenn man den Mauszeiger auf dem Button bewegt.
    @FXML
    void onMouseEnteredChangeColorCloseButton(MouseEvent event) {
        schliessen.setBackground(Background.fill(Color.RED));
    }

    //ändert wieder die Farbe, wenn dieser Mauszeiger sich nicht mehr auf dem Button befindet.
    @FXML
    void onMouseExitedChangeColorCloseButton(MouseEvent event) {
        schliessen.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ändert die Farbe des Speichern-Buttons
    @FXML
    void onMouseEnteredChangeColorSpeichernButton(MouseEvent event) {
        absenden.setBackground(Background.fill(Color.web("#0063E8")));
    }

    //Ändert die Farbe des Zurueck-Buttons
    @FXML
    void onMouseEnteredChangeColorZurueckButton(MouseEvent event) {
        zurueckbutton.setBackground(Background.fill(Color.web("#0063E8")));
    }

    //Stellt die Farbe des Speichern-Buttons wieder her.
    @FXML
    void onMouseExitedRestoreColorSpeichernButton(MouseEvent event) {
        absenden.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Stellt die Farbe des Zurueck-Buttons wieder her.
    @FXML
    void onMouseExitedRestoreColorZurueckButton(MouseEvent event) {
        zurueckbutton.setBackground(Background.fill(Color.TRANSPARENT));
    }

    @FXML
    void onMouseDraggedMoveWindows(MouseEvent event) {
        Stage stage2 = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage2.setX(event.getScreenX() - x);
        stage2.setY(event.getScreenY() - y);
    }

    @FXML
    void onMousePressedWindows(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    //Waehl die Einheit aus.
    public void einheitsAuswahl() {
        einheit.getItems().addAll("kg", "g", "liter", "Stück(e)", "Verpackung(en)", "selbst bestimmen");
        einheit.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
        einheit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals("selbst bestimmen")) {
                einheit.setVisible(false);
                einheit1.setVisible(true);
            }
        });

    }

    public void setUpdate(boolean b) {
        DB_Connect dbConnect = new DB_Connect();
        dbConnect.getQuery();
        this.update = b;
    }

    //Ändere die Textfarbe beim Eintragen von Informationen.
    public void farbeVonBeschrifftungAendern() {
        lebensmittel.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
        menge.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
        einheit.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
        einheit1.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
        fach.setStyle("-fx-text-fill: white;" +
                "-fx-background-color:  #264A7D;");
    }
}