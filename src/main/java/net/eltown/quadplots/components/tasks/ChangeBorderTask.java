package net.eltown.quadplots.components.tasks;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.math.Direction;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ChangeBorderTask {

    private final World level;
    private final int height;
    private final Material block;
    private final Vector plotBeginPos;
    private final int xMax, zMax;
    private final Direction[] directions;
    private final boolean edges;

    public ChangeBorderTask(Plot plot, Material block, World level, boolean edges, Direction... directions) {
        this.level = level;
        this.plotBeginPos = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(plot.getX(), plot.getZ());
        this.xMax = plotBeginPos.getBlockX() + Plot.Settings.getPlotSize() - 1;
        this.zMax = plotBeginPos.getBlockZ() + Plot.Settings.getPlotSize() - 1;
        this.height = Plot.Settings.getHeight();
        this.block = block;
        this.edges = edges;
        if (directions.length == 0) {
            this.directions = new Direction[]{Direction.ALL};
        } else this.directions = directions;
    }

    public void execute() {
            for (int x = plotBeginPos.getBlockX() + (edges ? 0 : 1); x <= xMax - (edges ? 0 : 1); x++) {
                // NORTH
                if (contains(Direction.NORTH)) level.getBlockAt(x, height + 1, plotBeginPos.getBlockZ()).setType(block);
                // SOUTH
                if (contains(Direction.SOUTH)) level.getBlockAt(x, height + 1, zMax).setType(block);
            }
            for (int z = plotBeginPos.getBlockZ() + (edges ? 0 : 1); z <= zMax - (edges ? 0 : 1); z++) {
                // WEST
                if (contains(Direction.WEST)) level.getBlockAt(plotBeginPos.getBlockX(), height + 1, z).setType(block);
                // EAST
                if (contains(Direction.EAST)) level.getBlockAt(xMax, height + 1, z).setType(block);
            }
    }

    private boolean contains(final Direction direction) {
        return Arrays.stream(directions).anyMatch(i -> i == direction || i == Direction.ALL);
    }

}
