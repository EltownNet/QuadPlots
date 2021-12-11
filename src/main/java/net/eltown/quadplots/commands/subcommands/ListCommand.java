package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;

public class ListCommand extends PlotCommand {

    public ListCommand(final QuadPlots plugin) {
        super(plugin, "list", "Zeige dir eine Liste mit deinen Plots an.", "/p list <optional: spieler>", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String target = args[0];

                final Player preTarget = Bukkit.getPlayer(target);
                if (preTarget != null) target = preTarget.getName();

                final LinkedList<Plot> plots = this.getPlugin().getApi().getPlots(target);
                if (plots.size() == 0) {
                    sender.sendMessage(Language.get("has.no.plot"));
                    return;
                }

                final SimpleWindow.Builder builder = new SimpleWindow.Builder("§8» §f" + target + "'s Plots", " ");

                plots.forEach((plot) -> {
                    builder.addButton("§8» §8[§7" + plot.getX() + "|" + plot.getZ() + "§8] §f" + plot.getName() + "\n§7" + plot.getDescription(), (p) -> {
                        p.teleport(plot.getPosition().toLocation(player.getWorld()));
                    });
                });

                builder.build().send(player);
            } else {
                final LinkedList<Plot> plots = this.getPlugin().getApi().getPlots(sender.getName());

                final SimpleWindow.Builder builder = new SimpleWindow.Builder("§8» §fDeine Plots", " ");

                plots.forEach((plot) -> {
                    builder.addButton("§8» §8[§7" + plot.getX() + "|" + plot.getZ() + "§8] §f" + plot.getName() + "\n§7" + plot.getDescription(), (p) -> {
                        p.teleport(plot.getPosition().toLocation(player.getWorld()));
                    });
                });

                builder.build().send(player);
            }
        }
    }
}
