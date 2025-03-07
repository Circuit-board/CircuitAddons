package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.getInstance;
import static cool.circuit.circuitAddons.CircuitAddons.homes;

public class spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if(!sender.hasPermission("circuitaddons.spawn")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        String path = "spawn";

        Location location = new Location(
                Bukkit.getWorld(CircuitAddons.spawn.getString(path + ".world")),
                CircuitAddons.spawn.getDouble(path + ".x"),
                CircuitAddons.spawn.getDouble(path + ".y"),
                CircuitAddons.spawn.getDouble(path + ".z"),
                (float) CircuitAddons.spawn.getDouble(path + ".yaw"),
                (float) CircuitAddons.spawn.getDouble(path + ".pitch")
        );

        player.teleport(location);
        player.sendMessage(getInstance().PREFIX + "Teleported to spawn!");

        return true;
    }
}
