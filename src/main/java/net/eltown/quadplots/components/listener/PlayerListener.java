package net.eltown.quadplots.components.listener;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.data.Road;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.CompletableFuture;

public record PlayerListener(QuadPlots plugin) implements Listener {

    @EventHandler
    public void on(final PlayerMoveEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        final Player player = event.getPlayer();

        final Plot from = this.plugin.getLocationAPI().getPlotByPosition(event.getFrom().toVector());
        final Plot to = this.plugin.getLocationAPI().getPlotByPosition(event.getTo().toVector());

        if (to != null) {
            if (player.getWalkSpeed() > 1) player.setWalkSpeed(0.1f);
            if (from == null) {
                if (to.isClaimed()) {
                    player.sendActionBar(
                            to.getName() + "§r - " + to.getX() + "|" + to.getZ() + "\n" +
                                    "Von: " + String.join(", ", to.getOwners())
                    );
                } else player.sendActionBar(to.getX() + "|" + to.getZ() + "\n§a/p claim");
            }

            if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;
            if (to.getBanned().contains(player.getName())) player.teleport(event.getFrom());
            if (from != null && from.getBanned().contains(player.getName()))
                player.teleport(player.getWorld().getSpawnLocation());
        } else if (!player.getAllowFlight() && player.isSneaking() && from == null) {
            player.setWalkSpeed(2f);
        } else {
            if (!player.isSneaking() && player.getWalkSpeed() > 1) event.getPlayer().setWalkSpeed(0.1f);
        }
    }

    // Todo
    @EventHandler
    public void on(final PlayerInteractEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR) return;

        Plot plot = null;
        if (event.getClickedBlock() != null) plot = this.plugin.getLocationAPI().getPlotByPosition(event.getClickedBlock().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);

        /*
        *         if (QuadPlots.getApi().isManager(event.getPlayer().getName())) return;
        if ((event.getItem() instanceof ItemEdible || event.getItem() instanceof ItemPotion) && event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
            return;
        if (QuadPlots.getApi().getProvider().getGeneratorInfo().getLevel().equalsIgnoreCase(event.getPlayer().getLevel().getName())) {
            final Plot plot = QuadPlots.getApi().getPlotByPosition(event.getBlock() != null ? event.getBlock().getLocation() : event.getPlayer().getPosition());
            if (plot != null) {
                if (!plot.canBuild(event.getPlayer().getName())) this.cancel(event.getPlayer(), event);
            }  else {
                final Road road = QuadPlots.getApi().getRoad(event.getBlock().getLocation());
                if (road != null && road.isMerged()) {
                    if (!road.getPlot().canBuild(event.getPlayer().getName())) this.cancel(event.getPlayer(), event);
                } else this.cancel(event.getPlayer(), event);
            }
        }*/
    }

}
