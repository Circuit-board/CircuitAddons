package cool.circuit.circuitAddons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cool.circuit.circuitAddons.CircuitAddons.perms;

public class Permissions implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /permissions <player|group> <add|remove> <name> <permission>");
            return false;
        }

        if (!sender.hasPermission("circuitaddons.permissions")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        String action = args[2];
        String permission = args[3];

        if (args[0].equalsIgnoreCase("player")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return false;
            }

            if (action.equalsIgnoreCase("add")) {
                perms.playerAdd(target.getWorld().getName(), target.getName(), permission);
                sender.sendMessage(ChatColor.GREEN + "Added permission " + permission + " to " + target.getName());
                target.sendMessage(ChatColor.GREEN + "You have been granted permission: " + permission);
            } else if (action.equalsIgnoreCase("remove")) {
                perms.playerRemove(target.getWorld().getName(), target.getName(), permission);
                sender.sendMessage(ChatColor.GREEN + "Removed permission " + permission + " from " + target.getName());
                target.sendMessage(ChatColor.RED + "You have been revoked permission: " + permission);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid action. Use 'add' or 'remove'.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("group")) {
            String group = args[1];
            String world = Bukkit.getWorlds().get(0).getName(); // Get first available world

            if (action.equalsIgnoreCase("add")) {
                perms.groupAdd(world, group, permission);
                sender.sendMessage(ChatColor.GREEN + "Added permission " + permission + " to group " + group);
            } else if (action.equalsIgnoreCase("remove")) {
                perms.groupRemove(world, group, permission);
                sender.sendMessage(ChatColor.GREEN + "Removed permission " + permission + " from group " + group);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid action. Use 'add' or 'remove'.");
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Invalid argument. Use /permissions <player|group> <add|remove> <name> <permission>");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("group", "player");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("player")) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("group")) {
                String[] groups = perms.getGroups();
                return (groups != null) ? Arrays.asList(groups) : List.of();
            }
        } else if (args.length == 3) {
            return List.of("add", "remove");
        } else if (args.length == 4) {
            return Bukkit.getPluginManager().getPermissions().stream()
                    .map(permission -> permission.getName())
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
