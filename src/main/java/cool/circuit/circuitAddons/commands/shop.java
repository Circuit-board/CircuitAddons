package cool.circuit.circuitAddons.commands;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cool.circuit.circuitAddons.CircuitAddons.*;
import static cool.circuit.circuitAddons.vault.CircuitBanks.*;

public class shop implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!sender.hasPermission("circuitaddons.shop")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("browse")) {
            cool.circuit.circuitAddons.menusystem.menus.shop menu = new cool.circuit.circuitAddons.menusystem.menus.shop(getMenuUtility(player));
            menu.open();
            return true;
        }

        if(!(args.length >= 1)) return false;

        if(args.length < 2 && !args[0].equalsIgnoreCase("shop")) {
            ((Player) sender).sendMessage("Usage: /shop <buy|sell> <item_name_in_full_caps>");
            return false;
        } else if(args[0].equalsIgnoreCase("shop") && args.length == 1) {
        }

        joinSync();

        // Check if the item exists in the configuration
        if (!items.contains("items." + args[1])) {
            player.sendMessage(ChatColor.RED + "Invalid item name!");
            return false;
        }

        // Get the price and material of the item
        double price = items.getDouble("items." + args[1] + ".price");
        Material material = Material.getMaterial(args[1].toUpperCase());

        if (material == null) {
            player.sendMessage(ChatColor.RED + "Invalid material!");
            return false;
        }

        // Use getBalance() to get the player's balance directly
        double balance = getEconomy().bankBalance("bank_" + player.getName()).balance;


        // Check if the player has enough balance
        if(args[0].equalsIgnoreCase("buy")) {
            if (balance >= price) {
                getEconomy().bankWithdraw("bank_" + player.getName(), price);
                ItemStack item = new ItemStack(material);
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "You have successfully bought " + args[1] + " for " + price + "!");
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 100.0f, 1.0f);
            } else {
                player.sendMessage(ChatColor.RED + "You don't have enough money to buy this item!");
            }
        } else if(args[0].equalsIgnoreCase("sell")) {
            Material materialToSell = Material.getMaterial(args[1].toUpperCase());
            if (materialToSell == null) {
                player.sendMessage(ChatColor.RED + "Invalid item!");
                return false;
            }

            // Check if the player has at least one of the item
            if (player.getInventory().contains(materialToSell)) {
                int amount = player.getInventory().all(materialToSell).values().stream().mapToInt(ItemStack::getAmount).sum();
                double salePrice = price / 2;  // Adjust the sale price as needed

                // Remove item from inventory and deposit balance
                player.getInventory().removeItem(new ItemStack(materialToSell, 1));  // Remove 1 item at a time, adjust as needed
                getEconomy().bankDeposit("bank_" + player.getName(), salePrice);

                player.sendMessage(ChatColor.GREEN + "You have successfully sold " + args[1] + " for " + salePrice + "!");
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 100.0f, 1.0f);
            } else {
                player.sendMessage(ChatColor.RED + "You don't have any " + args[1] + " to sell!");
            }
            updateBanksFile(player);
            saveSettings();
            sync();
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("buy", "sell","browse");
        } else if (args.length == 2) {
            if (items.contains("items")) {  // Ensure "items" exists
                if (items.getConfigurationSection("items") != null) {  // Prevent null reference
                    return new ArrayList<>(items.getConfigurationSection("items").getKeys(false));
                }
            }
        }
        return List.of();
    }
}
