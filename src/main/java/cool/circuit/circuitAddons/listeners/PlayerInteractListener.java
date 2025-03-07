package cool.circuit.circuitAddons.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            if (item == null || !item.hasItemMeta()) return;

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(getInstance(), "fireball");
            if (meta.getPersistentDataContainer().has(key)) {
                Fireball fireball = event.getPlayer().launchProjectile(Fireball.class);
                Vector velocity = fireball.getVelocity().normalize().multiply(4); // Increase multiplier for longer range
                fireball.setVelocity(velocity);
                Vector direction = event.getPlayer().getLocation().getDirection().normalize();
                direction.multiply(2.0); // Adjust strength of boost
                direction.setY(0.8); // Slight upward boost

                event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(direction));

                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    event.getPlayer().getInventory().setItemInMainHand(null);
                }

                event.setCancelled(true);
            }
        }
    }
}
