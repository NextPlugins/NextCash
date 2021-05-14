package com.nextplugins.cash;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.command.registry.CommandRegistry;
import com.nextplugins.cash.configuration.registry.ConfigurationRegistry;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.listener.registry.ListenerRegistry;
import com.nextplugins.cash.placeholder.registry.PlaceholderRegistry;
import com.nextplugins.cash.ranking.NPCRankingRegistry;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.ranking.runnable.NPCRunnable;
import com.nextplugins.cash.sql.SQLProvider;
import com.nextplugins.cash.stats.MetricsProvider;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.task.registry.TaskRegistry;
import lombok.Getter;
import lombok.val;
import me.bristermitten.pdm.PluginDependencyManager;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

@Getter
public final class NextCash extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private AccountDAO accountDAO;
    private AccountStorage accountStorage;
    private RankingStorage rankingStorage;

    private LocationManager locationManager;

    private File npcFile;
    private FileConfiguration npcConfiguration;

    public static NextCash getInstance() {
        return getPlugin(NextCash.class);
    }

    @Override
    public void onLoad() {

        saveDefaultConfig();

        npcFile = new File(getDataFolder(), "npcs.yml");
        if (!npcFile.exists()) saveResource("npcs.yml", false);

        npcConfiguration = YamlConfiguration.loadConfiguration(npcFile);

    }

    @Override
    public void onEnable() {

        getLogger().info("Baixando e carregando dependências necessárias...");

        val downloadTime = Stopwatch.createStarted();

        PluginDependencyManager.of(this)
                .loadAllDependencies()
                .exceptionally(throwable -> {

                    throwable.printStackTrace();

                    getLogger().severe("Ocorreu um erro durante a inicialização do plugin!");
                    Bukkit.getPluginManager().disablePlugin(this);

                    return null;

                })
                .join();

        downloadTime.stop();

        getLogger().log(Level.INFO, "Dependências carregadas com sucesso! ({0})", downloadTime);
        getLogger().info("Iniciando carregamento do plugin.");

        val loadTime = Stopwatch.createStarted();
        sqlConnector = SQLProvider.of(this).setup();
        sqlExecutor = new SQLExecutor(sqlConnector);

        accountDAO = new AccountDAO(sqlExecutor);
        accountStorage = new AccountStorage(accountDAO);
        rankingStorage = new RankingStorage();

        locationManager = new LocationManager();

        accountStorage.init();
        InventoryManager.enable(this);

        ConfigurationRegistry.of(this).register();
        ListenerRegistry.of(this).register();
        CommandRegistry.of(this).register();
        TaskRegistry.of(this).register();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            PlaceholderRegistry.register();
            NPCRankingRegistry.of(this).register();
        }, 3 * 20L);

        MetricsProvider.of(this).setup();

        loadTime.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso. ({0})", loadTime);
    }

    @Override
    public void onDisable() {
        for (NPC npc : NPCRunnable.NPC) {
            npc.destroy();
        }
        for (Hologram hologram : NPCRunnable.HOLOGRAM) {
            hologram.delete();
        }
    }

}
