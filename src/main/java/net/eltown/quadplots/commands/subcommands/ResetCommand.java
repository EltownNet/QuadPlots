package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.tasks.ChangeBorderTask;
import net.eltown.quadplots.components.tasks.ChangeWallTask;
import net.eltown.quadplots.components.tasks.ClearTask;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ResetCommand extends PlotCommand {

    public ResetCommand(final QuadPlots plugin) {
        super(plugin, "reset", "Leere dein Plot und gebe es frei.", "/p reset", Collections.emptyList());
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
                        player.sendMessage(Language.get("plot.reset.really"));
                        return;
                    }
                } else {
                    player.sendMessage(Language.get("plot.reset.really"));
                    return;
                }

                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    player.sendMessage(Language.get("plot.resetting"));
                    new ClearTask(plot, player.getWorld()).execute();
                    new ChangeBorderTask(plot, Plot.Settings.getBorder(), player.getWorld(), true).execute();
                    new ChangeWallTask(plot, Plot.Settings.getRoad(), player.getWorld()).execute();
                    plot.unclaim();
                    if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Resetted Plot " + plot.getX() + "|" + plot.getZ(), false);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }

}
