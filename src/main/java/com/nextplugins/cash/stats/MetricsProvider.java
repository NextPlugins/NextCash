package com.nextplugins.cash.stats;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.GeneralConfiguration;
import lombok.Data;
import org.bstats.bukkit.Metrics;

@Data(staticConstructor = "of")
public final class MetricsProvider {

    private final NextCash plugin;

    public void setup() {
        if (GeneralConfiguration.get(GeneralConfiguration::useBStats)) {
            int pluginId = 10041;
            new Metrics(plugin, pluginId);
            plugin.getTextLogger().info("Integração com o bStats configurada com sucesso.");
        } else {
            plugin.getTextLogger().info("Você desabilitou o uso do bStats, portanto, não utilizaremos dele.");
        }
    }

}
