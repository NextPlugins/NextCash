package com.nextplugins.cash.ranking.runnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public final class NPCRunnable implements Runnable {

    public static final List<Integer> NPCS = Lists.newArrayList();

    private final NextCash plugin;
    private final LocationManager locationManager;
    private final RankingStorage rankingStorage;

    @Override
    public void run() {
        clearPositions();

        val rankingAccounts = rankingStorage.getRankingAccounts();
        if (rankingAccounts.size() <= 0) return;

        val npcRegistry = CitizensAPI.getNPCRegistry();
        val position = new AtomicInteger(1);

        val hologramLines = RankingConfiguration.get(RankingConfiguration::hologramLines);
        val hologramHeight = RankingConfiguration.get(RankingConfiguration::hologramHeight);

        rankingAccounts.forEach((owner, balance) -> {

            if (!locationManager.getLocationMap().containsKey(position.get())) return;

            val location = locationManager.getLocation(position.get());
            if (!hologramLines.isEmpty()) {
                val hologramLocation = location.clone().add(0, hologramHeight, 0);
                val hologram = HologramsAPI.createHologram(plugin, hologramLocation);

                for (int i = 0; i < hologramLines.size(); i++) {
                    var replacedLine = hologramLines.get(i);

                    replacedLine = replacedLine.replace("$position", String.valueOf(position.get()));
                    replacedLine = replacedLine.replace("$player", owner);
                    replacedLine = replacedLine.replace("$amount", NumberUtil.format(balance));

                    hologram.insertTextLine(i, replacedLine);
                }

            }

            val npc = npcRegistry.createNPC(EntityType.PLAYER, "");
            npc.data().set("player-skin-name", owner);
            npc.setProtected(true);
            npc.spawn(location);

            NPCS.add(npc.getId());
            position.getAndIncrement();

        });

    }

    private void clearPositions() {
        for (val id : NPCS) {
            val npc = CitizensAPI.getNPCRegistry().getById(id);
            if (npc == null) continue;

            npc.despawn();
            npc.destroy();
        }

        HologramsAPI.getHolograms(plugin).forEach(Hologram::delete);
        NPCS.clear();
    }

}
