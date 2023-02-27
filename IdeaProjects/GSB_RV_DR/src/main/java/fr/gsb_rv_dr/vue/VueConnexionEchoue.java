package fr.gsb_rv_dr.vue;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

public class VueConnexionEchoue extends Dialog {
    public String auth ="Authentification";
    public String saisie ="Identifiant ou mot de passe invalide";
    public String fermer = "Fermer";


    public VueConnexionEchoue(){
        this.setTitle(auth);
        this.setHeaderText(saisie);

        VBox vbSaisies = new VBox();

        Label label = new Label("L'identifiant ou le mot de passe que vous avez saisie est incorrecte");

        vbSaisies.getChildren().add(label);



        this.getDialogPane().setContent(vbSaisies);

        ButtonType FERMER = new ButtonType(fermer, ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().add(FERMER);


    }

}
