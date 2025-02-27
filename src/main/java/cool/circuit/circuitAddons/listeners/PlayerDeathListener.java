package cool.circuit.circuitAddons.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;

import static cool.circuit.circuitAddons.CircuitAddons.PREFIX;
import static cool.circuit.circuitAddons.CircuitAddons.getSettings;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String deathMessage = getSettings().getString("messages.deathMessage");

        if (deathMessage == null) {
            System.out.println(PREFIX + "Death message is null! Try /ca update.");
            return;
        }

        String source = (player.getKiller() instanceof Player) ? player.getKiller().getName() : "unknown forces";
        String formattedMessage = deathMessage.replace("{player}", player.getName()).replace("{source}", source);

        // Ensure the message is not empty
        if (formattedMessage.isEmpty()) {
            event.setDeathMessage(PREFIX + "Default death message.");
            return;
        }

        // Convert color codes and set death message
        event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
    }
}
