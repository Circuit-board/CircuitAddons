package cool.circuit.circuitAddons.games.paintdrying;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class paintdrying {
    public static Level generateLevel(ItemStack pane, ItemStack fadingPane, Player player) {
        return new Level(pane,fadingPane,getMenuUtility(player),player);
    }
}
