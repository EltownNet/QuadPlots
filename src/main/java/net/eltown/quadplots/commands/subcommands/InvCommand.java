package net.eltown.quadplots.commands.subcommands;

import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.api.ItemAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InvCommand extends PlotCommand {

    private static String[] saved;

    public InvCommand(final QuadPlots plugin) {
        super(plugin, "inv", "test", "test", List.of());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "save":
                        saved = ItemAPI.playerInventoryToBase64(player.getInventory());
                        player.sendMessage("Inventar gesichert.");
                    break;
                    case "load":
                        player.getInventory().setContents(ItemAPI.itemStackArrayFromBase64(saved[0]));
                        player.getInventory().setArmorContents(ItemAPI.itemStackArrayFromBase64(saved[1]));
                    break;
                }
            }
        }
    }
}
