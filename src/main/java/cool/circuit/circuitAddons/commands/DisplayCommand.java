package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.display")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /display [text|item] [text|item_name]");
            return false;
        }


        if(!args[0].equals("text") && !args[0].equals("item")) {
            sender.sendMessage(ChatColor.RED + "Usage: /display [text|item] [text|item_name]");
            return false;
        }

        Location location = player.getLocation();
        location.setY(location.getY() + 2);
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());

        switch (args[0]) {
            case "text" -> {
                String text = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                TextDisplay display = player.getWorld().spawn(location, TextDisplay.class);
                display.setText(text);
                display.setAlignment(TextDisplay.TextAlignment.CENTER);
                display.setBillboard(Display.Billboard.CENTER);
            }
            case "item" -> {
                ItemStack item = new ItemStack(Material.valueOf(args[1]));
                ItemDisplay display = player.getWorld().spawn(location, ItemDisplay.class);
                display.setItemStack(item);
            }
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            return List.of("item", "text");
        }
        if(args[0].equals("item") && args.length == 2) {
            return Arrays.stream(Material.values()).map(Enum::name).toList();
        }

        return List.of();
    }
}
