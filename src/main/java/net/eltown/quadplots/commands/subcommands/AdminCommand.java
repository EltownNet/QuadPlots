package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.data.Road;
import net.eltown.quadplots.components.forms.custom.CustomWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class AdminCommand extends PlotCommand {

    public AdminCommand(final QuadPlots plugin) {
        super(plugin, "admin", "Adminpanel", "/p admin", Collections.emptyList(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player && sender.isOp()) {
            Plot plot = this.getPlugin().getLocationAPI().getPlotByPositionExact(player.getLocation().toVector());

            if (plot != null) {
                final CustomWindow form = new CustomWindow("Plot - Adminpanel");

                form.form()
                        .input("Plot Besitzer", String.join(",", plot.getOwners()), String.join(",", plot.getOwners()))
                        .label("\n§l§8[§c!§8]§r§c Achtung: Eine fehlerhafte Änderung der Flags kann die Funktion eines Plots stören.")
                        .input("Flags", String.join(",", plot.getFlags()), String.join(",", plot.getFlags()));

                form.onSubmit((p, f) -> {
                    plot.setOwners(f.getInput(0).split(","));
                    plot.setFlags(f.getInput(2).split(","));
                    sender.sendMessage("Plot aktualisiert.");

                    plot.update();
                });

                form.send(player);
            } else sender.sendMessage("§cBitte stelle dich auf ein Plot.");
        }
    }
}
