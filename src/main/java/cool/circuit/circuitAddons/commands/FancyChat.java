package cool.circuit.circuitAddons.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FancyChat implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.fancychat")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        String fullCommand = String.join(" ", args); // Join args into a single string
        String[] parsedArgs = fullCommand.split(",", 6); // Split only at the first 5 commas

        if (parsedArgs.length < 6) {
            player.sendMessage(ChatColor.RED + "Usage: /fancychat <message>,<color>,<action>,<value>,<hoverText>,<hoverTextColor>");
            return false;
        }

        String messageText = parsedArgs[0].trim();
        String colorArg = parsedArgs[1].trim().toUpperCase();
        String actionType = parsedArgs[2].trim().toLowerCase();
        String actionValue = parsedArgs[3].trim();
        String hoverText = parsedArgs[4].trim();
        String hoverColorArg = parsedArgs[5].trim().toUpperCase();

        TextComponent message = new TextComponent(messageText);

        // Handle color
        try {
            ChatColor bukkitColor = ChatColor.valueOf(colorArg);
            message.setColor(net.md_5.bungee.api.ChatColor.of(bukkitColor.asBungee().getColor()));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid color! Use a valid ChatColor.");
            return false;
        }

        // Click action handling
        if (actionType.equals("command")) {
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, actionValue));
        } else if (actionType.equals("url")) {
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, actionValue));
        } else {
            player.sendMessage(ChatColor.RED + "Invalid action! Use 'command' or 'url'.");
            return false;
        }

        // Hover text color handling
        ChatColor hoverColor;
        try {
            hoverColor = ChatColor.valueOf(colorArg);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid hover text color! Use a valid ChatColor.");
            return false;
        }

        // Hover event
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).color(net.md_5.bungee.api.ChatColor.of(hoverColor.asBungee().getColor())).create()));

        // Send the message
        for(Player i : Bukkit.getOnlinePlayers()) {
            i.spigot().sendMessage(message);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return List.of();
    }
}
