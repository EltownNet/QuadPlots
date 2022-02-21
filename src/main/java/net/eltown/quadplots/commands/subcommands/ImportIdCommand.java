package net.eltown.quadplots.commands.subcommands;

import lombok.SneakyThrows;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportIdCommand extends PlotCommand {

    public ImportIdCommand(final QuadPlots plugin) {
        super(plugin, "importid", "ImportId", "/importid <old_namespace> <new_namespace>", List.of(), true);
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isOp() && sender instanceof Player player) {
            if (args.length > 1) {
                String old = args[0];
                String newId = args[1];

                QuadPlots.translationKeys.put(old, newId);
                player.sendMessage("Added " + old + "->" + newId);

                List<String> toSave = new ArrayList<>();
                for (Map.Entry<String, String> keys : QuadPlots.translationKeys.entrySet()) {
                    toSave.add(keys.getKey() + "->" + keys.getValue());
                }
                QuadPlots.translationConfig.set("translationKeys", String.join("[T]", toSave));
                QuadPlots.translationConfig.save(QuadPlots.translationFile);
            }
        }
    }
}
