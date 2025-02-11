package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.CircuitAddons;
import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.menusystem.menu;
import cool.circuit.circuitAddons.menusystem.menus.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

import static cool.circuit.circuitAddons.CircuitAddons.getInstance;
import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class manage implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (CircuitAddons.getSettingsFile().getBoolean("guis.manage.enabled")) {

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Please specify a player!");
                return false;
            }

            if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                sender.sendMessage(ChatColor.RED + "Player does not exist!");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            cool.circuit.circuitAddons.menusystem.menus.manage menu = new cool.circuit.circuitAddons.menusystem.menus.manage(getMenuUtility(player), target);
            menu.open();

            return true;
        } else {
            Player player = (Player) sender;

            player.sendMessage(ChatColor.RED + "This feature isn't enabled!");
        }
        return false;
    }
}
