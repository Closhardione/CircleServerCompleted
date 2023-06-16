module com.example.circleserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.circleserver to javafx.fxml;
    exports com.example.circleserver;
}