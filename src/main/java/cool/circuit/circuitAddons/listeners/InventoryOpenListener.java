package cool.circuit.circuitAddons.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.itemToEnchant;

public class InventoryOpenListener implements Listener {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }


    public static List<Enchantment> getApplicableEnchantments(ItemStack item) {
        List<Enchantment> applicable = new ArrayList<>();

        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(item)) {
                applicable.add(enchantment);
            }
        }

        return applicable;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getView().getTitle().equals("Enchanting")) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem.getType().equals(Material.AIR)) {
                player.closeInventory();
                return;
            }

            itemToEnchant = heldItem;

            inv.setItem(9, heldItem); // Place held item in slot 9

            List<Enchantment> applicableEnchantments = getApplicableEnchantments(heldItem);
            int inventorySize = inv.getSize();
            int rows = inventorySize / 9;

            int enchantIndex = 0;

            // Loop through rows from top to bottom
            for (int row = 0; row < rows && enchantIndex < applicableEnchantments.size(); row++) {
                for (int col = 1; col <= 7 && enchantIndex < applicableEnchantments.size(); col++) { // Slots 1-7 in each row
                    int slot = row * 9 + col;

                    Enchantment enchantment = applicableEnchantments.get(enchantIndex);

                    ItemStack item = makeItem(
                            Material.ENCHANTED_BOOK,
                            ChatColor.GREEN + capitalizeFirstLetter(enchantment.getName().toLowerCase()).replace("_", " "),
                            List.of("", ChatColor.GREEN + "Click to view!"),
                            new HashMap<>(),
                            false
                    );

                    ItemMeta meta = item.getItemMeta();
                    if (meta instanceof EnchantmentStorageMeta storageMeta) {
                        storageMeta.addStoredEnchant(enchantment, 1, true);
                        item.setItemMeta(storageMeta);
                    }

                    inv.setItem(slot, item);
                    enchantIndex++;
                }
            }
        }
    }
}
