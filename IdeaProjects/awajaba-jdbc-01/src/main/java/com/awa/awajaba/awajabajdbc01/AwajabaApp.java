package com.awa.awajaba.awajabajdbc01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AwajabaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /**try{
            Class.forName("org.mariadb.jdbc.driver");

        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }**/
        FXMLLoader fxmlLoader = new FXMLLoader(AwajabaApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}