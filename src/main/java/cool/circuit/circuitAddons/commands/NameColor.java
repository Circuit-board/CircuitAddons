package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static cool.circuit.circuitAddons.CircuitAddons.circuitChat;
import static cool.circuit.circuitAddons.CircuitAddons.updateTablist;

public class NameColor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /namecolor <color>");
            return false;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[0].toUpperCase());
            if (!color.isColor()) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid color! Use a valid ChatColor.");
            return false;
        }

        String prefix = (circuitChat != null) ? circuitChat.getPlayerPrefix(player) : "";
        String newName = prefix + color + player.getName();

        player.setDisplayName(newName);
        player.setPlayerListName(newName);

        updateTablist();

        player.sendMessage(ChatColor.GREEN + "Your name color has been updated!");
        return true;
    }
}
