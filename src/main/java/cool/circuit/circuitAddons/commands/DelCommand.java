package cool.circuit.circuitAddons.commands;

import cool.circuit.circuitAddons.managers.LootboxManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DelCommand implements CommandExecutor, TabCompleter {

    private final LootboxManager lootboxManager;

    public DelCommand(LootboxManager lootboxManager) {
        this.lootboxManager = lootboxManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /del <lootboxName>");
            return true;
        }

        String lootboxName = args[0];
        if (lootboxManager.getLootbox(lootboxName) == null) {
            sender.sendMessage("That lootbox does not exist.");
            return true;
        }

        lootboxManager.removeLootbox(lootboxName);
        LootboxManager.saveLootboxes();
        sender.sendMessage("Successfully deleted lootbox: " + lootboxName);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return LootboxManager.lootboxes.stream()
                    .map(lootbox -> lootbox.name())
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
