package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class BanCommand extends PlotCommand {

    public BanCommand(final QuadPlots plugin) {
        super(plugin, "ban", "Verbanne Spieler von deinem Plot.", "/p ban <spieler>", Collections.singletonList("deny"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String target = args[0];

                final Player preTarget = Bukkit.getPlayer(target);
                if (preTarget != null) target = preTarget.getName();

                final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
                if (plot != null) {
                    if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                        plot.getBanned().add(target);
                        plot.update();

                        if (preTarget != null) {
                            final Plot prePlot = this.getPlugin().getLocationAPI().getPlotByPosition(preTarget.getLocation().toVector());
                            if (prePlot != null && prePlot.getStringId().equals(plot.getStringId())) {
                                preTarget.teleport(preTarget.getWorld().getSpawnLocation());
                            }
                        }

                        player.sendMessage(Language.get("plot.ban", target));
                        if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Banned " + target + " from Plot " + plot.getX() + "|" + plot.getZ(), false);
                    } else player.sendMessage(Language.get("no.plot.permission"));
                } else player.sendMessage(Language.get("not.in.a.plot"));

            } else player.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}