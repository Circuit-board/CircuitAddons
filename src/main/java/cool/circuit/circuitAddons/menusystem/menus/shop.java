package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import cool.circuit.circuitAPI.utils.makeItemUtil;
import cool.circuit.circuitAddons.CircuitAddons;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class shop extends Menu {
    public static HashMap<ItemStack, Integer> itemsList = new HashMap();

    public shop(MenuUtility menuUtility) {
        super(menuUtility);
    }

    protected void setMenuItems() {
        int j = 0;

        for(String i : CircuitAddons.items.getConfigurationSection("items").getKeys(false)) {
            if (j >= 54) {
                break;
            }

            Material item = Material.valueOf(i.toUpperCase(Locale.ROOT));
            String var10002 = String.valueOf(ChatColor.GRAY);
            ItemStack itemStack = makeItemUtil.makeItem(item, "", List.of(var10002 + "Price: " + CircuitAddons.items.getInt("items." + i + ".price"), " ", String.valueOf(ChatColor.GREEN) + "Click to purchase!"), new HashMap(), false);
            this.inv.setItem(j, itemStack);
            itemsList.put(itemStack, CircuitAddons.items.getInt("items." + i + ".price"));
            ++j;
        }

        int slots_to_fill_with_glass = 54 - j;

        for(int i = 0; i < slots_to_fill_with_glass; ++i) {
            this.inv.setItem(j + i, makeItemUtil.makeItem(Material.GRAY_STAINED_GLASS_PANE, "" + String.valueOf(ChatColor.RESET), List.of(), new HashMap(), false));
        }

    }

    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {
    }

    protected boolean cancelClicks() {
        return true;
    }

    protected int getSlots() {
        return 54;
    }

    protected String getTitle() {
        return "Shop";
    }

    public void open() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, this.getSlots(), this.getTitle());
        this.setMenuItems();
        this.menuUtility.getPlayer().openInventory(this.inv);
    }

    public @NotNull Inventory getInventory() {
        return this.inv;
    }
}
