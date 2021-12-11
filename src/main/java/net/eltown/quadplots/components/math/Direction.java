package net.eltown.quadplots.components.math;

import org.bukkit.entity.Player;

public enum Direction {

    NORTH,
    EAST,
    SOUTH,
    WEST,
    ALL;

    public static Direction getPlayerDirection(final Player player) {
        return switch (player.getFacing()) {
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

}
