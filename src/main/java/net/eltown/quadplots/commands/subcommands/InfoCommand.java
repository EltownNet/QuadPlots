package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends PlotCommand {

    public InfoCommand(final QuadPlots plugin) {
        super(plugin, "info", "Zeige die Informationen eines Plots an.", "/p info", List.of("i"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
            player.sendMessage(plot != null ?
                    Language.getNP("info.command",
                            plot.getName(), plot.getX(), plot.getZ(),
                            plot.getDescription(),
                            plot.getOwners().size(), String.join(", ", plot.getOwners()),
                            plot.getTrusted().size(), String.join(", ", plot.getTrusted()),
                            plot.getHelpers().size(), String.join(", ", plot.getHelpers())
                    ) : Language.get("not.in.a.plot"));
        }
    }
}
