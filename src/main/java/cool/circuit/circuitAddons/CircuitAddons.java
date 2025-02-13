package cool.circuit.circuitAddons;

import cool.circuit.circuitAddons.commands.*;
import cool.circuit.circuitAddons.listeners.InventoryClickListener;
import cool.circuit.circuitAddons.menusystem.MenuUtility;
import cool.circuit.circuitAddons.vault.CircuitBanks;
import cool.circuit.circuitAddons.vault.CircuitEconomy;
import io.papermc.paper.datacomponent.item.Fireworks;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;

import static cool.circuit.circuitAddons.vault.CircuitBanks.*;
import static cool.circuit.circuitAddons.vault.CircuitEconomy.playerBalances;

public final class CircuitAddons extends JavaPlugin implements Listener {

    ///////////////
    // Variables //
    /// ////////////

    public static CircuitAddons instance;

    public static ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public static ItemStack borderPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);

    private static final HashMap<Player, MenuUtility> MenuUtilityMap = new HashMap<>();

    private static Economy econ;
    private static Chat chat;
    private static Permission perms;

    public static String PREFIX;

    public static FileConfiguration settings;
    public static File settingsFile;

    public static FileConfiguration homes;
    public static File homesFile;

    public static FileConfiguration spawn;
    public static File spawnFile;

    public static FileConfiguration banks;
    public static File banksFile;

    public static FileConfiguration items;
    public static File itemsFile;

    //////////////////////
    // Basic Functions //

    /// /////////////////


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("No economy provider found!");
            return false;
        }

        econ = rsp.getProvider();
        getLogger().info("Successfully hooked into Vault's economy system.");
        return econ != null;
    }


    private void registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault is not installed! Cannot register economy.");
            return;
        }

        // Create and register CircuitEconomy as the economy provider
        CircuitEconomy economyProvider = new CircuitEconomy();
        getServer().getServicesManager().register(Economy.class, economyProvider, this, ServicePriority.High);
        econ = economyProvider;

        getLogger().info("CircuitEconomy registered as the economy provider.");
    }


    private void setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        RegisteredServiceProvider<Permission> permProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (permProvider != null) {
            perms = permProvider.getProvider();
        }
    }

    ///////////////////////////
    // Enable And Disabling //

    /// //////////////////////

    @Override
    public void onEnable() {
        instance = this;

        // Ensure data folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // ✅ Initialize settings file BEFORE loading
        settingsFile = new File(getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create settings.yml!");
            }
        }

        // ✅ Load settings AFTER file initialization
        loadSettings();
        saveSettings();

        setupChat();
        registerEconomy();

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault == null) {
            getLogger().severe("Vault is not installed! Disabling CircuitAddons...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy()) {
            getLogger().severe("No economy provider found! Disabling CircuitAddons...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // ✅ Initialize items.yml BEFORE using it
        itemsFile = new File(getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!items.contains("items")) {  // ⚠️ Fixed condition (was `if(items.contains("items"))`)
            items.createSection("items");
            try {
                items.save(itemsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ✅ Initialize banks.yml BEFORE using it
        loadBanks(); // Ensures banksFile is not null

        joinSync();

        // Register commands
        getCommand("circuitaddons").setExecutor(new circuitaddons());
        getCommand("alltitle").setExecutor(new alltitle());
        getCommand("manage").setExecutor(new manage());
        getCommand("sethome").setExecutor(new sethome());
        getCommand("home").setExecutor(new home());
        getCommand("setspawn").setExecutor(new setspawn());
        getCommand("spawn").setExecutor(new spawn());
        getCommand("economy").setExecutor(new economy());
        getCommand("balcheck").setExecutor(new BalCheck());
        getCommand("banks").setExecutor(new banks());
        getCommand("shop").setExecutor(new shop());

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        PREFIX = ChatColor.translateAlternateColorCodes('&', settings.getString("settings.prefix", "&0[&6CircuitAddons&0] &6"));

        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) {
                player.getPlayer().sendMessage(PREFIX + "Enabled!");
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
        sync();
    }

    ////////////////
    //Join Event///

    /// ////////////

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {

        itemsFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "items.yml");

        if (!itemsFile.exists()) {
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        items = YamlConfiguration.loadConfiguration(itemsFile);

        if(items.contains("items")) {
            items.createSection("items");
        }

        joinSync();

        List<Integer> colorValues = settings.getIntegerList("events.playerJoin.fireworks.color");

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        Location location = player.getLocation();

        if (colorValues.size() == 3) {
            Color color = Color.fromRGB(colorValues.get(0), colorValues.get(1), colorValues.get(2));
            if (settings.getBoolean("events.playerJoin.fireworks.enabled")) {
                String fireworkTypeStr = settings.getString("events.playerJoin.fireworks.pattern", "BALL"); // Default to BALL if missing
                try {
                    FireworkEffect.Type fireworkType = FireworkEffect.Type.valueOf(fireworkTypeStr.toUpperCase());
                    spawnFirework(location, fireworkType, color, 1);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning( "Invalid firework pattern in settings.yml: " + fireworkTypeStr);
                }
            }
        } else {
            Bukkit.getLogger().warning("Invalid firework color in settings.yml!");
        }

        boolean joinMessageEnabled = settings.getBoolean("settings.joinMessage");
        boolean joinTitleEnabled = settings.getBoolean("events.playerJoin.title.enabled");

        if (joinTitleEnabled) {
            String title = ChatColor.translateAlternateColorCodes('&',
                    settings.getString("events.playerJoin.title.title")
                            .replace("{server_name}", settings.getString("settings.server_name", "the server")));

            String subtitle = ChatColor.translateAlternateColorCodes('&',
                    settings.getString("events.playerJoin.title.subtitle")
                            .replace("{server_name}", settings.getString("settings.server_name", "the server")));

            player.sendTitle(title, subtitle);
        }
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
                            Bukkit.getLogger().warning("Invalid item in settings.yml: " + item);
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

        // If the file is missing, write default contents
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
                FileWriter writer = new FileWriter(settingsFile);
                writer.write(getDefaultConfigContents()); // Write hardcoded default config
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        settings = YamlConfiguration.loadConfiguration(settingsFile);

        // Load spawn.yml and homes.yml in a cleaner way
        spawn = loadOrCreateFile("spawn.yml");
        homes = loadOrCreateFile("homes.yml");
        banks = loadOrCreateFile("banks.yml");
        items = loadOrCreateFile("items.yml");
    }

    private String getDefaultConfigContents() {
        return """
                            # type /ca reload or /circuitaddons reload in chat to save changes!
                
                                                    settings:
                                                      joinMessage: true
                                                      prefix: '&0[&6CircuitAddons&0] &6' #https://easycodetools.com/tool/Minecraft-Text-Generator
                                                      server_name: 'Your server'
                                                    messages:
                                                      joinMessage: 'Welcome back to {server_name}, {player}!'
                                                      firstJoinMessage: 'Welcome to {server_name}, {player}!'
                                                    guis:
                                                      manage:
                                                        enabled: true
                                                      main:
                                                        enabled: true
                                                      alltitle:
                                                        enabled: true
                                                    rewards:
                                                      firstJoin:
                                                        items:
                                                          - 'STONE_SWORD:1'
                                                          - 'STONE_PICKAXE:1'
                                                          - 'STONE_AXE:1'
                                                    events:
                                                      playerJoin:
                                                        fireworks:
                                                          enabled: true
                                                          color: [255, 215, 0] # Must fit RGB format
                                                          pattern: STAR
                                                        title:
                                                          enabled: true
                                                          title: "&6Welcome To {server_name}!"
                                                          subtitle: "&bHave fun!"
                
                                                    # Don't modify unless you know what you are doing!
                
                                                    players: {}
                
                
                """;
    }

    /**
     * Ensures a YAML file exists and loads it, creating an empty one if missing.
     */
    private YamlConfiguration loadOrCreateFile(String fileName) {
        File file = new File(getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create empty file if missing
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }


    public static void reload() {
        instance.loadSettings(); // Reload settings.yml
        instance.saveSettings();
        CircuitBanks.loadSettings();
        sync();
        PREFIX = ChatColor.translateAlternateColorCodes('&',
                settings.getString("settings.prefix", "&0[&6CircuitAddons&0] &6"));
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

    public static Economy getEconomy() {
        return econ;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (chat == null || perms == null) return;

        String prefix = chat.getPlayerPrefix(event.getPlayer());
        String suffix = chat.getPlayerSuffix(event.getPlayer());

        // Add colors if needed
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        suffix = ChatColor.translateAlternateColorCodes('&', suffix);

        // Set the format: [Prefix] Name: Message Suffix
        String formattedMessage = prefix + event.getPlayer().getName() + " " + suffix + ": " + ChatColor.translateAlternateColorCodes('&', event.getMessage());
        event.setFormat(formattedMessage);
    }
    public static void loadBanks() {
        File dataFolder = Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder();

        // Ensure the data folder exists
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        banksFile = new File(dataFolder, "banks.yml");

        if (!banksFile.exists()) {
            try {
                banksFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create banks.yml: " + e.getMessage());
                return;
            }
        }

        banks = YamlConfiguration.loadConfiguration(banksFile);

        // Debugging logs
        if (banks.getKeys(false).isEmpty()) {
            saveBanks();
        }
    }
    public static void saveBanks() {
        if (banksFile == null) {
            File dataFolder = Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder();
            banksFile = new File(dataFolder, "banks.yml");
        }

        if (banks == null) {
            Bukkit.getLogger().severe("Error: banks is null! Cannot save settings.");
            return;
        }

        try {
            banks.save(banksFile);
            Bukkit.getLogger().info("Saved banks.yml successfully!");
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save banks.yml: " + e.getMessage());
        }
    }


}
