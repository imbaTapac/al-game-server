package com.aion.game;

import javafx.application.Application;
import javafx.application.Platform;
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
		primaryStage.setOnCloseRequest(e -> Platform.exit());
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimary() {
		return primary;
	}
}
