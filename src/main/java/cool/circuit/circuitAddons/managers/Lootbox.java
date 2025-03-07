package cool.circuit.circuitAddons.managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Lootbox(String name, int cost, List<ItemStack> rewards) {

    public void open(Player player) {
        if (rewards.isEmpty()) {
            player.sendMessage("This lootbox contains no rewards!");
            return;
        }

        int random = (int) (Math.random() * rewards.size());
        player.sendMessage(ChatColor.GREEN + "You recieved " + rewards.get(random).getType().name().toLowerCase().replace("_", " ") + ChatColor.GOLD +  " x" + rewards.get(random).getAmount());
        player.getInventory().addItem(rewards.get(random));
    }

}
