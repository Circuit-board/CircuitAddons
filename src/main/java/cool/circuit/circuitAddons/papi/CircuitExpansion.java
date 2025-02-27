package cool.circuit.circuitAddons.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cool.circuit.circuitAddons.CircuitAddons.getEconomy;
import static cool.circuit.circuitAddons.utils.NumberFormatter.formatNumber;

public class CircuitExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "circuit";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Circuit";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.6";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null; // Avoid null issues
        }
        if (params.equalsIgnoreCase("balance")) {
            return formatNumber((int) getEconomy().bankBalance("bank_" + player.getName()).balance);
        } else if (params.equalsIgnoreCase("health")) {
            return String.valueOf((int) player.getHealth());
        } else if (params.equalsIgnoreCase("maxHealth")) {
            return String.valueOf((int) player.getMaxHealth());
        }
        return null;
    }
}
