package com.nextplugins.cash.api.event.transactions;

import com.nextplugins.cash.api.event.CustomEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Data
@EqualsAndHashCode(callSuper = true)
public final class TransactionCompletedEvent extends CustomEvent {

    private final Player player;
    private final OfflinePlayer target;
    private final double amount;

}
