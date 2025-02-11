package cool.circuit.circuitAddons;

import cool.circuit.circuitAddons.commands.*;
import cool.circuit.circuitAddons.listeners.InventoryClickListener;
import cool.circuit.circuitAddons.menusystem.MenuUtility;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class CircuitAddons extends JavaPlugin implements Listener {

    public static  CircuitAddons instance;

    public static ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public static ItemStack borderPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);

    private static final HashMap<Player, MenuUtility> MenuUtilityMap = new HashMap<>();

    public static String PREFIX;

    public static FileConfiguration settings;
    public static File settingsFile;

    public static FileConfiguration homes;
    public static File homesFile;

    public static FileConfiguration spawn;
    public static File spawnFile;

    @Override
    public void onEnable() {



        instance = this;
        // Plugin startup logic
        getCommand("circuitaddons").setExecutor(new circuitaddons());
        getCommand("alltitle").setExecutor(new alltitle());
        getCommand("manage").setExecutor(new manage());
        getCommand("sethome").setExecutor(new sethome());
        getCommand("home").setExecutor(new home());
        getCommand("setspawn").setExecutor(new setspawn());
        getCommand("spawn").setExecutor(new spawn());

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(),this);
        Bukkit.getPluginManager().registerEvents(this,this);

        settingsFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(),"settings.yml");
        if(!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                // Java moment
            }
        }

        loadSettings();
        saveSettings();

        PREFIX = ChatColor.translateAlternateColorCodes('&', settings.getString("settings.prefix", "&0[&6CircuitAddons&0] &6"));

        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) { // Ensure the player is online before casting
                Player onlinePlayer = player.getPlayer();
                onlinePlayer.sendMessage(PREFIX + "Enabled!");
            }
        }
    }

    @Override
    public void onDisable() {
        PREFIX = ChatColor.translateAlternateColorCodes('&', settings.getString("settings.prefix", "&0[&6CircuitAddons&0] &6"));
        // Plugin shutdown logic
        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) { // Ensure the player is online before casting
                Player onlinePlayer = player.getPlayer();
                onlinePlayer.sendMessage(PREFIX + "Disabled!");
            }
        }
        saveSettings();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        List<Integer> colorValues = settings.getIntegerList("events.playerJoin.fireworks.color");

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        Location location = player.getLocation();

        if (colorValues.size() == 3) {
            Color color = Color.fromRGB(colorValues.get(0), colorValues.get(1), colorValues.get(2));
            if(settings.getBoolean("events.playerJoin.fireworks.enabled")) {
                String fireworkTypeStr = settings.getString("events.playerJoin.fireworks.pattern", "BALL"); // Default to BALL if missing
                try {
                    FireworkEffect.Type fireworkType = FireworkEffect.Type.valueOf(fireworkTypeStr.toUpperCase());
                    spawnFirework(location, fireworkType, color, 1);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning(PREFIX + "Invalid firework pattern in settings.yml: " + fireworkTypeStr);
                }
            }
        } else {
            Bukkit.getLogger().warning(PREFIX + "Invalid firework color in settings.yml!");
        }

        boolean joinMessageEnabled = settings.getBoolean("settings.joinMessage");

        if (joinMessageEnabled) {
            List<String> players = settings.getStringList("players");
            String message;

            if (players.contains(uuid)) {
                message = settings.getString("messages.joinMessage", "Welcome back {player}!")
                        .replace("{player}", player.getName())
                        .replace("{server_name}", settings.getString("settings.server_name", "the server"));
            } else {
                players.add(uuid);
                settings.set("players", players);
                getInstance().saveSettings(); // Use saveSettings() instead of saveConfig()

                message = settings.getString("messages.firstJoinMessage", "Welcome to {server_name}, {player}!")
                        .replace("{player}", player.getName())
                        .replace("{server_name}", settings.getString("settings.server_name", "Your server"));
                List<String> items = settings.getStringList("rewards.firstJoin.items");
                for (String item : items) {
                    String[] parts = item.split(":");
                    if (parts.length == 2) {
                        try {
                            Material material = Material.valueOf(parts[0].toUpperCase());
                            int amount = Integer.parseInt(parts[1]);
                            player.getInventory().addItem(new ItemStack(material, amount));
                        } catch (IllegalArgumentException e) {
                            Bukkit.getLogger().warning(PREFIX + "Invalid item in settings.yml: " + item);
                        }
                    }
                }
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message));
        }
    }

    public void saveSettings() {
        try {
            settings.save(settingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static MenuUtility getMenuUtility(Player p) {
        MenuUtility menuUtility;
        if (!(MenuUtilityMap.containsKey(p))) {
            menuUtility = new MenuUtility(p);
            MenuUtilityMap.put(p, menuUtility);

            return menuUtility;
        } else {
            return MenuUtilityMap.get(p);
        }
    }

    private void loadSettings() {
        settingsFile = new File(getDataFolder(), "settings.yml");

        if (!settingsFile.exists()) {
            saveResource("settings.yml", false); // Copy default settings.yml from resources folder
        }

        settings = YamlConfiguration.loadConfiguration(settingsFile);

        spawnFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "spawn.yml");

        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        spawn = YamlConfiguration.loadConfiguration(spawnFile);

        homesFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            try {
                homesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        homes = YamlConfiguration.loadConfiguration(homesFile);
    }

    public static void reload() {
        instance.loadSettings(); // Reload settings.yml
        instance.saveSettings();
        instance.PREFIX = ChatColor.translateAlternateColorCodes('&',
                instance.settings.getString("settings.prefix", "&0[&6CircuitAddons&0] &6"));
    }

    public static FileConfiguration getSettingsFile() {
        return settings;
    }


    public static CircuitAddons getInstance() {
        return instance;
    }

    public void spawnFirework(Location location, FireworkEffect.Type type, Color color, int power) {
        // Spawn the firework entity at the specified location
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);

        // Get the firework's metadata
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // Create the firework effect with the specified type and color
        FireworkEffect effect = FireworkEffect.builder()
                .with(type)
                .withColor(color)
                .flicker(true)
                .build();

        // Apply the effect and set the power (flight duration)
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
    }
}
