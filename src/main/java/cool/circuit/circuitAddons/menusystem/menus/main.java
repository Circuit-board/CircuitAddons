package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.*;

public class main extends Menu {
    public main(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(borderPane, pane);

        ItemStack manageStack = makeItem(
                Material.PLAYER_HEAD,
                ChatColor.GREEN + "Manage Player",
                List.of(ChatColor.GRAY + "To manage a player type: \"/manage [player name]\""),
                new HashMap<>(),
                false
        );

        ItemStack allTitleStack = makeItem(
                Material.YELLOW_DYE,
                ChatColor.YELLOW + "Send title to all players",
                List.of(
                        ChatColor.GRAY + "To send a title and/or subtitle to all players type: ",
                        ChatColor.GRAY + "\"/alltitle [subtitle|title|actionbar] [title|subtitle|actionbar], [subtitle], [actionbar]\""
                ),
                new HashMap<>(),
                false
        );

        ItemStack closeStack = makeItem(
                Material.ARROW,
                ChatColor.RED + "Close",
                List.of(),
                new HashMap<>(),
                false
        );

        ItemStack minecraftStack = makeItem(
                Material.GRASS_BLOCK,
                ChatColor.GREEN + "Minecraft inside of minecraft (Buggy)",
                List.of(ChatColor.GRAY + "Basiclly 2D minecraft inside of minecraft with 10x more bugs"),
                new HashMap<>(),
                false
        );

        ItemStack paintDryingStack = makeItem(
                Material.WHITE_STAINED_GLASS,
                ChatColor.WHITE + "Paint drying simulator",
                List.of(ChatColor.GRAY + "Literally..."),
                new HashMap<>(),
                false
        );

        ItemStack economyStack = makeItem(
                Material.EMERALD,
                ChatColor.GREEN + "Economy",
                List.of(
                        ChatColor.GRAY + "/eco set to set a persons balance",
                        ChatColor.GRAY + "/eco add to add to a persons balance",
                        ChatColor.GRAY + "/shop browse to view all items that have been set in the config",
                        ChatColor.GRAY + "/shop buy|sell to sell/buy things"
                ),
                new HashMap<>(),
                false
        );

        ItemStack slotGuesserStack = makeItem(
                Material.RED_STAINED_GLASS_PANE,
                ChatColor.RED + "Slot Guesser",
                List.of(ChatColor.GRAY + "Click a slot to guess!"),
                new HashMap<>(),
                false
        );

        inv.setItem(12, allTitleStack);
        inv.setItem(14, manageStack);
        inv.setItem(22, closeStack);
        inv.setItem(13, minecraftStack);
        inv.setItem(4,  paintDryingStack);
        inv.setItem(3,  economyStack);
        inv.setItem(5,  slotGuesserStack);
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {
        inventoryClickEvent.setCancelled(true);
    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    @Override
    protected int getSlots() {
        return 9 * 3;
    }

    @Override
    protected String getTitle() {
        return "Circuit Addons";
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
