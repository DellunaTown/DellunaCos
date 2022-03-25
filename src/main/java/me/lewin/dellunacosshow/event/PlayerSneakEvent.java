package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakEvent implements Listener {
    @EventHandler
    private void playerSneakEvent(PlayerToggleSneakEvent event) {
        if (Reference.OnUse && event.getPlayer().equals(Reference.USER)) {
            Player player = event.getPlayer();
            player.sendTitle("ⶀ", "", 1, 9, 14);
            Reference.OnUse = false;
            Reference.NPC.destroy();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(Reference.getOutLocation());
            Bukkit.getServer().getScheduler().cancelTask(Reference.TaskID);
        }
    }
}
