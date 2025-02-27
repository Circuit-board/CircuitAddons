//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cool.circuit.circuitAddons.games.minecraft;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import cool.circuit.circuitAPI.utils.makeItemUtil;
import cool.circuit.circuitAddons.CircuitAddons;
import java.io.IOException;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;

public class MineCraft extends Menu {
    private static final HashMap<UUID, MineCraft> activeGames = new HashMap<>();
    private static final int ROWS = 6;
    private static final int COLS = 9;
    private static final int START_ROW = 2;
    private static final int START_COL = 8;
    private int playerRow = 2;
    private int playerCol = 8;
    public List<Integer> airSlots = new ArrayList();
    public HashMap<Integer, ItemStack> placedItems = new HashMap();
    public ItemStack currentItem;
    public int playerSlot;

    public MineCraft(MenuUtility menuUtility) {
        super(menuUtility);
        this.currentItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        this.playerSlot = 53 - (this.playerRow * 9 + this.playerCol);
        activeGames.put(menuUtility.getPlayer().getUniqueId(), this);
    }

    public static MineCraft getInstance(UUID playerId) {
        return (MineCraft)activeGames.get(playerId);
    }

    public void update() {
        // Clear player's old position
        inv.setItem(playerSlot, new ItemStack(Material.AIR));

        // Clear only airSlots, ensuring placed items stay intact
        for (int i : airSlots) {
            if (i != playerSlot && !placedItems.containsKey(i)) {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }

        ItemStack lightBlueGlass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemStack limeGlass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

        for (int i = 0; i < 45; i++) {
            if (i <= 26) {
                inv.setItem(i, lightBlueGlass); // Top 3 rows
            } else if (i >= 36) {
                inv.setItem(i, limeGlass); // 5th row
            }
        }

        // Restore all placed items correctly
        for (Map.Entry<Integer, ItemStack> entry : placedItems.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

        // Update player slot and set player head
        this.playerSlot = 53 - (this.playerRow * 9 + this.playerCol);
        this.inv.setItem(playerSlot, makeItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "You!", List.of(), new HashMap<>(), false));

        // Ensure the bottom row is set properly

        // Save state
        this.saveMinecraft();
    }



    public void moveForward() {
        if (this.playerCol < 8) {
            ++this.playerCol;
            this.update();
        }

    }

    public void moveBackward() {
        if (this.playerCol > 0) {
            --this.playerCol;
            this.update();
        }

    }

    protected void setMenuItems() {
        this.playerSlot = 53 - (this.playerRow * 9 + this.playerCol);
        this.inv.setItem(playerSlot, makeItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "You!", List.of(), new HashMap<>(), false));

        this.inv.setItem(45, makeItem(Material.ARROW, String.valueOf(ChatColor.GOLD) + "Go Backward", List.of(String.valueOf(ChatColor.GRAY) + "Click to move left!"), new HashMap(), false));
        this.inv.setItem(53, makeItem(Material.ARROW, String.valueOf(ChatColor.GOLD) + "Go Forward", List.of(String.valueOf(ChatColor.GRAY) + "Click to move right!"), new HashMap(), false));
        this.inv.setItem(49, makeItem(Material.GRASS_BLOCK, String.valueOf(ChatColor.GREEN) + "Select an item!", List.of(String.valueOf(ChatColor.GRAY) + "Click to select an item!"), new HashMap(), false));

        for (int i : airSlots) {
            if (i != playerSlot && !placedItems.containsKey(i)) {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
        ItemStack lightBlueGlass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemStack limeGlass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);

        for (int i = 0; i < 45; i++) {
            if (i <= 26) {
                inv.setItem(i, lightBlueGlass); // Top 3 rows
            } else if (i >= 36) {
                inv.setItem(i, limeGlass); // 5th row
            }
        }

        // Restore all placed items correctly
        for (Map.Entry<Integer, ItemStack> entry : placedItems.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    public void saveMinecraft() {
        UUID playerId = this.menuUtility.getPlayer().getUniqueId();
        CircuitAddons.minecraftGame.set("player." + String.valueOf(playerId) + ".playerRow", this.playerRow);
        CircuitAddons.minecraftGame.set("player." + String.valueOf(playerId) + ".playerCol", this.playerCol);
        CircuitAddons.minecraftGame.set("player." + String.valueOf(playerId) + ".airSlots", this.airSlots);
        ConfigurationSection placedItemsSection = CircuitAddons.minecraftGame.createSection("player." + String.valueOf(playerId) + ".placedItems");

        for(Map.Entry<Integer, ItemStack> entry : this.placedItems.entrySet()) {
            placedItemsSection.set(((Integer)entry.getKey()).toString(), entry.getValue());
        }

        CircuitAddons.minecraftGame.set("player." + String.valueOf(playerId) + ".currentItem", this.currentItem);

        try {
            CircuitAddons.minecraftGame.save(CircuitAddons.minecraftGameFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMinecraft() {
        UUID playerId = this.menuUtility.getPlayer().getUniqueId();
        if (CircuitAddons.minecraftGame.contains("player." + String.valueOf(playerId))) {
            this.playerRow = CircuitAddons.minecraftGame.getInt("player." + String.valueOf(playerId) + ".playerRow", 2);
            this.playerCol = CircuitAddons.minecraftGame.getInt("player." + String.valueOf(playerId) + ".playerCol", 8);
            this.airSlots = CircuitAddons.minecraftGame.getIntegerList("player." + String.valueOf(playerId) + ".airSlots");
            ConfigurationSection placedItemsSection = CircuitAddons.minecraftGame.getConfigurationSection("player." + String.valueOf(playerId) + ".placedItems");
            if (placedItemsSection != null) {
                for(String key : placedItemsSection.getKeys(false)) {
                    try {
                        int slot = Integer.parseInt(key);
                        ItemStack item = placedItemsSection.getItemStack(key);
                        if (item != null) {
                            this.placedItems.put(slot, item);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }

            this.currentItem = CircuitAddons.minecraftGame.getItemStack("player." + String.valueOf(playerId) + ".currentItem", new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
    }

    protected int getSlots() {
        return 54;
    }

    protected String getTitle() {
        return "Minecraft";
    }

    public void open() {
        UUID playerId = this.menuUtility.getPlayer().getUniqueId();
        MineCraft existingGame = (MineCraft)activeGames.get(playerId);
        if (existingGame == null) {
            existingGame = new MineCraft(this.menuUtility);
            activeGames.put(playerId, existingGame);
            existingGame.loadMinecraft();
        }

        existingGame.inv = Bukkit.createInventory((InventoryHolder)null, existingGame.getSlots(), existingGame.getTitle());
        existingGame.loadMinecraft();
        existingGame.setMenuItems();
        this.menuUtility.getPlayer().playSound(this.menuUtility.getPlayer(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
        this.menuUtility.getPlayer().openInventory(existingGame.inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
