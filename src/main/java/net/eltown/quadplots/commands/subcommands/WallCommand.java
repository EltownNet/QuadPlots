package net.eltown.quadplots.commands.subcommands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.modal.ModalWindow;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.tasks.ChangeWallTask;
import net.eltown.servercore.ServerCore;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

public class WallCommand extends PlotCommand {

    private static final LinkedHashSet<Wall> walls = new LinkedHashSet<>();

    public WallCommand(final QuadPlots plugin) {
        super(plugin, "wand", "Ändere die Wand deines Plots.", "/p wand", Collections.singletonList("wall"), false);
    }

    static {
        walls.addAll(
                Arrays.asList(
                        new Wall(Material.QUARTZ_BLOCK, 0.99),
                        new Wall(Material.STONE, 99.95),
                        new Wall(Material.DIRT, 29.95)
                )
        );
    }

    private boolean hasBlock(final Player player, final Material block) {
        return player.hasPermission("plot.wall." + block.getKey().getNamespace());
    }

    private String getImage(final Material block) {
        return "http://45.138.50.23:3000/img/ui/plot/wall/" + block.getKey().getNamespace() + ".png";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());

            if (plot != null) {
                if (plot.isMerged()) {
                    sender.sendMessage(Language.get("plot.merge.command"));
                    return;
                }
                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    final SimpleWindow.Builder builder = new SimpleWindow.Builder("Plot-Wand", "Hier kannst du die Wände deines Plots ändern.");

                    walls.forEach(wall -> {

                        final Material block = wall.block;
                        final double price = wall.price;
                        final ItemStack itemStack = new ItemStack(block);

                        if (this.hasBlock(player, block)) {
                            builder.addButton(itemStack.getI18NDisplayName() + "\n§2Im Besitz", this.getImage(block), (p) -> {
                                new ModalWindow.Builder("Plot-Wand Ändern", "Möchtest du die Wand deines Plots zu §9" + itemStack.getI18NDisplayName() + "§r ändern?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                                                new ChangeWallTask(plot, block, player.getWorld()).execute();
                                                player.sendMessage(Language.get("wall.change", block.name()));
                                            });
                                        })
                                        .onNo((p1) -> {
                                            builder.build().send(player);
                                        })
                                        .build().send(player);
                            });
                        } else {
                            builder.addButton(itemStack.getI18NDisplayName() + "\n§0Kaufen für §a$" + ServerCore.getServerCore().getMoneyFormat().format(price), this.getImage(block), (p) -> {
                                new ModalWindow.Builder("Plotwand kaufen", "Möchtest du die Plotwand §9" + itemStack.getI18NDisplayName() + " §rfür §a$" + ServerCore.getServerCore().getMoneyFormat().format(price) + "§r kaufen?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            ServerCore.getServerCore().getEconomyAPI().getMoney(player.getName(), (money) -> {
                                                if (!(price > money)) {
                                                    ServerCore.getServerCore().getGroupAPI().addPlayerPermission(player.getName(), "plot.wall." + block.name());
                                                    ServerCore.getServerCore().getEconomyAPI().reduceMoney(player.getName(), price);
                                                    player.sendMessage(Language.get("wall.bought"));
                                                } else player.sendMessage(Language.get("not.enough.money"));
                                            });
                                        })
                                        .onNo((p1) -> {
                                            builder.build().send(player);
                                        })
                                        .build().send(player);
                            });
                        }
                    });

                    builder.build().send(player);
                } else player.sendMessage(Language.get("no.plot.permission"));
            } else player.sendMessage(Language.get("not.in.a.plot"));


        }
    }

    private record Wall(Material block, double price) {
    }

}
