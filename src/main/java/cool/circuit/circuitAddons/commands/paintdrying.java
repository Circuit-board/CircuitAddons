package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import cool.circuit.circuitAddons.menusystem.menus.paintdrying_main;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class paintdrying implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }
        if(!sender.hasPermission("circuitaddons.paintdrying")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        paintdrying_main menu = new paintdrying_main(getMenuUtility(player));
        menu.open();

        return true;
    }
}
