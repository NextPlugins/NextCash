package com.nextplugins.cash.placeholder.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.placeholder.CashPlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PlaceholderRegistry {

    private static final PluginManager MANAGER = Bukkit.getPluginManager();
    private static final String PLACEHOLDERS_API = "PlaceholderAPI";

    public static void register() {
        if (!MANAGER.isPluginEnabled(PLACEHOLDERS_API)) {
            NextCash.getInstance().getLogger().warning(PLACEHOLDERS_API + " não foi encontrado no servidor! " +
                    "Portanto, o ranking em NPC não será utilizado.");
            return;
        }

        new CashPlaceholderHook().register();
    }

}
