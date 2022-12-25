package ru.woxerss.superiorskyblock;

import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.upgrades.Upgrade;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

public class SuperiorSkyblockHook {
    static Integer ratesSum = 0;

    /**
     * Initializing of static fields
     */
    public static void load() {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade("generator-rates");

        for (Map.Entry<String, Integer> entry : 
            upgrade.getUpgradeLevel(1).getGeneratorAmounts(Environment.NORMAL).entrySet()) {
            
            ratesSum += entry.getValue();
        }
    }

    /**
     * Get max level of upgrade
     * 
     * @param name - upgrade name
     * @return max level of upgrade
     */
    public static int getUpgradeMaxLevel(String name) {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade(name);

        if (upgrade == null) {
            return -1;
        }

        return upgrade.getMaxUpgradeLevel();
    }

    /**
     * Get border size at level
     * 
     * @param level - upgrade level
     * @return border size at level
     */
    public static int getBorderSize(int level) {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade("border-size");

        if (upgrade == null) {
            return -1;
        }

        return upgrade.getUpgradeLevel(level).getBorderSize();
    }

    /**
     * Get generator rate at level
     * 
     * @param level - upgrade level
     * @param env - bukkit Environment 
     * @param material - block type
     * @return meterial rate at level
     */
    public static int getRowGeneratorRate(int level, Environment env, String material) {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade("generator-rates");

        if (upgrade == null) {
            return -1;
        }
        
        return upgrade.getUpgradeLevel(level).getGeneratorAmount(Key.of(material), env);
    }

    /**
     * Get generator rate as procent
     * 
     * @param level - upgrade level
     * @param env - bukkit Environment 
     * @param material - block type
     * @return procent of rate
     */
    public static String getProcentGeneratorRate(int level, Environment env, String material) {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade("generator-rates");

        if (upgrade == null) {
            return null;
        }

        Integer rate = upgrade.getUpgradeLevel(level).getGeneratorAmount(Key.of(material), env);

        return String.format("%.2f", rate / (ratesSum * 1.0) * 100);
    }

    /**
     * Get player's upgrade level
     * 
     * @param offlinePlayer - player
     * @param name - upgrade name
     * @return player's upgrade level
     */
    public static int getPlayersUpgradeLevel(OfflinePlayer offlinePlayer, String name) {
        Upgrade upgrade = SuperiorSkyblockAPI.getUpgrades().getUpgrade(name);

        if (upgrade == null) {
            return -1;
        }

        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(offlinePlayer.getUniqueId());
        return superiorPlayer.getIsland().getUpgradeLevel(upgrade).getLevel();
    }
}
