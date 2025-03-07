package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class setspawn implements CommandExecutor {
    public setspawn() {
        loadSettings(); // Ensure file is loaded when the class is initialized
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.setspawn")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        Location location = player.getLocation();

        String path = "spawn.";
        spawn.set(path + ".world", location.getWorld().getName());
        spawn.set(path + ".x", location.getX());
        spawn.set(path + ".y", location.getY());
        spawn.set(path + ".z", location.getZ());
        spawn.set(path + ".yaw", location.getYaw());
        spawn.set(path + ".pitch", location.getPitch());

        saveSettings();
        player.sendMessage(getInstance().PREFIX + "Spawn set successfully!");


        return true;
    }

    private void saveSettings() {
        try {
            spawn.save(spawnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        spawnFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "spawn.yml");

        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        spawn = YamlConfiguration.loadConfiguration(spawnFile);
    }
}
