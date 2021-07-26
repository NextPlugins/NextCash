package com.nextplugins.cash.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.nextplugins.cash.api.model.account.Account;

public final class AccountAdapter implements SQLResultAdapter<Account> {

    @Override
    public Account adaptResult(SimpleResultSet resultSet) {
        String accountOwner = resultSet.get("owner");
        double accountBalance = resultSet.get("balance");
        boolean receiveCash = resultSet.get("receive_cash") == "1";

        return Account.builder()
                .owner(accountOwner)
                .balance(accountBalance)
                .receiveCash(receiveCash)
                .build();
    }

}
