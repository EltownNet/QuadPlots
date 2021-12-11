package net.eltown.quadplots.components.tasks;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public class MergePlotTask {

    private final Plot p1, p2;
    private final boolean northMerge;
    private final World level;

    // NORTH -> SOUTH
    // EAST -> WEST
    public MergePlotTask(final World world, final Plot p1, final Plot p2, boolean northMerge) {
        this.level = world;
        this.p1 = p1;
        this.p2 = p2;
        this.northMerge = northMerge;
    }

    public void execute() {
        // NORTH -> SOUTH
        if (northMerge) {
            //final Vector northPos = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(p1.getX(), p1.getZ());
            final Vector southPos = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(p2.getX(), p2.getZ()).add(new Vector(0, 0, Plot.Settings.getPlotSize() - 1));// this.zMax = plotBeginPos.z + gen.getPlotSize() - 1;

            // Border
            for (int t = 0; t < Plot.Settings.getRoadWidth() + 1; t++) {
                level.getBlockAt(southPos.getBlockX(), southPos.getBlockY() + 1, southPos.getBlockZ() + t).setType(Plot.Settings.getClaimed());
                level.getBlockAt(southPos.getBlockX() + Plot.Settings.getPlotSize() - 1, southPos.getBlockY() + 1, southPos.getBlockZ() + t).setType(Plot.Settings.getClaimed());
            }

            for (int x = southPos.getBlockX() + 1; x < southPos.getBlockX() + Plot.Settings.getPlotSize() - 1; x++) {
                for (int z = 0; z < Plot.Settings.getRoadWidth() + 2; z++) {
                    level.getBlockAt(x, Plot.Settings.getHeight(), southPos.getBlockZ() + z).setType(Plot.Settings.getGround());
                    for (int y = Plot.Settings.getHeight() - 1; y > level.getMinHeight(); y--) {
                        level.getBlockAt(x, y, southPos.getBlockZ() + z).setType(Plot.Settings.getFill());
                    }
                }
            }
        } else {
            final Vector westPos = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(p2.getX(), p2.getZ());

            // Border
            for (int t = 0; t < Plot.Settings.getRoadWidth() + 1; t++) {
                level.getBlockAt(westPos.getBlockX() - t, westPos.getBlockY() + 1, westPos.getBlockZ()).setType(Plot.Settings.getClaimed());
                level.getBlockAt(westPos.getBlockX() - t, westPos.getBlockY() + 1, westPos.getBlockZ() + Plot.Settings.getPlotSize() - 1).setType(Plot.Settings.getClaimed());
            }

            for (int z = westPos.getBlockZ() + 1; z < westPos.getBlockZ() + Plot.Settings.getPlotSize() - 1; z++) {
                for (int x = 0; x < Plot.Settings.getRoadWidth() + 2; x++) {
                    level.getBlockAt(westPos.getBlockX() - x, Plot.Settings.getHeight(), z).setType(Plot.Settings.getGround());
                    for (int y = Plot.Settings.getHeight() - 1; y > level.getMinHeight(); y--) {
                        level.getBlockAt(westPos.getBlockX() - x, y, z).setType(Plot.Settings.getFill());
                    }
                }
            }
        }
    }

}
