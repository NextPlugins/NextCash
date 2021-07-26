package com.nextplugins.cash.placeholder.registry;

import com.nextplugins.cash.placeholder.CashPlaceholderHook;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Data(staticConstructor = "of")
public class PlaceholderRegistry {

    private final JavaPlugin plugin;

    public void register() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            plugin.getLogger().warning("PlaceholderAPI não foi encontrado no servidor! " +
                    "Portanto, o ranking em NPC não será utilizado.");
            return;
        }

        new CashPlaceholderHook().register();
    }

}
