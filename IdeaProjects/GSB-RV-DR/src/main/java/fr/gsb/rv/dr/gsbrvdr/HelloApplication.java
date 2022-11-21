package fr.gsb.rv.dr.gsbrvdr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,650,500);
        stage.setTitle("Modification");
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

        itemQuitter.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        itemDeconnecter.setDisable(true);
        menuRapport.setDisable(true);
        menuPraticien.setDisable(true);

        itemDeconnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        itemDeconnecter.setDisable(true);
                        menuRapport.setDisable(true);
                        menuPraticien.setDisable(true);

                    }
                }
        );

        itemConnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        itemDeconnecter.setDisable(false);
                        menuRapport.setDisable(false);
                        menuPraticien.setDisable(false);

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
                            Platform.exit();
                        }else{
                            alertQuitter.close();
                        }
                    }
                }
        );
    }

    public static void main(String[] args) {
        launch();
    }
}