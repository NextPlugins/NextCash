package com.nextplugins.cash.task;

import com.nextplugins.cash.NextCash;
import com.nextplugins.cash.api.model.account.Account;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Collection;

@RequiredArgsConstructor
public final class AccountSaveTask implements Runnable {

    private final NextCash plugin;

    @Override
    public void run() {
        val accountStorage = plugin.getAccountStorage();
        val accountDAO = plugin.getAccountDAO();

        Collection<Account> accounts = accountStorage.getAccounts().values();

        if (!accounts.isEmpty()) {
            for (Account account : accountStorage.getAccounts().values()) {
                accountDAO.saveOne(account);
            }
        }

        if (plugin.isDEBUG()) {
            plugin.getTextLogger().debug(String.format("Todas as contas que estão em uso atualmente foram salvas. (%s contas)", accounts.size()));
        }
    }

}
