package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.custom.CustomWindow;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsCommand extends PlotCommand {

    public SettingsCommand(final QuadPlots plugin) {
        super(plugin, "settings", "Verwalte die Einstellungen deines Plots.", "/p settings", List.of("einstellungen"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());

            if (plot != null) {
                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {

                    final SimpleWindow.Builder settingsSelector = new SimpleWindow.Builder("§7» §8Plot Einstellungen", "Hier kannst du die Einstellungen deines Plots verwalten.")
                            .addButton("§8» §fInformationen", (p) -> {
                                final CustomWindow customWindow = new CustomWindow("§7» §8Informationen");

                                customWindow.form()
                                        .label("Hier kannst du die Informationen deines Plots anpassen.")
                                        .input("§8» §fName", plot.getName(), plot.getName())
                                        .input("§8» §fBeschreibung", plot.getDescription(), plot.getDescription());

                                customWindow.onSubmit((rPlayer, response) -> {
                                    plot.setName(response.getInput(1));
                                    plot.setDescription(response.getInput(2));
                                    rPlayer.sendMessage(Language.get("information.updated"));
                                    plot.update();
                                    if (this.getPlugin().getApi().isManager(player.getName())) PluginsCommand.broadcastCommandMessage(player, "Changed info of Plot " + plot.getX() + "|" + plot.getZ(), false);
                                });
                                customWindow.send(player);
                            })
                            .addButton("§8» §fEinstellungen\n§e(Bald)", (p) -> {
                                // TODO
                            });

                    settingsSelector.build().send(player);

                } else sender.sendMessage(Language.get("no.plot.permission"));
            } else sender.sendMessage(Language.get("not.in.a.plot"));

        }
    }
}
