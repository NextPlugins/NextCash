package com.nextplugins.cash.task;

import com.nextplugins.cash.api.model.account.Account;
import com.nextplugins.cash.dao.AccountDAO;
import com.nextplugins.cash.storage.AccountStorage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class AccountSaveTask implements Runnable {

    private final AccountStorage accountStorage;
    private final AccountDAO accountDAO;

    @Override
    public void run() {
        for (Account account : accountStorage.getAccounts().values()) {
            accountDAO.saveOne(account);
        }
    }

}
