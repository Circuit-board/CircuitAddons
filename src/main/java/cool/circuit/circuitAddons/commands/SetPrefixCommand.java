package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.vault.CircuitChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetPrefixCommand implements CommandExecutor {
    private final CircuitChat circuitChat;

    public SetPrefixCommand(CircuitChat circuitChat) {
        this.circuitChat = circuitChat;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setprefix <player> <prefix>");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.prefix")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return false;
        }

        String prefix = ChatColor.translateAlternateColorCodes('&', args[1]); // Support color codes
        circuitChat.setPlayerPrefix(target, prefix);

        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s prefix to " + prefix);
        return true;
    }
}
