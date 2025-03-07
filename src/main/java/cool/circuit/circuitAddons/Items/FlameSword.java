package cool.circuit.circuitAddons.Items;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class FlameSword implements Listener {
    private static final NamespacedKey KEY = new NamespacedKey(getInstance(), "flamesword");

    public static ItemStack getFlameSword() {
        ItemStack flameSword = makeItem(
                Material.IRON_SWORD,
                ChatColor.GOLD + "Flame" + " " + ChatColor.RED + "Sword",
                List.of(ChatColor.GRAY + "Damage: " + ChatColor.RED + "20",
                        ChatColor.RESET + "",
                        ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Forged from flaming hot lava..."),
                new HashMap<>(),
                false
        );

        ItemMeta meta = flameSword.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.BOOLEAN, true);
            flameSword.setItemMeta(meta);
        }
        return flameSword;
    }

    public static void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getInstance(), "flamesword"), getFlameSword());
        recipe.shape(" B ", " B ", " S ");
        recipe.setIngredient('B', Material.LAVA_BUCKET);
        recipe.setIngredient('S', Material.STICK);
        getInstance().getServer().addRecipe(recipe);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    boolean hasTag = meta.getPersistentDataContainer().has(KEY, PersistentDataType.BOOLEAN);

                    if (hasTag) {
                        // Debug: Show entity type
                        // Apply the damage
                        event.setDamage(20);

                        // Check if the entity is fire-resistant
                        if (isFireResistant(event.getEntity())) {
                            player.sendMessage(ChatColor.RED + "The entity is fire-resistant and won't be set on fire.");
                        } else {
                            // Apply fire effect to the entity using setFireTicks
                            event.getEntity().setFireTicks(50); // Sets the entity on fire for 100 ticks (5 seconds)
                        }
                    }
                }
            }
        }
    }

    private boolean isFireResistant(Entity entity) {
        // Check for specific fire-resistant mobs
        if (entity instanceof Blaze || entity instanceof MagmaCube || entity instanceof Strider || entity instanceof WitherSkeleton || entity instanceof Warden) {
            return true;
        }

        // Check if the entity has the Fire Resistance effect
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }

        return false;
    }
}
