package gefrierschrank01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *Zeigt die eingetragenen Lebensmittel in der Tabelle an.
 *
 * @author Walter Dongmepi, Philip Heising, Momcilo Bajic
 */
public class Tabelle {
    private String lebensmittel;
    private float menge;
    private String einheit;
    private int fach;
    private String einfrierdatum;
    private String ablaufdatum;
    private int itemID;


    public Tabelle(int itemID, String lebensmittel, float menge, String einheit, int fach, String einfrierdatum, String ablaufdatum) {
        this.itemID = itemID;
        this.lebensmittel = lebensmittel;
        this.menge = menge;
        this.einheit = einheit;
        this.fach = fach;
        this.einfrierdatum = einfrierdatum;
        this.ablaufdatum = ablaufdatum;

    }

    //gib die gespeicherte ID der Datenbanktabelle zurück.
    public int getItemID() {
        return itemID;
    }
    //gib das eingetragene Lebensmittel zurück.
    public String getLebensmittel() {
        return lebensmittel;
    }

    //Macht die Änderung des Namens des Lebensmittels möglich.
    public void setLebensmittel(String leb) {
        this.lebensmittel = leb;
    }

    //gib die eingetragene Menge zurück.
    public float getMenge() {
        return menge;
    }

    //Macht die Änderung der Menge möglich.
    public void setMenge(float menge) {
        this.menge = menge;
    }

    //gib die eingetragene Einheit zurück.
    public String getEinheit() {return einheit;}

    //Ermögliche die Einheit zu ändern.
    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    //gib das eingetragene Fach zurück.
    public int getFach() {
        return fach;
    }

    //Ermögliche das Fach zu ändern.
    public void setFach(int fach) {
        this.fach = fach;
    }

    //gib das eingetragene Einfrierdatum zurück.
    public String getEinfrierdatum() {
        return einfrierdatum;
    }

    //Ermögliche das Einfrierdatum zu ändern.
    public void setEinfrierdatum(String einfrierdatum) {
        this.einfrierdatum = einfrierdatum;
    }

    //gib das eingetragene Ablaufdatum zurück.
    public String getAblaufdatum() {
        return ablaufdatum;
    }

    //Ermögliche das Ablaufdatum zu ändern.
    public void setAblaufdatum(String ablaufdatum) {
        this.ablaufdatum = ablaufdatum;
    }

}
