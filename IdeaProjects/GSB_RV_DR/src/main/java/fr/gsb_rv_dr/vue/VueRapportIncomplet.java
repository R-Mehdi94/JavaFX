package fr.gsb_rv_dr.vue;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VueRapportIncomplet extends Dialog {
    public String auth ="Rapport Visite";
    public String saisie ="Sélection incomplète";
    public String fermer = "Fermer";


    public VueRapportIncomplet(){
        this.setTitle(auth);
        this.setHeaderText(saisie);

        VBox vbSaisies = new VBox();

        Label label = new Label("La séléction que vous avez saisie est incomplète");

        vbSaisies.getChildren().add(label);



        this.getDialogPane().setContent(vbSaisies);

        ButtonType FERMER = new ButtonType(fermer, ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().add(FERMER);


    }
}
