package com.nextplugins.cash;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.api.metric.MetricProvider;
import com.nextplugins.cash.command.registry.CommandRegistry;
import com.nextplugins.cash.configuration.registry.ConfigurationRegistry;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.listener.ListenerRegistry;
import com.nextplugins.cash.placeholder.registry.PlaceholderRegistry;
import com.nextplugins.cash.ranking.NPCRankingRegistry;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.ranking.runnable.NPCRunnable;
import com.nextplugins.cash.sql.SQLProvider;
import com.nextplugins.cash.storage.AccountStorage;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.PlayerPointsFakeDownloader;
import com.nextplugins.cash.util.text.TextLogger;
import lombok.Getter;
import lombok.val;
import net.citizensnpcs.api.CitizensAPI;
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

    private final TextLogger textLogger = new TextLogger();

    private final boolean debug = getConfig().getBoolean("plugin.debug");

    private File npcFile;
    private FileConfiguration npcConfiguration;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        npcFile = new File(getDataFolder(), "npcs.yml");
        if (!npcFile.exists()) saveResource("npcs.yml", false);

        npcConfiguration = YamlConfiguration.loadConfiguration(npcFile);
    }

    @Override
    public void onEnable() {
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
        MetricProvider.of(this).register();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            PlaceholderRegistry.of(this).register();
            NPCRankingRegistry.of(this).register();
            rankingStorage.checkUpdate(true);
        }, 3 * 20L);

        PlayerPointsFakeDownloader.of(this).download();

        loadTime.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso. ({0})", loadTime);

    }

    @Override
    public void onDisable() {
        val unloadTiming = Stopwatch.createStarted();

        textLogger.info("Descarregando módulos do plugin... (0/2)");

        if (NPCRankingRegistry.getInstance().isEnabled()) {
            HologramsAPI.getHolograms(this).forEach(Hologram::delete);

            for (val id : NPCRunnable.NPCS) {
                val npc = CitizensAPI.getNPCRegistry().getById(id);
                if (npc == null) continue;

                npc.despawn();
                npc.destroy();
            }

            textLogger.info("NPCs e hologramas foram salvos e descarregados. (1/2)");
        }

        accountStorage.getCache().values().forEach(accountStorage.getAccountDAO()::saveOne);
        textLogger.info("Informações das contas foram salvas. (2/2)");

        unloadTiming.stop();

        textLogger.info(String.format("O plugin foi encerrado com sucesso. (%s)", unloadTiming));

    }

    public static NextCash getInstance() {
        return getPlugin(NextCash.class);
    }

}
