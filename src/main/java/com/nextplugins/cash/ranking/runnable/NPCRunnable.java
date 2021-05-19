package com.nextplugins.cash.ranking.runnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.storage.RankingStorage;
import com.nextplugins.cash.util.text.NumberFormat;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public final class NPCRunnable implements Runnable {

    public static List<NPC> NPC;
    public static List<Hologram> HOLOGRAM;

    static {
        NPC = Lists.newLinkedList();
        HOLOGRAM = Lists.newLinkedList();
    }

    private final NextCash plugin;
    private final LocationManager locationManager;
    private final RankingStorage rankingStorage;

    @Override
    public void run() {
        LinkedHashMap<String, Double> rankingAccounts = rankingStorage.getRankingAccounts();

        if (rankingAccounts.size() <= 0) return;

        for (NPC npc : NPC) {
            npc.destroy();
        }

        for (Hologram hologram : HOLOGRAM) {
            hologram.delete();
        }

        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();

        AtomicInteger position = new AtomicInteger(1);

        rankingAccounts.forEach((owner, balance) -> {

            if (!locationManager.getLocationMap().containsKey(position.get())) return;

            Location location = locationManager.getLocation(position.get());
            List<String> hologramLines = RankingConfiguration.get(RankingConfiguration::hologramLines);
            int hologramHeight = RankingConfiguration.get(RankingConfiguration::hologramHeight);

            if (!hologramLines.isEmpty()) {
                Location hologramLocation = location.clone().add(0, hologramHeight, 0);
                Hologram hologram = HologramsAPI.createHologram(plugin, hologramLocation);

                for (int i = 0; i < hologramLines.size(); i++) {
                    String replacedLine = hologramLines.get(i);

                    replacedLine = replacedLine.replace("$position", String.valueOf(position.get()));
                    replacedLine = replacedLine.replace("$player", owner);
                    replacedLine = replacedLine.replace("$amount", NumberFormat.format(balance));

                    hologram.insertTextLine(i, replacedLine);
                }

                HOLOGRAM.add(hologram);
            }

            NPC npc = npcRegistry.createNPC(EntityType.PLAYER, "");
            npc.data().set("player-skin-name", owner);
            npc.setProtected(true);
            npc.spawn(location);

            NPC.add(npc);
            position.getAndIncrement();

        });

    }

}
