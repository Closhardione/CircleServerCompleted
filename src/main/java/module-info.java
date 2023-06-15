module com.example.circleserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.circleserver to javafx.fxml;
    exports com.example.circleserver;
}