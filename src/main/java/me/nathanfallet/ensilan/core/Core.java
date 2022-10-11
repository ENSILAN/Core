package me.nathanfallet.ensilan.core;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.nathanfallet.ensilan.core.commands.LeaderboardCommand;
import me.nathanfallet.ensilan.core.commands.LoginCommand;
import me.nathanfallet.ensilan.core.commands.MoneyCommand;
import me.nathanfallet.ensilan.core.commands.RegisterCommand;
import me.nathanfallet.ensilan.core.commands.SetSpawnCommand;
import me.nathanfallet.ensilan.core.commands.SpawnCommand;
import me.nathanfallet.ensilan.core.events.PlayerAuthentication;
import me.nathanfallet.ensilan.core.events.PlayerChat;
import me.nathanfallet.ensilan.core.events.PlayerInteract;
import me.nathanfallet.ensilan.core.events.PlayerJoin;
import me.nathanfallet.ensilan.core.events.PlayerQuit;
import me.nathanfallet.ensilan.core.events.PlayerRespawn;
import me.nathanfallet.ensilan.core.events.ServerPing;
import me.nathanfallet.ensilan.core.events.SignChange;
import me.nathanfallet.ensilan.core.events.WorldProtection;
import me.nathanfallet.ensilan.core.interfaces.LeaderboardGenerator;
import me.nathanfallet.ensilan.core.interfaces.ScoreboardGenerator;
import me.nathanfallet.ensilan.core.interfaces.WorldProtectionRule;
import me.nathanfallet.ensilan.core.models.AbstractGame;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;
import me.nathanfallet.ensilan.core.models.Leaderboard;
import me.nathanfallet.ensilan.core.models.Money;

public class Core extends JavaPlugin {

    // Static instance
    private static Core instance;

    // Retrieve instance
    public static Core getInstance() {
        return instance;
    }

    // Properties
    private Connection connection;
    private List<EnsilanPlayer> players;
    private List<AbstractGame> games;
    private List<WorldProtectionRule> worldProtectionRules;
    private List<ScoreboardGenerator> scoreboardGenerators;
    private Map<String, LeaderboardGenerator> leaderboardGenerators;
    private Map<String, Leaderboard> leaderboards;
    private Location spawn;

    // Enable plugin
    @Override
    public void onEnable() {
        // Store instance
        instance = this;

        // Configuration stuff
        saveDefaultConfig();
        reloadConfig();

        // Check connection
        if (!initDatabase()) {
            return;
        }

        // Init players
        for (Player player : Bukkit.getOnlinePlayers()) {
            getPlayers().add(new EnsilanPlayer(player));
        }

        // Clear custom entities
        clearCustomEntities();

        // World protection rules
        if (getConfig().getBoolean("server.spawn_protection")) {
            getWorldProtectionRules().add(new WorldProtectionRule() {
                @Override
                public boolean isProtected(Location location) {
                    return location.getWorld().getName().equals(Bukkit.getWorlds().get(0).getName());
                }
                @Override
                public boolean isAllowedInProtectedLocation(Player player, EnsilanPlayer ep, Location location, Event event) {
                    if (
                        event instanceof PlayerInteractEvent &&
                        ((PlayerInteractEvent) event).getClickedBlock() != null &&
                        ((PlayerInteractEvent) event).getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)
                    ) {
                        return true;
                    }
                    return player.isOp();
                }
            });
        }

