package net.eltown.quadplots.components.listener;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public record BlockListener(QuadPlots plugin) implements Listener {

    @EventHandler
    public void on(final BlockBreakEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        final Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getBlock().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void on(final BlockPlaceEvent event) {
        if (this.plugin.getApi().isManager(event.getPlayer().getName())) return;

        final Plot plot = this.plugin.getLocationAPI().getPlotByPosition(event.getBlock().getLocation().toVector());

        if (plot != null) {
            if (!plot.canBuild(event.getPlayer().getName())) event.setCancelled(true);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void on(final BlockFromToEvent event) {
        // Liquids
        if (event.getBlock().isLiquid()) {
            final Plot to = this.plugin.getLocationAPI().getPlotByPosition(event.getToBlock().getLocation().toVector());
            if (to == null) event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(final BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(final EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(final TNTPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(final ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

}
