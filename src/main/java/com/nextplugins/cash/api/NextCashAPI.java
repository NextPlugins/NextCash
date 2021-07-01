package com.nextplugins.cash.api;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NextCashAPI {

    @Getter private static final NextCashAPI instance = new NextCashAPI();

    /**
     * Search all accounts to look for every with the entered custom filter.
     *
     * @param filter custom filter to search
     * @return {@link Set} with all accounts found
     */
    @Deprecated
    public Set<Account> findAccountsByFilter(Predicate<Account> filter) {
        return NextCash.getInstance().getAccountStorage()
                .getCache().synchronous()
                .asMap().values().stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    /**
     * Search all accounts to look for every with the entered custom filter.
     *
     * @param owner account owner name
     * @return {@link Optional} with the account found
     */
    public Optional<Account> findAccountByOwner(String owner) {
        return Optional.ofNullable(NextCash.getInstance().getAccountStorage().findAccount(Bukkit.getOfflinePlayer(owner)));
    }

    /**
     * Search all accounts to look for every with the entered custom filter.
     *
     * @param player an online player
     * @return {@link Optional} with the account found
     */
    public Optional<Account> findAccountByPlayer(Player player) {
        return Optional.of(NextCash.getInstance().getAccountStorage().findAccount(player));
    }

}
