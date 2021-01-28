package com.nextplugins.cash.placeholder;

import com.nextplugins.cash.api.NextCashAPI;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.util.NumberFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CashPlaceholderHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "cash";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NextPlugins";
    }

    @Override
    public @NotNull String getVersion() {
        return "v1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        final Account account = NextCashAPI.get(player);
        final double amount = account != null ? account.getBalance() : 0D;

        if (params.equalsIgnoreCase("amount")) {
            return NumberFormat.format(amount);
        }

        return "";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return onPlaceholderRequest(player.getPlayer(), params);
    }
}
