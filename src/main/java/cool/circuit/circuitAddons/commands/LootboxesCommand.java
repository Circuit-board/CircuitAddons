package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.managers.Lootbox;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;
import static cool.circuit.circuitAddons.managers.LootboxManager.lootboxes;

public class LootboxesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        // Check for permission
        if (!player.hasPermission("circuitaddons.lootboxes")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        // No arguments: List available lootboxes
        if (args.length == 0) {
            player.sendMessage(ChatColor.GREEN + "Available Lootboxes:");
            if (lootboxes.isEmpty()) {
                player.sendMessage(ChatColor.RED + "No lootboxes available.");
            } else {
                lootboxes.forEach(lootbox -> {
                    player.sendMessage(ChatColor.GOLD + lootbox.name() + ": " + ChatColor.YELLOW + "$" + lootbox.cost());
                });
            }
            player.sendMessage(ChatColor.YELLOW + "Usage: /lootboxes <lootbox_name> to open a lootbox.");
            return true;
        }

        // Normalize input for case-insensitivity
        String lootboxName = args[0].toLowerCase();
        Lootbox lootbox = lootboxes.stream()
                .filter(lb -> lb.name().equalsIgnoreCase(lootboxName))
                .findFirst()
                .orElse(null);

        if (lootbox == null) {
            player.sendMessage(ChatColor.RED + "Lootbox '" + args[0] + "' does not exist.");
            return false;
        }

        // Check if the player has enough money to buy the lootbox
        String bankAccount = "bank_" + player.getName();
        double balance = getEconomy().bankBalance(bankAccount).balance;

        if (balance < lootbox.cost()) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to buy this lootbox!");
            return false;
        }

        // Withdraw money and ensure transaction success
        if (getEconomy().bankWithdraw(bankAccount, lootbox.cost()).transactionSuccess()) {
            lootbox.open(player);
            player.sendMessage(ChatColor.GREEN + "You successfully opened the " + ChatColor.GOLD + lootbox.name() + ChatColor.GREEN + " lootbox!");
        } else {
            player.sendMessage(ChatColor.RED + "Transaction failed. Please try again.");
        }

        return true;
    }
}
