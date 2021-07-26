package com.nextplugins.cash.ranking;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.loader.LocationLoader;
import com.nextplugins.cash.ranking.runnable.NPCRunnable;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

@Data(staticConstructor = "of")
public final class NPCRankingRegistry {

    private final NextCash plugin;

    protected final PluginManager pluginManager = Bukkit.getPluginManager();
    protected final String citizens = "Citizens";
    protected final String holographicDisplays = "HolographicDisplays";

    public void register() {
        if (!pluginManager.isPluginEnabled(citizens)) {
            plugin.getLogger().warning(citizens + " não foi encontrado no servidor! Portanto, não" +
                    "o ranking em NPC não será utilizado.");
            return;
        } else if (!pluginManager.isPluginEnabled(holographicDisplays)) {
            plugin.getLogger().warning(holographicDisplays + " não foi encontrado no servidor! Portanto, não" +
                    "o ranking em NPC não será utilizado.");
            return;
        }

        new LocationLoader(plugin, plugin.getLocationManager()).loadLocations();

        int updateDelay = RankingConfiguration.get(RankingConfiguration::updateDelay);

        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(
                plugin,
                new NPCRunnable(plugin, plugin.getLocationManager(), plugin.getRankingStorage()),
                15,
                updateDelay * 20L
        );
    }

}
