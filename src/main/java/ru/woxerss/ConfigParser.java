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
import java.util.Vector;

import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ConfigParser extends PlaceholderExpansion {
    private final String VERSION = "1.2";

    HashMap<String, JSONObject> settings = new HashMap<>();

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
                this.getPlaceholderAPI().getLogger().warning("SB-Placeholder-Addon: Cannot make new folder!");
                return false;
            }
        }

        File exampleFile = new File(path.toString(), "example.json");

        if (!exampleFile.exists()) {
            try (PrintWriter pw = new PrintWriter(exampleFile)) {
                HashMap<String,Object> exampleJson = new HashMap<>();
                exampleJson.put("version", VERSION);
                exampleJson.put("author", "Woxerss");
                JSONObject data = new JSONObject(exampleJson);
                data.writeJSONString(pw);
            } catch (FileNotFoundException e) {
                this.getPlaceholderAPI().getLogger().warning("SB-Placeholder-Addon: Exception while writing to example file!");
                this.getPlaceholderAPI().getLogger().warning(e.toString());
                return false;
            } catch (IOException e) {
                this.getPlaceholderAPI().getLogger().warning("SB-Placeholder-Addon: Exception while dumping example json to file!");
                this.getPlaceholderAPI().getLogger().warning(e.toString());
                e.printStackTrace();
            }
        }
            
        for (final File file : path.toFile().listFiles()) {
            this.getPlaceholderAPI().getLogger().info("SB-Placeholder-Addon: Reading " + file.getName());

            JSONParser parser = new JSONParser();
            try {
                JSONObject data = (JSONObject) parser.parse(new FileReader(file));
                settings.put(file.getName().replace(".json", ""), data);
            } catch (IOException | ParseException e) {
                this.getPlaceholderAPI().getLogger().warning("SB-Placeholder-Addon: Exception while reading " + file.getName());
                this.getPlaceholderAPI().getLogger().warning(e.toString());
            }
        }

        Upgrades.load(getPlaceholderAPI());

        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        String paramsArray[] = params.split("_");
        Vector<String> paramsVector = new Vector<>();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < paramsArray.length; i++) {
            if (paramsArray[i].startsWith("$")) {
                sb.append(paramsArray[i] + "_");
                i++;

                while (!paramsArray[i].endsWith("$")) {
                    sb.append(paramsArray[i] + "_");
                    i++;
                }

                sb.append(paramsArray[i]);

                paramsVector.add(sb.toString().replace("$", "%"));
            } else {
                paramsVector.add(paramsArray[i]);
            }
        }

        if (paramsVector.get(0).equalsIgnoreCase("json")) {
            if (paramsVector.size() == 3) {
                JSONObject data = settings.get(paramsVector.get(1));
                if (data != null) {
                    return data.get(paramsVector.get(2)).toString();
                }
            }
        }

        if (paramsVector.get(0).equalsIgnoreCase("levelscount")) {
            if (paramsVector.size() == 2) {
                return Upgrades.getLevelsCount(paramsVector.get(1));
            }
        }

        if (paramsVector.get(0).equalsIgnoreCase("bordersize")) {
            if (paramsVector.size() == 2) {
                return Upgrades.getBorderSize(offlinePlayer, paramsVector.get(1));
            }
        }

        if (paramsVector.get(0).equalsIgnoreCase("normalgenrates")) {
            if (paramsArray.length == 3) {
                return Upgrades.getNormalGeneratorRates(offlinePlayer, paramsVector.get(1), paramsVector.get(2));
            }

            if (paramsArray.length == 4) {
                return Upgrades.getNormalGeneratorRates(offlinePlayer, paramsVector.get(1), paramsVector.get(2) + "_" + paramsVector.get(3));
            }
        }
        
        return null;
    }
}
