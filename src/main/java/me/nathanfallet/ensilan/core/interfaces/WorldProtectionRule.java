package me.nathanfallet.ensilan.core.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public interface WorldProtectionRule {

    // If an area is protected
    boolean isProtected(Location location);

    // If a player is allowed to intecract in a protected area
    boolean isAllowedInProtectedLocation(Player player, EnsilanPlayer ep, Location location, Event event);
    
}
