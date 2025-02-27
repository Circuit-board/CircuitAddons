package cool.circuit.circuitAddons.games.slotguesser;

import org.bukkit.entity.Player;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class slotguesser {
    public static Level generateLevel(int difficulty, Player player) {
        final int slot =  (int) (Math.random() * difficulty);
        if(!(difficulty ==  9) || !(difficulty == 18) | !(difficulty ==  27) || !(difficulty == 36) || !(difficulty == 45) || !(difficulty ==  54)){
            getMenuUtility(player).setSlot(slot);
            return new Level(difficulty,slot,getMenuUtility(player));

        }
        return null;
    }
}
