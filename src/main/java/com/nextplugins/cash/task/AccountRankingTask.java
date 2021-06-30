package com.nextplugins.cash.task;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.event.operations.CashRankingUpdateEvent;
import com.nextplugins.cash.api.model.account.Account;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
public final class AccountRankingTask implements Runnable {

    private final NextCash plugin;

    @Override
    public void run() {
        Set<Account> accounts = plugin.getAccountDAO().selectAll("ORDER BY balance DESC LIMIT 10");

        val rankingStorage = plugin.getRankingStorage();

        if (!accounts.isEmpty()) {
            rankingStorage.getRankingAccounts().clear();
            accounts.forEach(rankingStorage::addAccount);
            CashRankingUpdateEvent cashRankingUpdateEvent = new CashRankingUpdateEvent(accounts, Instant.now());
            Bukkit.getPluginManager().callEvent(cashRankingUpdateEvent);

            if (plugin.isDebug()) {
                plugin.getTextLogger().debug(String.format("As contas do ranking foram atualizadas. (%s contas)", accounts.size()));
            }
        }
    }

}
