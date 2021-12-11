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

public class TrustCommand extends PlotCommand {

    public TrustCommand(final QuadPlots plugin) {
        super(plugin, "trust", "Erlaube einem Spieler, auf deinem Plot abzubauen, auch wenn du Offline bist.", "/p trust <spieler>", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());

                if (plot != null) {
                    if (this.getPlugin().getApi().isManager(player.getName()) || plot.getOwners().contains(player.getName())) {
                        String target = args[0];

                        final Player preTarget = Bukkit.getPlayer(target);
                        if (preTarget != null) target = preTarget.getName();

                        plot.getTrusted().add(target);
                        plot.update();
                        player.sendMessage(Language.get("plot.trusted", target));
                        if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Trusted " + target + " on Plot " + plot.getX() + "|" + plot.getZ(), false);
                    } else player.sendMessage(Language.get("no.plot.permission"));
                } else player.sendMessage(Language.get("not.in.a.plot"));
            } else player.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}

