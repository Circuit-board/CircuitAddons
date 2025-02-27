package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static cool.circuit.circuitAddons.CircuitAddons.nicksList;
import static cool.circuit.circuitAddons.CircuitAddons.nicksListFile;

public class unnick implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        boolean isNicked = nicksList.getBoolean("nicks." + player.getName() + ".state", false);
        if (!isNicked) {
            sender.sendMessage(ChatColor.RED + "You are not nicked!");
            return false;
        }

        // Reset nickname settings
        nicksList.set("nicks." + player.getName() + ".state", false);
        nicksList.set("nicks." + player.getName() + ".text", null); // Remove nickname from config
        player.setDisplayName(player.getName()); // Reset display name
        player.setPlayerListName(player.getName()); // Reset tablist name

        try {
            nicksList.save(nicksListFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while saving your nickname.");
            return false;
        }

        player.sendMessage(ChatColor.GREEN + "Your nickname has been removed!");
        return true;
    }
}
