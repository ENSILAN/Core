package me.nathanfallet.ensilan.core.events;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.AbstractGame;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPLayerInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null) {
			if (e.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)) {
				for (AbstractGame g : Core.getInstance().getGames()) {
					if (g.getSigns() != null) {
						for (Location l : g.getSigns()) {
							if (e.getClickedBlock().getLocation().equals(l)) {
								for (AbstractGame c : Core.getInstance().getGames()) {
									for (UUID uuid : c.getAllPlayers()) {
										if (e.getPlayer().getUniqueId().equals(uuid)) {
											e.getPlayer().sendMessage("§cVous êtes déjà dans une partie !");
											return;
										}
									}
								}
								g.preJoin(
									e.getPlayer(),
									Core.getInstance().getPlayer(e.getPlayer().getUniqueId())
								);
							}
						}
					}
				}
			}
		}
	}

}
