package com.nextplugins.cash.placeholder;

import com.nextplugins.cash.api.NextCashAPI;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class CashPlaceholderHook extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "NextPlugins";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("amount")) {
            val account = NextCashAPI.getInstance().findAccountByPlayer(player).orElse(null);

            if (account == null) return "";

            return NumberUtil.format(account.getBalance());
        }

        if (params.equalsIgnoreCase("raw")) {
            val account = NextCashAPI.getInstance().findAccountByPlayer(player).orElse(null);

            if (account == null) return "";

            return String.valueOf(account.getBalance());
        }

        return "Placeholder inválida";
    }

}
