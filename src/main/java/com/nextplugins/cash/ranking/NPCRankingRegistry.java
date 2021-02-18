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

    protected final PluginManager MANAGER = Bukkit.getPluginManager();
    protected final String CITIZENS = "Citizens";
    protected final String HOLOGRAPHIC_DISPLAYS = "HolographicDisplays";

    public void register() {
        if (!MANAGER.isPluginEnabled(CITIZENS)) {
            plugin.getLogger().warning(CITIZENS + " não foi encontrado no servidor! Portanto, não" +
                    "o ranking em NPC não será utilizado.");
            return;
        } else if (!MANAGER.isPluginEnabled(HOLOGRAPHIC_DISPLAYS)) {
            plugin.getLogger().warning(HOLOGRAPHIC_DISPLAYS + " não foi encontrado no servidor! Portanto, não" +
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
