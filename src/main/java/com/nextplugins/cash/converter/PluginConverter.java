package com.nextplugins.cash.converter;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.MessageValue;
import com.nextplugins.cash.storage.AccountStorage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface PluginConverter {

    default ExecutorService executors() {
        return Executors.newFixedThreadPool(10);
    }

    default void whitelist(boolean state) {
        Bukkit.getServer().setWhitelist(state);

        if (state) {
            Bukkit.broadcast("nextcash.convert", MessageValue.get(MessageValue::convertStart));

            Bukkit.getOnlinePlayers()
                  .stream()
                  .filter(player -> !player.isWhitelisted())
                  .forEach(player -> {
                      String text = StringUtils.join(
                              MessageValue.get(MessageValue::convertWhitelistKick),
                              ChatColor.RESET + "\n");

                      player.kickPlayer(text);
                  });
        } else {
            Bukkit.broadcast("nextcash.convert", MessageValue.get(MessageValue::convertEnd));
        }
    }

    default void save(Account account, ExecutorService executors) {
        final AccountStorage storage = NextCash.getInstance().getAccountStorage();

        CompletableFuture.supplyAsync(() -> {
            storage.addAccount(account);

            return account;
        }, executors).whenComplete((value, $) ->
            Bukkit.broadcast(
                    "nextcash.convert",
                    ChatColor.YELLOW + value.getOwner().getName() + " account was converted to NextCash."));
    }

    SQLConnector originConnector();
    String nameField();
    String balanceField();
    String tableName();

    void convert();

}
