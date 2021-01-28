package com.nextplugins.cash.task.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.task.AccountRankingTask;
import com.nextplugins.cash.task.AccountSaveTask;
import lombok.Data;
import org.bukkit.scheduler.BukkitScheduler;

@Data(staticConstructor = "of")
public final class TaskRegistry {

    private final NextCash plugin;

    public void register() {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        // account save

        int accountSaveDelay = GeneralConfiguration.get(GeneralConfiguration::saveDelay);

        scheduler.runTaskTimerAsynchronously(
                plugin,
                new AccountSaveTask(plugin.getAccountStorage(), plugin.getAccountDAO()),
                accountSaveDelay * 20L,
                accountSaveDelay * 20L
        );

        // ranking update

        int accountRankingUpdateDelay = RankingConfiguration.get(RankingConfiguration::updateDelay);

        scheduler.runTaskTimerAsynchronously(
                plugin,
                new AccountRankingTask(plugin.getAccountDAO(), plugin.getRankingStorage()),
                accountRankingUpdateDelay * 20L,
                accountRankingUpdateDelay * 20L
        );

    }

}
