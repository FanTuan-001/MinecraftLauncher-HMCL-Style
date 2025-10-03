package com.mc.launcher.core.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private final ObservableList<GameVersion> installedVersions = FXCollections.observableArrayList();
    private final List<VersionProvider> versionProviders = new ArrayList<>();
    
    private GameManager() {
        initializeProviders();
    }
    
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    private void initializeProviders() {
        // 注册默认版本提供者
        versionProviders.add(new VanillaVersionProvider());
        versionProviders.add(new ForgeVersionProvider());
        versionProviders.add(new FabricVersionProvider());
    }
    
    public ObservableList<GameVersion> getInstalledVersions() {
        return FXCollections.unmodifiableObservableList(installedVersions);
    }
    
    public void loadInstalledVersions() {
        // 模拟加载已安装版本
        installedVersions.addAll(
            new GameVersion("1.20.1", "Vanilla", "2024-01-15"),
            new GameVersion("1.19.2-forge-43.2.0", "Forge", "2024-01-10"),
            new GameVersion("1.18.2-fabric-0.14.0", "Fabric", "2024-01-05")
        );
    }
    
    public void launchGame(GameVersion version) {
        System.out.println("启动游戏: " + version.getVersion());
        // 实际启动逻辑在这里实现
    }
    
    public List<VersionProvider> getVersionProviders() {
        return new ArrayList<>(versionProviders);
    }
    
    public void addVersionProvider(VersionProvider provider) {
        versionProviders.add(provider);
    }
}