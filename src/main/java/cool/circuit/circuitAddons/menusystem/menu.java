package cool.circuit.circuitAddons.menusystem;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class menu {

    protected MenuUtility menuUtility;

    public menu(MenuUtility menuUtility) {
        this.menuUtility = menuUtility;
    }

    protected int slots;
    protected Inventory inv;
    protected String title;

    protected String getTitle() {
        return title;
    }
    protected int getSlots() {
        return 0;
    }
    protected Inventory getInventory () {
        return inv;
    }

    protected abstract void setMenuItems();

    protected int[] patternSlots = { 0, 10, 18, 8, 16, 26, 20, 24, 2, 18, 6, 12, 14, 22, 4 };

    protected void addMenuBorder(ItemStack pane) {// The correct slot indices

        for (int slot : patternSlots) {
            inv.setItem(slot, pane);
        }
    }

    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
    };

    protected abstract void open();
}