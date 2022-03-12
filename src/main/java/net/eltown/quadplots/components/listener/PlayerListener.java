package net.eltown.quadplots.components.listener;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public record PlayerListener(QuadPlots plugin) implements Listener {

    @EventHandler
    public void on(final PlayerMoveEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        final Player player = event.getPlayer();

        final Plot from = this.plugin.getLocationAPI().getPlotByPosition(event.getFrom().toVector());
        final Plot to = this.plugin.getLocationAPI().getPlotByPosition(event.getTo().toVector());

        if (to != null) {
            //if (player.getWalkSpeed() > 1) player.setWalkSpeed(0.1f);
            if (from == null) {
                if (to.isClaimed()) {
                    player.sendActionBar(Component.text("§9" + to.getName() + "§r von " + String.join(", ", to.getOwners())));
                } else {
                    player.sendActionBar(Component.text("§fDieses Plot ist noch frei! §9/p claim"));
                }
            }

            if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;
            if (to.getBanned().contains(player.getName())) player.teleport(event.getFrom());
            if (from != null && from.getBanned().contains(player.getName()))
                player.teleport(player.getWorld().getSpawnLocation());
        } else if (!player.getAllowFlight() && player.isSneaking() && from == null) {
            //player.setWalkSpeed(2f);
        } else {
            //if (!player.isSneaking() && player.getWalkSpeed() > 1) event.getPlayer().setWalkSpeed(0.1f);
        }
    }

    @EventHandler
    public void on(final PlayerItemFrameChangeEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        final Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getItemFrame().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void on(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getEntity().getLocation().toVector());

            if (event.getEntity() instanceof Player && player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                return;
            }

            if (plot != null) {
                if (!plot.canBuild(player.getName())) event.setCancelled(true);
            } else event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(final HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player player) {
            Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getEntity().getLocation().toVector());

            if (plot != null) {
                if (!plot.canBuild(player.getName())) event.setCancelled(true);
            } else event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(final PlayerArmorStandManipulateEvent event) {
        Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getRightClicked().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void on(final PlayerInteractEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR) return;

        Plot plot = null;
        if (event.getClickedBlock() != null)
            plot = this.plugin.getLocationAPI().getPlotByPosition(event.getClickedBlock().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);
    }

}
