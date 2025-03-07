package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import cool.circuit.circuitAddons.menusystem.menus.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static cool.circuit.circuitAddons.CircuitAddons.*;
import static cool.circuit.circuitAddons.managers.LootboxManager.saveLootboxes;

public class circuitaddons implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false; // Stop command execution if not a player
        }

        if(!sender.hasPermission("circuitaddons.circuitaddons")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        // If there is one argument
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "menu" -> {
                    if(getSettings().getBoolean("guis.main.enabled")) {
                        main mm = new main(getMenuUtility(player));
                        mm.open();
                    } else {
                        player.sendMessage(ChatColor.RED + "This feature isn't enabled!");
                    }
                }
                case "reload" -> {
                    reload();
                    /*Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
                        Bukkit.getPluginManager().disablePlugin(getInstance());
                        Bukkit.getPluginManager().enablePlugin(getInstance());
                    }, 5L); // 5-tick delay to prevent issues
                    */player.sendMessage(getInstance().PREFIX + "Reload Successful!");
                }
                case "help" -> {
                    player.sendMessage(getInstance().PREFIX + "Use /ca menu for detailed instructions on stuff :)");
                }
                case "info" -> {
                    player.sendMessage(getInstance().PREFIX + "Version: 1.2.1, Author, CircuitBoard (Circuit)");
                }

                case "update" -> {
                    if (instance.settingsFile.exists()) {
                        if (instance.settingsFile.delete()) {
                            instance.getLogger().info("Settings file deleted. Reloading...");
                            reload();
                        } else {
                            instance.getLogger().warning("Failed to delete settings file.");
                        }
                    } else {
                        instance.getLogger().info("Settings file not found. Reloading anyway...");
                        reload();
                    }
                }
                default -> {
                    player.sendMessage(ChatColor.RED + "That's not an option!");
                    return false;
                }
            }
        } else if(args.length >= 4) {
            handleConfigCommand(sender,args);
        } else if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please provide an argument!");
        }

        return true;
    }

    private void handleConfigCommand(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ca config <set/get> <key> [value]");
            return;
        }

        if (args[1].equalsIgnoreCase("set") && args.length >= 4) {
            StringBuilder path = new StringBuilder("settings." + args[2]);
            for (int i = 3; i < args.length - 1; i++) {
                path.append(".").append(args[i]);
            }

            sender.sendMessage(ChatColor.YELLOW + "Setting path: " + path + " = " + args[args.length - 1]);
            settings.set(path.toString(), args[args.length - 1]);

            try {
                settings.save(settingsFile);
                sender.sendMessage(ChatColor.GREEN + "Config updated: " + path + " = " + args[args.length - 1]);
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + "Failed to save config!");
                e.printStackTrace();
            }

        } else if (args[1].equalsIgnoreCase("get")) {
            StringBuilder path = new StringBuilder("settings." + args[2]);
            for (int i = 3; i < args.length; i++) {
                path.append(".").append(args[i]);
            }

            Object value = settings.get(path.toString());
            sender.sendMessage(ChatColor.YELLOW + "Config value: " + path + " = " + value);
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid subcommand! Use 'set' or 'get'.");
        }
    }




    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("menu", "reload", "help", "info", "update", "config");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            return List.of("set", "get");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("set")) {
            ConfigurationSection settingsSection = settings.getConfigurationSection("settings");
            if (settingsSection != null) {
                return settingsSection.getKeys(false)
                        .stream()
                        .map(i -> i.toLowerCase(Locale.ROOT).replace("_", ""))
                        .sorted(Comparator.reverseOrder())
                        .toList();
            }
            return List.of();
        } else if (args.length == 4 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("set")) {
            ConfigurationSection subSection = settings.getConfigurationSection("settings." + args[2]);
            if (subSection != null) {
                return subSection.getKeys(false)
                        .stream()
                        .map(i -> i.toLowerCase(Locale.ROOT).replace("_", ""))
                        .sorted(Comparator.reverseOrder())
                        .toList();
            }
            return List.of();
        } else if (args.length == 5 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("set")) {
            ConfigurationSection deeperSection = settings.getConfigurationSection("settings." + args[2] + "." + args[3]);
            if (deeperSection != null) {
                return deeperSection.getKeys(false)
                        .stream()
                        .map(i -> i.toLowerCase(Locale.ROOT).replace("_", ""))
                        .sorted(Comparator.reverseOrder())
                        .toList();
            }
            return List.of();
        }
        return null;
    }
}
