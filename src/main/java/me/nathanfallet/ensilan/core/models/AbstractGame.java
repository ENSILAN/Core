package me.nathanfallet.ensilan.core.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;

public abstract class AbstractGame {

    /*
    * Properties
    */

    protected int gameNumber;
    protected GameState state;
	protected int currentCountValue;
    protected ArrayList<Location> signs;

    /*
    * Common methods
    */

    public AbstractGame(int gameNumber) {
        this.gameNumber = gameNumber;
		this.state = GameState.WAITING;
		this.currentCountValue = 0;
        loadSigns();
    }

	public GameState getState() {
		return state;
	}

	public ArrayList<Location> getSigns() {
		return signs;
	}

    public String getSignPath() {
        return getGameName().toLowerCase().replace(" ", "_")
               + "." + "game" + getGameNumber();
    }

    public void loadSigns() {
        signs = new ArrayList<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(
            new File(Core.getInstance().getDataFolder(), "signs.yml")
        );
        ConfigurationSection data = config.getConfigurationSection(getSignPath());
        if (data != null) {
            for (String sk : data.getKeys(false)) {
                signs.add(new Location(
                    Bukkit.getWorld(data.getString(sk + ".world")),
                    data.getInt(sk + ".x"),
                    data.getInt(sk + ".y"),
                    data.getInt(sk + ".z")
                ));
            }
        }
    }

    public void saveSigns() {
        File file = new File(Core.getInstance().getDataFolder(), "signs.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        int i = 1;
		for (Location l : signs) {
			config.set(getSignPath() + ".sign" + i + ".world" , l.getWorld().getName());
			config.set(getSignPath() + ".sign" + i + ".x" , l.getBlockX());
			config.set(getSignPath() + ".sign" + i + ".y" , l.getBlockY());
			config.set(getSignPath() + ".sign" + i + ".z" , l.getBlockZ());
            i++;
		}
        while (config.contains(getSignPath() + ".sign" + i)) {
            config.set(getSignPath() + ".sign" + i, null);
            i++;
        }
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public int getGameNumber() {
        return gameNumber;
    }

    public String getGameDescription() {
        return state.getText().replaceAll("%d", currentCountValue + "");
    }

    public void updateStatus() {
        // Here we update the status of the game
        if (state.equals(GameState.IN_GAME)) {
            mainHandler();
        } else {
            if (getPlayers().size() >= getMinPlayers() && state.equals(GameState.WAITING) && getCountdown() != 0) {
                state = GameState.START_COUNT;
                currentCountValue = getCountdown() + 1;
            }
            if (state.equals(GameState.START_COUNT) && getPlayers().size() < getMinPlayers()) {
                state = GameState.WAITING;
                currentCountValue = 0;
            }
            if (state.equals(GameState.START_COUNT)) {
                currentCountValue--;
                if (currentCountValue == 0) {
                    start();
                } else if (currentCountValue == 60 || currentCountValue == 30
                        || currentCountValue == 20 || currentCountValue == 10
                        || currentCountValue <= 5) {
                    for (UUID uuid : getPlayers()) {
                        Player p = Bukkit.getPlayer(uuid);
                        p.sendMessage(ChatColor.YELLOW + "Début de la partie dans " + currentCountValue + " secondes...");
                    }
                }
            }
        }

        // Iterate signs
		Iterator<Location> i = signs.iterator();
		while (i.hasNext()) {
			Block b = i.next().getBlock();
			if (b != null && b.getType().equals(Material.OAK_WALL_SIGN)) {
				Sign s = (Sign) b.getState();
				s.setLine(0, ChatColor.DARK_RED + "[" + getGameName() + "]");
				s.setLine(1, "Partie N°" + getGameNumber());
				s.setLine(2, getPlayers().size() + "/" + getMaxPlayers());
				s.setLine(3, getGameDescription());
				s.update();
			} else {
				i.remove();
			}
		}
	}

    public void preJoin(Player player, EnsilanPlayer ep) {
        if (state.equals(GameState.WAITING) || state.equals(GameState.START_COUNT)) {
			if (getMaxPlayers() == 0 || getPlayers().size() < getMaxPlayers()) {
				join(player, ep);
				player.sendMessage(ChatColor.GREEN + "Vous avez rejoins la partie N°" + getGameNumber());
			} else {
				player.sendMessage(ChatColor.RED + "La partie est pleine !");
			}
		} else {
			player.sendMessage(ChatColor.RED + "La partie a déjà commencé !");
		}
    }

    /*
    * Abstract methods
    */

    // Countdown before the start of the game. Zero to disable
    public abstract int getCountdown();

    // Number of players required for the game to start
    public abstract int getMinPlayers();

    // Max number of players in the game
    public abstract int getMaxPlayers();

    // Name of the game
    public abstract String getGameName();
    
    // Handle the start process of the game
    public abstract void start();

    // Handle the stop process of the game
    public abstract void stop();

    // Called every second
    public abstract void mainHandler();

    // Get players participating in the game (excluding those who lost)
    public abstract ArrayList<UUID> getPlayers();

    // Get all players of the game, even those who lost
    public abstract ArrayList<UUID> getAllPlayers();

    // Make a player join this game
    public abstract void join(Player player, EnsilanPlayer ep);

    /*
    * Game state
    */

    public enum GameState {

		WAITING("En attente..."),
        START_COUNT("Début dans %ds"),
        IN_GAME("En cours"),
        FINISHED("Terminé");

		private String text;

		GameState(String text) {
			this.text = text;
		}

        public String getText() {
            return text;
        }

	}

}
