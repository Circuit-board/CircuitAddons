package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.BlackListedWords;
import static cool.circuit.circuitAddons.CircuitAddons.blacklistFile;

public class blacklist implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length != 2) return false; // Simplified argument check

        if(!sender.hasPermission("circuitaddons.blacklist")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        String action = args[0].toLowerCase();
        String word = args[1].toLowerCase();

        if (action.equals("add")) {
            if (BlackListedWords.contains(word)) {
                sender.sendMessage(ChatColor.RED + "That word is already blacklisted!");
                return true;
            }
            BlackListedWords.add(word);
            CircuitAddons.blacklist.set("blacklist", BlackListedWords); // Fixed key
            saveBlacklist();
        } else if (action.equals("remove")) {
            if (!BlackListedWords.contains(word)) {
                sender.sendMessage(ChatColor.RED + "That word is not in the blacklist!");
                return true;
            }
            BlackListedWords.remove(word);
            CircuitAddons.blacklist.set("blacklist", BlackListedWords); // Fixed key
            saveBlacklist();
        } else {
            return false;
        }

        return true;
    }

    public static void saveBlacklist() {
        try {
            CircuitAddons.blacklist.save(blacklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("add", "remove");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return new ArrayList<>(BlackListedWords);
        }

        return List.of();
    }
}
