package com.nextplugins.cash.storage;

import com.google.inject.Inject;
import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.dao.AccountDAO;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AccountStorage {

    @Getter private final Map<String, Account> accounts = new LinkedHashMap<>();

    @Inject private AccountDAO accountDAO;

    public void init() {
        accountDAO.createTable();
    }

    public Account getByName(String owner) {

        Account account = accounts.getOrDefault(owner, null);
        if (account == null) {

            account = accountDAO.selectOne(owner);

            if (account == null) {

                account = Account.builder()
                        .owner(Bukkit.getOfflinePlayer(owner))
                        .balance(0)
                        .build();

                accountDAO.insertOne(account);

            }

            accounts.put(owner, account);

        }

        return account;

    }

    public void insertOne(Account account) {
        addAccount(account);
        accountDAO.insertOne(account);
    }

    public void deleteOne(Account account) {
        removeAccount(account);
        accountDAO.deleteOne(account);
    }

    public void purge(String owner) {
        Account account = accounts.getOrDefault(owner, null);

        if (account == null) return;

        accountDAO.saveOne(account);
        accounts.remove(account.getOwner().getName());
    }

    public void addAccount(Account account) {
        if (!accounts.containsKey(account.getOwner().getName())) {
            accounts.put(account.getOwner().getName(), account);
        }
    }

    public void removeAccount(Account account) {
        accounts.put(account.getOwner().getName(), account);
    }

}
