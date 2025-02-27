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
import static cool.circuit.circuitAddons.CircuitAddons.borderPane;

public class slotguesser_main extends Menu {
    public slotguesser_main(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(borderPane,pane);

        inv.setItem(11, makeItem(Material.LIME_WOOL, ChatColor.GREEN + "Level 1 (Easy)", List.of(), new HashMap<>(), false));
        inv.setItem(12, makeItem(Material.YELLOW_WOOL, ChatColor.YELLOW + "Level 2 (Normal)", List.of(), new HashMap<>(), false));
        inv.setItem(13, makeItem(Material.ORANGE_WOOL, ChatColor.GOLD + "Level 3 (Hard)", List.of(), new HashMap<>(), false));
        inv.setItem(14, makeItem(Material.RED_WOOL, ChatColor.RED + "Level 4 (Extreme)", List.of(), new HashMap<>(), false));
        inv.setItem(15, makeItem(Material.BLACK_WOOL, ChatColor.DARK_RED + "Level 5 (Impossible)", List.of(), new HashMap<>(), false));
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
        return "Slot Guesser | Main";
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
