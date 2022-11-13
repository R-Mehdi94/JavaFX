package com.awa.awajaba.awajabajdbc01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

import static javafx.application.Application.launch;

public class AwajabaApp {
    /*@Override
   public void start(Stage stage) throws IOException {
        try{
            Class.forName("org.mariadb.jdbc.driver");

        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        /*FXMLLoader fxmlLoader = new FXMLLoader(AwajabaApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }*/
    Connection connexion;

    public Connection getCo() throws ClassNotFoundException {
        try {
            Class.forName("org.mariadb.jdbc.driver");
            String url = "jdbc:mysql://localhost:3306/awajaba" ;
            String user = "awajabAdmin";
            String mdp = "azerty";
            connexion = DriverManager.getConnection(url, user, mdp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connexion;
    }

    public static void main(String[] args) {
        try{
            Class.forName("org.mariadb.jdbc.driver");
            String url = "jdbc:mysql://localhost:3306/awajaba" ;
            String user = "awajabAdmin";
            String mdp = "azerty";
            Connection connexion = DriverManager.getConnection(url, user, mdp);
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Membre");
            while(resultSet.next()){
                System.out.println(resultSet);//getString("nomConnexion"));
            }
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        launch();
    }
}