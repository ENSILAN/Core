package me.nathanfallet.ensilan.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;

public class PlayerAuthentication implements Listener {

    // Player events

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        String cmd = event.getMessage().split(" ")[0].toLowerCase();
        if (!ep.isAuthenticated() && !(cmd.equals("/login") || cmd.equals("/register"))) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerShear(PlayerShearEntityEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        // Authentication
        EnsilanPlayer ep = Core.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (!ep.isAuthenticated()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Authentication
        if (event.getWhoClicked() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getWhoClicked().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    // Entity events

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        // Authentication
        if (event.getDamager() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getDamager().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        // Authentication
        if (event.getTarget() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getTarget().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        // Authentication
        if (event.getEntity() instanceof Player) {
            EnsilanPlayer ep = Core.getInstance().getPlayer(event.getEntity().getUniqueId());
            if (!ep.isAuthenticated()) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
}
