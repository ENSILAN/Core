package me.nathanfallet.ensilan.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;
import me.nathanfallet.ensilan.core.models.Money;

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (sender instanceof Player) {
            // Cast player
            final Player player = (Player) sender;
            EnsilanPlayer ep = Core.getInstance().getPlayer(player.getUniqueId());

            // Send update message
            player.sendMessage(ChatColor.YELLOW + "Vérification de votre compte...");

            // Check result
            if (ep.getMoney() != null) {
                // Show balance
                player.sendMessage(ChatColor.GREEN + Money.name + " : " + ep.getCachedMoney() + Money.symbol);
            } else {
                // Error
                player.sendMessage(ChatColor.RED + "Impossible de vérifier votre compte !");
            }
        }
        return true;
    }

}
