package me.nathanfallet.ensilan.core.models;

import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.events.EnsilanPlayerLoadedEvent;

public class EnsilanPlayer {

    // Properties
    private UUID uuid;
    private PlayerScoreboard scoreboard;
    private long login;

    // Cached current data
    private Long money;
    private Long score;
    private Long victories;
    private Boolean admin;

    // Authentication
    private boolean authenticated;
    private String password;

    // Bukkit constructor
    public EnsilanPlayer(final Player player) {
        // Set properties
        this.uuid = player.getUniqueId();
        this.scoreboard = new PlayerScoreboard("ENSILAN");
        this.login = System.currentTimeMillis();
        this.authenticated = !Core.getInstance().isAuthenticationEnabled();

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
                password = result.getString("password");

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
                password = "";

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

    // Login time
    public long getLogin() {
        return login;
    }

    // Cached money
    public Long getCachedMoney() {
        return money;
    }

    // Cached score
    public Long getCachedScore() {
        return score;
    }

    // Cached victories
    public Long getCachedVictories() {
        return victories;
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

    // Get score
    public Long getScore() {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
				.prepareStatement("SELECT score FROM players WHERE uuid = ?");
			state.setString(1, uuid.toString());
			ResultSet result = state.executeQuery();
			if (result.next()) {
				score = result.getLong("score");
			}
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return score;
	}

    // Get victories
    public Long getVictories() {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
				.prepareStatement("SELECT victories FROM players WHERE uuid = ?");
			state.setString(1, uuid.toString());
			ResultSet result = state.executeQuery();
			if (result.next()) {
				victories = result.getLong("victories");
			}
			result.close();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return victories;
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

    // Set score
    public void setScore(Long newScore) {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
					.prepareStatement("UPDATE players SET score = ? WHERE uuid = ?");
			state.setLong(1, newScore);
			state.setString(2, uuid.toString());
			state.executeUpdate();
			state.close();
            score = newScore;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    // Set victories
    public void setVictories(Long newVictories) {
		try {
			PreparedStatement state = Core.getInstance().getConnection()
					.prepareStatement("UPDATE players SET victories = ? WHERE uuid = ?");
			state.setLong(1, newVictories);
			state.setString(2, uuid.toString());
			state.executeUpdate();
			state.close();
            victories = newVictories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    // Is player authenticated
    public boolean isAuthenticated() {
        return authenticated;
    }

    // Mask as authenticated
    public void authenticate() {
        this.authenticated = true;
    }

    // If the player has a password
    public boolean hasPassword() {
        return !password.isEmpty();
    }

    // Check password
    public boolean checkPassword(String withPassword) {
        return password.equals(hashPassword(withPassword));
    }

    // Set password
    public void setPassword(String newPassword) {
		try {
            newPassword = hashPassword(newPassword);
			PreparedStatement state = Core.getInstance().getConnection()
					.prepareStatement("UPDATE players SET password = ? WHERE uuid = ?");
			state.setString(1, newPassword);
			state.setString(2, uuid.toString());
			state.executeUpdate();
			state.close();
            password = newPassword;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    // Utility to hash a password
    private String hashPassword(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] digest = md5.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
