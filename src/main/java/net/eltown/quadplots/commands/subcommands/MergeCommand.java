package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.math.Direction;
import net.eltown.quadplots.components.tasks.ChangeBorderTask;
import net.eltown.quadplots.components.tasks.MergePlotTask;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MergeCommand extends PlotCommand {

    public MergeCommand(final QuadPlots plugin) {
        super(plugin, "merge", "Füge deine Plots zusammen.", "/p merge", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("plots.merge")) {
            if (sender instanceof Player player) {
                final Plot plot = QuadPlots.getInstance().getLocationAPI().getPlotByPositionExact(player.getLocation().toVector());

                if (plot != null) {
                    if (plot.isOwner(player.getName())) {
                        // ALPHA!
                        if (plot.isMerged()) sender.sendMessage(Language.get("plot.merge.alpha"));
                        final Direction direction = Direction.getPlayerDirection(player);

                        switch (direction) {
                            case NORTH -> {
                                final Plot toMergeN = plot.getSide(Direction.NORTH);
                                if (toMergeN.isOwner(player.getName())) {
                                    if (!plot.isMerged()) {
                                        // TODO: Origin für mehr als zwei Plots.
                                        plot.setOrigin(plot);
                                    }

                                    toMergeN.setOrigin(plot.isOrigin() ? plot : plot.getOrigin());
                                    plot.addMerge(Direction.NORTH);
                                    new ChangeBorderTask(plot, Material.AIR, player.getWorld(), false, Direction.NORTH).execute();
                                    toMergeN.addMerge(Direction.SOUTH);
                                    new ChangeBorderTask(toMergeN, Material.AIR, player.getWorld(), false, Direction.SOUTH).execute();
                                    new MergePlotTask(player.getWorld(), plot, toMergeN, true).execute();

                                    player.sendMessage(Language.get("plot.merging"));
                                } else {
                                    player.sendMessage(Language.get("plot.merge.sameowner"));
                                }
                            }
                            case EAST -> {
                                final Plot toMergeE = plot.getSide(Direction.EAST);
                                if (toMergeE.isOwner(player.getName())) {
                                    if (!plot.isMerged()) {
                                        plot.setOrigin(plot);
                                    }

                                    toMergeE.setOrigin(plot.isOrigin() ? plot : plot.getOrigin());
                                    plot.addMerge(Direction.EAST);
                                    new ChangeBorderTask(plot, Material.AIR, player.getWorld(), false, Direction.EAST).execute();
                                    toMergeE.addMerge(Direction.WEST);
                                    new ChangeBorderTask(toMergeE, Material.AIR, player.getWorld(), false, Direction.WEST).execute();
                                    new MergePlotTask(player.getWorld(), plot, toMergeE, false).execute();

                                    player.sendMessage(Language.get("plot.merging"));
                                } else {
                                    player.sendMessage(Language.get("plot.merge.sameowner"));
                                }
                            }
                            case SOUTH -> {
                                final Plot toMergeS = plot.getSide(Direction.SOUTH);
                                if (toMergeS.isOwner(player.getName())) {
                                    if (!plot.isMerged()) {
                                        plot.setOrigin(plot);
                                    }

                                    toMergeS.setOrigin(plot.isOrigin() ? plot : plot.getOrigin());
                                    plot.addMerge(Direction.SOUTH);
                                    new ChangeBorderTask(plot, Material.AIR, player.getWorld(), false, Direction.SOUTH).execute();
                                    toMergeS.addMerge(Direction.NORTH);
                                    new ChangeBorderTask(toMergeS, Material.AIR, player.getWorld(), false, Direction.NORTH).execute();
                                    new MergePlotTask(player.getWorld(), toMergeS, plot, true).execute();

                                    player.sendMessage(Language.get("plot.merging"));
                                } else {
                                    player.sendMessage(Language.get("plot.merge.sameowner"));
                                }
                            }
                            case WEST -> {
                                final Plot toMergeW = plot.getSide(Direction.WEST);
                                if (toMergeW.isOwner(player.getName())) {
                                    if (!plot.isMerged()) {
                                        plot.setOrigin(plot);
                                    }

                                    toMergeW.setOrigin(plot.isOrigin() ? plot : plot.getOrigin());

                                    plot.addMerge(Direction.WEST);
                                    new ChangeBorderTask(plot, Material.AIR, player.getWorld(), false, Direction.WEST).execute();
                                    toMergeW.addMerge(Direction.EAST);
                                    new ChangeBorderTask(toMergeW, Material.AIR, player.getWorld(), false, Direction.EAST).execute();
                                    new MergePlotTask(player.getWorld(), toMergeW, plot, false).execute();


                                    player.sendMessage(Language.get("plot.merging"));
                                } else {
                                    player.sendMessage(Language.get("plot.merge.sameowner"));
                                }
                            }
                        }

                    } else player.sendMessage(Language.get("no.permission"));
                } else player.sendMessage(Language.get("plot.merge.road"));
            }
        } else sender.sendMessage(Language.get("no.permission"));
    }
}
