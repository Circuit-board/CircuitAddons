package cool.circuit.circuitAddons.vault;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class CircuitPerms extends Permission {
    private static final List<String> groups = new ArrayList<>();
    private static final Map<UUID, Set<String>> playerPermissions = new HashMap<>();
    private static final Map<UUID, String> playerGroups = new HashMap<>();

    @Override
    public String getName() {
        return "CircuitPerms";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return true;
    }

    @Override
    public boolean playerHas(String world, String playerName, String permission) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        return playerPermissions.getOrDefault(player.getUniqueId(), Collections.emptySet()).contains(permission);
    }

    @Override
    public boolean playerAdd(String world, String playerName, String permission) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        playerPermissions.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(permission);
        return true;
    }

    @Override
    public boolean playerRemove(String world, String playerName, String permission) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        return playerPermissions.getOrDefault(player.getUniqueId(), Collections.emptySet()).remove(permission);
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return false; // Not implemented yet
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        return false; // Not implemented yet
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        return false; // Not implemented yet
    }

    @Override
    public boolean playerInGroup(String world, String playerName, String group) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        return group.equalsIgnoreCase(playerGroups.get(player.getUniqueId()));
    }

    @Override
    public boolean playerAddGroup(String world, String playerName, String group) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        playerGroups.put(player.getUniqueId(), group);
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, String playerName, String group) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        if (group.equalsIgnoreCase(playerGroups.get(player.getUniqueId()))) {
            playerGroups.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    @Override
    public String[] getPlayerGroups(String world, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return new String[0];
        return playerGroups.containsKey(player.getUniqueId()) ? new String[]{playerGroups.get(player.getUniqueId())} : new String[0];
    }

    @Override
    public String getPrimaryGroup(String world, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player == null ? "" : playerGroups.getOrDefault(player.getUniqueId(), "");
    }

    @Override
    public String[] getGroups() {
        return groups.toArray(new String[0]);
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }
}
