package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerTeleportEvent implements Listener {
    @EventHandler
    private void playerLeaveEvent(org.bukkit.event.player.PlayerTeleportEvent event) {
        if (Reference.OnUse && event.getPlayer().equals(Reference.USER)) {
            event.setCancelled(true);
        }
    }
}
