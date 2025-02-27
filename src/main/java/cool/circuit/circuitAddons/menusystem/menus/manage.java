package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.borderPane;
import static cool.circuit.circuitAddons.CircuitAddons.pane;
import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;

public class manage extends Menu {

    public Player target;

    public manage(MenuUtility menuUtility, Player target) {
        super(menuUtility);
        this.target = target;
    }

    @Override
    protected String getTitle() {
        return "Manage";
    }

    @Override
    protected int getSlots() {
        return 9 * 3;
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(borderPane, pane);

        ItemStack pandaStack = makeItem(
                Material.PANDA_SPAWN_EGG,
                ChatColor.WHITE + "Summon a mob at the player",
                List.of(ChatColor.GRAY + "Opens a menu to select which mob to summon at the player!"),
                new HashMap<>(),
                false
        );

        inv.setItem(14, pandaStack);

        ItemStack playerHeadStack = makeItem(
                Material.PLAYER_HEAD,
                target.getName(),
                List.of(),
                new HashMap<>(),
                false
        );

        inv.setItem(0, playerHeadStack);

        ItemStack closeStack = makeItem(
                Material.ARROW,
                ChatColor.RED + "Close",
                List.of(),
                new HashMap<>(),
                false
        );

        ItemStack banStack = makeItem(
                Material.BARRIER,
                ChatColor.DARK_RED + "Ban",
                List.of(ChatColor.GRAY + "Bans the target's IP Address!"),
                new HashMap<>(),
                false
        );

        ItemStack kickStack = makeItem(
                Material.REDSTONE_BLOCK,
                ChatColor.RED + "Kick",
                List.of(ChatColor.GRAY + "Kicks the target!"),
                new HashMap<>(),
                false
        );

        inv.setItem(22, closeStack);
        inv.setItem(12, banStack);
        inv.setItem(4, kickStack);
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

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
