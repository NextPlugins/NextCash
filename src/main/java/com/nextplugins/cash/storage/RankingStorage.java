package com.nextplugins.cash.storage;

import com.google.common.collect.Maps;
import com.nextplugins.cash.api.model.account.Account;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public final class RankingStorage {

    private final LinkedHashMap<String, Double> rankingAccounts = Maps.newLinkedHashMap();

    public void addAccount(Account account) {
        rankingAccounts.put(account.getOwner(), account.getBalance());
    }

}
