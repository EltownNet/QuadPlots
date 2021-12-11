package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SethomeCommand extends PlotCommand {

    public SethomeCommand(QuadPlots plugin) {
        super(plugin, "sethome", "Setze das Home deines Plots.", "/p sethome", Collections.singletonList("setspawn"), false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());

            if (plot != null) {
                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    plot.removeFlag("home");
                    plot.addFlag("home;" + player.getLocation().getX() + ";" + player.getLocation().getY() + ";" + player.getLocation().getZ());
                    plot.update();
                    player.sendMessage(Language.get("home.set"));
                    if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Changed home of Plot " + plot.getX() + "|" + plot.getZ(), false);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }
}
