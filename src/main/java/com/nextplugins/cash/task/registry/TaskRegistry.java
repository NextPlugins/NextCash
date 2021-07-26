package com.nextplugins.cash.task.registry;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.task.AccountRankingTask;
import lombok.Data;
import org.bukkit.scheduler.BukkitScheduler;

@Data(staticConstructor = "of")
public final class TaskRegistry {

    private final NextCash plugin;

    public void register() {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        // ranking update

        int accountRankingUpdateDelay = RankingConfiguration.get(RankingConfiguration::updateDelay);

        scheduler.runTaskTimerAsynchronously(
                plugin,
                new AccountRankingTask(plugin),
                0,
                accountRankingUpdateDelay * 20L
        );

    }

}
