package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAPI.exceptions.CircuitAPINotSetup;
import cool.circuit.circuitAPI.menusystem.Menu;
import cool.circuit.circuitAPI.menusystem.MenuUtility;
import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.pane;

public class enchanting_template extends Menu {
    private final Enchantment enchant;

    public enchanting_template(MenuUtility menuUtility, Enchantment enchant) {
        super(menuUtility);
        this.enchant = enchant;
    }

    @Override
    protected void setMenuItems() {
        for(int i = enchant.getStartLevel(); i < enchant.getMaxLevel()+1; i++) {
            ItemStack item = makeItem(
                    Material.ENCHANTED_BOOK,
                    ChatColor.LIGHT_PURPLE + "Level : " + i,
                    List.of(ChatColor.GRAY + "Cost: " + i*5," ",ChatColor.LIGHT_PURPLE + "Click to purchase!"),
                    new HashMap<>(),
                    false
            );
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(CircuitAddons.getInstance(),"cost"), PersistentDataType.INTEGER,i*5);
            item.setItemMeta(meta);
            inv.setItem(i+10,item);
        }
    }

    @Override
    protected void handleMenu(InventoryClickEvent inventoryClickEvent) throws CircuitAPINotSetup {

    }

    @Override
    protected boolean cancelClicks() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "Enchantment: " + enchant.getName().replace('_',' ').toLowerCase();
    }

    @Override
    protected int getSlots() {
        return 9*3;
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
