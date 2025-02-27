package cool.circuit.circuitAddons.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import static cool.circuit.circuitAddons.CircuitAddons.BlackListedWords;
import static cool.circuit.circuitAddons.CircuitAddons.MuteMap;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Get the player's message
        String message = event.getMessage();

        // Check for blacklisted words
        for (String word : BlackListedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot use that word!");
                event.setCancelled(true);
                return;
            }
        }

        // Check if player is muted
        UUID playerId = event.getPlayer().getUniqueId();
        if (MuteMap.getOrDefault(playerId, false)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are currently muted!");
            event.setCancelled(true);
        }
    }
}
