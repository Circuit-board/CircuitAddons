package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static cool.circuit.circuitAddons.CircuitAddons.PREFIX;

public class biome  implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players  can use this command!");
            return false;
        }

        Location location = player.getLocation();
        Biome biome = player.getWorld().getBiome(location);
        player.sendMessage(PREFIX + "You are in the " + biome.toString().toLowerCase(Locale.ROOT).replace("_"," ") + " biome!");

        return true;
    }
}
