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

public class lootbox_main extends Menu {
    public lootbox_main(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        inv.setItem(4, makeItem(
                Material.GREEN_WOOL,
                ChatColor.GREEN + "Create lootbox",
                List.of(ChatColor.GOLD + "Click to create a lootbox!"),
                new HashMap<>(),
                false
        ));
        /*inv.setItem(5, makeItem(
                Material.RED_WOOL,
                ChatColor.RED + "Delete lootbox",
                List.of(ChatColor.GOLD + "Click to delete a lootbox!"),
                new HashMap<>(),
                false
        ));*/
    }

    @Override
    protected String getTitle() {
        return "Loot Box Manager";
    }

    @Override
    protected int getSlots() {
        return 9;
    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
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
