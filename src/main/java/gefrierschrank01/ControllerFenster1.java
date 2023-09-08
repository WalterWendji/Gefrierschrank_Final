package gefrierschrank01;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

//import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Diese Klasse dient dazu, alle Befehle, die vom Programm
 * ausgeführt werden können, einzutippen und reinzupacken.
 *
 * Im Grunde sorgt das Programm dafür, dass man sich an die genaue Stelle der eingefrorenen Lebensmittel erinnert.
 * Beim Öffnen des Programms hat man folglich einen ganzen Überblick über die schon eingefrorenen Lebensmittel mit
 * ihren Einfrierdaten mit einbezogen.
 *
 * @author Walter Dongmepi
 */
public class ControllerFenster1 implements Initializable {

    @FXML
    private TableView<Tabelle> tableView;//Tabelle

    @FXML
    private TableColumn<Tabelle, String> ablaufdatum;

    @FXML
    private TableColumn<Tabelle, String> einfrierdatum;

    @FXML
    private TableColumn<Tabelle, String> einheit;

    @FXML
    private TableColumn<Tabelle, Integer> fach;

    @FXML
    private TableColumn<Tabelle, String> lebensmittel;

    @FXML
    private TableColumn<Tabelle, Float> menge;

    @FXML
    private ChoiceBox<String> language; //Sprachauswahl

    @FXML
    private Pane paneFenster1;

    @FXML
    private Label datum; //wird benutzt, um das aktuelle Datum anzugeben.

    @FXML
    private Button addButton; //Add Account Button

    @FXML
    private Button neuesFenster; //öffne das neue Fenster, wenn man auf "neu einfrieren" klickt.

    @FXML
    private Button menueButton;

    @FXML
    private Pane paneMenue;

    @FXML
    private TextField lebensmittelSuchenTextField;

    @FXML
    private Button minimieren;

    @FXML
    private Button schliessen;

    @FXML
    private Button gefrierschrankNutzer;

    @FXML
    private Button deleteAccount;

    @FXML
    private VBox vboxContainer;

    @FXML
    private AnchorPane anchorpane1;

    @FXML
    private StackPane stackpane; //Container des HBox

    @FXML
    private HBox hboxPrincipalUser;


    private int selectedItemId; //wird beim Laufen des Programms der ID eines Elements zugewiesen, wenn man darauf geklickt hat.
    private Button newUserButton;
    ActionEvent actionEvent = new ActionEvent();
    int value = 1; //Anzahl der Gefrierschranknutzer.
    static double x , y = 0; // Variable zuständig für die Bewegung des Fensters.
    private ResultSet resultSet;
    ControllerFenster3 jaOderNeinButtonVerwalten;
    ControllerFenster3 fenster3Anzeigen;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_gefrierschrank"; //Datenbank Verbindung
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";



    Tabelle tabelle = null;
    String query = null;
    Connection connection;
    PreparedStatement preparedStatement = null;
    ObservableList<Tabelle> tableList = FXCollections.observableArrayList();


