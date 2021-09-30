package com.nextplugins.cash;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.base.Stopwatch;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.nextplugins.cash.api.group.GroupWrapperManager;
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
import java.util.ArrayList;
import java.util.logging.Level;

@Getter
public final class NextCash extends JavaPlugin {

    private final TextLogger textLogger = new TextLogger();
    private final boolean debug = getConfig().getBoolean("plugin.debug");

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private AccountDAO accountDAO;
    private AccountStorage accountStorage;
    private RankingStorage rankingStorage;

    private LocationManager locationManager;
    private GroupWrapperManager groupWrapperManager;

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
        getLogger().info("Iniciando carregamento do plugin.");

        val loadTime = Stopwatch.createStarted();

        sqlConnector = SQLProvider.of(this).setup();
        sqlExecutor = new SQLExecutor(sqlConnector);

        accountDAO = new AccountDAO(sqlExecutor);
        accountStorage = new AccountStorage(accountDAO);
        locationManager = new LocationManager();
        groupWrapperManager = new GroupWrapperManager();
        rankingStorage = new RankingStorage(groupWrapperManager);

        accountStorage.init();

        InventoryManager.enable(this);

        ConfigurationRegistry.of(this).register();
        ListenerRegistry.of(this).register();
        CommandRegistry.of(this).register();
        MetricProvider.of(this).register();
        PlayerPointsFakeDownloader.of(this).download();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            PlaceholderRegistry.of(this).register();
            NPCRankingRegistry.of(this).register();
            groupWrapperManager.init();
            rankingStorage.checkUpdate(true);
        }, 3 * 20L);

        loadTime.stop();
        getLogger().log(Level.INFO, "Plugin inicializado com sucesso. ({0})", loadTime);

    }

    @Override
    public void onDisable() {
        val unloadTiming = Stopwatch.createStarted();

        textLogger.info("Descarregando módulos do plugin... (0/2)");

        unloadRanking();

        accountStorage.getCache().values().forEach(accountStorage.getAccountDAO()::saveOne);
        textLogger.info("Informações das contas foram salvas. (2/2)");

        unloadTiming.stop();

        textLogger.info(String.format("O plugin foi encerrado com sucesso. (%s)", unloadTiming));

    }

    private void unloadRanking() {
        if (NPCRankingRegistry.getInstance().isEnabled()) {

            if (NPCRankingRegistry.getInstance().isHolographicDisplays()) {
                HologramsAPI.getHolograms(this).forEach(Hologram::delete);
            } else {
                // jump concurrentmodificationexception
                val holograms = new ArrayList<CMIHologram>();
                val hologramManager = CMI.getInstance().getHologramManager();
                for (val entry : hologramManager.getHolograms().entrySet()) {
                    if (entry.getKey().startsWith("NextCash")) holograms.add(entry.getValue());
                }

                holograms.forEach(hologramManager::removeHolo);
            }

            for (val id : NPCRunnable.NPCS) {
                val npc = CitizensAPI.getNPCRegistry().getById(id);
                if (npc == null) continue;

                CitizensAPI.getNPCRegistry().deregister(npc);
            }

            textLogger.info("NPCs e hologramas foram salvos e descarregados. (1/2)");
        }
    }

}
