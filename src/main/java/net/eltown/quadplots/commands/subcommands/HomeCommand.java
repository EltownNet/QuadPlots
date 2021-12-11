package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;

public class HomeCommand extends PlotCommand {

    public HomeCommand(final QuadPlots plugin) {
        super(plugin, "home", "Teleportiere dich zu deinem oder das Plot von einem anderen.", "/p h <nummer:spieler> <nummer>", Collections.singletonList("h"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (args.length > 1) {
                    try {
                        String target = args[0];
                        final int id = Integer.parseInt(args[1]);

                        if (id <= 0) {
                            sender.sendMessage(Language.get("invalid.number"));
                            return;
                        }

                        final Player targetPlayer = Bukkit.getPlayer(target);
                        if (targetPlayer != null) target = targetPlayer.getName();

                        final LinkedList<Plot> plots = new LinkedList<>(this.getPlugin().getApi().getPlotsFiltered(target));

                        if (plots.size() != 0) {
                            if (plots.size() >= id) {
                                player.teleport(plots.get(id - 1).getPosition().toLocation(player.getWorld()));
                                player.sendMessage(Language.get("teleported"));
                            } else sender.sendMessage(Language.get("has.no.plot.with.id", id));
                        } else sender.sendMessage(Language.get("has.no.plot"));
                    } catch (final Exception ex) {
                        sender.sendMessage(Language.get("invalid.number"));
                    }
                } else {
                    try {
                        final int id = Integer.parseInt(args[0]);
                        if (id <= 0) {
                            sender.sendMessage(Language.get("invalid.number"));
                            return;
                        }
                        final LinkedList<Plot> plots = this.getPlugin().getApi().getPlotsFiltered(player.getName());
                        if (plots.size() >= id) {
                            player.teleport(plots.get(id - 1).getPosition().toLocation(player.getWorld()));
                            player.sendMessage(Language.get("teleported"));
                        } else sender.sendMessage(Language.get("no.plot.with.id", id));
                    } catch (final Exception ex) {
                        String target = args[0];

                        final Player targetPlayer = Bukkit.getPlayer(target);
                        if (targetPlayer != null) target = targetPlayer.getName();

                        final LinkedList<Plot> plots = this.getPlugin().getApi().getPlotsFiltered(target);

                        if (plots.size() != 0) {
                            player.teleport(plots.get(0).getPosition().toLocation(player.getWorld()));
                            player.sendMessage(Language.get("teleported"));
                        } else sender.sendMessage(Language.get("has.no.plot"));
                    }
                }
            } else {
                final LinkedList<Plot> plots = this.getPlugin().getApi().getPlotsFiltered(player.getName());
                if (plots.size() != 0) {
                    player.teleport(plots.get(0).getPosition().toLocation(player.getWorld()));
                    player.sendMessage(Language.get("teleported"));
                } else sender.sendMessage(Language.get("no.plot.with.id", 1));
            }
        }
    }

}
