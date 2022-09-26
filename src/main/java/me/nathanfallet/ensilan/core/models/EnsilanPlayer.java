package me.nathanfallet.ensilan.core.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.events.EnsilanPlayerLoadedEvent;

public class EnsilanPlayer {

    // Properties
    private UUID uuid;
    private PlayerScoreboard scoreboard;

    // Cached current data
    private Long money;
    private Boolean admin;

    // Bukkit constructor
    public EnsilanPlayer(final Player player) {
        // Set properties
        this.uuid = player.getUniqueId();
        this.scoreboard = new PlayerScoreboard("ENSILAN");

        // Update player data
        try {
            // Database fetch/update/insert
            PreparedStatement fetch = Core.getInstance().getConnection()
                .prepareStatement("SELECT * FROM players WHERE uuid = ?");
            fetch.setString(1, uuid.toString());
			ResultSet result = fetch.executeQuery();
            if (result.next()) {
                // Save data to cache
                money = result.getLong("money");
                admin = result.getBoolean("admin");

                // Update
                PreparedStatement update = Core.getInstance().getConnection()
                    .prepareStatement("UPDATE players SET name = ? WHERE uuid = ?");
                update.setString(1, player.getName());
                update.setString(2, uuid.toString());
                update.executeUpdate();
                update.close();
            } else {
                // Save data to cache
                money = 0L;
                admin = false;

                // Insert
                PreparedStatement insert = Core.getInstance().getConnection()
                    .prepareStatement("INSERT INTO players (uuid, name) VALUES(?, ?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.executeUpdate();
                insert.close();

                // Broadcast welcome message
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Bienvenue " + ChatColor.GOLD + player.getName()
                    + ChatColor.YELLOW + " sur le serveur de l'" + ChatColor.GOLD + "ENSILAN" + ChatColor.YELLOW + " !");
            }
            result.close();
            fetch.close();

            // Set operator
            player.setOp(admin != null && admin.booleanValue());

            // Player loaded, call event
            Bukkit.getPluginManager().callEvent(new EnsilanPlayerLoadedEvent(player, this));
        } catch (Exception e) {
            // Error, disconnect player
            e.printStackTrace();
            player.kickPlayer("Erreur lors de la vérification de votre identité dans la base de données !");
        }
    }

    // Retrieve UUID
    public UUID getUUID() {
        return uuid;
    }

    // Retrieve scoreboard
    public PlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    // Cached money
    public Long getCachedMoney() {
        return money;
    }

    // Cached admin
    public Boolean getCachedAdmin() {
        return admin;
    }

    // Get money
    public Long getMoney() {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
				.prepareStatement("SELECT money FROM players WHERE uuid = ?");
			state.setString(1, uuid.toString());
			ResultSet result = state.executeQuery();
			if (result.next()) {
				money = result.getLong("money");
			}
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return money;
	}

    // Get admin
    public Boolean getAdmin() {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
				.prepareStatement("SELECT admin FROM players WHERE uuid = ?");
			state.setString(1, uuid.toString());
			ResultSet result = state.executeQuery();
			if (result.next()) {
				admin = result.getBoolean("admin");
			}
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}

    // Set money
    public void setMoney(Long newMoney) {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
					.prepareStatement("UPDATE players SET money = ? WHERE uuid = ?");
			state.setLong(1, newMoney);
			state.setString(2, uuid.toString());
			state.executeUpdate();
			state.close();
            money = newMoney;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    // Set admin
    public void setAdmin(Boolean newAdmin) {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
					.prepareStatement("UPDATE players SET admin = ? WHERE uuid = ?");
			state.setBoolean(1, newAdmin);
			state.setString(2, uuid.toString());
			state.executeUpdate();
			state.close();
            admin = newAdmin;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
