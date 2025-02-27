package cool.circuit.circuitAddons.games.slotguesser;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.*;

public class Level extends Menu {
    private int slots;
    private int slot;
    private MenuUtility menuutility;
    public Level(int slots, int slot, MenuUtility menuUtility) {
        super(menuUtility);
        this.slots = slots;
        this.slot = slot;
        this.menuUtility = menuUtility;
    }

    public int getSlots() {
        return slots;
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < getSlots(); i++) {
            inv.setItem(i,makeItem(Material.RED_STAINED_GLASS_PANE," ", List.of(),new HashMap<>(),false));
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
    public void open() {
        inv = Bukkit.createInventory(null, slots,"Slot Guesser");
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
