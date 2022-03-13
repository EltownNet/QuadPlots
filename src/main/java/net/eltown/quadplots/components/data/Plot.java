package net.eltown.quadplots.components.data;

import lombok.Getter;
import lombok.Setter;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.math.Direction;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.*;

@Setter
@Getter
public class Plot {

    private final int x, z;
    private boolean claimed;
    private Set<String> owners = new HashSet<>();
    private Set<String> trusted = new HashSet<>(), helpers = new HashSet<>(), flags = new HashSet<>(), banned = new HashSet<>();

    public Plot(int x, int z, boolean claimed) {
        this.x = x;
        this.z = z;
        this.claimed = claimed;
    }

    public Plot(int x, int z, boolean claimed, List<String> owners, List<String> trusted, List<String> helpers, List<String> flags, List<String> banned) {
        this.claimed = claimed;
        this.x = x;
        this.z = z;
        this.owners.addAll(owners);
        this.trusted.addAll(trusted);
        this.helpers.addAll(helpers);
        this.flags.addAll(flags);
        this.banned.addAll(banned);
    }

    public void setOwners(final String... owners) {
        this.getOwners().clear();
        this.getOwners().addAll(List.of(owners));
    }

    public void setFlags(final String... flags) {
        this.getFlags().clear();
        this.getFlags().addAll(Arrays.asList(flags));
    }


    public boolean canBuild(final String player) {
        if (this.isOwner(player)) return true;
        else if (this.trusted.contains("*")) return true;
        else if (this.trusted.contains(player)) return true;
        else {
            if (this.helpers.contains(player)) {
                for (final String owner : this.getOwners()) {
                    if (this.getOwners().contains(owner)) return true;
                }
            }
        }
        return false;
    }

    public boolean hasMergeInDirection(final Direction direction) {
        return this.flags.contains("merge;" + direction.name().toLowerCase());
    }

    public boolean isMerged() {
        return this.flags.stream().anyMatch(s -> s.startsWith("origin"));
    }

    // Untested
    public Set<Plot> getMergedPlots() {
        final Set<Plot> plots = new HashSet<>();

        this.owners.forEach(owner -> {
            QuadPlots.getInstance().getApi().getPlots(owner).forEach((plot) -> {
                if (plot.isMerged() && this.is(plot.getOrigin())) plots.add(plot);
            });
        });

        return plots;
    }

    // Untested
    public Set<Direction> getMergedDirections() {
        final Set<Direction> directions = new HashSet<>();

        if (hasMergeInDirection(Direction.NORTH)) directions.add(Direction.NORTH);
        if (hasMergeInDirection(Direction.EAST)) directions.add(Direction.EAST);
        if (hasMergeInDirection(Direction.SOUTH)) directions.add(Direction.SOUTH);
        if (hasMergeInDirection(Direction.WEST)) directions.add(Direction.WEST);

        return directions;
    }

    public boolean isOrigin() {
        final int[] origin = this.getOriginXZ();
        return origin[0] == this.getX() && origin[1] == this.getZ();
    }

    public void claim(final String owner) {
        this.owners = new HashSet<>(Collections.singletonList(owner));
        this.claimed = true;

        QuadPlots.getInstance().getApi().updatePlot(this);
    }

    public void unclaim() {
        QuadPlots.getInstance().getApi().unclaimPlot(this);
    }

    public void unmerge() {
        this.removeFlag("origin");
        this.removeFlag("merge");

        QuadPlots.getInstance().getApi().updatePlot(this);
    }


    public Plot getOrigin() {
        if (isOrigin()) return this;
        for (final String str : this.flags) {
            if (str.startsWith("origin")) {
                final String[] split = str.split(";");
                final String[] xzStrings = split[1].split(":");

                return QuadPlots.getInstance().getApi().getPlotExact(Integer.parseInt(xzStrings[0]), Integer.parseInt(xzStrings[1]));
            }
        }

        return null;
    }

    public int[] getOriginXZ() {
        for (final String str : this.flags) {
            if (str.startsWith("origin")) {
                final String[] split = str.split(";");
                final String[] xzStrings = split[1].split(":");

                return new int[]{Integer.parseInt(xzStrings[0]), Integer.parseInt(xzStrings[1])};
            }
        }

        return null;
    }

    public Vector getPosition() {
        for (final String str : this.flags) {
            if (str.startsWith("home;")) {
                final String[] split = str.split(";");
                final double x = Double.parseDouble(split[1]), y = Double.parseDouble(split[2]), z = Double.parseDouble(split[3]);
                return new Vector(x, y, z);
            }
        }

        return QuadPlots.getInstance().getLocationAPI().getPositionByXZ(this.x, Plot.Settings.getHeight() + 2, this.z);
    }

    public void setName(final String name) {
        this.removeFlag("name");
        this.addFlag("name;" + name);
    }

    public void setDescription(final String name) {
        this.removeFlag("description");
        this.addFlag("description;" + name);
    }

    public String getName() {
        for (final String flag : flags) {
            if (flag.startsWith("name;")) {
                return flag.split(";")[1];
            }
        }

        return "Unbenannt";
    }

    public String getDescription() {
        for (final String flag : flags) {
            if (flag.startsWith("description;")) {
                return flag.split(";")[1];
            }
        }

        return "Keine Beschreibung angegeben.";
    }

    public void addFlag(final String str) {
        this.flags.add(str);
    }

    public void addFlag(final Flags flag) {
        this.addFlag(flag.name());
    }

    public void removeFlag(final String str) {
        this.flags.removeIf(flag -> flag.startsWith(str) || flag.equalsIgnoreCase(str));
    }

    public void removeFlag(final Flags flag) {
        this.removeFlag(flag.name());
    }

    public boolean hasFlag(final String key) {
        return this.flags.stream().anyMatch(s -> s.startsWith(key));
    }

    public boolean hasFlag(final Flags flag) {
        return this.hasFlag(flag.name());
    }

    public boolean isOwner(final String player) {
        return this.owners.contains(player);
    }

    public String getStringId() {
        return x + ";" + z;
    }

    public boolean is(final Plot compare) {
        return compare.getX() == this.getX() && compare.getZ() == this.getZ();
    }

    public Plot getSide(final Direction side) {
        return switch (side) {
            case NORTH -> QuadPlots.getInstance().getApi().getPlotExact(this.x, this.z - 1);
            case EAST -> QuadPlots.getInstance().getApi().getPlotExact(this.x + 1, this.z);
            case SOUTH -> QuadPlots.getInstance().getApi().getPlotExact(this.x, this.z + 1);
            case WEST -> QuadPlots.getInstance().getApi().getPlotExact(this.x - 1, this.z);
            default -> null;
        };
    }

    public void update() {
        QuadPlots.getInstance().getApi().updatePlot(this);
    }

    public void addMerge(final Direction direction) {
        this.addFlag("merge;" + direction.name().toLowerCase());
        QuadPlots.getInstance().getApi().updatePlot(this);
    }

    public void setOrigin(final Plot plot) {
        this.addFlag("origin;" + plot.getX() + ":" + plot.getZ());
        QuadPlots.getInstance().getApi().updatePlot(this);
    }

    public static class Settings {

        @Getter
        private static final int
                height = 64,
                plotSize = 52,
                roadWidth = 4,
                totalSize = plotSize + roadWidth;

        @Getter
        private static final Material
                fill = Material.STONE,
                road = Material.QUARTZ_BLOCK,
                ground = Material.GRASS_BLOCK,
                border = Material.SMOOTH_STONE_SLAB,
                claimed = Material.SANDSTONE_SLAB;

    }

}
