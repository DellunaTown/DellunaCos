package me.lewin.dellunacosshow.commands;

import me.lewin.dellunacosshow.Main;
import me.lewin.dellunacosshow.Reference;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CosCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (!(sender instanceof Player)) return true;
        if (!sender.isOp()) return true;

        Player player = (Player) sender;

        switch (args[0]){
            case "set":
                onCommandSet(args, player);
                break;
            case "enter":
                onCommandEnter(player);
                break;
            case "add":
                onCommandAdd(args, player);
                break;
            case "remove":
                onCommandRemove(args, player);
                break;
        }
        return true;
    }

    private void onCommandSet(String[] args, Player player) {
        switch (args[1]){
            case "viewpoint":
                FileConfiguration configA = getConfig();
                configA.set("viewpoint", player.getLocation());
                saveFile(configA, getFile());
                player.sendMessage(Reference.SUCCESS + "viewpoint 설정 완료");
                break;

            case "npc":
                FileConfiguration configB = getConfig();
                configB.set("NPC", player.getLocation());
                saveFile(configB, getFile());
                player.sendMessage(Reference.SUCCESS + "npc 설정 완료");
                break;

            case "out":
                FileConfiguration configC = getConfig();
                configC.set("out", player.getLocation());
                saveFile(configC, getFile());
                player.sendMessage(Reference.SUCCESS + "out 설정 완료");
                break;
        }
    }
    private void onCommandEnter(Player player) {
        if (Reference.OnUse) {
            player.sendMessage(Reference.FAIL + "누군가가 옷장을 이용중입니다. 잠시 기다려주세요.");
            return;
        }

        Location viewLocation = Reference.getViewPointLocation();
        Location NPCLocation = Reference.getNpcLocation();

        player.sendTitle("ⶀ", "", 1, 9, 14);

        Reference.NPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "델루나 옷장");
        Reference.NPC.getOrAddTrait(SkinTrait.class).setSkinName(player.getName());
        Reference.NPC.spawn(NPCLocation);
        Reference.NPC.data().setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false);
        Reference.NPC.faceLocation(Reference.getViewPointLocation());

        Reference.TaskID = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new TimerScheduler(), 0, 0).getTaskId();

        ArmorStand spectatorLock = (ArmorStand) player.getWorld().spawnEntity(viewLocation, EntityType.ARMOR_STAND);
        spectatorLock.setSilent(true);
        spectatorLock.setGravity(false);
        spectatorLock.setVisible(false);

        Reference.NPCLocation = Reference.NPC.getStoredLocation();
        Reference.OnUse = true;
        Reference.USER = player;
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(spectatorLock);
        player.sendMessage(Reference.SUCCESS + "옷장에 입장하셨습니다. 마음에 드는 리소스를 마음껏 구경해보세요.");
        player.sendMessage(Reference.SUCCESS + "좌클릭 : 옷장 열기");
        player.sendMessage(Reference.SUCCESS + "쉬프트 : 나가기");
    }
    private void onCommandAdd(String[] args, Player player) {
        ItemStack cos = player.getItemInHand();
        if (cos == null) {
            player.sendMessage(Reference.FAIL + "손에 리소스 아이템을 들어주세요");
            return;
        }

        switch (args[1]){
            case "new":
                YamlConfiguration configA = Reference.getConfig("//gui//PreviewMain.yml");
                ArrayList<Integer> slotsListA = new ArrayList<>();
                slotsListA.add(configA.getIntegerList("index").get(Integer.parseInt(args[4])));

                configA.set("item.new" + args[4] + ".type", args[2]);
                configA.set("item.new" + args[4] + ".color", Boolean.valueOf(args[3]));
                configA.set("item.new" + args[4] + ".slots", slotsListA);

                configA.set("item.new" + args[4] + ".material", cos.getType().toString());
                configA.set("item.new" + args[4] + ".customModelData", cos.getItemMeta().getCustomModelData());

                configA.set("item.new" + args[4] + ".name", cos.getItemMeta().getDisplayName());
                configA.set("item.new" + args[4] + ".lore", cos.getItemMeta().getLore());

                saveFile(configA, new File(plugin.getDataFolder() + "//gui//PreviewMain.yml"));

                player.sendMessage(Reference.SUCCESS + "추가 완료");
                break;

            default:
                if (!(new File(plugin.getDataFolder() + "//gui//Preview" + args[1] + ".yml").exists())) {
                    player.sendMessage(Reference.FAIL + "존재하지 않는 카테고리 이름입니다.");
                    break;
                }

                YamlConfiguration configB = Reference.getConfig( "//gui//Preview" + args[1] + ".yml");

                Integer count = configB.getInt("count");

                configB.set("item." + args[1] + count + ".type", args[2]);
                configB.set("item." + args[1] + count + ".color", Boolean.valueOf(args[3]));

                configB.set("item." + args[1] + count + ".material", cos.getType().toString());
                configB.set("item." + args[1] + count + ".customModelData", cos.getItemMeta().getCustomModelData());

                configB.set("item." + args[1] + count + ".name", cos.getItemMeta().getDisplayName());
                configB.set("item." + args[1] + count + ".lore", cos.getItemMeta().getLore());

                configB.set("count", count+1);

                saveFile(configB, new File(plugin.getDataFolder() + "//gui//Preview" + args[1] + ".yml"));

                player.sendMessage(Reference.SUCCESS + "추가 완료");
                break;
        }
    }
    private void onCommandRemove(String[] args, Player player) {
        switch (args[1]){
            case "new":
                YamlConfiguration configD = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "//gui//PreviewMain.yml"));
                configD.set("item.new" + args[2], null);
                saveFile(configD, new File(plugin.getDataFolder() + "//gui//PreviewMain.yml"));
                player.sendMessage(Reference.SUCCESS + "삭제 완료");
                break;
            default:
                if (!(new File(plugin.getDataFolder() + "//gui//Preview" + args[1] + ".yml").exists())) {
                    player.sendMessage(Reference.FAIL + "존재하지 않는 카테고리 이름입니다.");
                    break;
                }

                YamlConfiguration configB = Reference.getConfig( "//gui//Preview" + args[1] + ".yml");

                Integer count = configB.getInt("count");

                configB.set("item." + args[1] + count, null);
                configB.set("count", count-1);
                saveFile(configB, new File(plugin.getDataFolder() + "//gui//Preview" + args[1] + ".yml"));

                player.sendMessage(Reference.SUCCESS + "삭제 완료");
                break;
        }
    }

    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static File getFile() {

        return new File(plugin.getDataFolder(), "setting.dat");
    }
    public static FileConfiguration getConfig() {
        File file = getFile();
        return YamlConfiguration.loadConfiguration(file);
    }
    private static void saveFile(FileConfiguration config, File file){
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }

    static class TimerScheduler implements Runnable {
        Location NPCLocation = Reference.NPCLocation.clone();
        double x = NPCLocation.getX();
        double z = NPCLocation.getZ();
        double dis = Reference.getViewPointLocation().distance(NPCLocation);
        double count = 0;

        @Override
        public void run() {
            double cosX = dis * Math.cos(Math.toRadians(count));
            double sinZ = dis * Math.sin(Math.toRadians(count));

            if (x >= 0) NPCLocation.setX(x + cosX);
            else NPCLocation.setX(x - cosX);
            if (z <= 0) NPCLocation.setZ(z - sinZ);
            else NPCLocation.setZ(z + sinZ);

            System.out.println(NPCLocation);

            Reference.NPC.faceLocation(NPCLocation);
            count+=2;
        }
    }
}