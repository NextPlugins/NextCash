package com.nextplugins.cash.configuration.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.configuration.RankingConfiguration;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor(staticName = "of")
public final class ConfigurationRegistry {

    private final JavaPlugin plugin;

    public void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
            plugin,
            "messages.yml",
            "ranking.yml"
        );

        configurationInjector.injectConfiguration(
            MessageValue.instance(),
            GeneralConfiguration.instance(),
            RankingConfiguration.instance()
        );
    }

}
