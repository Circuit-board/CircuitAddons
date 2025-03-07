package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class unmute implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /unmute <UUID>");
            return true;
        }
        if(!sender.hasPermission("circuitaddons.unmute")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        try {
            UUID uuid = UUID.fromString(args[0]);
            if (MuteMap.containsKey(uuid)) {
                MuteMap.remove(uuid);
                mutes.set("mutes." + uuid, null); // Remove from config
                sender.sendMessage("Successfully unmuted " + uuid + ".");

                // Save changes to file
                try {
                    mutes.save(mutesFile);
                } catch (Exception e) {
                    sender.sendMessage("Failed to save mutes.yml!");
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage("That player is not muted.");
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Invalid UUID format.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(MuteMap.keySet().stream().map(UUID::toString).toList());
        }
        return List.of();
    }
}
