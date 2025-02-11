package cool.circuit.circuitAddons.menusystem.menus;


import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.menusystem.menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.borderPane;
import static cool.circuit.circuitAddons.CircuitAddons.pane;

public class manage extends menu {

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
        return 9*3;
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

        ItemStack pandaStack = new ItemStack(Material.PANDA_SPAWN_EGG);
        ItemMeta pandaMeta = pandaStack.getItemMeta();
        pandaMeta.setDisplayName(ChatColor.WHITE + "Summon a mob at the player");
        pandaMeta.setLore(List.of(ChatColor.GRAY + "Opens a menu to select which mob to summon at the player!"));
        pandaStack.setItemMeta(pandaMeta);

        inv.setItem(14,pandaStack);

        ItemStack playerHeadStack =  new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerHeadMeta = playerHeadStack.getItemMeta();
        playerHeadMeta.setDisplayName(target.getName());
        playerHeadStack.setItemMeta(playerHeadMeta);

        inv.setItem(0,playerHeadStack);

        menuUtility.setTarget(target);

        ItemStack closeStack =  new ItemStack(Material.ARROW);
        ItemMeta closeMeta = closeStack.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeStack.setItemMeta(closeMeta);

        inv.setItem(22,closeStack);

        ItemStack banStack =  new ItemStack(Material.BARRIER);
        ItemMeta banMeta = banStack.getItemMeta();
        banMeta.setDisplayName(ChatColor.DARK_RED + "Ban");
        banMeta.setLore(List.of(ChatColor.GRAY + "Bans the targets IP Address!"));
        banStack.setItemMeta(banMeta);

        ItemStack kickStack =  new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta kickMeta = kickStack.getItemMeta();
        kickMeta.setDisplayName(ChatColor.RED + "Kick");
        kickMeta.setLore(List.of(ChatColor.GRAY + "Kicks the target!"));
        kickStack.setItemMeta(kickMeta);

        inv.setItem(22,closeStack);
        inv.setItem(12,banStack);
        inv.setItem(4,kickStack);

        menuUtility.setTarget(target);
    }

    @Override
    public void open() {
        inv = Bukkit.createInventory(null, getSlots(),getTitle());

        setMenuItems();
        menuUtility.getPlayer().openInventory(inv);
    }
}
