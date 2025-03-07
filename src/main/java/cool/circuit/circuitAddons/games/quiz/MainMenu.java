package cool.circuit.circuitAddons.games.quiz;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.pane;

public class MainMenu extends Menu {
    public MainMenu(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected int getSlots() {
        return 9*3;
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(pane,pane);
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
    protected String getTitle() {
        return "Quiz | Main Menu";
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
