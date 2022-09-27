package me.nathanfallet.ensilan.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class AbstractGame {

    /*
    * Properties
    */

    protected GameState state;
	protected int currentCountValue;
    protected ArrayList<Location> signs;

    /*
    * Common methods
    */

    public AbstractGame() {
		state = GameState.WAITING;
		currentCountValue = 0;
    }

	public GameState getState() {
		return state;
	}

	public ArrayList<Location> getSigns() {
		return signs;
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

        // Check that signs are registered
        if (signs == null) {
            return;
        }

        // Iterate them
		Iterator<Location> i = signs.iterator();
		while (i.hasNext()) {
			Block b = i.next().getBlock();
			if (b != null && b.getType().equals(Material.OAK_SIGN)) {
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

    // Number of the game
    public abstract int getGameNumber();
    
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
