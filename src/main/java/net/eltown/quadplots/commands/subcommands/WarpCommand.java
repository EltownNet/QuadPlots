package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;

public class WarpCommand extends PlotCommand {

    public WarpCommand(QuadPlots plugin) {
        super(plugin, "warp", "Teleportiere dich zu einem Plot.", "/p warp <x> <z>", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 1) {
                try {
                    final int x = Integer.parseInt(args[0]), z = Integer.parseInt(args[1]);
                    final Vector v = this.getPlugin().getLocationAPI().getPositionByXZ(x, z);
                    final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(v);
                    final boolean generated = player.getWorld().getChunkAt(v.toLocation(player.getWorld())).isLoaded();
                    if (generated) {
                        player.teleport(plot.getPosition().toLocation(player.getWorld()));
                        player.sendMessage(Language.get("teleported"));
                    } else {
                        player.sendMessage(Language.get("plot.not.generated"));
                    }
                } catch (Exception ex) {
                    player.sendMessage(Language.get("invalid.number"));
                }
            } else player.sendMessage(Language.get("usage", this.getUsage()));
        }
    }
}