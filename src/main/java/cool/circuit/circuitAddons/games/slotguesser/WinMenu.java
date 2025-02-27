package cool.circuit.circuitAddons.games.slotguesser;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.getInstance;
import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class WinMenu extends Menu {
    private final int difficulty;

    public WinMenu(MenuUtility menuUtility, int difficulty) {
        super(menuUtility);
        this.difficulty = Math.min(difficulty, 54); // Max slots in a Bukkit inventory
        this.inv = Bukkit.createInventory(null, getSlots(), getTitle()); // Initialize inventory here
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < difficulty; i++) {
            inv.setItem(i, makeItem(Material.RED_STAINED_GLASS_PANE, " ", List.of(), new HashMap<>(), false));
        }

        int winSlot = menuUtility.getSlot();
        if (winSlot >= 0 && winSlot < difficulty) {
            inv.setItem(winSlot, makeItem(Material.GREEN_STAINED_GLASS_PANE, " ", List.of(), new HashMap<>(), false));
        }
    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    @Override
    protected int getSlots() {
        return (difficulty + 8) / 9 * 9; // Round up to the nearest multiple of 9
    }

    @Override
    protected String getTitle() {
        return "You Win!";
    }

    @Override
    public void open() {
        menuUtility.getPlayer().playSound(menuUtility.getPlayer(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
