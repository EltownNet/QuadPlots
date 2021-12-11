package net.eltown.quadplots.components.api;

import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.data.Road;
import net.eltown.quadplots.components.math.Direction;
import org.bukkit.util.Vector;

public record LocationAPI(API api, Provider provider) {

    // External
    public Plot getPlotByPosition(final Vector position) {
        return this.getPlotByPositionInternal(position, true, false);
    }

    public Plot getPlotByPositionExact(final Vector position) {
        return this.getPlotByPositionInternal(position, false, false);
    }

    public Vector getPositionByXZ(final int x, final int y, final int z) {
        return new Vector(x * Plot.Settings.getTotalSize(), y, z * Plot.Settings.getTotalSize());
    }

    public Vector getPositionByXZ(final int x, final int z) {
        return new Vector(x * Plot.Settings.getTotalSize(), Plot.Settings.getHeight(), z * Plot.Settings.getTotalSize());
    }

    public Plot findFreePlot(int x, int z) {
        return this.provider.findFreePlot(x, z);
    }

    public Vector getMiddle(int x, int z) {
       return getPositionByXZ(x, 65, z).add(new Vector(Plot.Settings.getPlotSize() / 2, 0, Plot.Settings.getPlotSize() / 2));
    }

    // Internal
    private Plot getPlotByPositionInternal(final Vector position, final boolean getOrigin, final boolean ignoreRoad) {
        int x = position.getX() >= 0 ? (int) Math.floor(position.getX() / Plot.Settings.getTotalSize()) : (int) Math.ceil((position.getX() - Plot.Settings.getPlotSize() + 1) / Plot.Settings.getTotalSize());//(int) position.getX() / this.getProvider().getGeneratorInfo().getTotalSize();
        int z = position.getZ() >= 0 ? (int) Math.floor(position.getZ() / Plot.Settings.getTotalSize()) : (int) Math.ceil((position.getZ() - Plot.Settings.getPlotSize() + 1) / Plot.Settings.getTotalSize());//(int) position.getX() / this.getProvider().getGeneratorInfo().getTotalSize();

        final double difX = position.getX() >= 0 ? Math.floor(position.getX() % Plot.Settings.getTotalSize()) : Math.abs((position.getX() - Plot.Settings.getPlotSize() + 1) % Plot.Settings.getTotalSize());
        final double difZ = position.getZ() >= 0 ? Math.floor(position.getZ()) % Plot.Settings.getTotalSize() : Math.abs((position.getZ() - Plot.Settings.getPlotSize() + 1) % Plot.Settings.getTotalSize());

        Plot plot = null;

        if (difX >= Plot.Settings.getPlotSize() - 1 || difZ >= Plot.Settings.getPlotSize() - 1 || difX == 0 || difZ == 0) {
            if (!ignoreRoad) {
                final Road road = this.getRoadInternal(position);
                if (road != null) {
                    plot = road.isMerged() ? road.plot() : null;
                } // Todo: Cross
            }
        } else plot = this.api.getPlotExact(x, z);

        if (plot == null) return null;
        return getOrigin ? plot.isMerged() ? plot.getOrigin() : plot : plot;
    }

    private Road getRoadInternal(final Vector position) {
        for (int i = 0; i < 7; i++) {
            final Plot xPlot = this.getPlotByPositionInternal(new Vector(position.getX() + i, position.getY(), position.getZ()), false, true);
            final Plot zPlot = this.getPlotByPositionInternal(new Vector(position.getX(), position.getY(), position.getZ() + i), false, true);

            if (xPlot != null) return new Road(xPlot, Direction.WEST);
            if (zPlot != null) return new Road(zPlot, Direction.NORTH);
        }

        return null;
    }

}
