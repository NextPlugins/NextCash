package com.nextplugins.cash.ranking;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.ranking.loader.LocationLoader;
import com.nextplugins.cash.ranking.runnable.NPCRunnable;
import lombok.Data;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@Data
public class NPCRankingRegistry {

    @Getter private static final NPCRankingRegistry instance = new NPCRankingRegistry();

    private NextCash plugin;

    private boolean enabled;
    private Runnable runnable;
    private boolean holographicDisplays;

    public static NPCRankingRegistry of(NextCash plugin) {
        instance.setPlugin(plugin);
        return instance;
    }

    public void register() {
        val pluginManager = Bukkit.getPluginManager();

        if (!pluginManager.isPluginEnabled("Citizens")) {
            plugin.getLogger().warning("Citizens não foi encontrado no servidor! Portanto, não" +
                "o ranking em NPC não será utilizado.");
            return;
        }

        if (!pluginManager.isPluginEnabled("CMI") && !pluginManager.isPluginEnabled("HolographicDisplays")) {
            plugin.getLogger().log(Level.WARNING,
                "Dependência não encontrada ({0}) O ranking em NPC não será usado.",
                "HolographicDisplays ou CMI"
            );
            return;
        }

        holographicDisplays = pluginManager.isPluginEnabled("HolographicDisplays");

        val locationLoader = new LocationLoader(plugin, plugin.getLocationManager());
        locationLoader.loadLocations();

        runnable = new NPCRunnable(plugin, plugin.getLocationManager(), plugin.getRankingStorage(), holographicDisplays);
        enabled = true;

        plugin.getLogger().info("Sistema de NPC registrado com sucesso.");
    }

}
