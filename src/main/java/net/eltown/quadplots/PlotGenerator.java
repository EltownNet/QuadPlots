package net.eltown.quadplots;

import net.eltown.quadplots.components.data.Plot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PlotGenerator extends ChunkGenerator {

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                final int blockX = chunkX << 4 | x;
                final int blockZ = chunkZ << 4 | z;

                final double difX = blockX >= 0 ? blockX % Plot.Settings.getTotalSize() : Math.abs((blockX - Plot.Settings.getPlotSize() + 1) % Plot.Settings.getTotalSize());
                final double difZ = blockZ >= 0 ? blockZ % Plot.Settings.getTotalSize() : Math.abs((blockZ - Plot.Settings.getPlotSize() + 1) % Plot.Settings.getTotalSize());

                Material ground = Plot.Settings.getFill();

                if (difX > Plot.Settings.getPlotSize() - 1 || difZ > Plot.Settings.getPlotSize() - 1) {
                    chunk.setBlock(x, Plot.Settings.getHeight(), z, Plot.Settings.getRoad());
                    ground = Plot.Settings.getRoad();
                } else if (difX == Plot.Settings.getPlotSize() - 1 || difZ == Plot.Settings.getPlotSize() - 1 || difZ == 0 || difX == 0) {
                    chunk.setBlock(x, Plot.Settings.getHeight() + 1, z, Plot.Settings.getBorder());
                    chunk.setBlock(x, Plot.Settings.getHeight(), z, Plot.Settings.getRoad());
                    ground = Plot.Settings.getRoad();
                } else {
                    chunk.setBlock(x, Plot.Settings.getHeight(), z, Plot.Settings.getGround());
                }

                for (int y = chunk.getMinHeight() + 1; y < Plot.Settings.getHeight(); y++) chunk.setBlock(x, y, z, ground);
                chunk.setBlock(x, chunk.getMinHeight(), z, Material.BEDROCK);
            }
        }
    }

    @Override
    public @Nullable Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return world.getHighestBlockAt(0, 0).getLocation().add(0, 2, 0);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new PlotBiomes();
    }

    public static class PlotBiomes extends BiomeProvider {

        @Override
        public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int i, int i1, int i2) {
            return Biome.PLAINS;
        }

        @Override
        public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
            return List.of(Biome.PLAINS);
        }
    }


}
