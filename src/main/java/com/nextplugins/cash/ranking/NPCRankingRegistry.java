package com.nextplugins.cash.ranking;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.loader.LocationLoader;
import com.nextplugins.cash.ranking.runnable.NPCRunnable;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

@Data(staticConstructor = "of")
public final class NPCRankingRegistry {

    private final NextCash plugin;


    public void register() {
        new LocationLoader(plugin, plugin.getLocationManager()).loadLocations();

        int updateDelay = RankingConfiguration.get(RankingConfiguration::updateDelay);

        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(
                plugin,
                new NPCRunnable(plugin, plugin.getLocationManager(), plugin.getRankingStorage()),
                0,
                updateDelay * 20L
        );
    }

}
