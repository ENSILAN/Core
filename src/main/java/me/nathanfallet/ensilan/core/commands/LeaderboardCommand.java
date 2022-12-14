package me.nathanfallet.ensilan.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.interfaces.LeaderboardGenerator;
import me.nathanfallet.ensilan.core.models.Leaderboard;

public class LeaderboardCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Check args
		if (args.length != 0) {
			// Create command
			if (args[0].equalsIgnoreCase("create") && sender instanceof Player) {
				// Check args are specified
				if (args.length >= 3) {
					// Check if id already exists
					if (!Core.getInstance().getLeaderboards().containsKey(args[1])) {
						// Create it
						Leaderboard leaderboard = new Leaderboard(((Player) sender).getLocation(), args[2], 10);
						Core.getInstance().getLeaderboards().put(args[1], leaderboard);
						sender.sendMessage(ChatColor.GREEN + "Le classement " + ChatColor.YELLOW + args[1]
								+ ChatColor.GREEN + " a bien été créé !");

						// Update entities
						leaderboard.update();
					} else {
						// Already exists
						sender.sendMessage(ChatColor.RED + "Le classement " + ChatColor.DARK_RED + args[1]
								+ ChatColor.RED + " existe déjà !");
					}
				} else {
					// Send help
					sender.sendMessage(ChatColor.RED + "/leaderboard create <nom> [type]");
				}
			}

			// List command
			else if (args[0].equalsIgnoreCase("list")) {
				// Send header
				sender.sendMessage(ChatColor.YELLOW + "------ " + ChatColor.GOLD + "Classements existants "
						+ ChatColor.YELLOW + "------");

				// Iterate leaderboards
				for (String key : Core.getInstance().getLeaderboards().keySet()) {
					// Retrieve leaderboard and send info
					Leaderboard leaderboard = Core.getInstance().getLeaderboards().get(key);
					sender.sendMessage(ChatColor.GOLD + key + ChatColor.YELLOW + " : " + leaderboard.getType());
				}
			}

			// Info command
			else if (args[0].equalsIgnoreCase("info")) {
				// Check args
				if (args.length == 2) {
					// Get leaderboard
					Leaderboard leaderboard = Core.getInstance().getLeaderboards().get(args[1]);

					// Check if it exists
					if (leaderboard != null) {
						// Print information
						sender.sendMessage(ChatColor.GOLD + args[1] + " : " + ChatColor.YELLOW + leaderboard.getType());
					} else {
						// Doesnt' exist
						sender.sendMessage(ChatColor.RED + "Le classement " + ChatColor.DARK_RED + args[1]
								+ ChatColor.RED + " n'existe pas !");
					}
				} else {
					// Send help
					sender.sendMessage(ChatColor.RED + "/leaderboard info <nom>");
				}
			}

			// Remove command
			else if (args[0].equalsIgnoreCase("remove")) {
				// Check args
				if (args.length == 2) {
					// Get leaderboard
					Leaderboard leaderboard = Core.getInstance().getLeaderboards().get(args[1]);

					// Check if it exists
					if (leaderboard != null) {
						// Kill leaderboard first
						leaderboard.kill();

						// Delete it
						Core.getInstance().getLeaderboards().remove(args[1]);
						sender.sendMessage(ChatColor.GREEN + "Le classement " + ChatColor.YELLOW + args[1]
								+ ChatColor.GREEN + " a bien été supprimé !");
					} else {
						// Doesnt' exist
						sender.sendMessage(ChatColor.RED + "Le classement " + ChatColor.DARK_RED + args[1]
								+ ChatColor.RED + " n'existe pas !");
					}
				} else {
					// Send help
					sender.sendMessage(ChatColor.RED + "/leaderboard remove <nom>");
				}
			}

			// List types command
			else if (args[0].equalsIgnoreCase("types")) {
				// Send header
				sender.sendMessage(ChatColor.YELLOW + "------ " + ChatColor.GOLD + "Type de classements disponibles "
						+ ChatColor.YELLOW + "------");

				// Iterate types
				for (String type : Core.getInstance().getLeaderboardGenerators().keySet()) {
					// Retrieve type and send info
					LeaderboardGenerator generator = Core.getInstance().getLeaderboardGenerators().get(type);
					sender.sendMessage(ChatColor.GOLD + type + ChatColor.YELLOW + " : " + generator.getTitle());
				}
			}

			// Set type command
			else if (args[0].equalsIgnoreCase("settype")) {
				// Check args
				if (args.length == 3) {
					// Get leaderboard
					Leaderboard leaderboard = Core.getInstance().getLeaderboards().get(args[1]);

					// Check if it exists
					if (leaderboard != null) {
						// Set type
						leaderboard.setType(args[2]);
					} else {
						// Doesnt' exist
						sender.sendMessage(ChatColor.RED + "Le classement " + ChatColor.DARK_RED + args[1]
								+ ChatColor.RED + " n'existe pas !");
					}
				} else {
					// Send help
					sender.sendMessage(ChatColor.RED + "/leaderboard settype <nom> <type>");
				}
			}

			// Set limit command
			else if (args[0].equalsIgnoreCase("setlimit")) {
				// Check args
				if (args.length == 3) {
					// Get leaderboard
					Leaderboard leaderboard = Core.getInstance().getLeaderboards().get(args[1]);

					// Check if it exists
					if (leaderboard != null) {
						// Set limit
						try {
							// Try to parse limit
							int limit = Integer.parseInt(args[2]);
							leaderboard.setLimit(limit);
							sender.sendMessage(ChatColor.GREEN + "Le classement " + ChatColor.YELLOW + args[1]
									+ ChatColor.GREEN + " a maintenant " + limit + " lignes !");
						} catch (NumberFormatException e) {
							// Error parsing limit
							sender.sendMessage(ChatColor.RED + "Nombre de lignes invalide !");
						}
					} else {
						// Doesnt' exist
						sender.sendMessage(ChatColor.RED + "Le classement " + ChatColor.DARK_RED + args[1]
								+ ChatColor.RED + " n'existe pas !");
					}
				} else {
					// Send help
					sender.sendMessage(ChatColor.RED + "/leaderboard setlimit <nom> <limit>");
				}
			}

			// Invalid sub command, send help
			else {
				sendHelp(sender);
			}
		}

		// No command specified, send help
		else {
			sendHelp(sender);
		}
		return true;
	}

	public void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "Aide du /leaderboard " + ChatColor.YELLOW
				+ "-----\n" + ChatColor.GOLD + "/leaderboard create <nom> [type] " + ChatColor.YELLOW
				+ ": Créer un classement.\n" + ChatColor.GOLD + "/leaderboard list " + ChatColor.YELLOW
				+ ": Afficher les classements existants.\n" + ChatColor.GOLD + "/leaderboard info <nom> "
				+ ChatColor.YELLOW + ": Afficher les informations sur un classement.\n" + ChatColor.GOLD
				+ "/leaderboard remove <nom> " + ChatColor.YELLOW + ": Supprimer un classement.\n" + ChatColor.GOLD
				+ "/leaderboard types " + ChatColor.YELLOW + ": Afficher les types de classements disponibles.\n"
				+ ChatColor.GOLD + "/leaderboard settype <nom> <type> " + ChatColor.YELLOW
				+ ": Changer le type d'un classement.\n" + ChatColor.GOLD + "/leaderboard setlimit <nom> <top> "
				+ ChatColor.YELLOW + ": Changer le nombre de lignes d'un classement.");
	}

}
