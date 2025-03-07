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
import static cool.circuit.circuitAddons.managers.LootboxManager.lootboxNames;
import static cool.circuit.circuitAddons.managers.LootboxManager.lootboxPrices;

public class lootbox_create extends Menu {
    public lootbox_create(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(pane,pane);

        inv.setItem(17, makeItem(
                Material.LIME_WOOL,
                ChatColor.GREEN + "Create",
                List.of(ChatColor.GOLD + "Click to create the lootbox!"),
                new HashMap<>(),
                false
        ));
        inv.setItem(12, makeItem(
                Material.CHEST,
                ChatColor.YELLOW + "Rewards",
                List.of(ChatColor.GOLD + "Click to choose the rewards of the lootbox!"),
                new HashMap<>(),
                false
        ));
        inv.setItem(13, makeItem(
                Material.NAME_TAG,
                ChatColor.WHITE + "Name",
                List.of(ChatColor.GOLD + "Click to choose the name of the lootbox!",ChatColor.GOLD + "Current name: " + lootboxNames.computeIfAbsent(menuUtility.getPlayer().getUniqueId(), s -> "Unnamed Lootbox")),
                new HashMap<>(),
                false
        ));
        inv.setItem(14, makeItem(
                Material.EMERALD,
                ChatColor.DARK_GREEN + "Price",
                List.of(ChatColor.GOLD + "Click to choose the price of the lootbox!",ChatColor.GOLD + "Current price: " + lootboxPrices.computeIfAbsent(menuUtility.getPlayer().getUniqueId(), p -> 0)),
                new HashMap<>(),
                false
        ));
    }

    @Override
    protected String getTitle() {
        return "Loot Box Creator";
    }

    @Override
    protected int getSlots() {
        return 9*3;
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
