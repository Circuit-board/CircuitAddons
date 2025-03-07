package cool.circuit.circuitAddons.listeners;

import cool.circuit.circuitAddons.CircuitAddons;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import static cool.circuit.circuitAddons.CircuitAddons.*;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String message = event.getMessage();

        for (String word : BlackListedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                player.sendMessage(ChatColor.RED + "You cannot use that word!");
                event.setCancelled(true);
                return;
            }
        }

        if (MuteMap.getOrDefault(playerId, false)) {
            player.sendMessage(ChatColor.RED + "You are currently muted!");
            event.setCancelled(true);
            return;
        }

        String prefix = circuitChat != null ? circuitChat.getPlayerPrefix(player) : "";
        if (prefix == null) prefix = "";

        String displayName = nicksList.getBoolean("nicks." + player.getName() + ".state", false)
                ? nicksList.getString("nicks." + player.getName() + ".text", player.getName())
                : player.getName();

        player.setDisplayName(prefix + displayName);
        player.setPlayerListName(prefix + displayName);

        event.setFormat(prefix + displayName + ": " + message);
    }

}
