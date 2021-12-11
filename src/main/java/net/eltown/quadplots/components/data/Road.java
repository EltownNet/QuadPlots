package net.eltown.quadplots.components.data;

import net.eltown.quadplots.components.math.Direction;

public record Road(Plot plot, Direction requiredMerge) {

    public boolean isMerged() {
        return plot.hasMergeInDirection(requiredMerge);
    }

}
