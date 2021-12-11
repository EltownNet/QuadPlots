package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.custom.CustomWindow;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SetinfoCommand extends PlotCommand {

    public SetinfoCommand(final QuadPlots plugin) {
        super(plugin, "setinfo", "Ändere deine Plot Informationen.", "/p setinfo", Collections.emptyList());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
            if (plot != null) {
                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {

                    final CustomWindow form = new CustomWindow("Plot info");

                    form.form()
                            .label("Hier kannst du die Informationen deines Plots verändern.")
                            .input("Name", plot.getName(), plot.getName())
                            .input("Beschreibung", plot.getDescription(), plot.getDescription());

                    form.onSubmit((p, f) -> {
                        plot.setName(f.getInput(1));
                        plot.setDescription(f.getInput(2));
                        p.sendMessage(Language.get("information.updated"));
                        plot.update();
                        if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Changed info of Plot " + plot.getX() + "|" + plot.getZ(), false);
                    });

                    form.send(player);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));
        }

        return;
    }
}
