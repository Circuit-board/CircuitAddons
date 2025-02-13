package cool.circuit.circuitAddons.menusystem;

import org.bukkit.entity.Player;

public class MenuUtility {

    protected Player player;
    protected Player target;

    public MenuUtility(Player player){
        this.player = player;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Player getTarget() {
        return this.target;
    }

    public Player getPlayer() {
        return player;
    }
}
