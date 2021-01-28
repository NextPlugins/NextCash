package com.nextplugins.cash.placeholder.registry;

import com.nextplugins.cash.placeholder.CashPlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PlaceholderRegistry {

    private static final PluginManager MANAGER = Bukkit.getPluginManager();
    private static final String PLACEHOLDERS_API = "PlaceholdersAPI";

    public static void register() {
        if (!MANAGER.isPluginEnabled(PLACEHOLDERS_API)) return;

        new CashPlaceholderHook().register();
    }

}
