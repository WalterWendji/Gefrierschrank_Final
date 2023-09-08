package gefrierschrank01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Diese Klasse stellt die Verbindung zur SQL Datenbank her.
 *
 * @author Walter Dongmepi, Philip Heising, Momcilo Bajic
 */
public class DB_Connect {
    private boolean update;
    private String query;
    private int tabelleId;

    private static Connection connection;
    /*
    verbindet die Datenbank zum Code.
     */
    public static Connection connect() {
        //Datenbank Login
        String url="jdbc:mysql://localhost:3306/db_gefrierschrank";
        String username="root";
        String password="";
        try {

            //Lädt den Datenbank-Verbindungstreiber
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            System.out.println("Verbindung Erfolgreich.");

        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("Verbindung fehlgeschlagen.");
        }
        return connection;
    }

    /*
    Füge die Informationen in die Datenbank hinzu.
    Ansonsten aktualisiere die einzelnen Informationen in der Datenbank.
     */
    public void getQuery() {

        if (update == false) {

            query = "INSERT INTO `tabelle`(`itemID`, `lebensmittel`, `menge`, `einheit`, `fach, einfrierdatum, ablaufdatum`) VALUES (?,?,?,?,?,?)";

        } else {
            query = "UPDATE `tabelle` SET "
                    + "`lebensmittel`=?,"
                    + "`menge`=?,"
                    + "`einheit`=?,"
                    + "`fach`=?,"
                    + "`einfrierdatum`=?,"
                    + "`ablaufdatum`= ? WHERE id = '" + tabelleId + "'";
        }
    }
}
