package me.lewin.dellunacosshow;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class GUI{
    public static Inventory set(String filename, Integer page) {
        Plugin plugin = JavaPlugin.getPlugin(Main.class);
        YamlConfiguration config = Reference.getConfig(filename);
        Inventory inv = Bukkit.getServer().createInventory(null, config.getInt("size"), config.getString("title"));

        for (String itemKey : config.getConfigurationSection("item").getKeys(false)) {
            if (config.getString("item." + itemKey + ".type").equals("PAGE_NEXT_ON")) {
                if (config.getInt("count") <= 21*page) continue;
            }
            if (config.getString("item." + itemKey + ".type").equals("PAGE_BACK_ON")) {
                if (page <= 1) continue;
            }

            if (config.getString("item." + itemKey + ".type").equals("COLOR_MAIN")) {
                for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                    inv.setItem(index, color(config, itemKey, page));
                }
                continue;
            }
            if (config.getString("item." + itemKey + ".type").equals("COLOR_SUB")) {
                for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                    inv.setItem(index, color(config, itemKey, page));
                }
                continue;
            }

            if (config.getString("item." + itemKey + ".type").equals("COSMETIC_IN")) {
                for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                    inv.setItem(index, cos());
                }
                continue;
            }
            if (config.getString("item." + itemKey + ".type").equals("COSMETIC_OUT")) {
                for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                    inv.setItem(index, cos());
                }
                continue;
            }

            if (config.getIntegerList("item." + itemKey + ".slots").size() != 0) {
                for (Integer index : config.getIntegerList("item." + itemKey + ".slots")) {
                    inv.setItem(index, icon(config, itemKey));
                }
            }
            else {
                if (Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) >= (page-1)*config.getIntegerList("index").size() && Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) < page*config.getIntegerList("index").size()) {
                    inv.setItem(config.getIntegerList("index").get(Integer.parseInt(itemKey.replaceAll("[^0-9]", "")) - (page-1)*config.getIntegerList("index").size()), icon(config, itemKey));
                }
            }
        }

        Reference.FilePath = plugin.getDataFolder() + "//" + filename;
        Reference.Page = page;

        return inv;
    }

    public static ItemStack icon(YamlConfiguration config, String key) {
        String material = config.getString("item." + key + ".material");
        Integer customModelData = config.getInt("item." + key + ".customModelData");
        String name = config.getString("item." + key + ".name");
        List<String> lore = config.getStringList("item." + key + ".lore");
        Boolean color = config.getBoolean("item." + key + ".color");

        ItemStack item = new ItemStack(Material.valueOf(material));

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setCustomModelData(customModelData);
        meta.setDisplayName(name);
        if (lore.size() != 0) meta.setLore(lore);
        if (color) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        return item;
    }
    public static ItemStack color(YamlConfiguration config, String key, Integer page) {
        String name = config.getString("item." + key + ".name");
        List<String> lore = config.getStringList("item." + key + ".lore");

        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setCustomModelData(10);
        meta.setDisplayName(name);
        if (lore.size() != 0) meta.setLore(lore);

        if (config.getString("item." + key + ".type").equals("COLOR_MAIN")) {
            ((LeatherArmorMeta) meta).setColor(Color.fromRGB(config.getIntegerList("item." + key + ".dye").get(0), config.getIntegerList("item." + key + ".dye").get(1),config.getIntegerList("item." + key + ".dye").get(2)));
            meta.addItemFlags(ItemFlag.HIDE_DYE);
        }
        if (config.getString("item." + key + ".type").equals("COLOR_SUB")) {
            String color = config.getStringList("color.subColor" + page).get(Integer.parseInt(key.replaceAll("[^0-9]", ""))-1);
            ((LeatherArmorMeta) meta).setColor(Color.fromRGB(Integer.parseInt(color.split(";")[0]), Integer.parseInt(color.split(";")[1]), Integer.parseInt(color.split(";")[2])));
            meta.addItemFlags(ItemFlag.HIDE_DYE);
        }

        item.setItemMeta(meta);

        return item;
    }
    public static ItemStack cos() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Reference.CosPath));
        String key = Reference.CosKey;

        String material = config.getString("item." + key + ".material");
        Integer customModelData = config.getInt("item." + key + ".customModelData");
        String name = config.getString("item." + key + ".name");
        List<String> lore = config.getStringList("item." + key + ".lore");

        ItemStack item = new ItemStack(Material.valueOf(material));

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setCustomModelData(customModelData);
        meta.setDisplayName(name);
        if (lore.size() != 0) meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}
