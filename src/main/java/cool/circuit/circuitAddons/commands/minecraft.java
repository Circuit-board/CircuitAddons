package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.games.minecraft.MineCraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class minecraft implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if(!sender.hasPermission("circuitaddons.minecraft")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

         MineCraft menu = new MineCraft(getMenuUtility(player));
        menu.open();

        return true;
    }
}
