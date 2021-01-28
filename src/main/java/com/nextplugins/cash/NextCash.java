package com.nextplugins.cash;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.command.registry.CommandRegistry;
import com.nextplugins.cash.configuration.registry.ConfigurationRegistry;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.guice.PluginInjector;
import com.nextplugins.cash.listener.registry.ListenerRegistry;
import com.nextplugins.cash.placeholder.registry.PlaceholderRegistry;
import com.nextplugins.cash.sql.SQLProvider;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.task.registry.TaskRegistry;
import lombok.Getter;
import me.bristermitten.pdm.PluginDependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class NextCash extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;
    private Injector injector;

    @Inject private AccountDAO accountDAO;
    @Inject private AccountStorage accountStorage;
    @Inject private RankingStorage rankingStorage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {
            try {
                injector = PluginInjector.of(this).get();
                injector.injectMembers(this);

                sqlConnector = SQLProvider.setup();
                sqlExecutor = new SQLExecutor(sqlConnector);

                accountStorage.init();

                ConfigurationRegistry.register();
                ListenerRegistry.register();
                CommandRegistry.register();
                TaskRegistry.register();
                PlaceholderRegistry.register();

                getLogger().info("Plugin inicializado com sucesso.");
            } catch (Throwable t) {
                t.printStackTrace();
                getLogger().severe("Ocorreu um erro durante a inicialização do plugin.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        });
    }

    public static NextCash getInstance() {
        return getPlugin(NextCash.class);
    }

}
