package cool.circuit.circuitAddons.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class CircuitEconomy implements Economy {

    public static final Map<String, Double> playerBalances = new HashMap<>();
    public static final Map<String, Double> bankAccounts = new HashMap<>();
    public static final Map<String, String> bankOwners = new HashMap<>();

    @Override
    public boolean createPlayerAccount(String playerName) {
        if (playerBalances.containsKey(playerName)) {
            return false; // Already exists
        }
        playerBalances.put(playerName, 0.0);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(player.getName());
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public EconomyResponse createBank(String bankName, String owner) {
        if (bankAccounts.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank already exists!");
        }
        bankAccounts.put(bankName, 0.0);
        bankOwners.put(bankName, owner);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String playerName) {
        if (!bankOwners.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist!");
        }
        boolean isOwner = bankOwners.get(bankName).equals(playerName);
        return new EconomyResponse(0, 0, isOwner ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>(bankAccounts.keySet());
    }

    @Override
    public double getBalance(String playerName) {
        return playerBalances.getOrDefault(playerName, 0.0);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return playerBalances.getOrDefault(offlinePlayer.getName(), 0.0);
    }

    @Override
    public double getBalance(String playerName, String account) {
        return playerBalances.getOrDefault(playerName + ":" + account, 0.0);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String account) {
        return getBalance(offlinePlayer.getName(), account);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Invalid amount!");
        }
        playerBalances.put(playerName, getBalance(playerName) + amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (amount < 0 || getBalance(playerName) < amount) {
            return new EconomyResponse(0, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds!");
        }
        playerBalances.put(playerName, getBalance(playerName) - amount);
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String bankName, double amount) {
        if (!bankAccounts.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist!");
        }
        bankAccounts.put(bankName, bankAccounts.get(bankName) + amount);
        return new EconomyResponse(amount, bankAccounts.get(bankName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double amount) {
        if (!bankAccounts.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist!");
        }
        double balance = bankAccounts.get(bankName);
        if (balance < amount) {
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Not enough funds!");
        }
        bankAccounts.put(bankName, balance - amount);
        return new EconomyResponse(amount, bankAccounts.get(bankName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        if (!bankAccounts.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist!");
        }
        bankAccounts.remove(bankName);
        bankOwners.remove(bankName);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankBalance(String bankName) {
        if (!bankAccounts.containsKey(bankName)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exist!");
        }
        double balance = bankAccounts.get(bankName);
        return new EconomyResponse(balance, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }


    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return playerBalances.containsKey(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "CircuitEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double v) {
        return String.format("$%.2f", v);
    }

    @Override
    public String currencyNamePlural() {
        return "Dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "Dollar";
    }
}
