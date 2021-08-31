package com.nextplugins.cash.storage;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.NPCRankingRegistry;
import lombok.Data;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Data
public final class RankingStorage {

    private final LinkedHashMap<String, Double> rankingAccounts = Maps.newLinkedHashMap();
    private long nextUpdate;

    public boolean checkUpdate(boolean force) {

        if (!force && nextUpdate > System.currentTimeMillis()) return false;

        nextUpdate = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(RankingConfiguration.get(RankingConfiguration::updateDelay));

        val plugin = NextCash.getInstance();
        val accounts = plugin.getAccountDAO().selectAll("ORDER BY balance DESC LIMIT 10");

        val rankingStorage = plugin.getRankingStorage();

        if (!accounts.isEmpty()) {

            rankingStorage.getRankingAccounts().clear();
            accounts.forEach(rankingStorage::addAccount);

            if (plugin.isDebug()) {
                plugin.getTextLogger().debug(String.format("As contas do ranking foram atualizadas. (%s contas)", accounts.size()));
            }

            val rankingVisual = NPCRankingRegistry.getInstance();
            if (!rankingVisual.isEnabled()) return true;

            val visualTime = Stopwatch.createStarted();

            plugin.getTextLogger().info("[Ranking] Iniciando atualização de ranking visual");

            // Leave from async. Entities can't be spawned in async.
            Bukkit.getScheduler().runTaskLater(plugin, rankingVisual.getRunnable(), 20L);

            visualTime.stop();

            if (plugin.isDebug()) {
                plugin.getTextLogger().debug(String.format("[Ranking] Atualização de ranking visual finalizada. (%s)", visualTime));
            }

        }

        return true;
    }

    public void addAccount(Account account) {
        rankingAccounts.put(account.getOwner(), account.getBalance());
    }

}
