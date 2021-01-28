package com.nextplugins.cash.configuration.registry;

import com.google.inject.Inject;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.configuration.MessageValue;

public final class ConfigurationRegistry {

    @Inject private static NextCash plugin;

    public static void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
                plugin,
                "messages.yml"
        );

        configurationInjector.injectConfiguration(
                MessageValue.instance(),
                GeneralConfiguration.instance()
        );
    }

}
