package fr.gsb.rv.dr.gsbrvdr;

import fr.gsb.rv.dr.entites.Visiteur;
import fr.gsb.rv.dr.technique.ConnexionBD;
import fr.gsb.rv.dr.technique.ConnexionException;
import fr.gsb.rv.dr.technique.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import javafx.util.Pair;
import vue.VueConnexion;

import java.io.IOException;
import java.util.Optional;

public class App extends Application {

    public Pane vueAccueil = new Pane();
    public Pane vueRapports = new Pane();
    public Pane vuePraticiens = new Pane();


    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException, ConnexionException {


        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,650,500);
        stage.setTitle("GSB-RV");
        stage.setScene(scene);
        stage.show();

        MenuBar barreMenus = new MenuBar();

        Menu menuFichier = new Menu("Fichier");
        Menu menuRapport = new Menu("Rapports");
        Menu menuPraticien = new Menu("Praticiens");


        MenuItem itemConnecter = new MenuItem("Se connecter");
        MenuItem itemDeconnecter = new MenuItem("Se deconnecter");
        MenuItem itemQuitter = new MenuItem("Quitter");

        MenuItem itemConsulter= new MenuItem("Consulter");

        MenuItem itemHesitant= new MenuItem("Hésitants");

        menuRapport.getItems().add(itemConsulter);

        menuPraticien.getItems().add(itemHesitant);

        menuFichier.getItems().addAll(itemConnecter,itemDeconnecter,itemQuitter);

        barreMenus.getMenus().addAll(menuFichier,menuRapport,menuPraticien);

        root.setTop(barreMenus);
        root.setCenter(FXMLLoader.load(getClass().getResource("hello-view.fxml")));

        itemQuitter.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));


        Session.fermer();


        itemDeconnecter.setDisable(true);
        menuRapport.setDisable(true);
        menuPraticien.setDisable(true);


        //Panneau---------------------------------

        StackPane panned = new StackPane();


        Text accueil = new Text("Accueil");

        Text rapports = new Text("Rapports");

        Text praticiens = new Text("Praticiens");

        VBox vBox = new VBox();

        vBox.getChildren().addAll(accueil);

        vueAccueil.getChildren().add(vBox);

        vueRapports.getChildren().add(rapports);

        vuePraticiens.getChildren().add(praticiens);


        panned.getChildren().addAll(vueAccueil.getChildren().get(0), vueRapports.getChildren().get(0) ,vuePraticiens.getChildren().get(0));

        System.out.println(panned.getChildren().get(0));
        System.out.println(panned.getChildren().get(1));
        System.out.println(panned.getChildren().get(2));


        root.setCenter(panned.getChildren().get(0));

        System.out.println(panned.getChildren().get(0));





        itemDeconnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Session.fermer();
                        itemDeconnecter.setDisable(true);
                        menuRapport.setDisable(true);
                        menuPraticien.setDisable(true);
                        itemConnecter.setDisable(false);
                        stage.setTitle("GSB-RV");
                        root.setCenter(panned.getChildren().get(0));
                    }
                }
        );

        itemConnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        VueConnexion vue = new VueConnexion();
                        Optional<Pair<String,String>> reponse = vue.showAndWait();

                        if(reponse.isPresent()){
                            try {
                                if(ModeleGsbRv.seConnecter(reponse.get().getKey(), reponse.get().getValue() )!=null){
                                    Visiteur visiteur = ModeleGsbRv.seConnecter(reponse.get().getKey(), reponse.get().getValue());
                                    Session.ouvrir(visiteur);
                                    itemDeconnecter.setDisable(false);
                                    menuRapport.setDisable(false);
                                    menuPraticien.setDisable(false);
                                    itemConnecter.setDisable(true);
                                    root.setCenter(panned.getChildren().get(0));
                                }else{
                                    Text t = new Text("Mot de passe ou matricule incorrect");
                                    root.setCenter(t);
                                }
                                /*else{

                                    VueConnexion vuse = new VueConnexion();
                                    Optional<Pair<String,String>> reponses = vue.showAndWait();

                                    vue.setContentText("Mot de passe ou matricule incorrecte");
                                    Alert alert= new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Authentification refusé");
                                    alert.setHeaderText("Votre mot de pass ou votre matricule est incorrecte");
                                    ButtonType btnOK = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);
                                    alert.getButtonTypes().setAll(btnOK);

                                    Optional<ButtonType> reponse2 = alert.showAndWait();

                                    if(reponse2.get() == btnOK){
                                        System.out.println("ok");
                                    }
                                }*/
                            }catch (ConnexionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

        itemQuitter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
                        alertQuitter.setTitle("Quitter");
                        alertQuitter.setHeaderText("Demande de confirmation");
                        alertQuitter.setContentText("Voulez-vous quitter l'application");
                        ButtonType btnOui = new ButtonType("Oui");
                        ButtonType btnNon = new ButtonType("Non");
                        alertQuitter.getButtonTypes().setAll(btnOui,btnNon);

                        Optional<ButtonType> reponse = alertQuitter.showAndWait();

                        if(reponse.get() == btnOui){
                            Session.fermer();
                            Platform.exit();
                        }else{
                            alertQuitter.close();
                        }
                    }
                }
        );


        itemConsulter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        root.setCenter(panned.getChildren().get(1));
                    }
                }
        );

        itemHesitant.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        root.setCenter(panned.getChildren().get(2));
                    }
                }
        );


    }

    public static void main(String[] args) {
        try {
            ConnexionBD.getConnexion();
        } catch (ConnexionException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        launch();
    }
}