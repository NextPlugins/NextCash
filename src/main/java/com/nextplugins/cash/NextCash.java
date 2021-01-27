package com.nextplugins.cash;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.listener.registry.ListenerRegistry;
import com.nextplugins.cash.sql.SQLProvider;
import com.nextplugins.cash.storage.AccountStorage;
import lombok.Getter;
import me.bristermitten.pdm.PluginDependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class NextCash extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private AccountDAO accountDAO;
    private AccountStorage accountStorage;

    @Override
    public void onEnable() {
        PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {
            try {
                sqlConnector = SQLProvider.of(this).setup();
                sqlExecutor = new SQLExecutor(sqlConnector);

                accountDAO = new AccountDAO(sqlExecutor);
                accountStorage = new AccountStorage(accountDAO);

                ListenerRegistry.of(this).register();

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
