module com.awa.awajaba.awajabajdbc01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    //requires java.sql;



    opens com.awa.awajaba.awajabajdbc01 to javafx.fxml;
    exports com.awa.awajaba.awajabajdbc01;
}