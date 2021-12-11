package net.eltown.quadplots.commands;

import net.eltown.quadplots.QuadPlots;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RootCommand extends Command {

    private final QuadPlots plugin;

    public RootCommand(final QuadPlots plugin) {
        super("plot");
        this.setAliases(List.of("p"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        this.plugin.getCommandHandler().handle(commandSender, args);
        return true;
    }


}
