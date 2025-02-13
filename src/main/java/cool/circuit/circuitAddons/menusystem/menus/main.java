package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.menusystem.menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class main extends menu {
    public main(MenuUtility menuUtility) {
        super(menuUtility);
    }

    @Override
    protected void setMenuItems() {
        addMenuBorder(borderPane);

        for (int i = 0; i < 9 * 3; i++) {
            boolean isBorder = false;

            for (int slot : patternSlots) {
                if (i == slot) {
                    isBorder = true;
                    break; // Stop checking if it's already identified as a border
                }
            }

            if (!isBorder) {
                inv.setItem(i, pane);
            }
        }

        ItemStack manageStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta manageMeta = manageStack.getItemMeta();
        manageMeta.setDisplayName(ChatColor.GREEN + "Manage Player");
        manageMeta.setLore(List.of(ChatColor.GRAY + "To manage a player type: \"/manage [player name]\""));
        manageStack.setItemMeta(manageMeta);

        ItemStack allTitleStack = new ItemStack(Material.YELLOW_DYE);
        ItemMeta allTitleMeta = allTitleStack.getItemMeta();
        allTitleMeta.setDisplayName(ChatColor.YELLOW + "Send title to all players");
        allTitleMeta.setLore(List.of(ChatColor.GRAY + "To send a title and/or subtitle to all players type: ",ChatColor.GRAY + "\"/alltitle [subtitle|title|both] [title] [subtitle]\""));
        allTitleStack.setItemMeta(allTitleMeta);

        inv.setItem(12,allTitleStack);
        inv.setItem(14,manageStack);

        ItemStack closeStack =  new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeStack.setItemMeta(closeMeta);

        inv.setItem(22,closeStack);
    }


    @Override
    protected int getSlots() {
        return 9*3;
    }

    @Override
    protected String getTitle() {
        return "Circuit Addons";
    }

    @Override
    public void open() {
        inv = Bukkit.createInventory(null,getSlots(),getTitle());
        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }
}
