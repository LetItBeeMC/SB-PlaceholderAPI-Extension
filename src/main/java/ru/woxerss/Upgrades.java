package ru.woxerss;

import java.io.File;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

public class Upgrades {
    public static FileConfiguration config;

    public static void load(PlaceholderAPIPlugin plugin) {
        File file = new File(plugin.getServer().getWorldContainer() + "/plugins/SuperiorSkyblock2/modules/upgrades/config.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public static String getLevelsCount(String name) {
        String valuePath = "upgrades." + name;
        return config.getConfigurationSection(valuePath).getKeys(false).size() + "";
    }

    public static String getBorderSize(OfflinePlayer offlinePlayer, String level) {
        if (level.startsWith("%") & level.endsWith("%")) {
            level = PlaceholderAPI.setPlaceholders(offlinePlayer, level);
        }

        String valuePath = "upgrades.border-size." + level + ".border-size";
        return config.getString(valuePath);
    }

    public static String getNormalGeneratorRates(OfflinePlayer offlinePlayer, String level, String material) {
        if (level.startsWith("%") & level.endsWith("%")) {
            level = PlaceholderAPI.setPlaceholders(offlinePlayer, level);
        }

        String valuePath = "upgrades.generator-rates." + level + ".generator-rates.normal." + material;
        return config.getString(valuePath);
    }
}
