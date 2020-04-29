package com.aion.game.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginFormController implements ControllerAction {

    private static final Logger LOG = LoggerFactory.getLogger(LoginFormController.class);

    @FXML
    private Label serverStatus = new Label();
    @FXML
    private Label currentOnline;
    @FXML
    private TextField login;
    @FXML
    private TextField password;

    private Alert alert = new Alert(Alert.AlertType.NONE);

    private boolean activeForm = false;

    private Socket clientSocket;

    private static LoginFormController instance;

    public LoginFormController() {
        this.activeForm = true;
        instance = this;
        exitApplication();
    }

    public static LoginFormController getInstance() {
        return instance;
    }

    @FXML
    public void handleServerStatus(Socket socket) {
        this.clientSocket = socket;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                LOG.info("Current status is [{}]", serverStatus.getText());
                if (socket.isConnected()) {
                    serverStatus.setText("ON");
                    serverStatus.setTextFill(Paint.valueOf("green"));
                } else {
                    serverStatus.setText("OFF");
                    serverStatus.setTextFill(Paint.valueOf("red"));
                }
                return null;
            }
        };
        Platform.runLater(task);
    }

    @FXML
    public void handleOnline(Socket socket) {
        this.clientSocket = socket;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                LOG.info("Current online is [{}]", currentOnline.getText());
                if (socket.isConnected()) {
                    try {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        LOG.info("Sending request");
                        dos.writeUTF("GetOnline");
                        dos.flush();
                        LOG.info("Request was send");
                        long response = dis.readLong();
                        LOG.info("Current online is [{}]", response);
                        currentOnline.setText(String.valueOf(response));
                    } catch (IOException e) {
                        LOG.error("[{}]", e);
                    }
                }
                return null;
            }
        };
        Platform.runLater(task);
    }


    @FXML
    private void startGame() throws IOException {
        if(checkLoginPass()) {
            this.activeForm = false;
            MainFormController controller = MainFormController.getInstance();
            controller.withSocket(clientSocket);
            controller.connectAndAuthorize(login.getText(), password.getText());
            controller.getNpcData();
        }
    }

    public boolean isActiveForm() {
        return activeForm;
    }

    private boolean checkLoginPass(){
        String userLogin = login.getText();
        String userPassword = password.getText();
        if(userLogin.isEmpty()){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Empty password");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
