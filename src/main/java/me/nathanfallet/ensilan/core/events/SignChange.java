package me.nathanfallet.ensilan.core.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.AbstractGame;

public class SignChange implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).startsWith("[") && e.getLine(0).endsWith("]")) {
			if (e.getPlayer().hasPermission("ensilan.gamesign")) {
				try {
					String gameName = e.getLine(0);
					int gameNumber = Integer.parseInt(e.getLine(1));
					AbstractGame g = null;
					for (AbstractGame g2 : Core.getInstance().getGames()) {
						if (
							gameName.equals("[" + g2.getGameName() + "]") &&
							g2.getGameNumber() == gameNumber
						) {
							g = g2;
						}
					}
					if (g == null) {
						throw new NumberFormatException();
					}
					g.getSigns().add(e.getBlock().getLocation());
				} catch (NumberFormatException ex) {
					e.setCancelled(true);
					e.getPlayer()
							.sendMessage(ChatColor.RED + "Entrez un nom et un numéro de partie valide !");
				}
			} else {
				e.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas la permission de faire ça !");
			}
		}
	}

}
