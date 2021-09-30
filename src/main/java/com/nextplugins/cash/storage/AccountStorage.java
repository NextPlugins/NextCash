package com.nextplugins.cash.storage;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.configuration.GeneralConfiguration;
import com.nextplugins.cash.dao.AccountDAO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@RequiredArgsConstructor
public final class AccountStorage {

    @Getter private final AccountDAO accountDAO;
    @Getter private final HashMap<String, Account> cache = new HashMap<>();

    public void init() {
        accountDAO.createTable();
        NextCash.getInstance().getTextLogger().info("DAO do plugin iniciado com sucesso.");
    }

    public void saveOne(String name) {
        val account = findAccountCache(name);
        if (account == null) return;

        accountDAO.saveOne(account);
    }

    private Account selectOne(String owner) {
        return accountDAO.selectOne(owner);
    }

    /**
     * Used to get created accounts by name
     *
     * @param name player name
     * @return {@link Account} found
     */
    @Nullable
    public Account findAccountCache(String name) {
        if (cache.containsKey(name)) return cache.get(name);
        return selectOne(name);
    }

    /**
     * Used to get accounts
     * If player is online and no have account, we will create a new for them
     * but, if is offline, will return null
     *
     * @param offlinePlayer player
     * @return {@link Account} found
     */
    @Nullable
    public Account findAccount(@NotNull OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            val player = offlinePlayer.getPlayer();
            if (player != null) return findAccount(player);
        }

        return findAccountCache(offlinePlayer.getName());
    }

    /**
     * Used to get accounts
     *
     * @param player player to search
     * @return {@link Account} found
     */
    @NotNull
    public Account findAccount(@NotNull Player player) {
        Account account = findAccountCache(player.getName());
        if (account == null) {
            account = Account.builder()
                    .owner(player.getName())
                    .balance(GeneralConfiguration.get(GeneralConfiguration::initialBalance))
                    .receiveCash(true)
                    .build();
            put(account);
        }

        return account;
    }

    /**
     * Used to get accounts
     *
     * @param name player to search
     * @return {@link Account} found
     */
    @Nullable
    public Account findAccountByName(@NotNull String name) {
        val playerExact = Bukkit.getPlayerExact(name);
        return playerExact == null ? findAccountCache(name) : findAccount(playerExact);
    }

    /**
     * Put account directly in cache (will be sync to database automaticly)
     *
     * @param account of player
     */
    public void put(@NotNull Account account) {
        cache.put(account.getOwner(), account);
    }

}
