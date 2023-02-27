package fr.gsb_rv_dr.vue;

import fr.gsb_rv_dr.App;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VueRapport extends Dialog {

    public String titre = "Rapport Visite" ;

    public String fermer = "Fermer";


    public  String rapportVisite = App.PanneauRapport.rapportVisite.toString();

    public VueRapport(){
        this.setTitle(titre);

        VBox vbSaisies = new VBox();

        Label label = new Label(rapportVisite);

        vbSaisies.getChildren().add(label);

        this.getDialogPane().setContent(vbSaisies);

        ButtonType FERMER = new ButtonType(fermer, ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().add(FERMER);




    }
}
