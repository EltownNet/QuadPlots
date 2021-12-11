package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MiddleCommand extends PlotCommand {

    public MiddleCommand(final QuadPlots plugin) {
        super(plugin, "middle", "Teleportiere dich in die Mitte eines Plots.", "/p middle", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPositionExact(player.getLocation().toVector());
            if (plot != null) {
                player.teleport(this.getPlugin().getLocationAPI().getMiddle(plot.getX(), plot.getZ()).toLocation(player.getWorld()));
                player.sendMessage(Language.get("plot.middle"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }
}
