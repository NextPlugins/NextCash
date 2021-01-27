package com.nextplugins.cash.configuration.registry;

import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import com.nextplugins.cash.configuration.MessageValue;
import lombok.Data;
import org.bukkit.plugin.java.JavaPlugin;

@Data(staticConstructor = "of")
public final class ConfigurationRegistry {

    private final JavaPlugin plugin;

    public void register() {
        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
                plugin,
                "messages.yml"
        );

        configurationInjector.injectConfiguration(
                MessageValue.instance()
        );
    }

}
