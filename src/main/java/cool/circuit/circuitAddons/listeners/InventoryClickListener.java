package cool.circuit.circuitAddons.listeners;


import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.menusystem.menus.manage;
import cool.circuit.circuitAddons.menusystem.menus.summonmob;
import io.papermc.paper.ban.BanListType;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Circuit Addons")) {

            int slot = event.getSlot();
            Player player = (Player) event.getWhoClicked();

            if (slot == 22) {
                player.closeInventory();
            }
            event.setCancelled(true);
        } else if (event.getView().getTitle().equals("Manage")) {

            int slot = event.getSlot();
            Player target = Bukkit.getPlayer(event.getInventory().getItem(13).getItemMeta().getDisplayName());
            Player player = (Player) event.getWhoClicked();

            if(slot == 4) {
                target.kickPlayer(ChatColor.RED + "L BOZO U GOT KICKED");
            } else if (slot == 14) {
                summonmob menu = new summonmob(getMenuUtility(player));
                menu.open();
            } else if (slot == 12) {
                if (target.getAddress() != null) {
                    String ip = target.getAddress().getHostString();
                    Bukkit.getBanList(BanList.Type.IP).addBan(ip, ChatColor.DARK_RED + "L BOZO U GOT BANNED", null, null);
                    target.kickPlayer(ChatColor.DARK_RED + "L BOZO U GOT BANNED");
                }
            } else if (slot == 22) {
                player.closeInventory();
            }

            event.setCancelled(true);
        } else if (event.getView().getTitle().equals("Summoning mob")) {
            event.setCancelled(true);

            int slot = event.getSlot();
            Player target = Bukkit.getPlayer(event.getInventory().getItem(0).getItemMeta().getDisplayName());
            Player player = (Player) event.getWhoClicked();

            if (slot == 22) {
                manage menu = new manage(getMenuUtility(player), target);
                menu.open();
            } else if (slot == 14) {
                Location targetLocation = target.getLocation();

                Panda panda = target.getWorld().spawn(targetLocation, Panda.class);
            } else if (slot == 12) {
                Location targetLocation = target.getLocation();

                Parrot parrot = target.getWorld().spawn(targetLocation, Parrot.class);
            } else if (slot == 4) {
                Location targetLocation = target.getLocation();

                Wolf wolf = target.getWorld().spawn(targetLocation, Wolf.class);
                wolf.setTamed(true);
                wolf.setOwner(target);
            }
        } else if(event.getView().getTitle().equals("Shop")) {
            event.setCancelled(true);
        }
    }
}
