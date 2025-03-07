package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

import static cool.circuit.circuitAddons.CircuitAddons.PREFIX;
import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;

public class banks implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.banks")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        String bankName = "bank_" + player.getName();

        // Log to console for debugging
        player.sendMessage(PREFIX + "Banks: " + getEconomy().getBanks());

        return true;
    }
}
