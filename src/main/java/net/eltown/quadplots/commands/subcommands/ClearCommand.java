package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.tasks.ClearTask;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ClearCommand extends PlotCommand {

    public ClearCommand(final QuadPlots plugin) {
        super(plugin, "clear", "Leere ein Plot.", "/p clear", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
            if (plot != null) {
                if (plot.isMerged()) {
                    sender.sendMessage(Language.get("plot.merge.command"));
                    return;
                }

                if (args.length > 0) {
                    if (!args[0].equalsIgnoreCase("confirm")) {
                        player.sendMessage(Language.get("plot.clear.really"));
                        return;
                    }
                } else {
                    player.sendMessage(Language.get("plot.clear.really"));
                    return;
                }

                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    player.sendMessage(Language.get("plot.clearing"));
                    new ClearTask(plot, player.getWorld()).execute();
                    if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Cleared Plot " + plot.getX() + "|" + plot.getZ(), false);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }

}