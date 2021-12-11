package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.custom.CustomWindow;
import net.eltown.quadplots.components.forms.modal.ModalWindow;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.tasks.ChangeBorderTask;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class ClaimCommand extends PlotCommand {

    public ClaimCommand(final QuadPlots plugin) {
        super(plugin, "claim", "Beanspruche ein freies Grundstück für dich.", "/p claim", List.of());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof final Player player) {
            final int maxPlots = this.getPlugin().getApi().getMaxPlots(player), currentPlots = this.getPlugin().getApi().getPlotAmount(player.getName());

            if (currentPlots >= maxPlots) {
                player.sendMessage(Language.get("plots.max"));
                return;
            }

            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPositionExact(player.getLocation().toVector());
            if (plot != null) {
                if (plot.isClaimed()) {
                    player.sendMessage(Language.get("plot.already.claimed", String.join(", ", plot.getOwners())));
                } else {
                    player.sendMessage(Language.get("plot.claim"));
                    plot.claim(player.getName());
                    final Vector plotPos = this.getPlugin().getLocationAPI().getPositionByXZ(plot.getX(), plot.getZ());

                    player.teleport(new Location(player.getWorld(), plotPos.getX(), plotPos.getY() + 2, plotPos.getZ()));
                    new ChangeBorderTask(plot, Plot.Settings.getClaimed(), player.getWorld(), true).execute();
                }
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }
}
