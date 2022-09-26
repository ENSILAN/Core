package me.nathanfallet.ensilan.core.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.nathanfallet.ensilan.core.Core;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Check is spawn location is from bed
        if (!event.isBedSpawn()) {
            // If not, set spawn
            event.setRespawnLocation(Core.getInstance().getSpawn());
        }
    }

}
