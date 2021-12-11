package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class ManageCommand extends PlotCommand {

    public ManageCommand(final QuadPlots plugin) {
        super(plugin, "manage", "Wechsle den Plot-Manager modus.", "/p manage", List.of("manager"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            if (this.getPlugin().getApi().toggleManage(sender.getName())) {
                sender.sendMessage(Language.get("plot.manage.on"));
                if (this.getPlugin().getApi().isManager(sender.getName())) PluginsCommand.broadcastCommandMessage(sender, "Switched to Plot-Manage mode", false);
            } else sender.sendMessage(Language.get("plot.manage.off"));
        }
    }
}
