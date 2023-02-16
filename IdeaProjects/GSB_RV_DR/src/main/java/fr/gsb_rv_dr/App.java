package fr.gsb_rv_dr;

import fr.gsb_rv_dr.entites.Praticien;
import fr.gsb_rv_dr.entites.RapportVisite;
import fr.gsb_rv_dr.entites.Visiteur;
import fr.gsb_rv_dr.modeles.ModeleGsbRv;
import fr.gsb_rv_dr.technique.ConnexionBD;
import fr.gsb_rv_dr.technique.ConnexionException;
import fr.gsb_rv_dr.technique.Session;
import fr.gsb_rv_dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb_rv_dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb_rv_dr.utilitaires.ComparateurDateVisite;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import fr.gsb_rv_dr.vue.VueConnexion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class App extends Application {

    static class PanneauAccueil {
        public static void show(BorderPane panneauAccueil) {
            String text = "Bienvenue";
            panneauAccueil.setCenter(new Label(text));
        }
    }

    static class PanneauRapport{

        public static void show(BorderPane PanneauRapport) {
            String text = "Rapport";
            PanneauRapport.setCenter(new Label(text));
        }
    }

    static class PanneauPraticiens extends Pane {


        private static final TableView<Praticien> tablePraticien = new TableView<>();

        public static ObservableList<Praticien> listPraticiens;

        static {
            try {
                listPraticiens = rafraichir();
            } catch (ConnexionException e) {
                throw new RuntimeException(e);
            }
        }


        private static final RadioButton rbCoefConfiance = new RadioButton();
        private static final RadioButton rbCoefNotoriete = new RadioButton();
        private static final RadioButton rbDateVisite = new RadioButton();


        public static void show(BorderPane PanneauPraticiens) throws ConnexionException {

            VBox vBoxPraticiens = new VBox();


            Label label = new Label("Sélectionner un critère de tri : ");

            vBoxPraticiens.getChildren().add(label);



            rbCoefConfiance.setSelected(true);

            rbCoefConfiance.setText("Confiance");
            rbCoefNotoriete.setText("Notoriété");
            rbDateVisite.setText("Date Visite");




            ToggleGroup toggleGroup = new ToggleGroup();

            rbCoefNotoriete.setToggleGroup(toggleGroup);
            rbCoefConfiance.setToggleGroup(toggleGroup);
            rbDateVisite.setToggleGroup(toggleGroup);




            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.getChildren().addAll(rbCoefConfiance, rbCoefNotoriete, rbDateVisite);

            hBox.setPadding(new Insets(10));

            vBoxPraticiens.getChildren().add(hBox);





            TableColumn<Praticien,Integer> colNum = new TableColumn<Praticien,Integer>("Numéro :");
            TableColumn<Praticien,String> colNom = new TableColumn<Praticien,String>("Nom :");
            TableColumn<Praticien,String> colVille = new TableColumn<Praticien,String>("Ville :");
            TableColumn<Praticien,Double> colCoefNotoriete = new TableColumn<Praticien,Double>("Coéficiant de notoriété :");
            TableColumn<Praticien, LocalDate> colDateDerniereVisite = new TableColumn<Praticien,LocalDate>("Date de la dernière visite :");
            TableColumn<Praticien,Integer> colDernierCoefConfiance = new TableColumn<Praticien,Integer>("Dernier coéficiant de confiance :");


            colNum.setCellValueFactory(new PropertyValueFactory<>("numero"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
            colCoefNotoriete.setCellValueFactory(new PropertyValueFactory<>("coefNotoriete"));
            colDateDerniereVisite.setCellValueFactory(new PropertyValueFactory<>("dateDerniereVisite"));
            colDernierCoefConfiance.setCellValueFactory(new PropertyValueFactory<>("dernierCoefConfiance"));

            tablePraticien.getColumns().addAll(colNum,colNom,colVille,colCoefNotoriete,colDateDerniereVisite,colDernierCoefConfiance);

            vBoxPraticiens.getChildren().add(tablePraticien);



            listPraticiens.sort(new ComparateurCoefConfiance());
            tablePraticien.setItems(listPraticiens);




            vBoxPraticiens.setPadding(new Insets(10));



            rbCoefConfiance.setOnAction((ActionEvent event) -> {
                listPraticiens.sort(new ComparateurCoefConfiance());
                tablePraticien.setItems(listPraticiens);
            });


            rbCoefNotoriete.setOnAction((ActionEvent event) -> {
                listPraticiens.sort(new ComparateurCoefNotoriete());
                tablePraticien.setItems(listPraticiens);
            });


            rbDateVisite.setOnAction((ActionEvent event) -> {
                listPraticiens.sort(new ComparateurDateVisite());
                Collections.reverse(listPraticiens);
            });




            PanneauPraticiens.setCenter(vBoxPraticiens);

        }


        public static ObservableList<Praticien> rafraichir() throws ConnexionException {
            List<Praticien> listPraticiens = ModeleGsbRv.getPraticiensHesitant();
            return FXCollections.observableList(listPraticiens);
        }



    }


    @Override
    public void start(Stage stage) throws IOException, ConnexionException {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,650,500);
        stage.setTitle("GSB-RV");
        stage.setScene(scene);
        stage.show();

        List<RapportVisite> a = ModeleGsbRv.getRapportVisite("a131","01");
        System.out.println(a);



        MenuBar barreMenus = new MenuBar();

        Menu menuFichier = new Menu("Fichier");
        Menu menuRapport = new Menu("Rapports");
        Menu menuPraticien = new Menu("Praticiens");


        MenuItem itemConnecter = new MenuItem("Se connecter");
        MenuItem itemDeconnecter = new MenuItem("Se deconnecter");
        MenuItem itemQuitter = new MenuItem("Quitter");

        MenuItem itemConsulter= new MenuItem("Consulter");

        MenuItem itemHesitant= new MenuItem("Hésitants");

        PanneauAccueil.show(root);

        menuRapport.getItems().add(itemConsulter);

        menuPraticien.getItems().add(itemHesitant);

        menuFichier.getItems().addAll(itemConnecter,itemDeconnecter,itemQuitter);

        barreMenus.getMenus().addAll(menuFichier,menuRapport,menuPraticien);

        root.setTop(barreMenus);

        itemQuitter.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));


        Session.fermer();


        itemDeconnecter.setDisable(true);
        menuRapport.setDisable(true);
        menuPraticien.setDisable(true);



        itemDeconnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        PanneauPraticiens.listPraticiens.sort(new ComparateurCoefConfiance());
                        Session.fermer();
                        itemDeconnecter.setDisable(true);
                        menuRapport.setDisable(true);
                        menuPraticien.setDisable(true);
                        itemConnecter.setDisable(false);
                        stage.setTitle("GSB-RV");
                        PanneauAccueil.show(root);
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



                                }
                                /*else{

                                    VueConnexion vuse = new VueConnexion();
                                    Optional<Pair<String,String>> reponses = fr.gsb_rv_dr.vue.showAndWait();

                                    fr.gsb_rv_dr.vue.setContentText("Mot de passe ou matricule incorrecte");
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
                        PanneauRapport.show(root);
                    }
                }
        );

        itemHesitant.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            PanneauPraticiens.listPraticiens = PanneauPraticiens.rafraichir();
                            PanneauPraticiens.show(root);
                        } catch (ConnexionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );


    }

    public static void main(String[] args) {
        try {
            ConnexionBD.getConnexion();
            // Tests
            List<Praticien> praticiens = ModeleGsbRv.getPraticiensHesitant();
            /*for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }

            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");


            praticiens.sort(new ComparateurCoefConfiance());
            for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }

            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            praticiens = ModeleGsbRv.getPraticiensHesitant();


            praticiens.sort(new ComparateurCoefNotoriete());
            for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }


            Collections.reverse(praticiens);
            for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }


            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            praticiens = ModeleGsbRv.getPraticiensHesitant();

            praticiens.sort(new ComparateurDateVisite());
            for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }


            Collections.reverse(praticiens);
            for(Praticien unPraticien : praticiens){
                System.out.println(unPraticien);
            }*/



        } catch (ConnexionException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        launch();
    }
}


