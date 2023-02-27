package fr.gsb_rv_dr;

import fr.gsb_rv_dr.entites.Praticien;
import fr.gsb_rv_dr.entites.RapportVisite;
import fr.gsb_rv_dr.entites.Visiteur;
import fr.gsb_rv_dr.modeles.ModeleGsbRv;
import fr.gsb_rv_dr.technique.ConnexionException;
import fr.gsb_rv_dr.technique.Mois;
import fr.gsb_rv_dr.technique.Session;
import fr.gsb_rv_dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb_rv_dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb_rv_dr.utilitaires.ComparateurDateVisite;
import fr.gsb_rv_dr.vue.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App extends Application {

    static class PanneauAccueil {
        public static void show(BorderPane panneauAccueil) {
            String text = "Bienvenue";
            panneauAccueil.setCenter(new Label(text));
        }
    }




   public static class PanneauRapport extends Pane{

        private static final ComboBox<Visiteur> cbVisiteur = new ComboBox<>();

        private static final ComboBox<Mois> cbMois = new ComboBox<>();

        private static final ObservableList<String> annees = FXCollections.observableArrayList();
        private static final ComboBox<String> cbAnnee = new ComboBox<>(annees);

        private static TableView<RapportVisite> tabRapport = new TableView<>();

        public static List<RapportVisite> rapportVisite = new ArrayList<>();

        public static  List<RapportVisite> rafraichir(String visiteur, int mois, String annee) throws ConnexionException {

            List<RapportVisite> listRapportVisite = ModeleGsbRv.getRapportVisite(visiteur,mois,annee);
            return listRapportVisite;

        }




        public static void show(BorderPane PanneauRapport) throws ConnexionException {

            if(cbVisiteur.getValue() == null){
                List<Visiteur> listVisiteur = ModeleGsbRv.getVisiteurs();
                cbVisiteur.setItems(FXCollections.observableList(listVisiteur));

                cbMois.getItems().addAll(Mois.values());


                int anneeCourante = Year.now().getValue();

                for (int annee = anneeCourante - 5; annee <= anneeCourante; annee++) {
                    annees.add(String.valueOf(annee));
                }
                cbAnnee.setValue(String.valueOf(anneeCourante));
            }


            VBox vBoxRapport = new VBox();




            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.getChildren().addAll(cbVisiteur, cbMois, cbAnnee);



            Button btnValide = new Button("Validé");

            vBoxRapport.setPadding(new Insets(10));
            vBoxRapport.setSpacing(10);


            TableColumn<RapportVisite,Integer> colNum = new TableColumn<RapportVisite,Integer>("Numéro du rapport de visite :");
            TableColumn<RapportVisite,String> colNomPraticien = new TableColumn<RapportVisite,String>("Nom du praticien :");
            TableColumn<RapportVisite, String> colVillePraticien = new TableColumn<RapportVisite,String>("Ville du praticien :");
            TableColumn<RapportVisite,LocalDate> colDateVisite= new TableColumn<RapportVisite,LocalDate>("Date Visite :");
            TableColumn<RapportVisite,LocalDate> colDateRedaction = new TableColumn<RapportVisite,LocalDate>("Date Redaction :");




            colNum.setCellValueFactory(new PropertyValueFactory<>("numero"));

            colNomPraticien.setCellValueFactory(param -> {
                String nom = param.getValue().getLePraticien().getNom();
                return new SimpleStringProperty(nom);
            });

            colVillePraticien.setCellValueFactory(param -> {
                String ville = param.getValue().getLePraticien().getVille();
                return new SimpleStringProperty(ville);
            });



            colDateVisite.setCellValueFactory(new PropertyValueFactory<>("dateVisite"));


            colDateVisite.setCellFactory(colonne -> {
                return new TableCell<RapportVisite, LocalDate>() {
                    protected void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText("");
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                            setText(getItem().format(formatter));
                        }
                    }
                };
            });



            colDateRedaction.setCellValueFactory(new PropertyValueFactory<>("dateRedaction"));

            colDateRedaction.setCellFactory(colonne -> {
                return new TableCell<RapportVisite, LocalDate>() {
                    protected void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText("");
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                            setText(getItem().format(formatter));
                        }
                    }
                };
            });

            if(tabRapport.getColumns().isEmpty()){
                tabRapport.getColumns().addAll(colNum, colNomPraticien, colVillePraticien, colDateVisite, colDateRedaction);
            }



            vBoxRapport.getChildren().addAll(hBox, btnValide, tabRapport);


            PanneauRapport.setCenter(vBoxRapport);

            btnValide.setOnAction((ActionEvent event) ->{
                if(cbVisiteur.getValue() == null || cbAnnee.getValue() == null || cbMois.getValue() == null){
                    VueRapportIncomplet vue = new VueRapportIncomplet();
                    vue.showAndWait();
                }else{
                    try {
                        rapportVisite = rafraichir(cbVisiteur.getValue().getMatricule(), cbMois.getValue().getNumero(), cbAnnee.getValue());
                        if(rapportVisite==null){
                            ObservableList<RapportVisite> vide = FXCollections.observableArrayList();
                            tabRapport.setItems(vide);
                        }else{
                            ObservableList<RapportVisite> vide = FXCollections.observableArrayList();

                            tabRapport.getColumns().clear();
                            tabRapport.getColumns().addAll(colNum, colNomPraticien, colVillePraticien, colDateVisite, colDateRedaction);

                            tabRapport.setItems(FXCollections.observableList(vide));

                            tabRapport.setItems(FXCollections.observableList(rapportVisite));
                        }

                    } catch (ConnexionException e) {
                        throw new RuntimeException(e);
                    }
                }

            });

            tabRapport.setRowFactory(ligne -> {
                return new TableRow<RapportVisite>(){
                    @Override
                    protected void updateItem(RapportVisite item, boolean empty) {
                        super.updateItem(item, empty);

                        if(item!=null){
                            if(item.isLu()){
                                setStyle("-fx-background-color: gold");
                            }else{
                                setStyle("-fx-background-color: cyan");
                            }
                        }
                    }
                };
            });


            tabRapport.setOnMouseClicked(
                    (MouseEvent event)->{
                        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){

                            int indiceRapport = tabRapport.getSelectionModel().getSelectedIndex();
                            RapportVisite rapportVisite = tabRapport.getItems().get(indiceRapport);

                            try {
                                ModeleGsbRv.setRapportVisiteLu(rapportVisite.getLeVisiteur().getMatricule(),rapportVisite.getNumero());
                            } catch (ConnexionException e) {
                                throw new RuntimeException(e);
                            }


                            String matricule = cbVisiteur.getValue().getMatricule();
                            List<RapportVisite> a ;
                            try {
                                a = rafraichir(matricule, cbMois.getValue().getNumero(),cbAnnee.getValue());
                            } catch (ConnexionException e) {
                                throw new RuntimeException(e);
                            }

                            tabRapport.setItems(FXCollections.observableList(a));


                            VueRapport vueRapport = new VueRapport();
                            vueRapport.showAndWait();


                        }
                    }
            );




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
    public void start(Stage stage)  {
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

        PanneauAccueil.show(root);

        menuRapport.getItems().add(itemConsulter);

        menuPraticien.getItems().add(itemHesitant);

        menuFichier.getItems().addAll(itemConnecter,itemDeconnecter,itemQuitter);

        barreMenus.getMenus().addAll(menuFichier,menuRapport,menuPraticien);

        root.setTop(barreMenus);

        /*Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/home/r-mehdi/SIO/JavaFX/IdeaProjects/GSB_RV_DR/src/main/resources/fr/gsb_rv_dr/GSB.png")));
        ImageView logoView = new ImageView(logo);
        root.setCenter(logoView);*/

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

                        if(reponse.isPresent() && !reponse.get().getKey().isEmpty() && !reponse.get().getValue().isEmpty()){
                            try {
                                if(ModeleGsbRv.seConnecter(reponse.get().getKey(), reponse.get().getValue() )!=null){
                                        Visiteur visiteur = ModeleGsbRv.seConnecter(reponse.get().getKey(), reponse.get().getValue());
                                        Session.ouvrir(visiteur);
                                        itemDeconnecter.setDisable(false);
                                        menuRapport.setDisable(false);
                                        menuPraticien.setDisable(false);
                                        itemConnecter.setDisable(true);

                                } else {
                                    VueConnexionEchoue vueEchoue = new VueConnexionEchoue();
                                    vueEchoue.showAndWait();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            VueConnexionVide vueVide= new VueConnexionVide();
                            vueVide.showAndWait();                        }

                    }
                }
        );

        /*else{
                            System.out.println("bon");
                            VueConnexionEchoue vueEchoue = new VueConnexionEchoue();
                            vueEchoue.showAndWait();
                        }*/

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
                        try {
                            PanneauRapport.show(root);
                        } catch (ConnexionException e) {
                            throw new RuntimeException(e);
                        }
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



