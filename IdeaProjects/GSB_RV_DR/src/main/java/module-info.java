module fr.gsb_rv_dr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;


    opens fr.gsb_rv_dr.entites;
    opens fr.gsb_rv_dr to javafx.fxml;
    exports fr.gsb_rv_dr;
}