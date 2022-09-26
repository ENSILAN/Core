package me.nathanfallet.ensilan.core.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Update player data
        Core.getInstance().getPlayers().add(new EnsilanPlayer(event.getPlayer()));

        // Set join message
        event.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GOLD
                + event.getPlayer().getName());

        // Send welcome message
        event.getPlayer().sendMessage(ChatColor.YELLOW + "Bienvenue sur le serveur de l'ENSILAN !");

        // Teleport to spawn if first join
        if (!event.getPlayer().hasPlayedBefore()) {
            event.getPlayer().teleport(Core.getInstance().getSpawn());
        }
    }

}
