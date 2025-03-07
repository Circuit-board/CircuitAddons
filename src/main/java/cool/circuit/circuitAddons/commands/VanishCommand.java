package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor {
    private static final List<Player> vanishedPlayers = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.vanish")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }


        if(vanishedPlayers.contains(player)) {
            player.setInvisible(false);
            player.sendMessage(ChatColor.RED + "You are now visible!");
            vanishedPlayers.remove(player);
        } else {
            player.setInvisible(true);
            player.sendMessage(ChatColor.GREEN + "You are now invisible!");
            vanishedPlayers.add(player);
        }

        return true;
    }
}
