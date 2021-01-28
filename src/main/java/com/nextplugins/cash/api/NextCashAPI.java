package com.nextplugins.cash.api;

import com.google.common.collect.Sets;
import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.storage.AccountStorage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NextCashAPI {

    @Getter public static final NextCashAPI instance = new NextCashAPI();

    private final AccountStorage accountStorage = NextCash.getInstance().getAccountStorage();

    /**
     * Search all accounts to look for one with the entered custom filter.
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the account found
     */
    public Optional<Account> findAccountByFilter(Predicate<Account> filter) {
        return allAccounts().stream()
                .filter(filter)
                .findFirst();
    }

    /**
     * Search all accounts to look for every with the entered custom filter.
     *
     * @param filter custom filter to search
     * @return {@link Set} with all accounts found
     */
    public Set<Account> findAccountsByFilter(Predicate<Account> filter) {
        return allAccounts().stream()
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
        return allAccounts().stream()
                .filter(account -> account.getOwner().getName().equals(owner))
                .findFirst();
    }

    /**
     * Search all accounts to look for every with the entered custom filter.
     *
     * @param player an online player
     * @return {@link Optional} with the account found
     */
    public Optional<Account> findAccountByPlayer(Player player) {
        return allAccounts().stream()
                .filter(account -> account.getOwner().getName().equals(player.getName()))
                .findFirst();
    }

    /**
     * Retrieve all accounts loaded so far.
     *
     * @return {@link Set} with accounts
     */
    public Set<Account> allAccounts() {
        return Sets.newLinkedHashSet(accountStorage.getAccounts().values());
    }

}
