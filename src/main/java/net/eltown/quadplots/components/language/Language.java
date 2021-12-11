package net.eltown.quadplots.components.language;

import lombok.SneakyThrows;
import net.eltown.quadplots.QuadPlots;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Language {

    private static Map<String, String> messages = new HashMap<>();
    private static String prefix;

    @SneakyThrows
    public static void init(QuadPlots plugin) {
        messages.clear();
        plugin.saveResource("messages.yml", false);
        YamlConfiguration m = new YamlConfiguration();
        m.load(new File(plugin.getDataFolder(), "messages.yml"));
        for (String key : m.getKeys(true)) messages.put(key, m.getString(key));
        prefix = m.getString("prefix");
    }

    public static String get(String key, Object... replacements) {
        String message = prefix.replace("&", "§") + messages.getOrDefault(key, "null").replace("&", "§");

        int i = 0;
        for (Object replacement : replacements) {
            message = message.replace("[" + i + "]", String.valueOf(replacement));
            i++;
        }

        return message;
    }

    public static String getNP(String key, Object... replacements) {
        String message = messages.getOrDefault(key, "null").replace("&", "§");

        int i = 0;
        for (Object replacement : replacements) {
            message = message.replace("[" + i + "]", String.valueOf(replacement));
            i++;
        }

        return message;
    }

}
