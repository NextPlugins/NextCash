package com.nextplugins.cash.listener.registry;

import com.google.inject.Inject;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.listener.UserConnectListener;
import com.nextplugins.cash.listener.UserDisconnectListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public final class ListenerRegistry {

    @Inject private static NextCash plugin;

    public static void register() {
        try {
            final Listener[] listeners = new Listener[] {
                    new UserConnectListener(),
                    new UserDisconnectListener()
            };

            for (Listener listener : listeners) {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }

            plugin.getLogger().info("Listeners registrados com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getLogger().severe("Não foi possível registrar os listeners!");
        }
    }

}
