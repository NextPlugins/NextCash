package com.nextplugins.cash.placeholder;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.NextCashAPI;
import com.nextplugins.cash.util.text.NumberUtil;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CashPlaceholderHook extends PlaceholderExpansion {

    private final NextCash plugin = NextCash.getInstance();

    @Override
    public @NotNull String getName() {
        return plugin.getName();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nextcash";
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
        if (player != null && params.equalsIgnoreCase("amount")) {

            val account = NextCashAPI.getInstance().findAccountByPlayer(player).orElse(null);
            val amount = account != null ? account.getBalance() : 0.0;

            return NumberUtil.format(amount);

        }

        return "";
    }

}
