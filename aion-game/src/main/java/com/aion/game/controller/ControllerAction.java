package com.aion.game.controller;


import com.aion.game.GameClient;
import com.aion.game.listener.ServerListener;

import javafx.application.Platform;
import javafx.stage.Stage;

public interface ControllerAction {

	default void exitApplication(){
		Stage current = GameClient.getPrimary();
		current.setOnCloseRequest(e -> {
			ServerListener.getInstance().terminate();
			Platform.exit();
		});
	}
}
