package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class enchanting implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        cool.circuit.circuitAddons.menusystem.menus.enchanting menu = new cool.circuit.circuitAddons.menusystem.menus.enchanting(getMenuUtility(player));
        menu.open();

        return true;
    }
}
