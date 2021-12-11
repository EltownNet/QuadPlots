package net.eltown.quadplots.commands;

import lombok.Getter;
import net.eltown.quadplots.QuadPlots;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public class PlotCommand {

    private final QuadPlots plugin;
    private final String name, description, usage;
    private final List<String> aliases;
    private final boolean hidden;

    public PlotCommand(QuadPlots plugin, String name, String description, String usage, List<String> aliases) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.hidden = false;
    }

    public PlotCommand(QuadPlots plugin, String name, String description, String usage, List<String> aliases, boolean hidden) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.hidden = hidden;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§cDer Command " + this.name + " ist noch nicht implementiert.");
    }

}
