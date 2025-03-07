package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
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

public class lootbox_rewards extends Menu {
    public lootbox_rewards(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {

        inv.setItem(31,makeItem(
                Material.EMERALD_BLOCK,
                ChatColor.GREEN + "Save",
                List.of(ChatColor.GRAY + "Click to save!"),
                new HashMap<>(),
                false
        ));
    }

    @Override
    protected int getSlots() {
        return 9*4;
    }

    @Override
    protected String getTitle() {
        return "Loot Box Rewards";
    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return false;
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
