package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class createentity implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /createentity <entity_type> <health> <speed multiplier> <damage multiplier> <custom_name...>");
            return false;
        }

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid entity type: " + args[0]);
            return false;
        }

        double health;
        try {
            health = Double.parseDouble(args[1]);
            if (health <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid health value: " + args[1]);
            return false;
        }

        double speedMultiplier;
        try {
            speedMultiplier = Double.parseDouble(args[2]);
            if (speedMultiplier <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid speed multiplier: " + args[2]);
            return false;
        }

        double damageMultiplier;
        try {
            damageMultiplier = Double.parseDouble(args[3]);
            if (damageMultiplier <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid damage multiplier: " + args[3]);
            return false;
        }

        // Convert color codes in the custom name
        String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", List.of(args).subList(4, args.length)));

        Entity entity = player.getWorld().spawnEntity(player.getLocation(), entityType);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
            livingEntity.setHealth(health);

            if (livingEntity.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
                double baseSpeed = livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue();
                livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(baseSpeed * speedMultiplier);
            }

            if (livingEntity.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
                double baseDamage = livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue();
                livingEntity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(baseDamage * damageMultiplier);
            }
        }

        sender.sendMessage(ChatColor.GREEN + "Spawned " + entityType.name() + " with " + health + " HP, speed x" + speedMultiplier +
                ", damage x" + damageMultiplier + ", named: " + name);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> entityNames = new ArrayList<>();
            for (EntityType type : EntityType.values()) {
                if (type.isSpawnable() && type.getName() != null) {
                    entityNames.add(type.name().toLowerCase());
                }
            }
            return entityNames;
        } else if (args.length == 2) {
            return List.of("20", "50", "100");
        } else if (args.length == 3) {
            return List.of("1", "1.5", "2", "3"); // Speed multiplier suggestions
        } else if (args.length == 4) {
            return List.of("1", "1.5", "2", "3", "5"); // Damage multiplier suggestions
        } else if (args.length >= 5) {
            return List.of(args[4]);
        }
        return List.of();
    }
}
