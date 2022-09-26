package me.nathanfallet.ensilan.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.Core;

public class SetSpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Check sender
		if (sender instanceof Player) {
			// Update spawn location
			Player p = (Player) sender;
			p.sendMessage(ChatColor.GREEN + "Le spawn a bien été définit !");
			Core.getInstance().setSpawn(p.getLocation());
		}
		return true;
	}

}
