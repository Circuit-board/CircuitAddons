package cool.circuit.circuitAddons.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
import static cool.circuit.circuitAddons.CircuitAddons.getInstance;

public class FireBall implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if(!sender.hasPermission("circuitaddons.fireball")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return false;
        }

        ItemStack fireBall = makeItem(
                Material.FIRE_CHARGE,
                ChatColor.GOLD + "" + ChatColor.BOLD +  "Fire Ball",
                List.of(ChatColor.GRAY + "Right-Click to use!"),
                new HashMap<>(),
                false
        );
        ItemMeta fireBallMeta = fireBall.getItemMeta();
        fireBallMeta.getPersistentDataContainer().set(new NamespacedKey(getInstance(),"fireball"), PersistentDataType.BOOLEAN, true);
        fireBall.setItemMeta(fireBallMeta);
        player.getInventory().addItem(fireBall);


        return true;
    }
}
