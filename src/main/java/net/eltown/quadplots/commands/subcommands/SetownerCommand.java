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

public class SetownerCommand extends PlotCommand {

    public SetownerCommand(final QuadPlots plugin) {
        super(plugin, "setowner", "Übertrage dein Plot an §leinen §rSpieler.", "/p setowner <spieler>", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
                if (plot != null) {
                    if (this.getPlugin().getApi().isManager(player.getName()) || plot.getOwners().contains(player.getName())) {
                        final Player target = Bukkit.getPlayer(args[0]);

                        if (target == null) {
                            player.sendMessage(Language.get("plot.setowner.online"));
                            return;
                        }

                        final int maxPlots = this.getPlugin().getApi().getMaxPlots(target);
                        final int currentPlots = this.getPlugin().getApi().getPlotAmount(target.getName());

                        if (maxPlots <= currentPlots) {
                            player.sendMessage(Language.get("plot.setowner.max"));
                            return;
                        }

                        if (args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
                            plot.getOwners().clear();
                            plot.getOwners().add(target.getName());
                            plot.update();
                            player.sendMessage(Language.get("plot.setowner", target.getName()));
                            target.sendMessage(Language.get("plot.setowner.ping", player.getName(), plot.getName(), plot.getX(), plot.getZ()));
                            if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Changed owner of " + plot.getX() + "|" + plot.getZ() + " to " + target, false);
                        } else sender.sendMessage(Language.get("plot.setowner.really", target.getName()));


                    } else sender.sendMessage(Language.get("no.plot.permission"));
                } else sender.sendMessage(Language.get("not.in.a.plot"));
            } else sender.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}
