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
import static cool.circuit.circuitAddons.CircuitAddons.*;

public class paintdrying_main extends Menu {
    public paintdrying_main(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < 9*3; i++) {
            inv.setItem(i,pane);
        }

        inv.setItem(11,makeItem(Material.RED_STAINED_GLASS_PANE,
                ChatColor.RED + "Red Paint",
                List.of(ChatColor.GRAY + "Click to watch paint dry!"),
                new HashMap<>(),
                false
        ));
        inv.setItem(12,makeItem(
                Material.GREEN_STAINED_GLASS_PANE,
                ChatColor.GREEN + "Green Paint",
                List.of(ChatColor.GRAY + "Click to watch paint dry!"),
                new HashMap<>(),
                false
        ));
        inv.setItem(13,makeItem(
                Material.BLUE_STAINED_GLASS_PANE,
                ChatColor.BLUE + "Blue Paint",
                List.of(ChatColor.GRAY + "Click to watch paint dry!"),
                new HashMap<>(),
                false
        ));
        inv.setItem(14,makeItem(
                Material.YELLOW_STAINED_GLASS_PANE,
                ChatColor.YELLOW + "Yellow Paint",
                List.of(ChatColor.GRAY + "Click to watch paint dry!"),
                new HashMap<>(),
                false
        ));
    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "Paint Drying Simulator";
    }

    @Override
    protected int getSlots() {
        return 9*3;
    }

    @Override
    public void open() {
        inv =  Bukkit.createInventory(null,getSlots(),getTitle());
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
