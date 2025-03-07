package cool.circuit.circuitAddons.listeners;

import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {
    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball) {
            event.setCancelled(true); // Prevent block damage
        }
    }
}
