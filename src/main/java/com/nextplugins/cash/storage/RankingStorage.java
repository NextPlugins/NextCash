package com.nextplugins.cash.storage;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.group.Group;
import com.nextplugins.cash.api.group.GroupWrapperManager;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.NPCRankingRegistry;
import com.nextplugins.cash.ranking.util.RankingChatBody;
import com.nextplugins.cash.util.text.ColorUtil;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.Data;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public final class RankingStorage {

    private final LinkedHashMap<String, String> rankingAccounts = Maps.newLinkedHashMap();
    private final RankingChatBody rankingChatBody = new RankingChatBody();
    private final GroupWrapperManager groupManager;
    private long nextUpdate;

    public boolean checkUpdate(boolean force) {
        if (!force && nextUpdate > System.currentTimeMillis()) return false;

        nextUpdate = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(RankingConfiguration.get(RankingConfiguration::updateDelay));

        val plugin = NextCash.getInstance();
        val rankLimit = RankingConfiguration.get(RankingConfiguration::rankingLimit);
        val accounts = plugin.getAccountDAO().selectAll("ORDER BY balance DESC LIMIT " + rankLimit);

        if (!accounts.isEmpty()) {
            rankingAccounts.clear();

            val rankingType = RankingConfiguration.get(RankingConfiguration::rankingType);
            val chatRanking = rankingType.equals("CHAT");
            val bodyLines = new LinkedList<String>();
            for (val account : accounts) {
                addAccount(account);
                if (chatRanking) {
                    val body = RankingConfiguration.get(RankingConfiguration::chatModelBody);

                    val position = new AtomicInteger(1);
                    Group group = groupManager.getGroup(account.getOwner());
                    bodyLines.add(body
                        .replace("$position", String.valueOf(position.getAndIncrement()))
                        .replace("$player", account.getOwner())
                        .replace("$amount", NumberUtil.format(account.getBalance()))
                        .replace("$prefix", group.getPrefix())
                        .replace("$suffix", group.getSuffix())
                    );
                }

                rankingChatBody.setBodyLines(bodyLines.toArray(new String[]{}));
            }
        } else {
            rankingChatBody.setBodyLines(new String[]{ColorUtil.colored(
                "  &cNenhum jogador está no ranking!"
            )});
        }

        if (plugin.isDebug()) {
            plugin.getTextLogger().debug(String.format("As contas do ranking foram atualizadas. (%s contas)", accounts.size()));
        }

        val rankingVisual = NPCRankingRegistry.getInstance();
        if (!rankingVisual.isEnabled()) return true;

        val visualTime = Stopwatch.createStarted();

        if (plugin.isDebug()) {
            plugin.getTextLogger().info("[Ranking] Iniciando atualização de ranking visual");
        }

        // Leave from async. Entities can't be spawned in async.
        Bukkit.getScheduler().runTaskLater(plugin, rankingVisual.getRunnable(), 20L);

        visualTime.stop();

        if (plugin.isDebug()) {
            plugin.getTextLogger().debug(String.format("[Ranking] Atualização de ranking visual finalizada. (%s)", visualTime));
        }

        return true;
    }

    public void addAccount(Account account) {
        rankingAccounts.put(account.getOwner(), NumberUtil.format(account.getBalance()));
    }

}
