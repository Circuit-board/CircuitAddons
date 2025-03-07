package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.getInstance;
import static cool.circuit.circuitAddons.CircuitAddons.homes;

public class home implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if(!sender.hasPermission("circuitaddons.home")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /home <name>");
            return false;
        }

        String path = "homes." + player.getUniqueId() + "." + args[0];

        if (homes.getString(path + ".world") == null) {
            sender.sendMessage(ChatColor.RED + "Home '" + args[0] + "' does not exist!");
            return false;
        }

        Location location = new Location(
                Bukkit.getWorld(homes.getString(path + ".world")),
                homes.getDouble(path + ".x"),
                homes.getDouble(path + ".y"),
                homes.getDouble(path + ".z"),
                (float) homes.getDouble(path + ".yaw"),
                (float) homes.getDouble(path + ".pitch")
        );

        player.teleport(location);
        player.sendMessage(getInstance().PREFIX +  "Teleported to home '" + args[0] + "'!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return List.of();

        String path = "homes." + player.getUniqueId();
        if (homes.getConfigurationSection(path) != null) {
            return new ArrayList<>(homes.getConfigurationSection(path).getKeys(false));
        }
        return List.of();
    }
}
