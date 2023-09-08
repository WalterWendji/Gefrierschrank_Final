package gefrierschrank01;

/**
 * Diese Klasse dient als reibungslose Kompilierung von dem Sourcecode in eine Jar-Datei.
 * Ohne diese Klasse kann die .jar Datei nicht funktionieren, weil die GefrierschrankMain-Klasse
 * von der Klasse Application erbt
 *
 * @version 0.1
 * @author Walter Dongmepi, Philip Heising, Momcilo Bajic
 */
public class MainKlasse {
    public static void main(String[] args) {
        GefrierschrankMain.main(args);
    }
}
