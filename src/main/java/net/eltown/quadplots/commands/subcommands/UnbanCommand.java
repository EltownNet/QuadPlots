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

public class UnbanCommand extends PlotCommand {

    public UnbanCommand(final QuadPlots plugin) {
        super(plugin, "unban", "Verbanne Spieler von deinem Plot.", "/p ban <spieler>", Collections.singletonList("undeny"));
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
                        plot.getBanned().remove(target);
                        plot.update();

                        player.sendMessage(Language.get("plot.unban", target));
                        if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Unbanned " + target + " from Plot " + plot.getX() + "|" + plot.getZ(), false);
                    } else player.sendMessage(Language.get("no.plot.permission"));
                } else player.sendMessage(Language.get("not.in.a.plot"));

            } else player.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}
