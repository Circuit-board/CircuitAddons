package cool.circuit.circuitAddons.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class WardenShortBow implements Listener {
    private static final NamespacedKey KEY = new NamespacedKey(getInstance(), "wardenshortbow");
    private static final ItemStack ITEM;

    static {
        ITEM = makeItem(
                Material.BOW,
                ChatColor.BLUE + "Warden Shortbow",
                List.of(ChatColor.GRAY + "Damage: " + ChatColor.RED + "50.0",
                        "",
                        ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "A warden's special essence of darkness..."),
                new HashMap<>(),
                false
        );

        ItemMeta meta = ITEM.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.BOOLEAN, true);
            ITEM.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onBowShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        if (item.getItemMeta().getPersistentDataContainer().has(KEY, PersistentDataType.BOOLEAN)) {
            if (!player.getInventory().contains(Material.SCULK)) {
                player.sendMessage(ChatColor.RED + "No sculk ammo!");
                event.setCancelled(true);
                return;
            }
            // Remove one Ender Pearl instead of all
            player.getInventory().removeItem(new ItemStack(Material.SCULK, 1));

            for(int i = 0; i < 3; i++) {
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setShooter(player);
                arrow.setVelocity(arrow.getVelocity().multiply(0.75));
                arrow.setCritical(true);
                arrow.setDamage(50.0);
            }

            event.setCancelled(true);
        }
    }
    public static void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getInstance(), "wardenshortbow"), ITEM);
        recipe.shape("SSS", "SBS", "SSS");
        recipe.setIngredient('S', Material.SCULK);
        recipe.setIngredient('B', Material.BOW);
        getInstance().getServer().addRecipe(recipe);
    }
}
