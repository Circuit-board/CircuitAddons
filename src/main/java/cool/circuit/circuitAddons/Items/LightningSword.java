package cool.circuit.circuitAddons.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class LightningSword implements Listener {

    private static final NamespacedKey KEY = new NamespacedKey(getInstance(), "lightning_sword");
    private static final ItemStack ITEM;
    static {
        ITEM = makeItem(
                Material.IRON_SWORD,
                ChatColor.YELLOW + "" + ChatColor.BOLD + "Lightning Sword",
                List.of(ChatColor.GRAY + "Damage: " + ChatColor.RED + "1",
                        ChatColor.RESET + "",
                        ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Zeus's favourite toy..."),
                new HashMap<>(),
                false
        );
        ItemMeta meta = ITEM.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.BOOLEAN, true);
            ITEM.setItemMeta(meta);
        }


    }

    public static void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(getInstance(), "lightningsword"), ITEM);
        recipe.shape(" T ", " T ", " S ");
        recipe.setIngredient('T', org.bukkit.Material.TORCH);
        recipe.setIngredient('S', org.bukkit.Material.STICK);
        getInstance().getServer().addRecipe(recipe);
    }
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();


        if (damager instanceof Player) {
            Player player = (Player) damager;
            ItemStack heldItem = player.getInventory().getItemInMainHand();


            if (heldItem.hasItemMeta() &&
                    heldItem.getItemMeta().getPersistentDataContainer().has(KEY, PersistentDataType.BOOLEAN)) {
                    event.setDamage(1);
                if (victim instanceof Player) {
                    Player target = (Player) victim;
                    target.getWorld().strikeLightning(target.getLocation());
                } else {
                    Entity target = victim;
                    target.getWorld().strikeLightning(target.getLocation());
                }
            }
        }
    }
}
