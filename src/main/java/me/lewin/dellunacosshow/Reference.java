package me.lewin.dellunacosshow;

import me.lewin.dellunacosshow.commands.CosCommand;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reference {
    public static final String SUCCESS = "§7[§a ! §7] §a";
    public static final String FAIL = "§7[§c ! §7] §c";

    public static Boolean OnUse = false;
    public static Player USER;
    public static NPC NPC;
    public static String FilePath = "";
    public static Integer Page = 1;
    public static ArrayList<String> GuiTitleList = new ArrayList<>();

    public static Location NPCLocation = getNpcLocation();
    public static Integer TaskID = 0;

    public static String CosPath = "";
    public static String CosKey = "";

    public static ArmorStand spectatorLock;

    public static Location getNpcLocation() {
        FileConfiguration config = CosCommand.getConfig();
        return config.getLocation("NPC");
    }
    public static Location getViewPointLocation() {
        FileConfiguration config = CosCommand.getConfig();
        return config.getLocation("viewpoint");
    }
    public static Location getOutLocation() {
        FileConfiguration config = CosCommand.getConfig();
        return config.getLocation("out");
    }

    public static ArrayList<Location> List = new ArrayList<>();

    public static YamlConfiguration getConfig(String filename) {
        Plugin plugin = JavaPlugin.getPlugin(Main.class);
        if (filename.contains("plugins\\DellunaCosShow"))
            return YamlConfiguration.loadConfiguration(new File(filename));
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "//" + filename));
    }

    public static void getGuiTitles() {
        Plugin plugin = JavaPlugin.getPlugin(Main.class);
        GuiTitleList.clear();
        for (File file : new File(plugin.getDataFolder() + "\\gui").listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            GuiTitleList.add(config.getString("title"));
        }
    }
}
