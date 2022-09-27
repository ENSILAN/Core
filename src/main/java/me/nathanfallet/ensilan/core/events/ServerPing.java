package me.nathanfallet.ensilan.core.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.nathanfallet.ensilan.core.Core;

public class ServerPing implements Listener {

	private String getMinecraftVersion() {
		String version = Bukkit.getVersion();
		int start = version.indexOf("MC: ") + 4;
		int end = version.length() - 1;
		return version.substring(start, end);
	}

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd(
            ChatColor.GREEN + Core.getInstance().getConfig().getString("server.name") +
            ChatColor.GRAY + " | " +
            ChatColor.AQUA + Core.getInstance().getConfig().getString("server.ip") +
            ChatColor.GRAY + " | " +
            ChatColor.RED + getMinecraftVersion() + "\n" +
            ""
        );
    }
    
}
