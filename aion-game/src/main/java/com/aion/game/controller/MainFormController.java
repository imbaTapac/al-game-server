package com.aion.game.controller;

import com.aion.commons.net.AttackPacket;
import com.aion.game.GameClient;
import com.aion.server.model.gameobjects.VisibleObject;
import com.aion.server.model.npc.Npc;
import com.aion.server.model.player.Player;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainFormController implements ControllerAction {

    private static final Logger LOG = LoggerFactory.getLogger(MainFormController.class);

    private List<VisibleObject> npcData = new ArrayList<>();
    private List<Npc> npcs = new ArrayList<>();
    private Player player = new Player();
    private Socket socket;

    private int choosedNpc;

    @FXML
    private Button attack;
    @FXML
    private Label playerName;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label bossLabel;
    @FXML
    private Text bossName;
    @FXML
    private Text bossHP;
    @FXML
    private ListView<Npc> bossList;
    @FXML
    private TextArea logView;

    private static MainFormController instance;

    public MainFormController() {
        instance = this;
        exitApplication();
    }

    static MainFormController getInstance() throws IOException {
        Stage main = GameClient.getPrimary();
        Parent root = FXMLLoader.load(MainFormController.class.getResource("/stages/game.fxml"));
        Scene scene = new Scene(root);
        main.setScene(scene);
        main.setTitle("AionBoss");
        main.show();
        return instance;
    }

    @FXML
    public void initialize() {
        for (VisibleObject vo : npcData) {
            if (vo instanceof Npc) {
                npcs.add((Npc) vo);
            }
        }
        playerName.setText(player.getNickName());
        ObservableList<Npc> list = FXCollections.observableList(npcs);
        bossList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bossList.setItems(list);
        bossList.autosize();
        progressBar.setStyle("-fx-accent: red;");
    }

    @SuppressWarnings("unchecked")
    public void getNpcData() {
        Service<List<VisibleObject>> s = new Service() {
            @Override
            protected Task<List<VisibleObject>> createTask() {
                return new Task<List<VisibleObject>>() {
                    @Override
                    protected List<VisibleObject> call() throws Exception {
                        List<VisibleObject> templates = null;
                        if (socket.isConnected()) {
                            try {
                                DataInputStream dis = new DataInputStream(socket.getInputStream());
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                LOG.info("Sending request to get NPCs");
                                dos.writeUTF("GetNpcTemplates");
                                dos.flush();
                                LOG.info("Request was send");
                                byte[] bytes = new byte[8192];
                                dis.read(bytes);
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                                ObjectInputStream os = new ObjectInputStream(byteArrayInputStream);
                                templates = (List<VisibleObject>) os.readObject();
                                LOG.info("Received [{}]", npcData);
                                LOG.info("Read [{}] bytes", bytes.length);
                            } catch (IOException e) {
                                LOG.error("[{}]", e);
                            }
                        }
                        return templates;
                    }
                };
            }
        };
        s.setOnSucceeded(w -> {
            npcData.addAll(s.getValue());
            initialize();
            LOG.info("Succeed [{}]", npcData);
        });
        s.start();
    }

    @SuppressWarnings("unchecked")
    public void connectAndAuthorize(String login, String password) {
        Service<Player> s = new Service() {
            @Override
            protected Task<Player> createTask() {
                return new Task<Player>() {
                    @Override
                    protected Player call() throws Exception {
                        Player p = null;
                        if (socket.isConnected()) {
                            try {
                                DataInputStream dis = new DataInputStream(socket.getInputStream());
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                LOG.info("Trying to authorize player {}",login);
                                dos.writeUTF("AuthPlayer");
                                dos.flush();
                                LOG.info("Request player was send");
                                dos.writeUTF(login);
                                dos.flush();
                                byte []bytes = new byte[1512];
                                dis.read(bytes);
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                                ObjectInputStream os = new ObjectInputStream(byteArrayInputStream);
                                p = (Player) os.readObject();
                                LOG.info("Received [{}]", p);
                                LOG.info("Read [{}] bytes", bytes.length);
                            } catch (IOException e) {
                                LOG.error("[{}]", e);
                            }
                        }
                        return p;
                    }
                };
            }
        };
        s.setOnSucceeded(w -> {
            this.player = s.getValue();
            LOG.info("Succeed [{}]", player);
        });
        s.start();
    }


    public void onPressListElement() {
        choosedNpc = bossList.getSelectionModel().selectedIndexProperty().get();
        bindProgress(npcs.get(choosedNpc));
        bossName.setText(bossList.getSelectionModel().getSelectedItem().getName().toUpperCase());
        bossHP.setText(String.valueOf(bossList.getSelectionModel().getSelectedItem().getLifeStats().getCurrentHp()));
    }

    @FXML
    public void startAttack() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                int playerDamage;
                int baseAttack = player.getAttack();
                int totalDamage = 0;
                final int criticalChance = 18;
                final int initialBossHP = bossList.getSelectionModel().getSelectedItem().getLifeStats().getMaxHp();
                int currentHP;
                if (socket.isConnected()) {
                    try {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        LOG.info("Trying to attack {}", bossList.getSelectionModel().getSelectedItem().getName());
                        int chance = (int) ((1) + (Math.random() * ((100) - (1))));
                        if (chance > 0 && chance <= criticalChance) {
                            logView.appendText("\nCritical attack");
                            logView.setWrapText(true);
                            playerDamage = (int) ((baseAttack * 1.5) + (Math.random() * ((baseAttack * 5.2) - (baseAttack * 1.5))));
                        } else {
                            playerDamage = (int) ((baseAttack * 1.5) + (Math.random() * ((baseAttack * 4.5) - (baseAttack * 1.5))));
                        }
                        LOG.debug("Sending player damage as {}", playerDamage);
                        dos.writeUTF("AttackCreature");
                        dos.flush();

                        AttackPacket packet = new AttackPacket(bossList.getSelectionModel().getSelectedItem().getNpcId(), playerDamage, player.getNickName());
                        oos.writeObject(packet);
                        dos.write(bos.toByteArray());
                        dos.flush();
                        currentHP = dis.readInt();
                        bossList.getSelectionModel().getSelectedItem().getLifeStats().setCurrentHp(currentHP);
                        bindProgress(bossList.getSelectionModel().getSelectedItem());
                        logView.appendText("\nYour attack to boss is " + playerDamage);
                        totalDamage += playerDamage;
                        logView.appendText("\nYour progress is " + totalDamage + " / " + bossList.getSelectionModel().getSelectedItem().getLifeStats().getMaxHp());
                        if (playerDamage >= currentHP) {
                            totalDamage -= playerDamage;
                            playerDamage = currentHP;
                            totalDamage += playerDamage;
                        }

                        bossHP.textProperty().set(String.valueOf(currentHP));

                        System.out.println("\nCurrent bossHP is " + currentHP + "\n");
                        if (currentHP <= 0) {
                            logView.appendText("Congrats the boss is dead");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                return null;
            }
        };

        Platform.runLater(task);
    }

    void withSocket(Socket socket) {
        this.socket = socket;
    }

    private void bindProgress(VisibleObject visibleObject) {
        if (visibleObject instanceof Npc) {
            Npc npc = (Npc) visibleObject;
            Long currentHp = Long.valueOf(npc.getLifeStats().getCurrentHp());// for cases when result of *100 is greather than Intereg.MAX_VALUE
            Double progress = (currentHp * 100 / npc.getLifeStats().getMaxHp()) / 100.0;
            progressBar.progressProperty().bind(new ObjectBinding<Number>() {
                @Override
                protected Number computeValue() {
                    return progress;
                }
            });
            progressIndicator.progressProperty().bind(new ObjectBinding<Number>() {
                @Override
                protected Number computeValue() {
                    return progress;
                }
            });
        }
    }
}