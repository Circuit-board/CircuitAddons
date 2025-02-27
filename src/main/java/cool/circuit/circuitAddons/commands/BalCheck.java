package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;
import static cool.circuit.circuitAddons.vault.CircuitBanks.joinSync;
import static cool.circuit.circuitAddons.vault.CircuitBanks.sync;
import static cool.circuit.circuitAddons.vault.CircuitEconomy.bankAccounts;

public class BalCheck implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /balcheck <bank name>");
            return true;
        }

        joinSync();

        String targetName = args[0];

        // Check if the bank exists
        if (!getEconomy().getBanks().contains(targetName)) {
            sender.sendMessage(ChatColor.RED + "That bank does not exist!");
            return true;
        }

        EconomyResponse response = getEconomy().bankBalance(targetName);

        if (response == null || response.type != EconomyResponse.ResponseType.SUCCESS) {
            sender.sendMessage(ChatColor.RED + "Failed to retrieve balance.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + targetName + "'s bank balance: " + getEconomy().format(response.balance));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return getEconomy().getBanks();
        }
        return List.of();
    }
}
