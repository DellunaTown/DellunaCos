package me.lewin.dellunacosshow;

import me.lewin.dellunacosshow.commands.*;
import me.lewin.dellunacosshow.event.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Plugin plugin = JavaPlugin.getPlugin(Main.class);

        if (!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        this.getCommand("cos").setExecutor(new CosCommand());
        Bukkit.getPluginCommand("cos").setTabCompleter(new CosCommandTabCompleter());

        Bukkit.getPluginManager().registerEvents(new PlayerSneakEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new GuiClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEvent(), this);

        Reference.getGuiTitles();
    }
}
