package com.aion.game;

import com.aion.game.listener.ServerListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameClient extends Application {
    private static Stage primary;

    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/stages/main.fxml"));
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Aion");
        primaryStage.show();
        ServerListener.getInstance().call();

    }

    public static Stage getPrimary() {
        return primary;
    }
}
