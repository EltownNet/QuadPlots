package net.eltown.quadplots.components.api;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public record API(QuadPlots plugin, Provider provider) {

    private static final Set<String> managers = new HashSet<>();

    public LinkedList<Plot> getPlots(final String player) {
        return this.provider.getPlots(player);
    }

    public LinkedList<Plot> getPlotsFiltered(final String player) {
        return this.provider.getPlotsFiltered(player);
    }

    public void updatePlot(final Plot plot) {
        this.provider.updatePlot(plot);
    }

    public void unclaimPlot(final Plot plot) {
        this.provider.unclaimPlot(plot);
    }

    public int getPlotAmount(final String player) {
        return this.provider.getPlotAmount(player);
    }

    public Plot getPlot(final int x, final int z) {
        return this.provider.getPlot(x, z, true);
    }

    public Plot getPlotExact(final int x, final int z) {
        return this.provider.getPlot(x, z, false);
    }

    public int getMaxPlots(final Player player) {
        if (player.isOp()) return Integer.MAX_VALUE;
        final AtomicInteger plots = new AtomicInteger(2);

        player.getEffectivePermissions().forEach((perm) -> {
            if (perm.getPermission().startsWith("plots.claim.")) {
                String max = perm.getPermission().replace("plots.claim.", "");
                if (max.equalsIgnoreCase("unlimited")) {
                    plots.set(Integer.MAX_VALUE);
                } else {
                    try {
                        final int num = Integer.parseInt(max);
                        if (num > plots.get()) plots.set(num);
                    } catch (NumberFormatException ex) {}
                }
            }
        });

        return plots.get();
    }

    public boolean toggleManage(final String player) {
        if (managers.contains(player)) {
            managers.remove(player);
            return false;
        } else {
            managers.add(player);
            return true;
        }
    }

    public boolean isManager(final String player) {
        return managers.contains(player);
    }

}
