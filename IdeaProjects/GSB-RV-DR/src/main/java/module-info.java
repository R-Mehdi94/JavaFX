module fr.gsb.rv.dr.gsbrvdr {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.gsb.rv.dr.gsbrvdr to javafx.fxml;
    exports fr.gsb.rv.dr.gsbrvdr;
}