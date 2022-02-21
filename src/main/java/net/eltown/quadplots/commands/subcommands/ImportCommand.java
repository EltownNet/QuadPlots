package net.eltown.quadplots.commands.subcommands;

import lombok.SneakyThrows;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.commands.PlotCommand;
import net.eltown.quadplots.components.data.Plot;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class ImportCommand extends PlotCommand {

    public ImportCommand(final QuadPlots plugin) {
        super(plugin, "import", "Import a Plot", "/p import", List.of(), true);
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isOp() && sender instanceof Player player) {
            if (args.length > 1) {
                final Plot plot = this.getPlugin().getLocationAPI().getPlotByPosition(player.getLocation().toVector());
                if (plot != null) {
                    int X = Integer.parseInt(args[0]);
                    int Z = Integer.parseInt(args[1]);

                    CompletableFuture.runAsync(() -> {
                        player.sendMessage("Starting Import...");
                        File file = new File(this.getPlugin().getDataFolder() + "/export/", X + "-" + Z + ".yml");
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                        final String toImport = yaml.getString("exportData");
                        final Vector baseLocation = this.getPlugin().getLocationAPI().getPositionByXZ(plot.getX(), plot.getZ()).add(new Vector(1, 0, 1)).setY(0);

                        getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                            for (final String eBlock : toImport.split(Pattern.quote("[B]"))) {
                                final String[] data = eBlock.split(Pattern.quote("-"));
                                int x = Integer.parseInt(data[0]);
                                int y = Integer.parseInt(data[1]);
                                int z = Integer.parseInt(data[2]);

                                String mat = data[3]/*.split(":")[1]*/.toUpperCase();
                                BlockFace face = switch (data[4]) {
                                    //case "0" -> BlockFace.DOWN;
                                    //case "1" -> BlockFace.UP;
                                    case "2" -> BlockFace.NORTH;
                                    case "3" -> BlockFace.SOUTH;
                                    case "4" -> BlockFace.WEST;
                                    case "5" -> BlockFace.EAST;
                                    default -> null;
                                };

                                Axis axis = switch (data[4]) {
                                    case "x" -> Axis.X;
                                    case "y" -> Axis.Y;
                                    case "z" -> Axis.Z;
                                    default -> null;
                                };

                                Slab.Type type = switch (data[4]) {
                                    case "u" -> Slab.Type.TOP;
                                    case "b" -> Slab.Type.BOTTOM;
                                    default -> null;
                                };

                                if (QuadPlots.translationKeys.containsKey(mat)) mat = QuadPlots.translationKeys.get(mat);

                                try {
                                    Material material = Material.valueOf(mat);
                                    Block block = player.getWorld().getBlockAt(
                                            baseLocation.getBlockX() + x,
                                            baseLocation.getBlockY() + y,
                                            baseLocation.getBlockZ() + z
                                    );
                                    block.setType(material);

                                    BlockState blockState = player.getWorld().getBlockState(
                                            baseLocation.getBlockX() + x,
                                            baseLocation.getBlockY() + y,
                                            baseLocation.getBlockZ() + z
                                    );


                                    if (blockState.getBlockData() instanceof Leaves leaves) {
                                        leaves.setPersistent(true);
                                        blockState.setBlockData(leaves);
                                        blockState.update(true);
                                    }

                                    if (blockState.getBlockData() instanceof Slab slab && type != null) {
                                        slab.setType(type);
                                        blockState.setBlockData(slab);
                                        blockState.update(true);
                                    }

                                    if (blockState.getBlockData() instanceof Stairs stairs) {
                                        if (data[5].equalsIgnoreCase("d")) {
                                            stairs.setHalf(Bisected.Half.TOP);
                                        } else {
                                            stairs.setHalf(Bisected.Half.BOTTOM);
                                        }
                                        blockState.setBlockData(stairs);
                                        blockState.update(true);
                                    }



                                    if (face != null) {
                                        final BlockData blockData = player.getWorld().getBlockData(
                                                baseLocation.getBlockX() + x,
                                                baseLocation.getBlockY() + y,
                                                baseLocation.getBlockZ() + z
                                        );

                                        if (blockData instanceof Directional directional) {
                                            directional.setFacing(face);
                                            block.setBlockData(directional);
                                        }

                                        if (blockData instanceof Rotatable rotatable) {
                                            rotatable.setRotation(face);
                                            block.setBlockData(rotatable);
                                        }


                                    } else if (axis != null) {
                                        final BlockData blockData = player.getWorld().getBlockData(
                                                baseLocation.getBlockX() + x,
                                                baseLocation.getBlockY() + y,
                                                baseLocation.getBlockZ() + z
                                        );

                                        if (blockData instanceof Orientable orientable) {
                                            orientable.setAxis(axis);
                                            block.setBlockData(orientable);
                                        }

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    player.sendMessage(ex.getMessage());
                                    player.sendMessage("Error! Check Console for more details.");
                                }

                            }
                            player.sendMessage("Done.");
                        });

                    });

                }

            }
        }
    }
}
