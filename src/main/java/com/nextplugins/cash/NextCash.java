package com.nextplugins.cash;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.nextplugins.cash.sql.SQLProvider;
import lombok.Getter;
import me.bristermitten.pdm.PluginDependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NextCash extends JavaPlugin {

    @Getter private SQLConnector sqlConnector;

    @Override
    public void onEnable() {
        PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {
            try {
                sqlConnector = SQLProvider.of(this).setup();

                getLogger().info("Plugin inicializado com sucesso.");
            } catch (Throwable t) {
                t.printStackTrace();
                getLogger().severe("Ocorreu um erro durante a inicialização do plugin.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        });
    }

    public NextCash getInstance() {
        return getPlugin(NextCash.class);
    }

}
