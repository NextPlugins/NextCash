package com.nextplugins.cash.api.event.operations;

import com.nextplugins.cash.api.event.CustomEvent;
import com.nextplugins.cash.api.model.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public final class CashRankingUpdateEvent extends CustomEvent {

    private final Set<Account> accountList;
    private final Instant updateInstant;

}
