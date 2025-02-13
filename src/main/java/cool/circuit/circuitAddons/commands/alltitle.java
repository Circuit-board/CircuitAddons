package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class alltitle implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false; // Stop command execution if not a player
        }

        Player player = (Player) sender;

        if(CircuitAddons.getSettingsFile().getBoolean("guis.alltitle.enabled")) {

            if (args.length < 2) {
                player.sendMessage("Usage: /alltitle <title/subtitle> <text>");
                return false;
            }

            String type = args[0].toLowerCase();
            String fullText = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();

            if (type.equals("title")) {
                // Split by up to two commas
                String[] parts = fullText.split(",", 3);
                String title = parts[0].trim();
                String subtitle = parts.length > 1 ? parts[1].trim() : "";
                String actionbar = parts.length > 2 ? parts[2].trim() : "";

                for (Player i : Bukkit.getOnlinePlayers()) {
                    i.sendTitle(ChatColor.translateAlternateColorCodes('&',title), ChatColor.translateAlternateColorCodes('8',subtitle), 10, 40, 10);
                    if (!actionbar.isEmpty()) {
                        i.sendActionBar(ChatColor.translateAlternateColorCodes('&',actionbar)); // Now sending action bar to all players
                    }
                }
            } else if (type.equals("subtitle")) {
                for (Player i : Bukkit.getOnlinePlayers()) {
                    i.sendTitle("", ChatColor.translateAlternateColorCodes('&',fullText), 10, 40, 10); // Fixed: Sending to all players
                }
            } else if (type.equals("actionbar")) {
                if (args.length < 2) {
                    player.sendMessage(getInstance().PREFIX + "Usage: /command actionbar <message>");
                    return false;
                }
                for (Player i : Bukkit.getOnlinePlayers()) {
                    i.sendActionBar(fullText); // Fixed: Sending action bar to all players
                }
            } else {
                player.sendMessage(getInstance().PREFIX + "Invalid option! Use 'title', 'subtitle', or 'actionbar'.");
                return false;
            }
        } else {
            player.sendMessage(ChatColor.RED + "This feature isn't enabled!");
        }




        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> commandArgs = new ArrayList<>();

        if (args.length == 1) {
            commandArgs.add("title");
            commandArgs.add("subtitle");
            commandArgs.add("actionbar");

            // Filter suggestions based on user input
            List<String> results = new ArrayList<>();
            for (String arg : commandArgs) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(arg);
                }
            }
            return results;
        } else if (args.length == 2 || args.length == 3) {
            return List.of("");
        }

        return null; // Avoid returning null (prevents errors)
    }
}
