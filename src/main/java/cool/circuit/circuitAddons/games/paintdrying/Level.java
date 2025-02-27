package cool.circuit.circuitAddons.games.paintdrying;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class Level extends Menu {
    private final ItemStack pane;
    private final ItemStack fadingPane;
    private final Inventory inv;
    private int slotsCompleted;
    private final Player player;

    public Level(ItemStack pane, ItemStack fadingPane, MenuUtility menuUtility, Player player) {
        super(menuUtility);
        this.pane = pane;
        this.fadingPane = fadingPane;
        this.inv = Bukkit.createInventory(null, getSlots(), getTitle());
        this.slotsCompleted = 0;
        this.player = player;
    }

    @Override
    protected void setMenuItems() {
        for (int i = 0; i < getSlots(); i++) {
            inv.setItem(i, pane);
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                int slot;
                int attempts = 0;
                int maxAttempts = 45; // Prevent infinite loops

                do {
                    slot = (int) (Math.random() * getSlots());
                    attempts++;
                } while (attempts < maxAttempts && inv.getItem(slot) != null && inv.getItem(slot).getType().equals(fadingPane.getType()));

// Fallback if no slot was found
                if (attempts >= maxAttempts) {
                    player.closeInventory();
                    return;
                }

                inv.setItem(slot, fadingPane);
                slotsCompleted++;
            }
        }.runTaskTimer(CircuitAddons.getInstance(), 0L, 20L);
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
        return 54;
    }

    @Override
    protected String getTitle() {
        return "Paint Drying";
    }

    @Override
    public void open() {
        setMenuItems();
        player.openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
