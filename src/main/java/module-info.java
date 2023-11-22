module com.example.crs_fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.crs_fx to javafx.fxml, java.base;
    opens Backend to javafx.base;
    exports com.example.crs_fx;
}