package net.eltown.quadplots.components.api;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import net.eltown.quadplots.QuadPlots;
import net.eltown.quadplots.components.data.Plot;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class Provider {

    private final QuadPlots plugin;
    private final MongoClientURI uri;
    private final String database, collection;
    private MongoClient client;
    private MongoCollection<Document> coll;
    private final LinkedHashMap<String, Plot> plots = new LinkedHashMap<>();

    public void connect() {
        this.plugin.getLogger().info("§eVerbindung zur Datenbank wird hergestellt...");
        this.client = new MongoClient(uri);
        this.coll = this.client.getDatabase(database).getCollection(collection);

        this.plugin.getLogger().info("§aErfolgreich mit der Datenbank verbunden.");

        this.plugin.getLogger().info("§ePlots werden in den Cache geladen...");
        for (Document doc : this.coll.find()) {
            final String id = doc.getString("_id");
            final int x = Integer.parseInt(id.split(";")[0]);
            final int z = Integer.parseInt(id.split(";")[1]);

            this.plots.put(
                    doc.getString("_id"),
                    new Plot(x, z, true,
                            doc.getList("owners", String.class),
                            doc.getList("trusted", String.class),
                            doc.getList("helpers", String.class),
                            doc.getList("flags", String.class),
                            doc.getList("banned", String.class))
            );
        }
        this.plugin.getLogger().info("§a" + this.plots.size() + " Plots wurden in den Cache geladen.");
    }

    public Plot getPlot(int x, int z, boolean checkMerge) {
        final String id = x + ";" + z;
        if (plots.containsKey(id)) {
            Plot plot = plots.get(id);
            if (checkMerge && plot.isMerged() && !plot.isOrigin()) plot = plot.getOrigin();
            return plot;
        } else return new Plot(x, z, false);
        //return plots.containsKey(id) ? plots.get(id) : new Plot(x, z, false);
    }

    public int getPlotAmount(final String player) {
        final AtomicInteger amount = new AtomicInteger(0);
        this.plots.values().forEach((p) -> {
            if (p.isOwner(player)) amount.set(amount.get() + 1);
        });
        return amount.get();
    }

    public LinkedList<Plot> getPlots(String player) {
        final LinkedList<Plot> plots = new LinkedList<>();

        for (final Plot plot : this.plots.values()) {
            if (plot.isOwner(player)) plots.add(plot);
        }

        return plots;
    }

    public LinkedList<Plot> getPlotsFiltered(String player) {
        final LinkedList<Plot> plots = new LinkedList<>(this.getPlots(player));
        plots.removeIf(p -> p.isMerged() && !p.isOrigin());
        return plots;
    }

    public void updatePlot(Plot plot) {
        CompletableFuture.runAsync(() -> {
            final Document doc = this.coll.find(new Document("_id", plot.getStringId())).first();
            if (doc != null) {
                try {
                    this.coll.updateOne(new Document("_id", plot.getStringId()),
                            new Document("$set",
                                    new Document("owners", new ArrayList<>(plot.getOwners()))
                                            .append("trusted", new ArrayList<>(plot.getTrusted()))
                                            .append("helpers", new ArrayList<>(plot.getHelpers()))
                                            .append("banned", new ArrayList<>(plot.getBanned()))
                                            .append("flags", new ArrayList<>(plot.getFlags()))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                this.coll.insertOne(
                        new Document("_id", plot.getStringId())
                                .append("owners", plot.getOwners())
                                .append("trusted", new ArrayList<String>())
                                .append("helpers", new ArrayList<String>())
                                .append("banned", new ArrayList<String>())
                                .append("flags", new ArrayList<String>())
                );
            }
        });

        this.plots.put(plot.getStringId(), plot);
    }

    public void unclaimPlot(Plot plot) {
        CompletableFuture.runAsync(() -> this.coll.deleteOne(new Document("_id", plot.getStringId())));
        this.plots.remove(plot.getStringId());
    }

    public Plot findFreePlot(int amplifierX, int amplifierZ) {
        if (plots.size() == 0) return getPlot(0, 0, false);

        // ToDo: Derzeit geht es nur wenn das erste Plot bei X: 0 und Z: 0 ist.

        int lastX = 0;
        int lastZ = 0;

        for (Plot plot : plots.values()) {

            int x = plot.getX() - lastX;
            int y = plot.getZ() - lastZ;
            int diff = Math.abs(x * y);

            if (diff < 4) {
                lastX = plot.getX();
                lastZ = plot.getZ();
                // - |
                Plot cb = getPlot(plot.getX() + 1, plot.getZ(), false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX(), plot.getZ() + 1, false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX() - 1, plot.getZ(), false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX(), plot.getZ() - 1, false);
                if (!cb.isClaimed()) return cb;

                // / \
                cb = getPlot(plot.getX() + 1 + amplifierX, plot.getZ() - 1 + amplifierZ, false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX() - 1 + amplifierX, plot.getZ() + 1 + amplifierZ, false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX() - 1 + amplifierX, plot.getZ() - 1 + amplifierZ, false);
                if (!cb.isClaimed()) return cb;

                cb = getPlot(plot.getX() + 1 + amplifierX, plot.getZ() + 1 + amplifierZ, false);
                if (!cb.isClaimed()) return cb;
            }
        }

        amplifierX += 1;
        amplifierZ -= 1;

        return findFreePlot(amplifierX, amplifierZ);
    }

}
