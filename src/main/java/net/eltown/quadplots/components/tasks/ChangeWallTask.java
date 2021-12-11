package net.eltown.quadplots.components.tasks;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public class ChangeWallTask {

    private final World level;
    private final int height;
    private final Material block;
    private final Vector plotBeginPos;
    private final int xMax, zMax;

    public ChangeWallTask(Plot plot, Material block, World level) {
        this.level = level;
        this.plotBeginPos = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(plot.getX(), plot.getZ());
        this.xMax = plotBeginPos.getBlockX() + Plot.Settings.getPlotSize() - 1;
        this.zMax = plotBeginPos.getBlockZ() + Plot.Settings.getPlotSize() - 1;
        this.height = Plot.Settings.getHeight();
        this.block = block;
    }

    public void execute() {
            for (int x = plotBeginPos.getBlockX(); x <= xMax; x++) {
                for (int y = level.getMinHeight() + 1; y <= height; y++) {
                    level.getBlockAt(x, y, plotBeginPos.getBlockZ()).setType(block);
                    level.getBlockAt(x, y, zMax).setType(block);
                }
            }
            for (int z = plotBeginPos.getBlockZ(); z <= zMax; z++) {
                for (int y = level.getMinHeight() + 1; y <= height; y++) {
                    level.getBlockAt(plotBeginPos.getBlockX(), y, z).setType(block);
                    level.getBlockAt(xMax, y, z).setType(block);
                }
            }
    }
}
