package me.nathanfallet.ensilan.core.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.interfaces.WorldProtectionRule;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class WorldProtection implements Listener {

    // Player events

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        Location location = event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : event.getPlayer().getLocation();
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(location) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, location, event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getRightClicked().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getRightClicked().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getRightClicked().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getRightClicked().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // World protection rules
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getEntity(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShear(PlayerShearEntityEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getEntity().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getEntity().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        Location location = event.getCaught() != null ? event.getCaught().getLocation() : event.getPlayer().getLocation();
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(location) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, location, event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getBed().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getBed().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getBlock().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getBlock().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getItem().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getItem().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // World protection rules
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getItemDrop().getLocation()) &&
                !rule.isAllowedInProtectedLocation(event.getPlayer(), ep, event.getItemDrop().getLocation(), event)
            ) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                return;
            }
        }
    }

    // Entity events

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // World protection rules
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getEntity(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        // World protection rules
        if (event.getDamager() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getDamager().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getDamager(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    event.getDamager().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // World protection rules
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getEntity(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // World protection rules
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getEntity(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    event.getEntity().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        // World protection rules
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
                if (
                    rule.isProtected(event.getEntity().getLocation()) &&
                    !rule.isAllowedInProtectedLocation((Player) event.getEntity(), ep, event.getEntity().getLocation(), event)
                ) {
                    event.setCancelled(true);
                    event.getEntity().sendMessage(ChatColor.RED + "Vous n'avez pas le droit de faire ça ici !");
                    return;
                }
            }
        }
    }

    // World event

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        // World protection rules
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getEntity().getLocation())
            ) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    @EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		// World protection rules
        for (WorldProtectionRule rule : Core.getInstance().getWorldProtectionRules()) {
            if (
                rule.isProtected(event.getBlock().getLocation())
            ) {
                event.setCancelled(true);
                return;
            }
        }
	}
    
}
