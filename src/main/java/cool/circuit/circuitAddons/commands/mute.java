package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class mute implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return false;
        }

        UUID targetUUID = target.getUniqueId();

        if (MuteMap.containsKey(targetUUID)) {
            sender.sendMessage(ChatColor.RED + "That player is already muted!");
            return false;
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline.");
            return true;
        }

        // Mute player
        MuteMap.put(target.getUniqueId(), true);
        sender.sendMessage(ChatColor.RED + "Muted " + target.getName());
        target.sendMessage(ChatColor.RED + "You have been muted!");

        mutes.set("mutes." + target.getUniqueId(),true);
        try {
            mutes.save(mutesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player i : Bukkit.getOnlinePlayers()) {
                players.add(i.getName());
            }
            return players;
        }
        return List.of();
    }
}
