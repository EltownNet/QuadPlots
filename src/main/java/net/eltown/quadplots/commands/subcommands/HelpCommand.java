package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.forms.modal.ModalWindow;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class HelpCommand extends PlotCommand {

    public HelpCommand(QuadPlots plugin) {
        super(plugin, "help", "wurst", "wurst", Collections.emptyList(), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final SimpleWindow.Builder form = new SimpleWindow.Builder("§bPlot Commands", "Hier findest du alle Befehle die du für dein Grundstück brauchst.");
            this.getPlugin().getCommandHandler().getCommands().forEach((e) -> {
                if (!e.isHidden()) {
                    form.addButton("/p " + e.getName() + "\n§7" + e.getDescription(), (p) -> {
                        new ModalWindow.Builder("/p " + e.getName(), e.getDescription() + "\n\nVerwendung: " + e.getUsage(), "§l§c«", "§cSchließen")
                                .onYes((pp) -> execute(sender, args))
                                .onNo((pp) -> {})
                                .build().send(p);
                    });
                }
            });
            form.build().send(player);
        }
    }

}
