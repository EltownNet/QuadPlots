package net.eltown.quadplots.components.tasks;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ClearTask {

    final World level;
    final Vector start;
    final int xMax, zMax, startZ;

    public ClearTask(final Plot plot, final World level) {
        Vector position = QuadPlots.getInstance().getLocationAPI().getPositionByXZ(plot.getX(), plot.getZ());
        this.start = new Vector(position.getX() + 1, 0, position.getZ() + 1);
        this.xMax = (int) (position.getX() + Plot.Settings.getPlotSize() - 2);
        this.zMax = (int) (position.getZ() + Plot.Settings.getPlotSize() - 2);
        this.startZ = (int) this.start.getZ();
        this.level = level;
    }

    public void execute() {
            try {
                Material block;

                while (this.start.getX() < this.xMax) {
                    while (this.start.getZ() < this.zMax) {
                        while (this.start.getY() < this.level.getMaxHeight()) {
                            if (this.start.getY() == Plot.Settings.getHeight()) {
                                block = Plot.Settings.getGround();
                            } else if (this.start.getY() < Plot.Settings.getHeight()) {
                                block = this.start.getY() != level.getMinHeight() ? Plot.Settings.getFill() : Material.BEDROCK;
                            } else block = Material.AIR;
                            level.getBlockAt(this.start.getBlockX(), this.start.getBlockY(), this.start.getBlockZ()).setType(block);
                            //this.level.setBlock(this.start, block);
                            this.start.add(new Vector(0, 1, 0));
                        }
                        this.start.add(new Vector(0, 0, 1));
                        this.start.setY(0);
                    }
                    this.start.setZ(startZ);
                    this.start.add(new Vector(1, 0, 0));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
}
