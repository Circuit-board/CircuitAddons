package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class sethome implements TabExecutor {

    public sethome() {
        loadSettings(); // Ensure file is loaded when the class is initialized
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please provide an argument!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.sethome")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        Location location = player.getLocation();

        // Save home location
        String path = "homes." + player.getUniqueId() + "." + args[0];
        homes.set(path + ".world", location.getWorld().getName());
        homes.set(path + ".x", location.getX());
        homes.set(path + ".y", location.getY());
        homes.set(path + ".z", location.getZ());
        homes.set(path + ".yaw", location.getYaw());
        homes.set(path + ".pitch", location.getPitch());

        saveSettings();

        player.sendMessage(getInstance().PREFIX + "Home set!");

        return true;
    }

    private void saveSettings() {
        try {
            homes.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        homesFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            try {
                homesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        homes = YamlConfiguration.loadConfiguration(homesFile);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of("");
    }
}
