package com.nextplugins.cash.listener.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.listener.UserConnectListener;
import com.nextplugins.cash.listener.UserDisconnectListener;
import lombok.Data;
import org.bukkit.Bukkit;

@Data(staticConstructor = "of")
public final class ListenerRegistry {

    private final NextCash plugin;

    public void register() {
        try {
            Bukkit.getPluginManager().registerEvents(
                    new UserConnectListener(plugin.getAccountStorage()),
                    plugin
            );
            Bukkit.getPluginManager().registerEvents(
                    new UserDisconnectListener(plugin.getAccountStorage()),
                    plugin
            );
            plugin.getLogger().info("Listeners registrados com sucesso.");
        } catch (Throwable t) {
            t.printStackTrace();
            plugin.getLogger().severe("Não foi possível registrar os listeners!");
        }
    }

}
