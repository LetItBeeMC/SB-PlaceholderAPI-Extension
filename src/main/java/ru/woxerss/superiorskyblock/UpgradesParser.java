package ru.woxerss.superiorskyblock;

import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

public class UpgradesParser {

    /**
     * Parser initialization
     * 
     * @param plugin - instance of placeholder plugin
     */
    public static void load(PlaceholderAPIPlugin plugin) {
        SuperiorSkyblockHook.load();
    }
    
    /**
     * Get upgrades level count
     * 
     * @param name - upgrade name
     * @return Level count
     */
    public static String getLevelsCount(String name) {
        return SuperiorSkyblockHook.getUpgradeMaxLevel(name) + "";
    }

    /**
     * Get border size at level
     * 
     * @param offlinePlayer - player
     * @param level - level of normal (overworld) generator
     * @return Border size at level
     */
    public static String getBorderSize(OfflinePlayer offlinePlayer, String level) {
        if (level.startsWith("%") & level.endsWith("%")) {
            level = PlaceholderAPI.setPlaceholders(offlinePlayer, level);
        }

        return SuperiorSkyblockHook.getBorderSize(Integer.parseInt(level)) + "";
    }

    /**
     * Get generator rate in procent
     * 
     * @param offlinePlayer - player
     * @param level - level of normal (overworld) generator
     * @param material - generator material
     * @return Generator rate for material in procent
     */
    public static String getNormalGeneratorRates(OfflinePlayer offlinePlayer, String level, String material) {
        if (level.startsWith("%") & level.endsWith("%")) {
            level = PlaceholderAPI.setPlaceholders(offlinePlayer, level);
        }

        return SuperiorSkyblockHook.getProcentGeneratorRate(Integer.parseInt(level), Environment.NORMAL, material);
    }

    /**
     * Get formatted string with difference between current and next generator level
     * 
     * @param offlinePlayer - player
     * @param material - generator material
     * @param arrowColor - color code
     * @param valueColor - color code
     * @return String with rates difference (3.00 --> 3.15) 
     */
    public static String getDifferenceGeneratorRates(OfflinePlayer offlinePlayer, String material, String arrowColor, String valueColor) {
        Integer maxLevel = SuperiorSkyblockHook.getUpgradeMaxLevel("generator-rates");
        Integer currentlevel = SuperiorSkyblockHook.getPlayersUpgradeLevel(offlinePlayer, "generator-rates");

        if (currentlevel < maxLevel) {
            String currentRate = SuperiorSkyblockHook.getProcentGeneratorRate(currentlevel, Environment.NORMAL, material);
            String nextRate = SuperiorSkyblockHook.getProcentGeneratorRate(currentlevel + 1, Environment.NORMAL, material);
            
            return currentRate + arrowColor + " --> " + valueColor + nextRate;
        } else {
            String currentRate = SuperiorSkyblockHook.getProcentGeneratorRate(currentlevel, Environment.NORMAL, material);
            return currentRate + valueColor + " MAX";
        }
    }
}

