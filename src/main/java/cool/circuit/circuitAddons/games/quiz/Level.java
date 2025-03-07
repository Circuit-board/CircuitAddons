package cool.circuit.circuitAddons.games.quiz;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.pane;

public class Level extends Menu {

    private final int question1;
    private final int question2;
    private final List<Integer> answers;

    public Level(MenuUtility menuUtility, int answer,List<Integer> answers, int question1, int question2) {
        super(menuUtility);
        this.question1 = question1;
        this.question2 = question2;
        this.answers = answers;
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
    protected int getSlots() {
        return 9*3;
    }

    @Override
    protected String getTitle() {
        return "Quiz";
    }

    @Override
    protected void open() {
        inv = Bukkit.createInventory(null, getSlots(), getTitle());
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
