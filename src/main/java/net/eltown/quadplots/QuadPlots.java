package net.eltown.quadplots;

import com.mongodb.MongoClientURI;
import lombok.Getter;
import net.eltown.quadplots.commands.RootCommand;
import net.eltown.quadplots.commands.SubCommandHandler;
import net.eltown.quadplots.components.api.API;
import net.eltown.quadplots.components.api.LocationAPI;
import net.eltown.quadplots.components.api.Provider;
import net.eltown.quadplots.components.language.Language;
import net.eltown.quadplots.components.listener.BlockListener;
import net.eltown.quadplots.components.listener.PlayerListener;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class QuadPlots extends JavaPlugin {

    private SubCommandHandler commandHandler;
    private API api;
    private LocationAPI locationAPI;
    @Getter
    private static QuadPlots instance;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        Language.init(this);

        this.commandHandler = new SubCommandHandler();
        this.commandHandler.init(this);

        final Provider provider = new Provider(this, new MongoClientURI(this.getConfig().getString("MongoDB")), "eltown", "a2_plots");
        provider.connect();
        this.api = new API(this, provider);
        this.locationAPI = new LocationAPI(this.api, provider);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        this.getServer().getCommandMap().register("quadplots", new RootCommand(this));
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new PlotGenerator();
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        return new PlotGenerator.PlotBiomes();
    }

}
