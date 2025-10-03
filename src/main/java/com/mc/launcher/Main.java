package com.mc.launcher;

import com.mc.launcher.core.auth.AuthManager;
import com.mc.launcher.core.game.GameManager;
import com.mc.launcher.core.plugins.PluginManager;
import com.mc.launcher.ui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {
    
    private static Main instance;
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        
        // 初始化核心组件
        initializeCoreComponents();
        
        // 加载主界面
        loadMainWindow();
    }
    
    private void initializeCoreComponents() {
        // 初始化认证管理器
        AuthManager.getInstance().initialize();
        
        // 初始化游戏管理器
        GameManager.getInstance().loadInstalledVersions();
        
        // 加载插件
        PluginManager.getInstance().loadPlugins();
    }
    
    private void loadMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mc/launcher/ui/views/MainWindow.fxml"));
        Parent root = loader.load();
        
        MainController controller = loader.getController();
        controller.setMainApp(this);
        
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource("/assets/css/style.css")).toExternalForm());
        
        primaryStage.setTitle("Minecraft Launcher - HMCL/PCL2 Style");
        primaryStage.getIcons().add(new Image(
            Objects.requireNonNull(getClass().getResourceAsStream("/assets/icons/icon.png"))));
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        
        // 移除默认窗口装饰，使用自定义标题栏
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        primaryStage.show();
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}