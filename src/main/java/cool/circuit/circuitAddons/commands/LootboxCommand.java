package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.menusystem.menus.lootbox_create;
import cool.circuit.circuitAddons.menusystem.menus.lootbox_main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;
import static cool.circuit.circuitAddons.managers.LootboxManager.*;

public class LootboxCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (!player.hasPermission("circuitaddons.lootbox")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        // If no arguments and no input is active, open lootbox_main menu
        if (args.length == 0 && !isInputtingName && !isInputtingPrice) {
            new lootbox_main(getMenuUtility(player)).open();
            return true;
        }

        // Ensure there is at least one argument before accessing args[1]
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /lootbox | /lootbox set <name|price> <value>");
            return false;
        }

        // Ensure name or price input is active before allowing "set"
        if (!isInputtingName && !isInputtingPrice) {
            player.sendMessage(ChatColor.RED + "No name / price input active!");
            return false;
        }

        // Ensure the command is "set"
        if (!args[0].equalsIgnoreCase("set")) {
            player.sendMessage(ChatColor.RED + "Usage: /lootbox | /lootbox set <name|price> <value>");
            return false;
        }

        // Ensure correct number of arguments before accessing args[2]
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /lootbox set <name|price> <value>");
            return false;
        }

        // Handling name input
        if (isInputtingName) {
            lootboxNames.put(player.getUniqueId(), args[2]);
            isInputtingName = false;
            player.sendMessage(ChatColor.GREEN + "Name set to " + args[2]);
            new lootbox_create(getMenuUtility(player)).open();
            return true;
        }

        // Handling price input
        if (isInputtingPrice) {
            try {
                int price = Integer.parseInt(args[2]);
                lootboxPrices.put(player.getUniqueId(), price);
                isInputtingPrice = false;
                player.sendMessage(ChatColor.GREEN + "Price set to " + price);
                new lootbox_create(getMenuUtility(player)).open();
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid price! Please enter a valid number.");
                return false;
            }
        }

        new lootbox_main(getMenuUtility(player)).open();
        return true;
    }
}
