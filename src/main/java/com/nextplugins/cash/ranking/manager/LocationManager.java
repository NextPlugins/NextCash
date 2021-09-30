package com.nextplugins.cash.ranking.manager;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public final class LocationManager {

    @Getter
    private final Map<Integer, Location> locationMap;

    public LocationManager() {
        locationMap = new HashMap<>();
    }

    public Location getLocation(int position) {
        return locationMap.get(position);
    }

}
