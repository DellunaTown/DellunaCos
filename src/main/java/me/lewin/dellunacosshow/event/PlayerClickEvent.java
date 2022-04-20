package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import me.lewin.dellunacosshow.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickEvent implements Listener {
    @EventHandler
    private void playerClickEvent(PlayerInteractEvent event) {
        if (Reference.OnUse && event.getPlayer().equals(Reference.USER)) {
            Player player = event.getPlayer();
            if (!player.getSpectatorTarget().equals(Reference.spectatorLock)) {
                player.setSpectatorTarget(Reference.spectatorLock);
            }
            player.openInventory(GUI.set("gui//PreviewMain.yml", 1));
        }
    }
}
