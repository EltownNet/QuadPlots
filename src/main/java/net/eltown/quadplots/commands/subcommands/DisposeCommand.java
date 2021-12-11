package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DisposeCommand extends PlotCommand {

    public DisposeCommand(final QuadPlots plugin) {
        super(plugin, "dispose", "Gebe dein Plot zum claimen frei.", "/p dispose", Collections.singletonList("unclaim"));
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
                        player.sendMessage(Language.get("plot.dispose.really"));
                        return;
                    }
                } else {
                    player.sendMessage(Language.get("plot.dispose.really"));
                    return;
                }

                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    player.sendMessage(Language.get("plot.disposing"));
                    plot.unclaim();
                    if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Disposed Plot " + plot.getX() + "|" + plot.getZ(), false);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }
}