        // Initialize leaderboards
        Core.getInstance().getLeaderboardGenerators().put("money", new LeaderboardGenerator() {
            @Override
            public List<String> getLines(int limit) {
                ArrayList<String> lines = new ArrayList<String>();

                try {
                    // Fetch data to MySQL Database
                    Statement state = Core.getInstance().getConnection().createStatement();
                    ResultSet result = state.executeQuery(
                            "SELECT name, money " +
                                    "FROM players " +
                                    "WHERE money > 0 " +
                                    "ORDER BY money DESC " +
                                    "LIMIT " + limit);

                    // Set lines
                    while (result.next()) {
                        lines.add(
                                result.getString("name") +
                                        ChatColor.GOLD + " - " + ChatColor.YELLOW +
                                        result.getInt("money") + Money.symbol);
                    }
                    result.close();
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return lines;
            }

            @Override
            public String getTitle() {
                return "Money";
            }
        });
        Core.getInstance().getLeaderboardGenerators().put("score", new LeaderboardGenerator() {
            @Override
            public List<String> getLines(int limit) {
                ArrayList<String> lines = new ArrayList<String>();

                try {
                    // Fetch data to MySQL Database
                    Statement state = Core.getInstance().getConnection().createStatement();
                    ResultSet result = state.executeQuery(
                            "SELECT name, score " +
                                    "FROM players " +
                                    "WHERE score > 0 " +
                                    "ORDER BY score DESC " +
                                    "LIMIT " + limit);

                    // Set lines
                    while (result.next()) {
                        lines.add(
                                result.getString("name") +
                                        ChatColor.GOLD + " - " + ChatColor.YELLOW +
                                        result.getInt("score") + " points");
                    }
                    result.close();
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return lines;
            }

            @Override
            public String getTitle() {
                return "Score";
            }
        });
        Core.getInstance().getLeaderboardGenerators().put("victories", new LeaderboardGenerator() {
            @Override
            public List<String> getLines(int limit) {
                ArrayList<String> lines = new ArrayList<String>();

                try {
                    // Fetch data to MySQL Database
                    Statement state = Core.getInstance().getConnection().createStatement();
                    ResultSet result = state.executeQuery(
                            "SELECT name, victories " +
                                    "FROM players " +
                                    "WHERE victories > 0 " +
                                    "ORDER BY victories DESC " +
                                    "LIMIT " + limit);

                    // Set lines
                    while (result.next()) {
                        lines.add(
                                result.getString("name") +
                                        ChatColor.GOLD + " - " + ChatColor.YELLOW +
                                        result.getInt("victories") + " victoires");
                    }
                    result.close();
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return lines;
            }

            @Override
            public String getTitle() {
                return "Victoires";
            }
        });

        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerAuthentication(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(), this);
        Bukkit.getPluginManager().registerEvents(new ServerPing(), this);
        Bukkit.getPluginManager().registerEvents(new SignChange(), this);
        Bukkit.getPluginManager().registerEvents(new WorldProtection(), this);

        // Register commands
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
        getCommand("money").setExecutor(new MoneyCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());

        // Register tasks
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Store current time
                long now = System.currentTimeMillis();

                // Refresh games
                for (AbstractGame game : getGames()) {
                    game.updateStatus();
                }

                // Create scoreboard lines
                FileConfiguration conf = getConfig();
                List<String> headerLines = new ArrayList<>();
                List<String> footerLines = new ArrayList<>();
                headerLines.add(ChatColor.AQUA + "");
                headerLines.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Serveur :");
                headerLines.add(ChatColor.WHITE + conf.getString("server.name"));
                headerLines.add(ChatColor.WHITE + "" + getPlayers().size() + " joueurs");
                headerLines.add(ChatColor.GREEN + "");
                headerLines.add(ChatColor.GREEN + "" + ChatColor.BOLD + Money.name + " :");
                footerLines.add(ChatColor.YELLOW + "");
                footerLines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + conf.getString("server.ip"));

                // Apply to eveyone
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Merge lines
                    List<String> lines = new ArrayList<>();
                    EnsilanPlayer ep = getPlayer(player.getUniqueId());

                    // Header
                    lines.addAll(headerLines);

                    // Money
                    lines.add(ChatColor.WHITE + ""
                            + (ep.getCachedMoney() != null ? ep.getCachedMoney() + Money.symbol : "Chargement..."));

                    // Extra lines from other plugins
                    for (ScoreboardGenerator generator : getScoreboardGenerators()) {
                        lines.addAll(generator.generateLines(player, ep));
                    }

                    // Footer
                    lines.addAll(footerLines);

                    // Apply
                    ep.getScoreboard().update(player, lines);

                    // Check authentification state
                    if (!ep.isAuthenticated()) {
                        if (now - ep.getLogin() > 60_000) {
                            player.kickPlayer("Delai de connexion expiré !");
                        } else if (ep.hasPassword()) {
                            player.sendMessage(ChatColor.RED + "Utilisez " + ChatColor.DARK_RED + "/login <password> "
                                    + ChatColor.RED + "pour vous connecter.");
                        } else {
                            player.sendMessage(ChatColor.RED + "Utilisez " + ChatColor.DARK_RED
                                    + "/register <password> <password> " + ChatColor.RED + "pour vous inscrire.");
                        }
                    }
                }

                // Refresh leaderboards
                for (Leaderboard leaderboard : getLeaderboards().values()) {
                    leaderboard.update();
                }
            }
        }, 0, 20);

        // Saving task
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Save leaderboards
                saveLeaderboards();
            }
        }, 300 * 20, 300 * 20);
    }

    // Disable plugin
    @Override
    public void onDisable() {
        // Remove players
        for (EnsilanPlayer player : getPlayers()) {
            player.getScoreboard().kill();
        }
        players = null;

        // Remove games
        for (AbstractGame game : games) {
            game.saveSigns();
        }
        games = null;

        // Clear world protection rules
        worldProtectionRules = null;

        // Clear scoreboard generators
        scoreboardGenerators = null;

        // Save leaderboards
        saveLeaderboards();

        // Clear leaderboards and generators
        for (Leaderboard leaderboard : getLeaderboards().values()) {
            leaderboard.kill();
        }
        leaderboards = null;
        leaderboardGenerators = null;

        // Clear custom entitues
        clearCustomEntities();

        // Save and close database connection
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Retrieve or initialize database connection
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                FileConfiguration conf = getConfig();
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + conf.getString("database.host") + ":" + conf.getInt("database.port") + "/"
                                + conf.getString("database.database") + "?autoReconnect=true",
                        conf.getString("database.user"), conf.getString("database.password"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getLogger()
                    .severe("[ENSILAN Core] Un probleme est survenue lors de la connexion a la base de donnees");
            Bukkit.getLogger().severe("[ENSILAN Core] Verifiez les informations dans le fichier de configuration");
            Bukkit.getPluginManager().disablePlugin(this);
            return null;
        }
        return connection;
    }

    // Initialize database
    private boolean initDatabase() {
        try {
            Statement create = getConnection().createStatement();
            create.executeUpdate("CREATE TABLE IF NOT EXISTS `players` (" +
                    "`uuid` varchar(255) NOT NULL," +
                    "`name` varchar(255) NOT NULL," +
                    "`password` varchar(255) NOT NULL DEFAULT ''," +
                    "`money` bigint NOT NULL DEFAULT '0'," +
                    "`score` bigint NOT NULL DEFAULT '0'," +
                    "`victories` bigint NOT NULL DEFAULT '0'," +
                    "`admin` tinyint NOT NULL DEFAULT '0'," +
                    "PRIMARY KEY (`uuid`)" +
                    ")");
            create.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Retrieve players
    public List<EnsilanPlayer> getPlayers() {
        // Init players if needed
        if (players == null) {
            players = new ArrayList<>();
        }

        // Return players
        return players;
    }

    // Retrieve a player from its UUID
    public EnsilanPlayer getPlayer(UUID uuid) {
        // Iterate players
        for (EnsilanPlayer player : getPlayers()) {
            if (player.getUUID().equals(uuid)) {
                return player;
            }
        }

        // No player found
        return null;
    }

    // Retrieve games
    public List<AbstractGame> getGames() {
        // Init list if needed
        if (games == null) {
            games = new ArrayList<>();
        }

        // Return list
        return games;
    }

    // Retrieve protection rules
    public List<WorldProtectionRule> getWorldProtectionRules() {
        // Init list if needed
        if (worldProtectionRules == null) {
            worldProtectionRules = new ArrayList<>();
        }

        // Return list
        return worldProtectionRules;
    }

    // Retrieve scoreboard generators
    public List<ScoreboardGenerator> getScoreboardGenerators() {
        // Init list if needed
        if (scoreboardGenerators == null) {
            scoreboardGenerators = new ArrayList<>();
        }

        // Return list
        return scoreboardGenerators;
    }

    // Retrieve leaderboard generators
    public Map<String, LeaderboardGenerator> getLeaderboardGenerators() {
        // Init list if needed
        if (leaderboardGenerators == null) {
            leaderboardGenerators = new HashMap<>();
        }

        // Return list
        return leaderboardGenerators;
    }

    // Retrieve leaderboards
    public Map<String, Leaderboard> getLeaderboards() {
        // Init list if needed
        if (leaderboards == null) {
            leaderboards = new HashMap<>();

            // And read from file
            FileConfiguration file = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "leaderboards.yml"));
            for (String key : file.getKeys(false)) {
                // Extract variables
                Location location = new Location(Bukkit.getWorld(file.getString(key + ".location.world")),
                        file.getDouble(key + ".location.x"), file.getDouble(key + ".location.y"),
                        file.getDouble(key + ".location.z"));
                String type = file.getString(key + ".type");
                int limit = file.getInt(key + ".limit");

                // Put in list
                leaderboards.put(key, new Leaderboard(location, type, limit));
            }
        }

        // Return list
        return leaderboards;
    }

    // Save leaderboards
    public void saveLeaderboards() {
        // Get file
        File empty = new File(getDataFolder(), ".empty");
        File source = new File(getDataFolder(), "leaderboards.yml");
        FileConfiguration file = YamlConfiguration.loadConfiguration(empty);

        // Set keys
        for (String key : getLeaderboards().keySet()) {
            Leaderboard leaderboard = leaderboards.get(key);
            file.set(key + ".location.world", leaderboard.getLocation().getWorld().getName());
            file.set(key + ".location.x", leaderboard.getLocation().getX());
            file.set(key + ".location.y", leaderboard.getLocation().getY());
            file.set(key + ".location.z", leaderboard.getLocation().getZ());
            file.set(key + ".type", leaderboard.getType());
            file.set(key + ".limit", leaderboard.getLimit());
        }

        // Save file
        try {
            file.save(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get spawn location
    public Location getSpawn() {
        // Check if spawn is loaded
        if (spawn == null) {
            // Get file
            File f = new File(getDataFolder(), "spawn.yml");

            // Return default spawn location if it doesn't exist
            if (!f.exists()) {
                return Bukkit.getWorlds().get(0).getSpawnLocation();
            }

            // Else, read from file
            FileConfiguration config = YamlConfiguration.loadConfiguration(f);
            spawn = new Location(Bukkit.getWorld(config.getString("world")), config.getDouble("x"),
                    config.getDouble("y"), config.getDouble("z"));
            spawn.setYaw(config.getLong("yaw"));
            spawn.setPitch(config.getLong("pitch"));
        }

        // Return spawn location
        return spawn;
    }

    // Set spawn location
    public void setSpawn(Location spawn) {
        // Update loaded spawn
        this.spawn = spawn;

        // Get file
        File f = new File(getDataFolder(), "spawn.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);

        // Set location
        config.set("world", spawn.getWorld().getName());
        config.set("x", spawn.getX());
        config.set("y", spawn.getY());
        config.set("z", spawn.getZ());
        config.set("yaw", spawn.getYaw());
        config.set("pitch", spawn.getPitch());

        // Save
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear custom entities
    private void clearCustomEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getCustomName() != null && entity.getCustomName().startsWith(ChatColor.COLOR_CHAR + "")) {
                    entity.remove();
                }
            }
        }
    }

    // Authentication

    public boolean isAuthenticationEnabled() {
        return !Bukkit.getOnlineMode();
    }

}
