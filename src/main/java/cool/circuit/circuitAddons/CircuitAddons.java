package cool.circuit.circuitAddons;

import cool.circuit.circuitAddons.commands.*;
import cool.circuit.circuitAddons.listeners.ChatListener;
import cool.circuit.circuitAddons.listeners.InventoryClickListener;
import cool.circuit.circuitAddons.listeners.InventoryOpenListener;
import cool.circuit.circuitAddons.listeners.PlayerDeathListener;
import cool.circuit.circuitAddons.papi.CircuitExpansion;
import cool.circuit.circuitAddons.vault.CircuitEconomy;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cool.circuit.circuitAPI.menusystem.MenuManager.setup;
import static cool.circuit.circuitAPI.utils.makeItemUtil.makeItem;
/*import static cool.circuit.circuitAddons.games.circuitclicker.CircuitClicker.getLeaderboard;
import static cool.circuit.circuitAddons.games.circuitclicker.CircuitClicker.scores*/
import static cool.circuit.circuitAddons.vault.CircuitBanks.*;

import cool.circuit.circuitAPI.menusystem.MenuUtility;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

public final class CircuitAddons extends JavaPlugin implements Listener {

     ///////////////
    // Variables //
   ///////////////

    public static CircuitAddons instance;

    private static Scoreboard currentScoreboard;

    public static ItemStack pane = makeItem(
            Material.BLACK_STAINED_GLASS_PANE,
            " ",
            List.of(),
            new HashMap<>(),
            false
    );

    public static ItemStack borderPane = makeItem(
            Material.YELLOW_STAINED_GLASS_PANE,
            " ",
            List.of(),
            new HashMap<>(),
            false
    );

    private static final HashMap<Player, MenuUtility> MenuUtilityMap = new HashMap<>();
    public static final HashMap<UUID, Boolean> MuteMap = new HashMap<>();
    public static final List<String> BlackListedWords = new ArrayList<>();

    public static ItemStack itemToEnchant = null;
    public static Enchantment currentEnchant = null;
    public static int currentEnchantLevel = 0;

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

    public static FileConfiguration mutes;
    public static File mutesFile;

    public static FileConfiguration blacklist;
    public static File blacklistFile;

    public static FileConfiguration scoreList;
    public static File scoreListFile;

    public static FileConfiguration minecraftGame;
    public static File minecraftGameFile;

    public static FileConfiguration nicksList;
    public static File nicksListFile;

    private LuckPerms luckPerms;

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

        setup(Bukkit.getServer(),this);

        // Ensure data folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        luckPerms = LuckPermsProvider.get();

        // ✅ Initialize mutes.yml BEFORE using it
        mutesFile = new File(getDataFolder(), "mutes.yml");
        if (!mutesFile.exists()) {
            try {
                mutesFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create mutes.yml!");
            }
        }

        mutes = YamlConfiguration.loadConfiguration(mutesFile); // ✅ Load mutes file

        scoreListFile = new File(getDataFolder(), "scoreList.yml");
        if (!scoreListFile.exists()) {
            try {
                scoreListFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create scoreList.yml!");
            }
        }

        scoreList = YamlConfiguration.loadConfiguration(scoreListFile);

