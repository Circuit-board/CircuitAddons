package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import cool.circuit.circuitAddons.managers.Lootbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.pane;
import static cool.circuit.circuitAddons.managers.LootboxManager.lootboxes;

public class lootbox_delete extends Menu {
    public lootbox_delete(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(pane, pane);

        // No lootboxes available
        if (lootboxes.isEmpty()) {
            inv.setItem(13, makeItem(
                    Material.BARRIER,
                    ChatColor.RED + "No Lootboxes Available",
                    List.of(ChatColor.GOLD + "No lootboxes are available to delete."),
                    new HashMap<>(),
                    false
            ));
            return;
        }

        // Display lootboxes in the inventory
        int slot = 10; // Start placing lootboxes from slot 10
        for (Lootbox lootbox : lootboxes) {
            if (slot > 16) {
                break; // Avoid exceeding the inventory
            }
            inv.setItem(slot, makeItem(
                    Material.CHEST,
                    ChatColor.YELLOW + lootbox.name(),
                    List.of(ChatColor.GOLD + "Click to delete this lootbox!"),
                    new HashMap<>(),
                    false
            ));
            slot++;
        }

        // Add a back button to return to the main menu (if needed)
        inv.setItem(26, makeItem(
                Material.ARROW,
                ChatColor.GREEN + "Back",
                List.of(ChatColor.GOLD + "Go back to the main menu."),
                new HashMap<>(),
                false
        ));
    }

    @Override
    protected String getTitle() {
        return "Loot Box Deletion";
    }

    @Override
    protected int getSlots() {
        return 9 * 3;
    }

    @Override
    protected void handleMenu(InventoryClickEvent event) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true; // Prevent players from moving items
    }

    @Override
    public void open() {
        inv = Bukkit.createInventory(null, getSlots(), getTitle());

        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
