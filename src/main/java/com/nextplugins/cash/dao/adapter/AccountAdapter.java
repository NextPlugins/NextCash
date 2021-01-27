package com.nextplugins.cash.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.nextplugins.cash.api.model.account.Account;
import org.bukkit.Bukkit;

public final class AccountAdapter implements SQLResultAdapter<Account> {

    @Override
    public Account adaptResult(SimpleResultSet resultSet) {

        String accountOwner = resultSet.get("owner");
        double accountBalance = resultSet.get("balance");

        return Account.builder()
                .owner(Bukkit.getOfflinePlayer(accountOwner))
                .balance(accountBalance)
                .build();

    }

}
