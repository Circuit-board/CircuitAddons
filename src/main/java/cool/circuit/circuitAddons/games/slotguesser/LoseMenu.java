package cool.circuit.circuitAddons.games.slotguesser;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;

public class LoseMenu extends Menu {
    int difficulty;
    public LoseMenu(MenuUtility menuUtility,int difficulty) {
        super(menuUtility);
        this.difficulty = difficulty;
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < difficulty; i++) {
            inv.setItem(i,makeItem(Material.RED_STAINED_GLASS_PANE," ", List.of(),new HashMap<>(),false));
        }
        inv.setItem(menuUtility.getSlot(), makeItem(Material.GREEN_STAINED_GLASS_PANE," ", List.of(),new HashMap<>(),false));
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
        return difficulty;
    }

    @Override
    protected String getTitle() {
        return "You lose!";
    }

    @Override
    public void open() {
        menuUtility.getPlayer().playSound( menuUtility.getPlayer(), Sound.BLOCK_BEACON_DEACTIVATE,1,1);
        inv = Bukkit.createInventory(null, getSlots(), getTitle());
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
