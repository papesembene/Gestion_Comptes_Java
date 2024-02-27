module gestion.sn.gestion_comptes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens gestion.sn to javafx.fxml;
    exports gestion.sn;
    exports gestion.sn.repository;
    exports gestion.sn.model;
}