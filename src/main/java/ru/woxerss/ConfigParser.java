package ru.woxerss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ConfigParser extends PlaceholderExpansion {
    private final String VERSION = "1.1";
    HashMap<String, JSONObject> settings = new HashMap<>();

    FileConfiguration upgradesConfig;

    @Override
    public String getIdentifier() {
        return "configparser";
    }

    @Override
    public String getAuthor() {
        return "Woxerss";
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public boolean canRegister() {
        Path path = Paths.get(this.getPlaceholderAPI().getDataFolder().getAbsolutePath(), "custom_json"); 

        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (!path.toFile().mkdir()) {
                this.getPlaceholderAPI().getLogger().warning("CustomJsonAddon: Cannot make new folder!");
                return false;
            }
        }

        File exampleFile = new File(path.toString(), "example.json");

        if (!exampleFile.exists()) {
            try (PrintWriter pw = new PrintWriter(exampleFile)) {
                HashMap<String,Object> exampleJson = new HashMap<>();
                exampleJson.put("version", "1.0");
                exampleJson.put("author", "Woxerss");
                JSONObject data = new JSONObject(exampleJson);
                data.writeJSONString(pw);
            } catch (FileNotFoundException e) {
                this.getPlaceholderAPI().getLogger().warning("CustomJsonAddon: Exception while writing to example file!");
                this.getPlaceholderAPI().getLogger().warning(e.toString());
                return false;
            } catch (IOException e) {
                this.getPlaceholderAPI().getLogger().warning("CustomJsonAddon: Exception while dumping example json to file!");
                this.getPlaceholderAPI().getLogger().warning(e.toString());
                e.printStackTrace();
            }
        }
            
        for (final File file : path.toFile().listFiles()) {
            this.getPlaceholderAPI().getLogger().info("CustomJsonAddon: Reading " + file.getName());

            JSONParser parser = new JSONParser();
            try {
                JSONObject data = (JSONObject) parser.parse(new FileReader(file));
                settings.put(file.getName().replace(".json", ""), data);
            } catch (IOException | ParseException e) {
                this.getPlaceholderAPI().getLogger().warning("CustomJsonAddon: Exception while reading " + file.getName());
                this.getPlaceholderAPI().getLogger().warning(e.toString());
            }
        }

        File file = new File(this.getPlaceholderAPI().getServer().getWorldContainer() + "/plugins/SuperiorSkyblock2/modules/upgrades/config.yml");
        upgradesConfig = YamlConfiguration.loadConfiguration(file);

        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        String paramsArray[] = params.split("_");

        if (paramsArray[0].equalsIgnoreCase("json")) {
            if (paramsArray.length == 3) {
                JSONObject data = settings.get(paramsArray[1]);
                if (data != null) {
                    return data.get(paramsArray[2]).toString();
                }
            }
        }

        if (paramsArray[0].equalsIgnoreCase("upgradescount")) {
            String valuePath = "upgrades." + paramsArray[1];
            if (paramsArray.length == 2) {
                return upgradesConfig.getConfigurationSection(valuePath).getKeys(false).size() + "";
            }
        }

        if (paramsArray[0].equalsIgnoreCase("upgrades")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < paramsArray.length; i++) {
                sb.append(paramsArray[i]);
                if (i < paramsArray.length - 1) {
                    sb.append(".");
                }
            }

            String valuePath = sb.toString();

            return upgradesConfig.getString(valuePath);
        }
        
        return null;
    }
}
