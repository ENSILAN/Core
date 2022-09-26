package me.nathanfallet.ensilan.core.interfaces;

import java.util.List;

import org.bukkit.entity.Player;

import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public interface ScoreboardGenerator {

    List<String> generateLines(Player player, EnsilanPlayer ep);
    
}
