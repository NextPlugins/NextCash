package com.nextplugins.cash.api.event.operations;

import com.nextplugins.cash.api.event.CustomEvent;
import com.nextplugins.cash.api.model.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@Getter
public final class CashRankingUpdateEvent extends CustomEvent {

    private final Set<Account> accountList;
    private final Instant updateInstant;

    public CashRankingUpdateEvent(Set<Account> accountList, Instant updateInstant) {
        super(true);
        this.accountList = accountList;
        this.updateInstant = updateInstant;
    }
}
