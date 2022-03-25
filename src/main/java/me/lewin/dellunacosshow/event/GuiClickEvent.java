package me.lewin.dellunacosshow.event;

import me.lewin.dellunacosshow.Reference;
import me.lewin.dellunacosshow.GUI;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.File;
import java.util.Set;

public class GuiClickEvent implements Listener {
    @EventHandler
    private void playerClickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && Reference.GuiTitleList.contains(event.getView().getTitle())) {
            if (event.getClickedInventory().equals(event.getView().getBottomInventory())) return;
            event.setCancelled(true);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Reference.FilePath));
            Set<String> itemKeySet = config.getConfigurationSection("item").getKeys(false);

            String Key = "";
            for (String itemKey : itemKeySet) {
                if (config.getIntegerList("item." + itemKey + ".slots").size() != 0){
                    for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                        if (index == event.getSlot()) {
                            Key = itemKey;
                        }
                    }
                }
                else {
                    if (Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) >= (Reference.Page-1)*config.getIntegerList("index").size() && Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) < Reference.Page*config.getIntegerList("index").size()) {
                        if (event.getSlot() == config.getIntegerList("index").get(Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) - (Reference.Page-1)*config.getIntegerList("index").size())){
                            Key = itemKey;
                        }
                    }
                }
            }

            switch (config.getString("item." + Key + ".type")) {
                case "OPEN_GUI":
                    event.getWhoClicked().openInventory(GUI.set(config.getString("item." + Key + ".guiLink"), 1));
                    break;
                case "COSMETIC_HAND":
                    if ((Boolean) config.get("item." + Key + ".color")) {
                        Reference.CosPath = Reference.FilePath;
                        Reference.CosKey = Key;
                        event.getWhoClicked().openInventory(GUI.set("gui//Color.yml", 1));
                        break;
                    }
                    EntityEquipment equipmentA = ((LivingEntity) Reference.NPC.getEntity()).getEquipment();
                    equipmentA.clear();
                    equipmentA.setItemInMainHand(GUI.icon(config, Key));
                    event.getWhoClicked().closeInventory();
                    break;
                case "COSMETIC_HEAD":
                    if ((Boolean) config.get("item." + Key + ".color")) {
                        Reference.CosPath = Reference.FilePath;
                        Reference.CosKey = Key;
                        event.getWhoClicked().openInventory(GUI.set("gui//Color.yml", 1));
                        break;
                    }
                    EntityEquipment equipmentB = ((LivingEntity) Reference.NPC.getEntity()).getEquipment();
                    equipmentB.clear();
                    equipmentB.setItem(EquipmentSlot.HEAD, GUI.icon(config, Key));
                    event.getWhoClicked().closeInventory();
                    break;
                case "PAGE_NEXT_ON":
                    event.getWhoClicked().openInventory(GUI.set(config.getString("item." + Key + ".guiLink"), Reference.Page + 1));
                    break;
                case "PAGE_BACK_ON":
                    event.getWhoClicked().openInventory(GUI.set(config.getString("item." + Key + ".guiLink"), Reference.Page - 1));
                    break;
                case "COLOR_MAIN":
                    event.getWhoClicked().openInventory(GUI.set("gui//Color.yml", Integer.parseInt(Key.replaceAll("[^0-9]", ""))));
                    break;
                case "COLOR_SUB":
                    ItemStack item = event.getClickedInventory().getItem(config.getIntegerList("item.cosOutput.slots").get(0));
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(((LeatherArmorMeta)event.getCurrentItem().getItemMeta()).getColor());
                    meta.addItemFlags(ItemFlag.HIDE_DYE);
                    item.setItemMeta(meta);
                    event.getClickedInventory().setItem(config.getIntegerList("item.cosOutput.slots").get(0), item);
                    break;
                case "COSMETIC_IN":
                case "COSMETIC_OUT":
                    YamlConfiguration configA = YamlConfiguration.loadConfiguration(new File(Reference.CosPath));
                    if (configA.getString("item." + Reference.CosKey + ".type").equals("COSMETIC_HAND")) {
                        EntityEquipment equipmentC = ((LivingEntity) Reference.NPC.getEntity()).getEquipment();
                        equipmentC.clear();
                        equipmentC.setItemInMainHand(event.getCurrentItem());
                        event.getWhoClicked().closeInventory();
                    }
                    if (configA.getString("item." + Reference.CosKey + ".type").equals("COSMETIC_HEAD")) {
                        EntityEquipment equipmentC = ((LivingEntity) Reference.NPC.getEntity()).getEquipment();
                        equipmentC.clear();
                        equipmentC.setItem(EquipmentSlot.HEAD, event.getCurrentItem());
                        event.getWhoClicked().closeInventory();
                    }
                    break;
            }
        }
    }
}