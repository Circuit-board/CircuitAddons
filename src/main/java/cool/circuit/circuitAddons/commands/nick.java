package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import static cool.circuit.circuitAddons.CircuitAddons.nicksList;
import static cool.circuit.circuitAddons.CircuitAddons.nicksListFile;

public class nick implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        if(!sender.hasPermission("circuitaddons.nick")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        boolean isNickedCheck1 = nicksList.getBoolean("nicks." + player.getName() + ".state", false);
        String isNickedCheck2 = nicksList.getString("nicks." + player.getName() + ".text");

        if (isNickedCheck1 && isNickedCheck2 != null && isNickedCheck2.equals(ChatColor.translateAlternateColorCodes('&', String.join(" ", args)))) {
            player.sendMessage(ChatColor.RED + "You already have this nickname.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /nick <name>");
            return true;
        }

        String nickname = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
        player.setDisplayName(nickname);
        player.setPlayerListName(nickname);
        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to: " + nickname);

        nicksList.set("nicks." + player.getName() + ".state", true);
        nicksList.set("nicks." + player.getName() + ".text", nickname);

        try {
            nicksList.save(nicksListFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while saving your nickname.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return List.of();
    }
}
