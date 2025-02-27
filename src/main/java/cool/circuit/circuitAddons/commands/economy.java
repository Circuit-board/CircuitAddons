package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import cool.circuit.circuitAddons.vault.CircuitBanks;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.PREFIX;
import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;
import static cool.circuit.circuitAddons.utils.NumberFormatter.formatNumber;
import static cool.circuit.circuitAddons.vault.CircuitBanks.*;

public class economy implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        joinSync();

        if (args.length < 3) {
            sender.sendMessage("Usage: /economy <set|add> <player> <amount>");
            return false;
        }

        String action = args[0].toLowerCase();
        OfflinePlayer target = sender.getServer().getOfflinePlayer(args[1]);

        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            if (amount < 0) {
                sender.sendMessage("Amount must be positive.");
                return false;
            }

            String bankName = "bank_" + target.getName();
            if (!getEconomy().hasAccount(bankName)) {
                getEconomy().createBank(bankName, target);
            }


            switch (action) {
                case "set":
                    target.getPlayer().sendMessage(PREFIX + "Set " + target.getName() + "'s Balance to " + formatNumber((int) amount));
                    setBalance(bankName, (int) amount);
                    break;

                case "add":
                    addToBalance(bankName, (int) amount);
                    target.getPlayer().sendMessage(PREFIX + "Added " + formatNumber((int) amount) + " To bank: " + bankName + "!");
                    break;


                default:
                    sender.sendMessage("Invalid action. Use /economy <set|add> <player> <amount>");
                    return false;
            }
            updateBanksFile(player);
            saveSettings();
            sync();
            return true;

        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount. Please enter a valid number.");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch (args.length) {
            case 1:
                suggestions.add("set");
                suggestions.add("add");
                break;

            case 2:
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
                break;

            case 3:
                suggestions.add("100");
                suggestions.add("1000");
                suggestions.add("5000");
                suggestions.add("10000");
                suggestions.add("50000");
                suggestions.add("100000");
                suggestions.add("500000");
                suggestions.add("1000000");
                break;
        }

        return filterSuggestions(args[args.length - 1], suggestions);
    }

    private List<String> filterSuggestions(String input, List<String> options) {
        List<String> filtered = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                filtered.add(option);
            }
        }
        return filtered;
    }
}
