package me.nathanfallet.ensilan.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class EnsilanPlayerLoadedEvent extends PlayerEvent {

    // Bukkit requirement
    private static final HandlerList handlers = new HandlerList();

    // Bukkit requirement
    public static HandlerList getHandlerList() {
        return handlers;
    }

    // Properties
    private EnsilanPlayer ep;

    // Constructor
    public EnsilanPlayerLoadedEvent(Player player, EnsilanPlayer ep) {
        // Init event
        super(player);

        // Store data
        this.ep = ep;
    }

    // Bukkit requirement
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    // Get PopolPlayer
    public EnsilanPlayer getPopolPlayer() {
        return ep;
    }

}
