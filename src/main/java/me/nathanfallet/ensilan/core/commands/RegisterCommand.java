package me.nathanfallet.ensilan.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class RegisterCommand implements CommandExecutor {

    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if sender is a player
        if (sender instanceof Player) {
            // Cast player
            final Player player = (Player) sender;
            EnsilanPlayer ep = Core.getInstance().getPlayer(player.getUniqueId());

            if (args.length == 2) {
                // Check authentication status
                if (ep.isAuthenticated()) {
                    player.sendMessage(ChatColor.RED + "Vous êtes déjà connecté !");
                    return true;
                }
                if (ep.hasPassword()) {
                    player.sendMessage(ChatColor.RED + "Vous avez déjà un compte !\nUtilisez " + ChatColor.DARK_RED + "/login <password>");
                    return true;
                }

                // Check and save password
                if (args[0].equals(args[1])) {
                    ep.setPassword(args[0]);
                    ep.authenticate();
                    player.sendMessage(ChatColor.GREEN + "Authentification réussie !");
                } else {
                    player.sendMessage(ChatColor.RED + "Les deux mots de passe ne correspondent pas !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage : " + ChatColor.DARK_RED + "/register <password> <password>");
            }
        }
        return true;
    }
    
}
