package cool.circuit.circuitAddons.vault;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static cool.circuit.circuitAddons.CircuitAddons.*;
import static cool.circuit.circuitAddons.vault.CircuitEconomy.bankAccounts;
import static cool.circuit.circuitAddons.vault.CircuitEconomy.playerBalances;

public class CircuitBanks {

    public static void setBalance(String bankName, int amount) {
        // First, reset the bank balance
        EconomyResponse withdrawResponse = getEconomy().bankWithdraw(bankName, getEconomy().getBalance(bankName)); // Withdraw everything
        if (!withdrawResponse.transactionSuccess()) {
            Bukkit.getLogger().severe("Failed to withdraw from bank: " + withdrawResponse.errorMessage);
            return; // Abort if the withdrawal failed
        }

        EconomyResponse depositResponse = getEconomy().bankDeposit(bankName, amount); // Deposit the new amount
        if (!depositResponse.transactionSuccess()) {
            Bukkit.getLogger().severe("Failed to deposit into bank: " + depositResponse.errorMessage);
        }

        // Update local storage and config
        bankAccounts.put(bankName, (double) amount);
        banks.set("banks." + bankName + ".balance", (double) amount);
        saveSettings();
    }

    public static void addToBalance(String bankName, int amount) {
        EconomyResponse depositResponse = getEconomy().bankDeposit(bankName, amount);
        if (!depositResponse.transactionSuccess()) {
            Bukkit.getLogger().severe("Failed to deposit into bank: " + depositResponse.errorMessage);
        }
    }

    public static void sync() {
        for (String bankName : getEconomy().getBanks()) {
            double currentBalance = getEconomy().bankBalance(bankName).balance;
            double storedBalance = banks.getDouble("banks." + bankName + ".balance");
            if (currentBalance != storedBalance) {
                banks.set("banks." + bankName + ".balance", currentBalance);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            String bankName = "bank_" + player.getName();
            if (!getEconomy().getBanks().contains(bankName)) {
                getEconomy().createBank(bankName, player.getName());
            }
        }
    }

    public static void updateBanksFile(Player player) {
        if (banks == null || banksFile == null) {
            return;
        }

        String bankName = "bank_" + player.getName();
        double balance = getEconomy().bankBalance(bankName).balance;  // Corrected the balance fetching
        banks.set("banks." + bankName + ".balance", balance);

        // Save the file after modifying it
        try {
            banks.save(banksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void joinSync() {
        for (String bankName : banks.getConfigurationSection("banks").getKeys(false)) {
            if (!bankAccounts.containsKey(bankName)) {
                double balance = banks.getDouble("banks." + bankName + ".balance");
                bankAccounts.put(bankName, balance);
            }
        }
    }

    public static void saveSettings() {
        if (banksFile == null) {
            File dataFolder = Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder();
            banksFile = new File(dataFolder, "banks.yml");
        }

        if (banks == null) {
            Bukkit.getLogger().severe("Error: banks is null! Cannot save settings.");
            return;
        }

        try {
            banks.save(banksFile);
            Bukkit.getLogger().info("Saved banks.yml successfully!");
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save banks.yml: " + e.getMessage());
        }
    }

    public static void loadSettings() {
        if (Bukkit.getPluginManager().getPlugin("CircuitAddons") == null) {
            return;
        }

        banksFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "banks.yml");

        if (!banksFile.exists()) {
            try {
                banksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        banks = YamlConfiguration.loadConfiguration(banksFile);
    }
}
