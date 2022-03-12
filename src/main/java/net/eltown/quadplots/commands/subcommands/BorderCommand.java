package net.eltown.quadplots.commands.subcommands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.eltown.quadplots.components.forms.modal.ModalWindow;
import net.eltown.quadplots.components.forms.simple.SimpleWindow;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.math.Direction;
import net.eltown.quadplots.components.tasks.ChangeBorderTask;
import net.eltown.servercore.ServerCore;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.ModalForm;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

public class BorderCommand extends PlotCommand {

    private static final LinkedHashSet<Border> border = new LinkedHashSet<>();

    public BorderCommand(final QuadPlots plugin) {
        super(plugin, "rand", "Ändere den Rand deines Plots.", "/p rand", Collections.singletonList("border"), false);
    }

    static {
        border.addAll(
                Arrays.asList(
                        new Border(Material.AIR, 299.95),
                        new Border(Plot.Settings.getBorder(), 0.99),
                        new Border(Plot.Settings.getClaimed(), 0.99),
                        new Border(Material.STONE_BRICK_SLAB, 129.95),
                        new Border(Material.OAK_SLAB, 99.95),
                        new Border(Material.SPRUCE_SLAB, 99.95),
                        new Border(Material.BIRCH_SLAB, 99.95),
                        new Border(Material.JUNGLE_SLAB, 99.95),
                        new Border(Material.ACACIA_SLAB, 99.95),
                        new Border(Material.DARK_OAK_SLAB, 99.95)
                )
        );
    }

    private boolean hasBlock(final Player player, final Material block) {
        return player.hasPermission("plot.border." + block.name());
    }

    private String getImage(final Material block) {
        return "http://eltown.net:3000/img/ui/plot/border/" + block.name() + ".png";
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
                    final SimpleWindow.Builder borderForm = new SimpleWindow.Builder("§7» §8Plot Rand", "Hier kannst du den Rand deines Plots ändern.");

                    border.forEach(wall -> {

                        final Material block = wall.block;
                        final double price = wall.price;
                        final ItemStack itemStack = new ItemStack(block);

                        if (this.hasBlock(player, block)) {
                            borderForm.addButton(itemStack.getI18NDisplayName() + "\n§2Im Besitz", this.getImage(block), (p) -> {
                                new ModalWindow.Builder("§7» §8Plot Rand Ändern", "Möchtest du den Rand deines Plots zu §9" + itemStack.getI18NDisplayName() + "§r ändern?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                                                new ChangeBorderTask(plot, block, player.getWorld(), true).execute();
                                                player.sendMessage(Language.get("border.change", block.name()));
                                            });
                                        })
                                        .onNo((p1) -> {
                                            borderForm.build().send(player);
                                        })
                                        .build().send(player);
                            });
                        } else {
                            borderForm.addButton(itemStack.getI18NDisplayName() + "\n§0Kaufen für §a$" + ServerCore.getServerCore().getMoneyFormat().format(price), this.getImage(block), (p) -> {
                                new ModalWindow.Builder("Plot-Rand kaufen", "Möchtest du den Plot-Rand §9" + itemStack.getI18NDisplayName() + " §rfür §a$" + ServerCore.getServerCore().getMoneyFormat().format(price) + "§r kaufen?", "§aJa", "§cZurück")
                                        .onYes((p1) -> {
                                            ServerCore.getServerCore().getEconomyAPI().getMoney(player.getName(), (money) -> {
                                                if (!(price > money)) {
                                                    ServerCore.getServerCore().getGroupAPI().addPlayerPermission(player.getName(), "plot.border." + block.name());
                                                    ServerCore.getServerCore().getEconomyAPI().reduceMoney(player.getName(), price);
                                                    player.sendMessage(Language.get("border.bought"));
                                                } else player.sendMessage(Language.get("not.enough.money"));
                                            });
                                        })
                                        .onNo((p1) -> {
                                            borderForm.build().send(player);
                                        })
                                        .build().send(player);
                            });
                        }
                    });

                    borderForm.build().send(player);
                } else player.sendMessage(Language.get("no.plot.permission"));

            } else player.sendMessage(Language.get("not.in.a.plot"));
        }
    }

    private record Border(Material block, double price) {
    }

}
