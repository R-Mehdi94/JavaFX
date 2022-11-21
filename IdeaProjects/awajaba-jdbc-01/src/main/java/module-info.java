module com.awa.awajaba.awajabajdbc01 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.awa.awajaba.awajabajdbc01 to javafx.fxml;
    exports com.awa.awajaba.awajabajdbc01;
}