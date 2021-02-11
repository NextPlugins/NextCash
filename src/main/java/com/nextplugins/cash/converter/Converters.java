package com.nextplugins.cash.converter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Converters {

    @Getter private static final Map<String, PluginConverter> converters = new HashMap<>();

    public static PluginConverter of(String key) {
        return converters.getOrDefault(key, null);
    }

}
