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
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        throw new NotImplementedException("/p wall is not implemented.");
        /*if (sender instanceof Player player) {
            final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());

            if (plot != null) {
                if (plot.isMerged()) {
                    sender.sendMessage(Language.get("plot.merge.command"));
                    return;
                }
                if (this.getPlugin().getApi().isManager(player.getName()) || plot.isOwner(player.getName())) {
                    final SimpleWindow.Builder builder = new SimpleWindow.Builder("Plot-Wand", "Hier kannst du die Wände deines Plots ändern.");

                    walls.forEach(wall -> {

                        final Material block = wall.getBlock();
                        final double price = wall.getPrice();

                        if (this.hasBlock(player, block)) {
                            builder.addButton(block.name() + "\n§2Im Besitz", this.getImage(block), (p) -> {
                                new ModalWindow.Builder("Plot-Wand Ändern", "Möchtest du die Wand deines Plots zu §9" + block.name() + "§r ändern?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            new ChangeWallTask(plot, block, player.getWorld()).execute();
                                            player.sendMessage(Language.get("wall.change", block.name()));
                                        })
                                        .onNo((p1) -> {
                                            builder.build().send(player);
                                        })
                                        .build().send(player);
                            });
                        } else {
                            builder.addButton(block.name() + "\n§0Kaufen für §a$" + Economy.getAPI().getMoneyFormat().format(price), this.getImage(block), (p) -> {
                                new ModalWindow.Builder("Plotwand kaufen", "Möchtest du die Plotwand §9" + block.name() + " §rfür §a$" + Economy.getAPI().getMoneyFormat().format(price) + "§r kaufen?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            Economy.getAPI().getMoney(player, (money) -> {
                                                if (!(price > money)) {
                                                    ServerCoreAPI.getGroupAPI().addPlayerPermission(player.getName(), "plot.wall." + block.name());
                                                    Economy.getAPI().reduceMoney(player, price);
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
        return;*/
    }

    @Getter
    @AllArgsConstructor
    private static class Wall {

        private final Material block;
        private final double price;

    }

}
