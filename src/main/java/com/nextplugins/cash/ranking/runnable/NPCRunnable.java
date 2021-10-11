package com.nextplugins.cash.ranking.runnable;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.configuration.RankingConfiguration;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.storage.RankingStorage;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

@RequiredArgsConstructor
public final class NPCRunnable implements Runnable {

    public static final List<Integer> NPCS = Lists.newArrayList();
    public static final List<String> HOLOGRAMS = Lists.newArrayList();

    private final NextCash plugin;
    private final LocationManager locationManager;
    private final RankingStorage rankingStorage;

    private final boolean holographicDisplays;

    @Override
    public void run() {
        clearPositions();
        if (locationManager.getLocationMap().isEmpty()) return;

        val accounts = rankingStorage.getRankingAccounts();
        val keySet = accounts.keySet().toArray();
        val hologramLines = RankingConfiguration.get(RankingConfiguration::hologramLines);
        val nobodyLines = RankingConfiguration.get(RankingConfiguration::nobodyHologramLines);
        for (val entry : locationManager.getLocationMap().entrySet()) {
            val position = entry.getKey();
            val location = entry.getValue();
            if (location == null || location.getWorld() == null) continue;

            val chunk = location.getChunk();
            if (!chunk.isLoaded()) chunk.load(true);

            val username = (String) (position - 1 < keySet.length ? keySet[position - 1] : null);
            if (username == null) {
                if (!nobodyLines.isEmpty()) {
                    val hologramLocation = location.clone().add(0, 3, 0);
                    if (holographicDisplays) {
                        val hologram = HologramsAPI.createHologram(plugin, hologramLocation);

                        for (val nobodyLine : nobodyLines) {
                            hologram.appendTextLine(nobodyLine.replace("$position", String.valueOf(position)));
                        }
                    } else {
                        val cmiHologram = new CMIHologram("NextCash" + position, hologramLocation);
                        for (val nobodyLine : nobodyLines) {
                            cmiHologram.addLine(nobodyLine.replace("$position", String.valueOf(position)));
                        }

                        CMI.getInstance().getHologramManager().addHologram(cmiHologram);
                        cmiHologram.update();

                        HOLOGRAMS.add("NextCash" + position);
                    }
                }
            } else {
                val amount = accounts.get(username);
                if (!hologramLines.isEmpty()) {
                    val group = plugin.getGroupWrapperManager().getGroup(username);
                    val hologramLocation = location.clone().add(0, 3, 0);
                    if (holographicDisplays) {
                        val hologram = HologramsAPI.createHologram(plugin, hologramLocation);
                        for (val hologramLine : hologramLines) {
                            hologram.appendTextLine(hologramLine
                                .replace("$position", String.valueOf(position))
                                .replace("$player", username)
                                .replace("$prefix", group.getPrefix())
                                .replace("$suffix", group.getSuffix())
                                .replace("$amount", amount)
                            );
                        }
                    } else {
                        val cmiHologram = new CMIHologram("NextCash" + position, hologramLocation);
                        for (val hologramLine : hologramLines) {
                            cmiHologram.addLine(hologramLine
                                .replace("$position", String.valueOf(position))
                                .replace("$player", username)
                                .replace("$prefix", group.getPrefix())
                                .replace("$suffix", group.getSuffix())
                                .replace("$amount", amount)
                            );
                        }

                        CMI.getInstance().getHologramManager().addHologram(cmiHologram);
                        cmiHologram.update();

                        HOLOGRAMS.add("NextCash" + position);
                    }
                }
            }

            val npcRegistry = CitizensAPI.getNPCRegistry();

            val npc = npcRegistry.createNPC(EntityType.PLAYER, "");
            val skinName = username == null ? "Yuhtin" : username;
            npc.data().set("player-skin-name", skinName);
            npc.data().set("NextCash", true);
            npc.setProtected(true);
            npc.spawn(location);
            npc.getEntity().setMetadata("NextCash", new FixedMetadataValue(NextCash.getInstance(), true));

            NPCS.add(npc.getId());
        }
    }

    private void clearPositions() {
        try {
            for (val npc : CitizensAPI.getNPCRegistry()) {
                if (!npc.data().has("NextCash")) continue;

                npc.despawn();
                npc.destroy();
            }

        } catch (Exception exception) {
            for (val id : NPCRunnable.NPCS) {
                val npc = CitizensAPI.getNPCRegistry().getById(id);
                if (npc == null) continue;

                npc.despawn();
                npc.destroy();
            }
        }

        if (holographicDisplays) HologramsAPI.getHolograms(plugin).forEach(Hologram::delete);
        else {
            for (val entry : HOLOGRAMS) {
                val cmiHologram = CMI.getInstance().getHologramManager().getHolograms().get(entry);
                if (cmiHologram == null) continue;

                CMI.getInstance().getHologramManager().removeHolo(cmiHologram);
            }
        }

        HOLOGRAMS.clear();
        NPCS.clear();
    }

}
