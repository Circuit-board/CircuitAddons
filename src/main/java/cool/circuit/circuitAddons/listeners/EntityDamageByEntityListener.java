package cool.circuit.circuitAddons.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Arrow arrow) {
            ProjectileSource shooter = arrow.getShooter();
            if (shooter instanceof Player shooterPlayer) {
                shooterPlayer.playSound(shooterPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                shooterPlayer.playSound(shooterPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, .5f, .5f);
            }
        }
    }
}
