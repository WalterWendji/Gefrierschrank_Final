module gefrier.gefrierschrank_final {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens gefrierschrank01 to javafx.fxml;
    exports gefrierschrank01;
}