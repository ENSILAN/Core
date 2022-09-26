package me.nathanfallet.ensilan.core.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Get player data
        EnsilanPlayer player = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());

        // Kill scoreboard
        player.getScoreboard().kill();

        // Remove player data
        Core.getInstance().getPlayers().remove(player);

        // Set quit message
        event.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.GOLD
                + event.getPlayer().getName());
    }

}
