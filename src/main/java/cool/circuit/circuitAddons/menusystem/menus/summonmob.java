package cool.circuit.circuitAddons.menusystem.menus;

import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.menusystem.menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.borderPane;
import static cool.circuit.circuitAddons.CircuitAddons.pane;

public class summonmob extends menu {
    public summonmob(MenuUtility menuUtility) {
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

        Player target = menuUtility.getTarget();

        ItemStack playerHeadStack =  new ItemStack(Material.PLAYER_HEAD);
        ItemMeta playerHeadMeta = playerHeadStack.getItemMeta();
        playerHeadMeta.setDisplayName(target.getName());
        playerHeadStack.setItemMeta(playerHeadMeta);

        inv.setItem(0,playerHeadStack);

        ItemStack backStack = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backStack.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "Back");
        backStack.setItemMeta(backMeta);

        inv.setItem(22, backStack);

        ItemStack pandaStack = new ItemStack(Material.PANDA_SPAWN_EGG);
        ItemMeta pandaMeta = pandaStack.getItemMeta();
        pandaMeta.setDisplayName(ChatColor.WHITE + "Summon a panda at the player");
        pandaMeta.setLore(List.of(ChatColor.GRAY + "Summons a panda at the player!"));
        pandaStack.setItemMeta(pandaMeta);

        inv.setItem(14,pandaStack);

        ItemStack parrotStack = new ItemStack(Material.PARROT_SPAWN_EGG);
        ItemMeta parrotMeta = parrotStack.getItemMeta();
        parrotMeta.setDisplayName(ChatColor.WHITE + "Summon a parrot at the player");
        parrotMeta.setLore(List.of(ChatColor.GRAY + "Summons a parrot at the player!"));
        parrotStack.setItemMeta(parrotMeta);

        inv.setItem(12,parrotStack);

        ItemStack wolfStack = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta wolfMeta = wolfStack.getItemMeta();
        wolfMeta.setDisplayName(ChatColor.WHITE + "Summon a dog at the player");
        wolfMeta.setLore(List.of(ChatColor.GRAY + "Summons a dog at the player!"));
        wolfStack.setItemMeta(wolfMeta);

        inv.setItem(4,wolfStack);

    }

    @Override
    protected String getTitle() {
        return "Summoning mob";
    }

    @Override
    protected int getSlots() {
        return 9*3;
    }

    @Override
    public void open() {
        inv = Bukkit.createInventory(null,getSlots(),getTitle());

        setMenuItems();

        menuUtility.getPlayer().openInventory(inv);
    }
}
