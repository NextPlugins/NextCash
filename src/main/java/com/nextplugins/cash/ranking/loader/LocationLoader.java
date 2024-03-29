package com.nextplugins.cash.ranking.loader;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.ranking.manager.LocationManager;
import com.nextplugins.cash.ranking.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.List;

@RequiredArgsConstructor
public final class LocationLoader {

    private final NextCash plugin;
    private final LocationManager locationManager;

    public void loadLocations() {
        List<String> locations = plugin.getNpcConfiguration().getStringList("npc.locations");
        if (locations.isEmpty()) {
            plugin.getTextLogger().info("Não foi encontrado nenhuma localização para gerar os NPCs!");
            return;
        }

        for (String line : locations) {
            int position = Integer.parseInt(line.split(" ")[0]);
            Location location = LocationUtil.byStringNoBlock(line.split(" ")[1].split(","));

            locationManager.getLocationMap().put(position, location);
        }
        plugin.getTextLogger().info(
            "Foram carregados " + locationManager.getLocationMap().size() + " posições no ranking de npc!"
        );
    }

}
