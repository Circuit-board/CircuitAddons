package cool.circuit.circuitAddons.listeners;

import cool.circuit.circuitAddons.managers.Lootbox;
import cool.circuit.circuitAddons.managers.LootboxManager;
import cool.circuit.circuitAddons.menusystem.menus.lootbox_create;
import cool.circuit.circuitAddons.menusystem.menus.lootbox_rewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;
import static cool.circuit.circuitAddons.managers.LootboxManager.*;
import static cool.circuit.circuitAddons.managers.LootboxManager.lootboxes;

public class LootboxListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return; // Ensure the click is by a player
        int slot = event.getSlot();
        UUID playerId = player.getUniqueId();

        // Check if the inventory title matches
        if (event.getView().getTitle().equals("Loot Box Rewards")) {
            // Check the clicked slot

            event.setCancelled(slot > 26 && slot != 31); // Prevent interaction with invalid slots

            if (slot == 31) {

                List<ItemStack> items = new ArrayList<>();

                // Collect items from slots 0 to 26
                for (int i = 0; i < 27; i++) {
                    ItemStack item = event.getInventory().getItem(i);
                    if (item != null) { // Null check to avoid adding empty items
                        items.add(item);
                    }
                }

                // Ensure lootboxRewards map is initialized for the player
                if (!lootboxRewards.containsKey(playerId)) {
                    lootboxRewards.put(playerId, new ArrayList<>()); // Initialize if not present
                }

                // Add items to player's lootbox rewards
                lootboxRewards.get(playerId).addAll(items);

                // Open the lootbox_create menu
                lootbox_create menu = new lootbox_create(getMenuUtility(player));
                menu.open();

            } else if (event.getView().getTitle().equals("Loot Box Deletion")) {
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                event.setCancelled(true);
                // Handle lootbox deletion
                if (itemName != null && itemName.startsWith(ChatColor.YELLOW.toString())) {
                    String lootboxName = itemName.replace(ChatColor.YELLOW + "", "");
                    lootboxName = lootboxName.trim(); // Clean up

                    String finalLootboxName = lootboxName;
                    Lootbox lootbox = lootboxes.stream()
                            .filter(lb -> lb.name().equals(finalLootboxName))
                            .findFirst()
                            .orElse(null);

                    if (lootbox != null) {
                        // Remove the lootbox
                        lootboxes.remove(lootbox);
                        player.sendMessage(ChatColor.GREEN + "Lootbox '" + lootboxName + "' has been deleted.");
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatColor.RED + "Lootbox not found!");
                    }
                    event.setCancelled(true);
                    return;
                }

                // Handle back button
                if (event.getSlot() == 26) {
                    // Implement this if you want to go back to the main menu (e.g., lootbox_main)
                    player.closeInventory();
                    // openMainMenu(); // Uncomment if you have a method to open the main menu.
                }

                event.setCancelled(true); // Cancel the event to prevent moving items
            }
        }
    }
}
