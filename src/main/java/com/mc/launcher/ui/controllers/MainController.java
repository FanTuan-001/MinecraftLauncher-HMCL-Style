package com.mc.launcher.ui.controllers;

import com.mc.launcher.Main;
import com.mc.launcher.core.game.GameVersion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    
    @FXML private BorderPane rootPane;
    @FXML private HBox titleBar;
    @FXML private Label titleLabel;
    
    private Main mainApp;
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupWindowDragging();
        loadGameView();
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    private void setupWindowDragging() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        titleBar.setOnMouseDragged(event -> {
            Stage stage = mainApp.getPrimaryStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    
    private void loadGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mc/launcher/ui/views/GameView.fxml"));
            Parent gameView = loader.load();
            
            GameController controller = loader.getController();
            controller.setMainController(this);
            
            rootPane.setCenter(gameView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleMinimize() {
        mainApp.getPrimaryStage().setIconified(true);
    }
    
    @FXML
    private void handleMaximize() {
        Stage stage = mainApp.getPrimaryStage();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }
    
    @FXML
    private void handleClose() {
        System.exit(0);
    }
    
    public void showVersionDetails(GameVersion version) {
        // 更新版本详情显示
        if (version != null) {
            titleLabel.setText("Minecraft Launcher - " + version.getVersion());
        }
    }
}