package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeaveEvent implements Listener {
    @EventHandler
    private void playerLeaveEvent(org.bukkit.event.player.PlayerQuitEvent event) {
        if (Reference.OnUse && event.getPlayer().equals(Reference.USER)) {
            Player player = event.getPlayer();
            Reference.OnUse = false;
            Reference.NPC.destroy();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(Reference.getOutLocation());
            Bukkit.getServer().getScheduler().cancelTask(Reference.TaskID);
            Reference.spectatorLock.remove();
        }
    }
}