    //Sorgt dafür, dass die Befehle, direkt nach dem Oeffnen des Fensters ausgeführt werden.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadInformation();
        displayDataFromDatabase();
        datumAngeben();
        languageChoiceBoxMenueVerwalten();
        buttonHintergrundfarbe();
        farbeVonBeschrifftungAendern();
        fuegeNeuesButtonhinzu(actionEvent);

    }

    //Aktualisiere die Tabelle nach einer Änderung.
    private void refreshTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            tableList.clear();
            query = "SELECT * FROM item";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tableList.add(new Tabelle( //Hole die Daten aus der Datenbanktabelle und füge diese in der Tabelle hinzu
                        resultSet.getInt("Item_ID"),
                        resultSet.getString("Item_Name"),
                        resultSet.getFloat("Quantity"),
                        resultSet.getString("M_Unit"),
                        resultSet.getInt("Freezer_Shelf"),
                        resultSet.getString("Freeze_Date"),
                        resultSet.getString("Exp_Date")));
                tableView.setItems(tableList);
                System.out.println("Die Methode wird ausgefüht...");
            }

        } catch (SQLException e) {
            Logger.getLogger(ControllerFenster1.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
    }


    /*
    Überträgt die entgegengenommenen Informationen in die Tabelle.
     */
    public void loadInformation()  {
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            tableView.setEditable(true); //Mach die Bearbeitung von der Tabelle möglich.
            lebensmittel.setEditable(true); //Mach die Bearbeitung von dieser Zelle möglich.
            lebensmittel.setCellFactory(TextFieldTableCell.forTableColumn()); //Füge das TextFeld in die Zelle.
            lebensmittel.setCellValueFactory(new PropertyValueFactory<>("lebensmittel"));

            menge.setEditable(true);
            menge.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            menge.setCellValueFactory(new PropertyValueFactory<>("menge"));

            einheit.setEditable(true);
            einheit.setCellFactory(TextFieldTableCell.forTableColumn());
            einheit.setCellValueFactory(new PropertyValueFactory<>("einheit"));

            fach.setEditable(true);
            fach.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            fach.setCellValueFactory(new PropertyValueFactory<>("fach"));

            einfrierdatum.setEditable(true);
            einfrierdatum.setCellFactory(TextFieldTableCell.forTableColumn());
            einfrierdatum.setCellValueFactory(new PropertyValueFactory<>("einfrierdatum"));

            ablaufdatum.setEditable(true);
            ablaufdatum.setCellFactory(TextFieldTableCell.forTableColumn());
            ablaufdatum.setCellValueFactory(new PropertyValueFactory<>("ablaufdatum"));

            // Ermöglicht das ID eines Lebensmittels aus der Datenbank zu holen, sobald man dieses in der Tabelle ausgewählt hat.
            tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    selectedItemId = newValue.getItemID(); //Wird das ID von dem ausgewählten Lebensmittel zugewiesen.
                    System.out.println("Das ist das ID: " + selectedItemId);
                    lebensmittel.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setLebensmittel(event.getNewValue());
                        updateDataFromDataBase("Item_Name", event.getNewValue(), selectedItemId);
                    });
                    menge.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setMenge(event.getNewValue());
                        updateDataFromDataBase("Quantity", String.valueOf(event.getNewValue()), selectedItemId);
                    });
                    einheit.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setEinheit(event.getNewValue());
                        updateDataFromDataBase("M_Unit", event.getNewValue(), selectedItemId);
                    });
                    fach.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setFach(event.getNewValue());
                        updateDataFromDataBase("Freezer_Shelf", String.valueOf(event.getNewValue()), selectedItemId);
                    });
                    einfrierdatum.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setEinfrierdatum(event.getNewValue());
                        updateDataFromDataBase("Freeze_Date", event.getNewValue(), selectedItemId);
                    });
                    ablaufdatum.setOnEditCommit(event -> {
                        tabelle = event.getRowValue();
                        tabelle.setAblaufdatum(event.getNewValue()); //ersetzt den alten Wert durch den Neuen.
                        updateDataFromDataBase("Exp_Date", event.getNewValue(), selectedItemId);
                    });
                }

            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Aktualisiere die Daten in der Datenbank, nachdem ein Benutzer etwas in der Tabelle bearbeitet hat.
    private void updateDataFromDataBase(String column, String newValue, int itemID) {
        String lebensmittelID = String.valueOf(itemID);
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatementTest = connection.prepareStatement("UPDATE item SET " + column + " = ? WHERE Item_ID = ?");
        ) {
            preparedStatementTest.setString(1, newValue);
            preparedStatementTest.setString(2, lebensmittelID);
            int rowsUpdated = preparedStatementTest.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Das Update hat geklappt. Wunderbar");
            } else {
                System.out.println("Das hat nicht geklappt.");
            }
        } catch (SQLException e) {
            System.err.println("Errrroorr");
            e.printStackTrace(System.err);
        }
    }

    //Zeigt Daten aus der Datenbank an.
    public void displayDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query1 = "SELECT Item_ID, Item_Name, Quantity, M_Unit, Freezer_Shelf, CAST(Freeze_Date AS CHAR) AS Freeze_Date, CAST(Exp_Date AS CHAR) AS Exp_Date FROM item";
            PreparedStatement statement = connection.prepareStatement(query1);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("Item_ID");
                String lebensmittel = resultSet.getString("Item_Name");
                float menge = resultSet.getFloat("Quantity");
                String einheit = resultSet.getString("M_Unit");
                int fach = resultSet.getInt("Freezer_Shelf");
                String einfrierdatum = resultSet.getString("Freeze_Date");
                String ablaufdatum = resultSet.getString("Exp_Date");
                Tabelle entry = new Tabelle(itemID, lebensmittel, menge, einheit, fach, einfrierdatum, ablaufdatum);
                tableView.getItems().add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Zeige ein Tooltip/Popup, wenn sich der Mauszeiger auf einer Zelle befindet.
        tableView.setRowFactory(tv -> new TableRow<Tabelle>() {
            private Tooltip tooltip = new Tooltip();

            //Zeige das Popupmenu unter einer bestimmten Bedingung.
            @Override
            public void updateItem (Tabelle tabelle1, boolean empty) {
                super.updateItem(tabelle1, empty);
                if (tabelle1 == null) {
                    setTooltip(null);// Wenn die Tabelle leer ist, soll nichts angezeigt werden.
                }
                else { //Ansonsten zeige das Popupmenu an.
                    tooltip.setText("Mache einen Rechtsklick, um das ausgewählte Lebensmittel zu löschen und mache einen Doppelklick, um das Lebensmittel zu bearbeiten!");
                    setTooltip(tooltip);
                }
            }
        });

        MenuItem menuItem = new MenuItem("Löschen"); //Erstellt das Menu-Item mit dem Namen "Löschen"
        ContextMenu contextMenuTabelle = new ContextMenu();//Erstellt das Kontextmenu.
        contextMenuTabelle.getItems().add(menuItem); //Füge das Menu-Item in dem Kontextmenu hinzu.

        //Führe die Löschen-Operation durch, wenn man auf das Menu-Item klickt.
        menuItem.setOnAction(event -> {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                tabelle = tableView.getSelectionModel().getSelectedItem();
                query = "DELETE FROM item WHERE Item_ID = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(selectedItemId));
                preparedStatement.execute();
                tableList.remove(tabelle);
                refreshTable(); //Aktualisiere nach dem Löschen des Elements die Tabelle.
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tableView.setContextMenu(contextMenuTabelle); //Zeig das Kontextmenu in der Tabelle an, wenn man auf eine Zelle einen Rechtsklick macht.
    }

    /*
    öffne ein zweites Fenster, wenn man auf den Knopf "neu einfrieren" klickt.
     */
    @FXML
    void oeffneNeuesFenster(MouseEvent event) throws IOException {
        Stage stage1 = (Stage) neuesFenster.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage1.setScene(scene);
        stage1.show();
    }

    //Öffne das Menü für das Hinzufügen von Konten.
    @FXML
    void paneMenueOn(MouseEvent event) {
        paneMenue.setVisible(true);
        editablleButton(gefrierschrankNutzer, hboxPrincipalUser, stackpane);
    }

    //Schließe das Menü für das Hinzufügen von Konten, wenn man auf das Fenster klickt.
    @FXML
    void paneMenueOff(MouseEvent event) {
        if (paneMenue.isVisible() == true)
            paneMenue.setVisible(false);
    }

    //Ändert die Farbe des Menus-Buttons
    @FXML
    void onMouseEnteredChangeColorMenueButton(MouseEvent event) {
        menueButton.setBackground(Background.fill(Color.web("#6AAAFF")));
    }

    //stellt die Farbe des Menu-Buttons wieder her.
    @FXML
    void OnMouseExitedRestoreColorMenueButton(MouseEvent event) {
        menueButton.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ändert die Farbe des Addbuttons.
    @FXML
    void onMouseEnteredChangeColorAddButton(MouseEvent event) {
        addButton.setBackground(Background.fill(Color.web("#6AAAFF")));
    }

    //stellt die Farbe des Addbuttons wieder her.
    @FXML
    void onMouseExitedRestoreColorAddButton(MouseEvent event) {
        addButton.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ändert die Farbe des Einfrierenbuttons.
    @FXML
    void onMouseEnteredChangeColorEinfrierenButton(MouseEvent event) {
        neuesFenster.setBackground(Background.fill(Color.web("#0063E8")));
    }

    //Stellt die Farbe des Einfrierbuttons wieder her.
    @FXML
    void onMouseExitedRestoreColorEinfrierenButton(MouseEvent event) {
        neuesFenster.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ermöglich die Suche von einem eingetragenen Lebensmittel in der Tabelle bzw. in der Datenbank.
    @FXML
    void lebensmittelSuchen(KeyEvent event) {
        String searchText = lebensmittelSuchenTextField.getText();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Item_ID, Item_Name, Quantity, M_Unit, Freezer_Shelf, CAST(Freeze_Date AS CHAR) AS Freeze_Date, CAST(Exp_Date AS CHAR) AS Exp_Date FROM item WHERE Item_Name LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1,searchText + "%");
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();
            List<Tabelle> filteredData = new ArrayList<>();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("Item_ID");
                String lebensmittel = resultSet.getString("Item_Name");
                float menge = resultSet.getFloat("Quantity");
                String einheit = resultSet.getString("M_Unit");
                int fach = resultSet.getInt("Freezer_Shelf");
                String einfrierdatum = resultSet.getString("Freeze_Date");
                String ablaufdatum = resultSet.getString("Exp_Date");
                Tabelle entry = new Tabelle(itemID, lebensmittel, menge, einheit, fach, einfrierdatum, ablaufdatum);
                filteredData.add(entry);
            }

            tableView.setItems(FXCollections.observableArrayList(filteredData));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeig das heutige Datum in der Ecke rechts unten an.
     */
    public void datumAngeben() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            datum.setText(now.format(formatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();//Zeig das heutige Datum
    }

    /*
    schließt das Fenster, wenn man auf den Kreuz-Button klickt.
     */
    @FXML
    void closeWindows(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    //Minimiert das Fenster, wenn man auf den Strich-Button klickt.
    @FXML
    void minimizeWindows(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);

    }

    //Sorgt dafür, dass die Hintergrundfarbe der Button transparent bleibt.
    public void buttonHintergrundfarbe() {
        schliessen.setBackground(Background.fill(Color.TRANSPARENT));
        minimieren.setBackground(Background.fill(Color.TRANSPARENT));
        menueButton.setBackground(Background.fill(Color.TRANSPARENT));
        addButton.setBackground(Background.fill(Color.TRANSPARENT));
        neuesFenster.setBackground(Background.fill(Color.TRANSPARENT));
        gefrierschrankNutzer.setBackground(Background.fill(Color.TRANSPARENT));
        deleteAccount.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Verwaltung des ChoiceBox Menüs.
    public void languageChoiceBoxMenueVerwalten() {
        language.getItems().addAll("Französisch", "Englisch", "Deutsch");
        language.getStyleClass().add("choice-box");
    }
    //Ändere die Textfarbe beim Eintragen von Informationen.
    public void farbeVonBeschrifftungAendern() {
        lebensmittelSuchenTextField.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: #264A7D;");
    }

    //Füge einen neuen User in den Container, wenn man auf den Button "add Nutzer" klickt
    @FXML
    void fuegeNeuesButtonhinzu(ActionEvent event) {
        addButton.setOnAction(event1 -> {
            value++; //Wird die Nutzeranzahl jeweils um 1 größer
            //Füge den neuen Button auf den VBox Container hinzu
            vboxContainer.getChildren().add(addContainerForButtons());
        });
    }

    //Wird das aktuelle Gefrierschrank-Konto geöffnet, wenn man auf den Button "Nutzer 1" klickt.
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == gefrierschrankNutzer) {
            Stage stage1 = (Stage) gefrierschrankNutzer.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            String cssFilePath = getClass().getResource("/design.css").toExternalForm();
            scene.getStylesheets().add(cssFilePath);
            stage1.setScene(scene);
            stage1.show();
        }
    }

    //Öffne ein neues Interface, wenn man auf irgendwelche erstellten New-User-Button klickt.
    public void newInterface(ActionEvent event) {
        try {
            Button newButton = (Button)event.getSource();
            Stage stage2 = (Stage) newButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(GefrierschrankMain.class.getResource("interface.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            String cssFilePath = getClass().getResource("/design.css").toExternalForm();
            scene.getStylesheets().add(cssFilePath);
            stage2.setScene(scene);
            System.out.println("Die Verbindung hat geklappt");
            stage2.show();
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden der FXML Datei. ", e);
        }
    }

    //füge ein StackPane-Container hinzu, in dem der User-Button und der Löschen-Button stehen muss.
    public StackPane addContainerForButtons() {
        StackPane stackePane1 = new StackPane();
        stackePane1.getStyleClass().add("stack-pane");
        HBox hBoxUser = new HBox();
        hBoxUser.setStyle("-fx-pref-height: 42;" +
                "-fx-pref-width: 103;" +
                "-fx-background-color: transparent;");
        //Hinzufügen von dem Löschen-Button und New-User-Button
        hBoxUser.getChildren().addAll(newUserButtonBearbeiten(value), deleteButtonBearbeiten(stackePane1));
        stackePane1.getChildren().add(hBoxUser);
        editablleButton(newUserButton, hBoxUser, stackePane1);
        return stackePane1;
    }

    //Erstellt einen Löschen-Button und füge seine Eigentschaften dazu.
    public Button deleteButtonBearbeiten(StackPane stackPane) {
        Button deleteButton = new Button();
        deleteButton.getStyleClass().add("bloom-button-1");
        deleteButton.setStyle("-fx-pref-height: 40;" +
                "-fx-pref-width: 40;");
        Image imageDeleteButton = new Image("/icons8-denied-64.png"); //Hole das benötigte Foto aus dem Ordner
        ImageView imageViewDeleteButton = new ImageView(imageDeleteButton);//Füge das geholte Foto zu dem Button hinzu
        imageViewDeleteButton.setFitWidth(24);
        imageViewDeleteButton.setFitHeight(25);
        deleteButton.setGraphic(imageViewDeleteButton);
        deleteButton.setVisible(false);
        //Mache den Löschen-Button sichtbar, wenn sich der Mauszeiger auf dem HBox-Container befindet.
        stackPane.setOnMouseEntered(event -> {
            deleteButton.setVisible(true);
        });
        //Mache den Löschen-Button unsichtbar, wenn sich das Mauszeiger überhaupt nicht mehr auf diesem Container befindet.
        stackPane.setOnMouseExited(event -> {
            deleteButton.setVisible(false);
        });
        //Mache das Löschen von dem ganzen User Button möglich
        deleteButton.setOnAction(event -> {
            try {
                accountLoeschen(stackPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return deleteButton;
    }

    //Erstellt einen New-User-Button und füge die Eigentschaften hinzu.
    public Button newUserButtonBearbeiten(int i) {
        newUserButton = new Button();
        //Popup anzeigen, wenn der Mausezeiger auf dem Button ist.
        Tooltip tooltip = new Tooltip("Mach ein Rechtklick, um umbenennen zu können!");
        newUserButton.setOnAction(this::newInterface); //verbinde den Button mit der Methode newInterface();
        newUserButton.getStyleClass().add("bloom-button"); //Füge eine Css-Klasse zum Button hinzu.
        newUserButton.getStyleClass().add("button-newUser");//Füge eine Css-Klasse zum Button hinzu.
        Image imageUser = new Image("/icons8_account_50px.png");
        ImageView imageview = new ImageView(imageUser);
        newUserButton.setText("Nutzer " + i);
        imageview.setFitHeight(29);
        imageview.setFitWidth(31);
        imageview.setStyle("-fx-translate-x: -12;");
        newUserButton.setGraphic(imageview);
        tooltip.setShowDelay(Duration.ZERO);// der Tooltip wird sofort angezeigt.
        tooltip.setShowDuration(Duration.seconds(30));
        newUserButton.setTooltip(tooltip);

        return newUserButton;
    }

    /*
    Mache die Änderung der Beschriftung eines Buttons für das Nutzerkonto möglich.
     */
    public void editablleButton(Button button, HBox hBox, StackPane stackPane) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Beschriftung ändern");
        contextMenu.getItems().add(menuItem);
        menuItem.setOnAction(event -> {
            hBox.setVisible(false); //Mach den HBox-Container unsichtbar.
            TextField textField = new TextField(button.getText()); //erstell ein Textfield mit der Beschrifftung des Buttons.
            //übernehme den eingetragenen Namen aus dem TextField und ersetz die bereits vorhandene Beschriftung des Buttons durch die neue.
            textField.setOnAction(textEvent -> {
                button.setText(textField.getText());
                Pane anchorPane = (Pane) textField.getParent();
                anchorPane.getChildren().remove(textField); //Entfernt das TextField wenn man auf die Enter-Taste gedrückt hat.
                hBox.setVisible(true); // Macht den HBox-Container wieder sichtbar.
            });
            Pane anchorPane = (Pane) stackPane.getParent();
            stackPane.getChildren().add(textField); // Füg das TextField in dem StackPane-Container hinzu.
            textField.requestFocus();
        });
        button.setContextMenu(contextMenu); // Es öffnet sich ein Kontextmenü, wenn man ein Rechtklickt auf dem Button macht.
    }

    //Lösche den Nutzerbutton und das entsprechende Nutzerkonto, wenn man auf den Löschen-Button klickt.
    public void accountLoeschen(StackPane stackPane) throws IOException {
        fenster3Anzeigen = new ControllerFenster3();
        fenster3Anzeigen.warnungsFensterAnzeigenn(new ControllerFenster3.OnChoiceComplete() {
            //überträgt die Auswahl an das ControllerFenster3
            @Override
            public void choiceComplete(boolean choice) {
                if (choice == true) {
                    // Aktion für "Ja" Button
                    System.out.println("JaButton ist geklickt, wunderbar.");
                    vboxContainer.getChildren().remove(stackPane);
                    System.out.println("Das Löschen hat geklappt.");
                } else {
                    // Aktion für "Nein" Button
                    System.out.println("NeinButton ist geklickt, wunderbar.");
                }
            }
        });

    }

    //Mach den Löschen-Button sichtbar, wenn sich der Mauszeiger auf dem HBox-Container befindet.
    @FXML
    void onMouseEnteredDeleteButtonSichtbarMachen(MouseEvent event) {
        deleteAccount.setVisible(true);
    }


    //Mach den Löschen-Button unsichtbar, wenn sich der Mauszeiger nicht mehr auf dem HBox-Container befindet.
    @FXML
    void onMouseExitedDeleteButtonUnsichtbarMachen(MouseEvent event) {
        deleteAccount.setVisible(false);
    }

    //Lösche das vorhandene Konto, das mit diesem Button verbunden ist.
    @FXML
    void loescheDasKonto(ActionEvent event) throws IOException {
        fenster3Anzeigen = new ControllerFenster3();
        fenster3Anzeigen.warnungsFensterAnzeigenn(new ControllerFenster3.OnChoiceComplete() {
            @Override
            public void choiceComplete(boolean choice) {
                if (choice == true) {
                    // Aktion für "Ja" Button
                    System.out.println("JaButton ist geklickt, wunderbar.");
                    try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String sql = "DELETE FROM item;";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        refreshTable();
                        tableList.clear();
                        statement.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Aktion für "Nein" Button
                    System.out.println("NeinButton ist geklickt, wunderbar.");
                }
            }
        });
    }

    //stellt die Hintergrundfarbe wieder her, wenn die Maustaste freigegeben wurde.
    @FXML
    void restoreBackgroundColorDeleteButton(MouseEvent event) {
        deleteAccount.setBackground(Background.fill(Color.web("#FF7D7D")));
    }

    //verstärkt die Farbe des Delete-Buttons, wenn man darauf geklickt hat.
    @FXML
    void BackgroundColorDeleteAccountButtonIncrease(MouseEvent event) {
        deleteAccount.setBackground(Background.fill(Color.web("#FF0202")));
    }

    //ändert die Hintergrundfarbe des Delete-Buttons in rot, wenn der Mauszeiger darauf ist.
    @FXML
    void changeBackgroundColorDeleteAccountButton(MouseEvent event) {
        deleteAccount.setBackground(Background.fill(Color.web("#FF7D7D")));
    }

    //stellt die Hintergrundfarbe des Delete-Buttons wieder her, wenn der Mauszeiger nicht mehr auf dem Button ist.
    @FXML
    void restoreBackgroundColorDeleteAccountButton(MouseEvent event) {
        deleteAccount.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ändert die Hintergrundfarbe des Buttons "Nutzer 1", sobald der Mauszeiger darauf ist.
    //Und mach den Delete Button sichtbar.
    @FXML
    void onMouseEnteredChangeColorButtonGefrierschrank(MouseEvent event) {
        gefrierschrankNutzer.setBackground(Background.fill(Color.web("#6AAAFF")));
    }

    //stellt die Hintergrundfarbe des Buttons "Nutzer 1" wieder her, sobald der Mauszeiger nicht mehr darauf ist.
    @FXML
    void onMouseExitedRestoreColorButtonGefrierschrank(MouseEvent event) {
        gefrierschrankNutzer.setBackground(Background.fill(Color.TRANSPARENT));
    }

    //Ändert die Hintergrundfarbe des neuen erstellten Buttons, sobald der Mauszeiger darauf ist.
    public void onMouseEnteredChangeColorButtonNewButton(MouseEvent event) {
        Button newButton = (Button)event.getSource();
        newButton.setBackground(Background.fill(Color.web("#6AAAFF")));
    }

    //stellt die Hintergundfarbe des neuen erstellten Buttons wieder her, sobald der Mauszeiger nicht mehr darauf ist.
    public void onMouseExitedRestoreColorButtonNewButton(MouseEvent event) {
        Button newButton = (Button)event.getSource();
        newButton.setBackground(Background.fill(Color.TRANSPARENT));
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

    //bereitet das Fenter für die Bewegung vor.
    @FXML
    void onMousePressedFensterVorbereiten(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    //Ermöglicht die Bewegung des Fensters.
    @FXML
    void onMouseDraggedFensterBewegen(MouseEvent event) {
        Stage stageFenster1 = (Stage)((Node) event.getSource()).getScene().getWindow();
        stageFenster1.setX(event.getScreenX() - x);
        stageFenster1.setY(event.getScreenY() - y);
    }
}