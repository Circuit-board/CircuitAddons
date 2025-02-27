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

public class summonmob extends Menu {
    public summonmob(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(borderPane, pane);

        Player target = menuUtility.getTarget();

        ItemStack playerHeadStack = makeItem(
                Material.PLAYER_HEAD,
                target.getName(),
                List.of(),
                new HashMap<>(),
                false
        );

        inv.setItem(0, playerHeadStack);

        ItemStack backStack = makeItem(
                Material.ARROW,
                ChatColor.WHITE + "Back",
                List.of(),
                new HashMap<>(),
                false
        );

        inv.setItem(22, backStack);

        ItemStack pandaStack = makeItem(
                Material.PANDA_SPAWN_EGG,
                ChatColor.WHITE + "Summon a panda at the player",
                List.of(ChatColor.GRAY + "Summons a panda at the player!"),
                new HashMap<>(),
                false
        );

        inv.setItem(14, pandaStack);

        ItemStack parrotStack = makeItem(
                Material.PARROT_SPAWN_EGG,
                ChatColor.WHITE + "Summon a parrot at the player",
                List.of(ChatColor.GRAY + "Summons a parrot at the player!"),
                new HashMap<>(),
                false
        );

        inv.setItem(12, parrotStack);

        ItemStack wolfStack = makeItem(
                Material.WOLF_SPAWN_EGG,
                ChatColor.WHITE + "Summon a dog at the player",
                List.of(ChatColor.GRAY + "Summons a dog at the player!"),
                new HashMap<>(),
                false
        );

        inv.setItem(4, wolfStack);
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "Summoning mob";
    }

    @Override
    protected int getSlots() {
        return 9 * 3;
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
