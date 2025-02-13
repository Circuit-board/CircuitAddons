package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import cool.circuit.circuitAddons.menusystem.menus.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class circuitaddons implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return false; // Stop command execution if not a player
        }

        // If there is one argument
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "menu" -> {
                    if(getSettingsFile().getBoolean("guis.main.enabled")) {
                        main mm = new main(getMenuUtility(player));
                        mm.open();
                    } else {
                        player.sendMessage(ChatColor.RED + "This feature isn't enabled!");
                    }
                }
                case "reload" -> {
                    reload();
                    player.sendMessage(getInstance().PREFIX + "Reload Successful!");
                }
                case "help" -> {
                    player.sendMessage(getInstance().PREFIX + "Use /ca menu for detailed instructions on stuff :)");
                }
                case "info" -> {
                    player.sendMessage(getInstance().PREFIX + "Version: 1.0, Author, CircuitBoard (Circuit)");
                }
                case "preview" -> {
                    player.sendMessage(ChatColor.YELLOW + "Join Message: " + getInstance().PREFIX + getInstance().getConfig().get("messages.joinMessage"));
                    player.sendMessage(ChatColor.YELLOW + "First Join Message: " + getInstance().PREFIX + getInstance().getConfig().get("messages.firstJoinMessage"));
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
        } else if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please provide an argument!");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commandArgs = new ArrayList<>();

        // If no arguments or the first argument is incomplete
        if (args.length == 1) {
            commandArgs.add("help");
            commandArgs.add("info");
            commandArgs.add("menu");
            commandArgs.add("reload");
            commandArgs.add("preview");
            commandArgs.add("update");

            // Filter suggestions based on user input
            List<String> results = new ArrayList<>();
            for (String arg : commandArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(arg);
                }
            }
            return results;
        } else if (args.length > 1) {
            return List.of("");
        }

        return null;
    }
}
