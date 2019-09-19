package com.aion.game.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aion.game.listener.ServerListener;
import com.aion.game.service.ServerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginFormController implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(LoginFormController.class);

	@FXML
	private Label serverStatus = new Label();
	@FXML
	private Label currentOnline;

	private Scene scene;

	private static LoginFormController instance;

	public LoginFormController(){
		instance = this;
	}

	public static LoginFormController getInstance(){
		return instance;
	}

	@FXML
	public void handleServerStatus(Socket socket) {
		LOG.info("Current status is [{}]", serverStatus.getText());
		if(socket.isConnected()) {
			serverStatus.setText("ON");
			serverStatus.setTextFill(Paint.valueOf("green"));
			LOG.info("Current status is [{}]", serverStatus.getText());
		} else {
			serverStatus.setText("OFF");
			serverStatus.setTextFill(Paint.valueOf("red"));
			LOG.info("Current status is [{}]", serverStatus.getText());
		}
	}

	@FXML
	public void initialize() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ServerTask task = new ServerTask();
		task.start();
	}


	public void closeSystem(){
		ServerListener.getInstance().terminate();
		Platform.exit();
		System.exit(0);
	}

}