        if (!mutes.contains("mutes")) { // ⚠️ Ensure "mutes" section exists
            mutes.createSection("mutes");
            try {
                mutes.save(mutesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        minecraftGameFile = new File(getDataFolder(), "minecraftGame.yml");
        if (!minecraftGameFile.exists()) {
            try {
                minecraftGameFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create scoreList.yml!");
            }
        }
        minecraftGame = YamlConfiguration.loadConfiguration(minecraftGameFile);

        nicksListFile = new File(getDataFolder(), "nicks.yml");
        if (!nicksListFile.exists()) {
            try {
                nicksListFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create nicks.yml!");
            }
        }

        nicksList = YamlConfiguration.loadConfiguration(nicksListFile);

        // ✅ Initialize settings.yml BEFORE loading
        settingsFile = new File(getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Failed to create settings.yml!");
            }
        }

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
        blacklistFile = new File(getDataFolder(), "blacklist.yml");
        if (!itemsFile.exists()) {
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        items = YamlConfiguration.loadConfiguration(itemsFile); // ✅ Load items file
        blacklist = YamlConfiguration.loadConfiguration(blacklistFile);

        if (!items.contains("items")) {
            items.createSection("items");
            try {
                items.save(itemsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*for(Player i : Bukkit.getOnlinePlayers()) {
            scores.put(i.getUniqueId(), scoreList.getInt("scores." + i.getUniqueId() + ".score"));
        }*/

        // ✅ Initialize banks.yml BEFORE using it
        loadBanks(); // Ensures banksFile is not null

        joinSync();

        /*if (scoreList.contains("highscores")) {
            ConfigurationSection section = scoreList.getConfigurationSection("highscores");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    try {
                        UUID playerId = UUID.fromString(key);
                        int highScore = section.getInt(key);
                        CircuitClicker.highScores.put(playerId, highScore);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid UUID in highscores: " + key);
                    }
                }
            }
        }*/

        new BukkitRunnable() {
            @Override
            public void run() {
                updateTablist();
            }
        }.runTaskTimer(this, 0L, 100L);

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
        getCommand("mute").setExecutor(new mute());
        getCommand("unmute").setExecutor(new unmute());
        getCommand("blacklist").setExecutor(new blacklist());
        getCommand("biome").setExecutor(new biome());
        getCommand("gma").setExecutor(new gma());
        getCommand("gmc").setExecutor(new gmc());
        getCommand("gms").setExecutor(new gms());
        getCommand("gmsp").setExecutor(new gmsp());
        getCommand("slotguesser").setExecutor(new slotguesser());
        getCommand("paintdrying").setExecutor(new paintdrying());
        getCommand("minecraft").setExecutor(new minecraft());
        getCommand("createentity").setExecutor(new createentity());
        getCommand("enchanting").setExecutor(new cool.circuit.circuitAddons.commands.enchanting());
        getCommand("heal").setExecutor(new heal());
        getCommand("nick").setExecutor(new nick());
        getCommand("unnick").setExecutor(new unnick());
        getCommand("npc").setExecutor(new npc());

        startScoreboardUpdater();

        if (nicksList.getConfigurationSection("nicks") != null) {
            for (String i : nicksList.getConfigurationSection("nicks").getKeys(false)) {
                if (nicksList.getBoolean("nicks." + i + ".state", false)) {
                    Player player = Bukkit.getPlayer(i);
                    if (player != null) { // Ensure player is online
                        String nickname = nicksList.getString("nicks." + i + ".text", player.getName());
                        player.setDisplayName(nickname);
                        player.setPlayerListName(nickname);
                        LuckPerms luckPerms = LuckPermsProvider.get();
                        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                        if (user != null) {
                            MetaNode node = MetaNode.builder("nickname", nickname).build();
                            user.data().add(node);
                            luckPerms.getUserManager().saveUser(user);

                            // Ensure LuckPerms applies changes immediately
                            luckPerms.getUserManager().loadUser(player.getUniqueId()).thenAccept(updatedUser -> {
                                Bukkit.getScheduler().runTask(this, () -> {
                                    player.setPlayerListName(nickname);
                                });
                            });
                        }
                    }
                }
            }
        }


        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);


        if(settings.getString("settings.prefix") == null) {
            return;
        }
        if (settings.getString("settings.prefix") == null) {
            PREFIX = "";
        } else {
            PREFIX = ChatColor.translateAlternateColorCodes('&',
                    settings.getString("settings.prefix", "&6[CircuitAddons] &e"));
        }
        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) {
                player.getPlayer().sendMessage(PREFIX + "Enabled!");
            }
        }

        ConfigurationSection muteSection = mutes.getConfigurationSection("mutes");
        if (muteSection != null) {
            for (String uuid : muteSection.getKeys(false)) {
                MuteMap.put(UUID.fromString(uuid), true);
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CircuitExpansion().register();
        }
        for (String word : blacklist.getStringList("blacklist")) { // Correct way to fetch list
            if (!BlackListedWords.contains(word)) {
                BlackListedWords.add(word);
            }
        }
        for(Player i : Bukkit.getOnlinePlayers())
            createScoreBoard(i);
    }



    @Override
    public void onDisable() {
        if (settings.getString("settings.prefix") == null) {
            PREFIX = "";
        } else {
            PREFIX = ChatColor.translateAlternateColorCodes('&',
                    settings.getString("settings.prefix", "&6[CircuitAddons] &e"));
        }

        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) { // Ensure the player is online before casting
                Player onlinePlayer = player.getPlayer();
                onlinePlayer.sendMessage(PREFIX + "Disabled!");
            }
        }
        saveSettings();
        sync();
        try {
            scoreList.save(scoreListFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////
    //Join Event///
    /// ////////////

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        itemsFile = new File(Bukkit.getPluginManager().getPlugin("CircuitAddons").getDataFolder(), "items.yml");

        if (!itemsFile.exists()) {
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        items = YamlConfiguration.loadConfiguration(itemsFile);

        if (!items.contains("items")) {
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
                    spawnFirework(location, fireworkType, color, 10);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Invalid firework pattern in settings.yml: " + fireworkTypeStr);
                }
            }
        } else {
            Bukkit.getLogger().warning("Invalid firework color in settings.yml!");
        }

        boolean joinMessageEnabled = settings.getBoolean("settings.joinMessage");
        boolean joinTitleEnabled = settings.getBoolean("events.playerJoin.title.enabled");

        if (joinTitleEnabled) {
            String titleRaw = ChatColor.translateAlternateColorCodes('&',settings.getString("events.playerJoin.title.title", "&6Welcome To {server_name}!"));
            String subtitleRaw = ChatColor.translateAlternateColorCodes('&',settings.getString("events.playerJoin.title.subtitle", "&bHave fun!"));

            titleRaw = titleRaw.replace("{server_name}", settings.getString("settings.server_name", "the server"));
            subtitleRaw = subtitleRaw.replace("{server_name}", settings.getString("settings.server_name", "the server"));


            player.sendTitle(titleRaw, subtitleRaw, 10, 20, 10);
        }

        if (joinMessageEnabled) {
            List<String> players = settings.getStringList("players");
            String messageRaw;

            if (players.contains(uuid)) {
                messageRaw = settings.getString("messages.joinMessage", "Welcome back {player}!")
                        .replace("{player}", player.getName())
                        .replace("{server_name}", settings.getString("settings.server_name", "the server"));
            } else {
                players.add(uuid);
                settings.set("players", players);
                getInstance().saveSettings();

                messageRaw = settings.getString("messages.firstJoinMessage", "Welcome to {server_name}, {player}!")
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

            player.sendMessage(messageRaw);
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
        mutes = loadOrCreateFile("mutes.yml");
        blacklist = loadOrCreateFile("blacklist.yml");
        scoreList = loadOrCreateFile("scoreList.yml");
        nicksList = loadOrCreateFile("nicks.yml");
    }

    private String getDefaultConfigContents() {
        return """
                            # type /ca reload or /circuitaddons reload in chat to save changes!
                
                                                    settings:
                                                      joinMessage: true
                                                      prefix: '&0[&6CircuitAddons&0] &6'
                                                      server_name: 'Your server'
                                                      scoreboard:
                                                        title: '&eCIRCUIT ADDONS'
                                                        lines: []
                                                      tablist:
                                                        header: '&eCircuit Addons'
                                                        footer: '&eYou are using Circuit Addons!'
                                                      motd:
                                                        lines: []
                                                    messages:
                                                      joinMessage: 'Welcome back to {server_name}, {player}!'
                                                      firstJoinMessage: 'Welcome to {server_name}, {player}!'
                                                      deathMessage: '{player} got internally rekt!'
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
                                                          subtitle: "&9Have fun!"
                                                        
                
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



        // Correctly refresh the scoreboard

        // Update PREFIX
        PREFIX = ChatColor.translateAlternateColorCodes('&',
                settings.getString("settings.prefix", "&e&6[CircuitAddons] &6"));

        // **FORCE REASSIGN SCOREBOARD TO ALL PLAYERS**
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(instance.currentScoreboard);
        }
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

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        // Convert legacy color codes (& -> §)
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        suffix = ChatColor.translateAlternateColorCodes('&', suffix);

        // Convert & codes in the message as well
        String legacyMessage = ChatColor.translateAlternateColorCodes('&', event.getMessage());

        // Set formatted chat message
        event.setFormat(prefix + "%s" + suffix + ": " + legacyMessage);
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

    public static FileConfiguration getSettings() {
        return settings;
    }

    public static ItemStack getYouWinDiamond() {
        ItemStack item = makeItem(Material.DIAMOND,ChatColor.GOLD + "" + ChatColor.BOLD + "Win Diamond",List.of(ChatColor.GRAY + "Obtained by winning slot guesser"),new HashMap<>(), false);
        return item;
    }
    private void registerRecipies() {}
    private Objective objective;

    private String parsePlaceholders(Player player, String input) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            String parsed = PlaceholderAPI.setPlaceholders(player, input);
            return parsed;
        }
        return input;
    }

    private void startScoreboardUpdater() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    createScoreBoard(player);
                }
            }
        }.runTaskTimer(this,0L,40L);
    }

    private void createScoreBoard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard board = manager.getNewScoreboard();
        objective = board.registerNewObjective("circuitaddons", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', (settings.getString("settings.scoreboard.title", "<gold>CIRCUIT ADDONS"))));

        List<String> lines = settings.getStringList("settings.scoreboard.lines");
        int currentSlot = lines.size();

        for (String line : lines) {
            String formattedLine = ChatColor.translateAlternateColorCodes('&', parsePlaceholders(player,line));
            objective.getScore(formattedLine).setScore(currentSlot);
            currentSlot--;
        }

        player.setScoreboard(board);

        instance.currentScoreboard = board;
    }

    public String getLuckPermsPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";

        return ChatColor.translateAlternateColorCodes('&', user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix());
    }

    private void updateTablist() {
        String header = ChatColor.translateAlternateColorCodes('&', settings.getString("settings.tablist.header", "You are using Circuit Addons!") + "\n");
        String footer = "\n" + ChatColor.translateAlternateColorCodes('&', settings.getString("settings.tablist.footer", "Online players: " + Bukkit.getOnlinePlayers().size()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            String prefix = getLuckPermsPrefix(player);
            String displayName = player.getName();

            if (nicksList.getBoolean("nicks." + player.getName() + ".state", false)) {
                displayName = nicksList.getString("nicks." + player.getName() + ".text", player.getName());
            }

            player.setPlayerListHeaderFooter(header, footer);
            player.setPlayerListName(prefix + displayName);
        }
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        List<String> lines = settings.getStringList("settings.motd.lines");
        StringBuilder linesFormatted = new StringBuilder();

        for (String line : lines) {
            linesFormatted.append(ChatColor.translateAlternateColorCodes('&', line)).append("\n");
        }

        event.setMotd(linesFormatted.toString().trim()); // Set the MOTD
    }
}