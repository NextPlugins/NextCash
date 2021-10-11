package com.nextplugins.cash.api.metric;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor(staticName = "of")
public final class MetricProvider {

    private final JavaPlugin plugin;

    public void register() {

        System.setProperty("bstats.relocatecheck", "false");

        new MetricsConnector(plugin, 10155);
        plugin.getLogger().info("Métrica de uso habilitada com sucesso.");

    }

}
