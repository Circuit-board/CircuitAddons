package cool.circuit.circuitAddons.vault;

import cool.circuit.circuitAddons.CircuitAddons;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CircuitChat extends Chat {
    private final HashMap<UUID, String> prefixes = new HashMap<>();

    public CircuitChat(Permission perms) {
        super(perms);
    }

    public void saveChat(File file, FileConfiguration fileConfig) {
        // Convert UUID keys to String for saving
        HashMap<String, String> tempMap = new HashMap<>();
        for (UUID uuid : prefixes.keySet()) {
            tempMap.put(uuid.toString(), prefixes.get(uuid));
        }

        fileConfig.set("prefixes", tempMap);
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadChat(FileConfiguration fileConfig) {
        if (fileConfig.getConfigurationSection("prefixes") == null) {
            System.out.println("No prefixes found in the config.");
            return;
        }

        prefixes.clear();
        for (String key : fileConfig.getConfigurationSection("prefixes").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String prefix = fileConfig.getString("prefixes." + key);
                prefixes.put(uuid, prefix);
                System.out.println("Loaded prefix: " + prefix + " for " + uuid);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID in prefixes.yml: " + key);
            }
        }
    }



    @Override
    public String getName() {
        return "CircuitChat";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPlayerPrefix(String world, String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return "";

        return prefixes.getOrDefault(player.getUniqueId(), "");
    }

    @Override
    public void setPlayerPrefix(String world, String playerName, String prefix) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return;

        prefixes.put(player.getUniqueId(), prefix + " ");
        player.setDisplayName(prefix + " " + player.getName());
        player.setPlayerListName(prefix + " " + player.getName());
        saveChat(CircuitAddons.prefixesFile, CircuitAddons.prefixes);

        // Reload the prefix from storage
        loadChat(CircuitAddons.prefixes);
    }


    @Override
    public String getPlayerSuffix(String world, String playerName) {
        return "";
    }

    @Override
    public void setPlayerSuffix(String world, String playerName, String suffix) {
        // Not implemented
    }

    @Override
    public String getGroupPrefix(String world, String group) {
        return "";
    }

    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        // Not implemented
    }

    @Override
    public String getGroupSuffix(String world, String group) {
        return "";
    }

    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        // Not implemented
    }

    @Override
    public int getPlayerInfoInteger(String world, String playerName, String node, int defaultValue) {
        return defaultValue;
    }

    @Override
    public void setPlayerInfoInteger(String world, String playerName, String node, int value) {
        // Not implemented
    }

    @Override
    public int getGroupInfoInteger(String world, String group, String node, int defaultValue) {
        return defaultValue;
    }

    @Override
    public void setGroupInfoInteger(String world, String group, String node, int value) {
        // Not implemented
    }

    @Override
    public double getPlayerInfoDouble(String world, String playerName, String node, double defaultValue) {
        return defaultValue;
    }

    @Override
    public void setPlayerInfoDouble(String world, String playerName, String node, double value) {
        // Not implemented
    }

    @Override
    public double getGroupInfoDouble(String world, String group, String node, double defaultValue) {
        return defaultValue;
    }

    @Override
    public void setGroupInfoDouble(String world, String group, String node, double value) {
        // Not implemented
    }

    @Override
    public boolean getPlayerInfoBoolean(String world, String playerName, String node, boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public void setPlayerInfoBoolean(String world, String playerName, String node, boolean value) {
        // Not implemented
    }

    @Override
    public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public void setGroupInfoBoolean(String world, String group, String node, boolean value) {
        // Not implemented
    }

    @Override
    public String getPlayerInfoString(String world, String playerName, String node, String defaultValue) {
        return defaultValue;
    }

    @Override
    public void setPlayerInfoString(String world, String playerName, String node, String value) {
        // Not implemented
    }

    @Override
    public String getGroupInfoString(String world, String group, String node, String defaultValue) {
        return defaultValue;
    }

    @Override
    public void setGroupInfoString(String world, String group, String node, String value) {
        // Not implemented
    }
}
