package cool.circuit.circuitAddons.managers;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;
import static cool.circuit.circuitAddons.CircuitAddons.lootboxesFile;

public class LootboxManager {

    public static final HashMap<UUID, String> lootboxNames = new HashMap<>();
    public static final HashMap<UUID, Integer> lootboxPrices = new HashMap<>();
    public static final HashMap<UUID, List<ItemStack>> lootboxRewards = new HashMap<>();

    public static boolean isInputtingName = false;
    public static boolean isInputtingPrice = false;

    public static final List<Lootbox> lootboxes = new ArrayList<>();

    public static boolean createLootbox(String name, int cost, ItemStack... rewards) {
        if(lootboxes.stream().anyMatch(lootbox -> lootbox.name().equals(name))) {
            return false;
        }
        if(lootboxes.size() > 18) {
            return false;
        }

        lootboxes.add(new Lootbox(name, cost, Arrays.asList(rewards)));
        return true;
    }
    public Lootbox getLootbox(String name) {
        return lootboxes.stream().filter(lootbox -> lootbox.name().equals(name)).findFirst().orElse(null);
    }
    public void removeLootbox(String name) {
        lootboxes.removeIf(lootbox -> lootbox.name().equals(name));
    }

    private void openLootbox(String name, Player player) {
        Lootbox lootbox = getLootbox(name);
        if (lootbox == null) return;
        lootbox.open(player);
    }

    public void buyLootbox(String name, Player player) {
        double balance = getEconomy().bankBalance("bank_" + player.getName()).balance;

        if(balance < lootboxes.get(lootboxes.indexOf(getLootbox(name))).cost()) {
            player.sendMessage("You do not have enough money to buy this lootbox!");
            return;
        }

        getEconomy().bankWithdraw("bank_" + player.getName(), lootboxes.get(lootboxes.indexOf(getLootbox(name))).cost());
        openLootbox(name, player);
    }

    public static void saveLootboxes() {
        CircuitAddons.lootboxes.set("lootboxes", null); // Clears old lootboxes

        for (Lootbox lootbox : lootboxes) {
            CircuitAddons.lootboxes.set("lootboxes." + lootbox.name() + ".cost", lootbox.cost());
            CircuitAddons.lootboxes.set("lootboxes." + lootbox.name() + ".rewards", lootbox.rewards());
        }

        try {
            CircuitAddons.lootboxes.save(lootboxesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLootboxes() {
        if (!CircuitAddons.lootboxes.contains("lootboxes")) {
            CircuitAddons.lootboxes.createSection("lootboxes");
        }

        ConfigurationSection section = CircuitAddons.lootboxes.getConfigurationSection("lootboxes");
        if (section == null) {
            return; // Prevent NullPointerException
        }

        for (String key : section.getKeys(false)) {
            int cost = CircuitAddons.lootboxes.getInt("lootboxes." + key + ".cost", 0);

            List<ItemStack> rewards = new ArrayList<>();
            List<?> rawList = CircuitAddons.lootboxes.getList("lootboxes." + key + ".rewards");

            if (rawList != null) {
                for (Object item : rawList) {
                    if (item instanceof ItemStack) {
                        rewards.add((ItemStack) item);
                    } else {
                        Bukkit.getLogger().warning("Invalid reward in lootbox " + key);
                    }
                }
            }

            lootboxes.add(new Lootbox(key, cost, rewards));
        }
    }


}
