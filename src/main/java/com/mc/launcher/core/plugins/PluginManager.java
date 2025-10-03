package com.mc.launcher.core.plugins;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private static PluginManager instance;
    private final List<LauncherPlugin> plugins = new ArrayList<>();
    private final Gson gson = new Gson();
    
    private PluginManager() {}
    
    public static synchronized PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }
    
    public void loadPlugins() {
        Path pluginsDir = Paths.get("plugins");
        if (!Files.exists(pluginsDir)) {
            try {
                Files.createDirectories(pluginsDir);
            } catch (IOException e) {
                System.err.println("无法创建插件目录: " + e.getMessage());
                return;
            }
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pluginsDir, "*.jar")) {
            for (Path pluginPath : stream) {
                loadPlugin(pluginPath);
            }
        } catch (IOException e) {
            System.err.println("加载插件时出错: " + e.getMessage());
        }
    }
    
    private void loadPlugin(Path pluginPath) {
        try (URLClassLoader pluginLoader = new URLClassLoader(
            new URL[]{pluginPath.toUri().toURL()}, getClass().getClassLoader())) {
            
            InputStream manifestStream = pluginLoader.getResourceAsStream("plugin-manifest.json");
            if (manifestStream == null) {
                System.err.println("插件清单不存在: " + pluginPath);
                return;
            }
            
            PluginManifest manifest = gson.fromJson(
                new InputStreamReader(manifestStream), PluginManifest.class);
            
            Class<?> pluginClass = pluginLoader.loadClass(manifest.getMainClass());
            LauncherPlugin plugin = (LauncherPlugin) pluginClass.getDeclaredConstructor().newInstance();
            
            plugin.onLoad(new SimplePluginContext());
            plugins.add(plugin);
            
            System.out.println("插件加载成功: " + manifest.getName());
            
        } catch (Exception e) {
            System.err.println("加载插件失败: " + pluginPath);
            e.printStackTrace();
        }
    }
    
    public List<LauncherPlugin> getPlugins() {
        return new ArrayList<>(plugins);
    }
    
    public static class PluginManifest {
        private String name;
        private String version;
        private String mainClass;
        private String author;
        
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getMainClass() { return mainClass; }
        public String getAuthor() { return author; }
    }
    
    private static class SimplePluginContext implements PluginContext {
        @Override
        public void registerVersionProvider(Object provider) {
            // 注册版本提供者
        }
        
        @Override
        public void addSettingsTab(String title, Object content) {
            // 添加设置选项卡
        }
    }
}