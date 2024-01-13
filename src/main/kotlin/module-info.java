module com.unitech.registraionform {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires jbcrypt;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.unitech.registraionform to javafx.fxml;
    exports com.unitech.registraionform;
}