package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        Player player = (Player) sender;

        if (CircuitAddons.getSettingsFile().getBoolean("guis.alltitle.enabled")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /alltitle <title/subtitle/actionbar> <text>");
                return false;
            }

            String type = args[0].toLowerCase();
            String fullText = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
            String formattedText = ChatColor.translateAlternateColorCodes('&', fullText);

            if (type.equals("title")) {
                for (Player i : Bukkit.getOnlinePlayers()) {
                    i.sendTitle(formattedText, "", 10, 40, 10);
                }
            } else if (type.equals("subtitle")) {
                for (Player i : Bukkit.getOnlinePlayers()) {
                    i.sendTitle("", formattedText, 10, 40, 10);
                }
            } else if (type.equals("actionbar")) {
                sendActionBarToAll(formattedText);
            } else {
                player.sendMessage(getInstance().PREFIX + "Invalid option! Use 'title', 'subtitle', or 'actionbar'.");
                return false;
            }
        } else {
            player.sendMessage(ChatColor.RED + "This feature isn't enabled!");
        }

        return true;
    }

    private void sendActionBarToAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Arrays.asList("title", "subtitle", "actionbar");
        }
        return new ArrayList<>();
    }
}
