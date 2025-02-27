package cool.circuit.circuitAddons.games.minecraft;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;

public class BlockSelection extends Menu {

    public static final ItemStack[] GLASS_PANES = {
            new ItemStack(Material.GLASS_PANE),
            new ItemStack(Material.WHITE_STAINED_GLASS_PANE),
            new ItemStack(Material.ORANGE_STAINED_GLASS_PANE),
            new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE),
            new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE),
            new ItemStack(Material.YELLOW_STAINED_GLASS_PANE),
            new ItemStack(Material.LIME_STAINED_GLASS_PANE),
            new ItemStack(Material.PINK_STAINED_GLASS_PANE),
            new ItemStack(Material.GRAY_STAINED_GLASS_PANE),
            new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE),
            new ItemStack(Material.CYAN_STAINED_GLASS_PANE),
            new ItemStack(Material.PURPLE_STAINED_GLASS_PANE),
            new ItemStack(Material.BLUE_STAINED_GLASS_PANE),
            new ItemStack(Material.BROWN_STAINED_GLASS_PANE),
            new ItemStack(Material.GREEN_STAINED_GLASS_PANE),
            new ItemStack(Material.RED_STAINED_GLASS_PANE),
            new ItemStack(Material.BLACK_STAINED_GLASS_PANE)
    };

    public static final ItemStack[] GLASS_BLOCKS = {
            new ItemStack(Material.GLASS),
            new ItemStack(Material.WHITE_STAINED_GLASS),
            new ItemStack(Material.ORANGE_STAINED_GLASS),
            new ItemStack(Material.MAGENTA_STAINED_GLASS),
            new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS),
            new ItemStack(Material.YELLOW_STAINED_GLASS),
            new ItemStack(Material.LIME_STAINED_GLASS),
            new ItemStack(Material.PINK_STAINED_GLASS),
            new ItemStack(Material.GRAY_STAINED_GLASS),
            new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS),
            new ItemStack(Material.CYAN_STAINED_GLASS),
            new ItemStack(Material.PURPLE_STAINED_GLASS),
            new ItemStack(Material.BLUE_STAINED_GLASS),
            new ItemStack(Material.BROWN_STAINED_GLASS),
            new ItemStack(Material.GREEN_STAINED_GLASS),
            new ItemStack(Material.RED_STAINED_GLASS),
            new ItemStack(Material.BLACK_STAINED_GLASS)
    };

    public static final ItemStack[] WOOL_BLOCKS = {
            new ItemStack(Material.WHITE_WOOL),
            new ItemStack(Material.ORANGE_WOOL),
            new ItemStack(Material.MAGENTA_WOOL),
            new ItemStack(Material.LIGHT_BLUE_WOOL),
            new ItemStack(Material.YELLOW_WOOL),
            new ItemStack(Material.LIME_WOOL),
            new ItemStack(Material.PINK_WOOL),
            new ItemStack(Material.GRAY_WOOL),
            new ItemStack(Material.LIGHT_GRAY_WOOL),
            new ItemStack(Material.CYAN_WOOL),
            new ItemStack(Material.PURPLE_WOOL),
            new ItemStack(Material.BLUE_WOOL),
            new ItemStack(Material.BROWN_WOOL),
            new ItemStack(Material.GREEN_WOOL),
            new ItemStack(Material.RED_WOOL),
            new ItemStack(Material.BLACK_WOOL)
    };

    public static ItemStack[] ALL_ITEMS = new ItemStack[0];

    public BlockSelection(MenuUtility menuUtility) {
        super(menuUtility);
        List<ItemStack> allItemsList = new ArrayList<>(Arrays.asList(ALL_ITEMS));

        for (ItemStack i : GLASS_BLOCKS) {
            allItemsList.add(i);
        }
        for (ItemStack i : GLASS_PANES) {
            allItemsList.add(i);
        }
        for (ItemStack i : WOOL_BLOCKS) {
            allItemsList.add(i);
        }

// If ALL_ITEMS should be updated:
        ALL_ITEMS = allItemsList.toArray(new ItemStack[0]);

    }

    public String capitalize(String input) {
        String[] words = input.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }

        return result.toString().trim();
    }


    @Override
    protected void setMenuItems() {

// Place items in reverse order from slot 53 to 0
        for (int i = 0; i < Math.min(ALL_ITEMS.length, 54); i++) {
            inv.setItem(53 - i, ALL_ITEMS[i]);
        }

        inv.setItem(0, makeItem(
                Material.ARROW,
                "Back",
                List.of(),
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
    protected int getSlots() {
        return 9*6;
    }

    @Override
    protected String getTitle() {
        return "Items";
    }

    @Override
    public void open() {
        inv = Bukkit.createInventory(null, getSlots(), getTitle());
        setMenuItems();
        menuUtility.getPlayer().playSound(menuUtility.getPlayer(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
        menuUtility.getPlayer().openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
