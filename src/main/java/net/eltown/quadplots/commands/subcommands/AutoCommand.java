package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.tasks.ChangeBorderTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class AutoCommand extends PlotCommand {

    public AutoCommand(final QuadPlots plugin) {
        super(plugin, "auto", "Finde automatisch ein freies Grundstück.", "/p auto", Collections.singletonList("a"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CompletableFuture.runAsync(() -> {
            if (sender instanceof Player player) {
                final int maxPlots = this.getPlugin().getApi().getMaxPlots(player), currentPlots = this.getPlugin().getApi().getPlotAmount(player.getName());

                if (currentPlots >= maxPlots) {
                    player.sendMessage(Language.get("plots.max"));
                    return;
                }

                player.sendMessage(Language.get("plot.searching"));

                final Plot plot = this.getPlugin().getLocationAPI().findFreePlot(0, 0);
                player.sendMessage(Language.get("plot.claim"));
                plot.claim(player.getName());
                player.teleport(this.getPlugin().getLocationAPI().getPositionByXZ(plot.getX(), Plot.Settings.getHeight() + 2, plot.getZ()).toLocation(player.getWorld()));
                new ChangeBorderTask(plot, Plot.Settings.getClaimed(), player.getWorld(), true).execute();

            }
        });
    }
}
