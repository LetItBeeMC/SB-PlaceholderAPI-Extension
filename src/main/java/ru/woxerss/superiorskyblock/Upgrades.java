package ru.woxerss.superiorskyblock;

import java.io.File;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

/**
 * @deprecated
 */
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

    public static String getNormalGeneratorRates(OfflinePlayer offlinePlayer, String level, String position, String material) {
        if (level.startsWith("%") & level.endsWith("%")) {
            level = PlaceholderAPI.setPlaceholders(offlinePlayer, level);
        }

        if (position.equals("1")) {
            String valuePath = "upgrades.generator-rates." + level + ".generator-rates.normal." + material;
            String value = config.getString(valuePath);
            return convertRatesToProcents(value);
        }

        if (position.equals("2")) {
            if (isNumeric(level)) {
                int levelInt = Integer.parseInt(level);
                if (levelInt < config.getConfigurationSection("upgrades.generator-rates").getKeys(false).size()) {
                    levelInt++;
                    String valuePath = "upgrades.generator-rates." + levelInt + ".generator-rates.normal." + material;
                    String value = config.getString(valuePath);
                    return convertRatesToProcents(value);
                } else {
                    return "MAX";
                }
            }
        }

        return null;
    }

    public static String convertRatesToProcents(String rate) {
        if (isNumeric(rate)) {
            int rateInt = Integer.parseInt(rate);
            return String.format("%.2f", rateInt / 10000.0 * 100);
        } else {
            return null;
        }
    }

    public static boolean isNumeric(String value) {
        if (value.matches("[0-9]+[\\.]?[0-9]*")) {
            return true;
        } else {
            return false;
        }
    }
}
