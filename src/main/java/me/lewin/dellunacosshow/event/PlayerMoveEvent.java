package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {
    @EventHandler
    private void playerLeaveEvent(org.bukkit.event.player.PlayerMoveEvent event) {
        if (Reference.OnUse && event.getPlayer().equals(Reference.USER)) {
            event.setCancelled(true);
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().setSpectatorTarget(Reference.spectatorLock);
        }
    }
}
