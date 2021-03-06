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

public class KickCommand extends PlotCommand {

    public KickCommand(final QuadPlots plugin) {
        super(plugin, "kick", "Kicke einen Spieler von deinem Plot.", "/p kick <spieler>", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                final Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(Language.get("player.not.found"));
                    return;
                }

                final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
                if (plot != null) {
                    if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {

                        final Plot prePlot = this.getPlugin().getLocationAPI().getPlotByPosition(target.getLocation().toVector());
                        if (prePlot != null && prePlot.getStringId().equals(plot.getStringId())) {
                            target.teleport(target.getWorld().getSpawnLocation());
                            player.sendMessage(Language.get("plot.kick", target.getName()));
                            if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Kicked " + target + " from Plot " + plot.getX() + "|" + plot.getZ(), false);
                        } else player.sendMessage(Language.get("player.cant.kick"));

                    } else player.sendMessage(Language.get("no.plot.permission"));
                } else player.sendMessage(Language.get("not.in.a.plot"));

            } else player.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}
